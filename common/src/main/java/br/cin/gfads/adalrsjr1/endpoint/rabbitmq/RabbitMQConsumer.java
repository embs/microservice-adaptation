package br.cin.gfads.adalrsjr1.endpoint.rabbitmq;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import br.cin.gfads.adalrsjr1.endpoint.ReceiverEndpoint;
import br.cin.gfads.adalrsjr1.endpoint.rabbitmq.RabbitMQSubscriber.Builder;

public class RabbitMQConsumer implements ReceiverEndpoint {

	private static final Logger log = LoggerFactory.getLogger(RabbitMQConsumer.class);

	public static class Builder {

		private String host;
		private String queue;
		private boolean durable; 
		private BlockingQueue<byte[]> buffer;
		private int port = 5672;

		public int getPort() {
			return port;
		}

		public Builder withPort(int port) {
			this.port = port;
			return this;
		}

		public String getHost() {
			return host;
		}

		public String getQueue() {
			return queue;
		}

		public boolean isDurable() {
			return durable;
		}

		public Builder withHost(String host) {
			this.host = host;
			return this;
		}

		public Builder withQueue(String queue) {
			this.queue = queue;
			return this;
		}

		public Builder withDurable(boolean durable) {
			this.durable = durable;
			return this;
		}

		public BlockingQueue<byte[]> getBuffer() {
			return buffer;
		}

		public Builder withBuffer(BlockingQueue<byte[]> buffer) {
			this.buffer = buffer;
			return this;
		}

		public RabbitMQConsumer build() {
			RabbitMQConsumer consumer = null;
			try {
				consumer =  new RabbitMQConsumer(this);
			}
			catch(RuntimeException e) {
				log.warn("trying again... {}", e.getMessage());
				Thread.sleep(1000);
				return build();
			}
			finally {
				return consumer;
			}
		}
	}

	public static RabbitMQConsumer.Builder builder() {
		return new RabbitMQConsumer.Builder();
	}

	private ConnectionFactory factory;
	private Connection connection;
	private Channel channel;
	private Consumer consumer;

	private String queue;

	private RabbitMQConsumer(RabbitMQConsumer.Builder builder) {
		factory = new ConnectionFactory();
		factory.setAutomaticRecoveryEnabled(true);
		factory.setHost(builder.getHost());
		factory.setPort(builder.getPort());
//		factory.setHandshakeTimeout(5000);

		try{
			connection = factory.newConnection(Executors.newFixedThreadPool(20));
			channel = connection.createChannel();
			queue = builder.getQueue();

			channel.queueDeclare(getQueue(), builder.isDurable(), false, false, null);
			channel.basicQos(1);
			consumer = new InnerConsumer(channel, builder.getBuffer());
			channel.basicConsume(queue, false, consumer);
		}
		catch(Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void stop() {
		try {
			channel.close();
			connection.close();
		}
		catch(Exception e) {
			throw new RuntimeException(e);
		}
	}

	public String getQueue() {
		return queue;
	}

	public Channel getChannel() {
		return channel;
	}

	static private class InnerConsumer extends DefaultConsumer {
		private BlockingQueue<byte[]> buffer;
		public InnerConsumer(Channel channel, BlockingQueue<byte[]> buffer) {
			super(channel);
			this.buffer = buffer;
		}

		@Override
		public void handleDelivery(String consumerTag, Envelope envelope,
				AMQP.BasicProperties properties, byte[] body) throws IOException {
			try {
				if(buffer.offer(body,100, TimeUnit.MILLISECONDS)) {
					getChannel().basicAck(envelope.getDeliveryTag(), false);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) throws InterruptedException {
		ArrayBlockingQueue<byte[]> buffer = new ArrayBlockingQueue<>(1);

		RabbitMQConsumer consumer = RabbitMQConsumer.builder()
				.withBuffer(buffer)
				.withDurable(false)
				.withHost("10.0.75.1")
				.withQueue("task-queue")
				.build();

		Random r = new Random();
		while(true) {
			String message = new String(buffer.take());
			System.err.println(" [x] " + " Received '" + message + "'");
			Thread.sleep(r.nextInt(100));
		}

	}

}
