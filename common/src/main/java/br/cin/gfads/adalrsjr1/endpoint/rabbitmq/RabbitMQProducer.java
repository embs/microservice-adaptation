package br.cin.gfads.adalrsjr1.endpoint.rabbitmq;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

import br.cin.gfads.adalrsjr1.endpoint.SenderEndpoint;

public class RabbitMQProducer implements SenderEndpoint {

	private static final Logger log = LoggerFactory.getLogger(RabbitMQProducer.class);

	public static class Builder {

		private String host;
		private String queue;
		private boolean durable; 
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

		public RabbitMQProducer build() {
			RabbitMQProducer producer = null;
			try {
				producer = new RabbitMQProducer(this);
			}
			catch(RuntimeException e) {
				log.warn("trying again... {}", e.getMessage());
				Thread.sleep(1000);
				producer = build();
			}
			finally {
				return producer;
			}
			
		}
	}

	public static RabbitMQProducer.Builder builder() {
		return new RabbitMQProducer.Builder();
	}

	private ConnectionFactory factory;
	private Connection connection;
	private Channel channel;

	private String queue;

	private RabbitMQProducer(RabbitMQProducer.Builder builder) {
		factory = new ConnectionFactory();
		factory.setAutomaticRecoveryEnabled(true);
		factory.setHost(builder.getHost());
		factory.setPort(builder.getPort());
		factory.setHandshakeTimeout(5000);
		try{
			connection = factory.newConnection(Executors.newFixedThreadPool(20));
			channel = connection.createChannel();
			queue = builder.getQueue();

			channel.queueDeclare(getQueue(), builder.isDurable(), false, false, null);
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

	@Override
	public Object send(byte[] message) {
		boolean result = false;
		try {
			channel.basicPublish("", getQueue(), MessageProperties.PERSISTENT_TEXT_PLAIN, message);
			result = true;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		finally {
			return result;
		}
	}

	public static void main(String[] args) {
		RabbitMQProducer producer = RabbitMQProducer.builder()
				.withDurable(false)
				.withHost("10.0.75.1")
				.withQueue("task-queue")
				.build();

		try(Scanner scanner = new Scanner(System.in)) {
			while(scanner.hasNext()) {
				String message = scanner.nextLine();
				if(message.equals("quit")) break;
				for(int i = 0; i < 100; i++)
					producer.send((""+i).getBytes());
				//				producer.send(message.getBytes());
				System.err.println("[X] Sent >>> " + message);
			}
		}
		finally {
			producer.stop();
		}
	}

}
