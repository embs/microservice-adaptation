package br.cin.gfads.adalrsjr1.endpoint.rabbitmq;

import static br.cin.gfads.adalrsjr1.endpoint.rabbitmq.ExchangeType.FANOUT;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Stopwatch;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import br.cin.gfads.adalrsjr1.common.events.SymptomEvent;
import br.cin.gfads.adalrsjr1.endpoint.ReceiverEndpoint;

public class RabbitMQSubscriber implements ReceiverEndpoint {

	private static final Logger log = LoggerFactory.getLogger(RabbitMQSubscriber.class);

	static public class Builder {
		private String exchangeName;
		private String routingKey;
		private String host;
		private boolean exchangeDurable;
		private BlockingQueue<byte[]> buffer;
		private ExchangeType exchangeType; 
		private int port = 5672;

		public int getPort() {
			return port;
		}

		public Builder withPort(int port) {
			this.port = port;
			return this;
		}

		public String getExchangeName() {
			return exchangeName;
		}
		public Builder withExchangeName(String exchangeName) {
			this.exchangeName = exchangeName;
			return this;
		}
		public String getRoutingKey() {
			return routingKey;
		}
		public Builder withRoutingKey(String routingKey) {
			this.routingKey = routingKey;
			return this;
		}
		public String getHost() {
			return host;
		}
		public Builder withHost(String host) {
			this.host = host;
			return this;
		}
		public boolean isExchangeDurable() {
			return exchangeDurable;
		}
		public Builder withExchangeDurable(boolean exchangeDurable) {
			this.exchangeDurable = exchangeDurable;
			return this;
		}
		public BlockingQueue<byte[]> getBuffer() {
			return buffer;
		}
		public Builder withBuffer(BlockingQueue<byte[]> buffer) {
			this.buffer = buffer;
			return this;
		}
		public ExchangeType getExchangeType() {
			return exchangeType;
		}
		public Builder withExchangeType(ExchangeType exchangeType) {
			this.exchangeType = exchangeType;
			return this;
		}

		public RabbitMQSubscriber build() {
			RabbitMQSubscriber subscriber = null;
			try {
				subscriber = new RabbitMQSubscriber(this);
			}
			catch(RuntimeException e) {
				Thread.sleep(1000);
				subscriber = build();
			}
			finally {
				return subscriber;
			}

		}
	}

	public static RabbitMQSubscriber.Builder builder() {
		return new RabbitMQSubscriber.Builder();
	}

	private ConnectionFactory factory;
	private Connection connection;
	private Channel channel;

	private String exchangeName;
	private String routingKey;
	private String queue;

	private Consumer consumer;

	private RabbitMQSubscriber(RabbitMQSubscriber.Builder builder) {
		this.exchangeName = builder.getExchangeName();
		this.routingKey = builder.getRoutingKey();

		factory = new ConnectionFactory();
		factory.setAutomaticRecoveryEnabled(true);
		factory.setHost(builder.getHost());
		factory.setPort(builder.getPort());

		try {
			connection = factory.newConnection();
			channel = connection.createChannel();

			channel.exchangeDeclare(getExchangeName(), builder.getExchangeType().name().toLowerCase(), builder.isExchangeDurable());
			this.queue = channel.queueDeclare().getQueue();
			channel.queueBind(getQueue(), getExchangeName(), getRoutingKey());

			consumer = new InnerConsumer(channel, builder.getBuffer());
			channel.basicConsume(getQueue(), true, consumer);

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

	private Consumer instantiateConsumer(Class<? extends Consumer> consumerClass, Channel channel) throws Exception {
		Consumer consumer = null;

		Constructor<? extends Consumer> constructor = consumerClass.getConstructor(Channel.class);
		consumer = (Consumer) constructor.newInstance(channel);

		return consumer;
	}

	public void process() {
		this.consumer = new DefaultConsumer(channel){
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope,
					AMQP.BasicProperties properties, byte[] body) throws IOException {
				String message = new String(body, "UTF-8");
				System.err.println(" [x] "+ this + " Received '" + message + "'");
			}
		};
		try {
			channel.basicConsume(getQueue(), true, consumer);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public String getExchangeName() {
		return exchangeName;
	}

	public String getRoutingKey() {
		return routingKey;
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
				int count = 0;
				while(!buffer.offer(body,100, TimeUnit.MILLISECONDS)) {
					if(count < 3) {
						count++;
						log.warn("trying to put a RabbitMQ message into buffer... try "+ count);
					}
					else {
						log.error("message from RabbitMQ lost " + body);
					}
				}
			} catch (InterruptedException e) {
				log.error(e.getMessage());
				throw new RuntimeException(e);
			}
		}

	}

	public static void main(String[] args) throws Exception {
		BlockingQueue<byte[]> buffer = new ArrayBlockingQueue<>(100);

		RabbitMQSubscriber sub = RabbitMQSubscriber.builder()
				.withBuffer(buffer)
				.withExchangeDurable(true)
				.withExchangeName("fluentd.fanout")
				.withExchangeType(FANOUT)
				//.withHost("10.0.75.1")
				.withHost("10.66.66.22")
				.withRoutingKey("")
				.build();
		int i = 0;

		while(true) {

			try{
				Stopwatch watch = Stopwatch.createStarted();
				SymptomEvent symptom = new SymptomEvent(buffer.take());
				
				System.err.println(symptom);
				
				long t1 = Long.parseLong(symptom.tryGet("timeMillis"));
				long t2 = System.currentTimeMillis();
				
				System.out.println(watch.stop() + " :: " + t1 + " :: " + t2 + " :: " + (t2-t1) + "ms");
				
			} catch(Exception e) {
				e.printStackTrace();
			}

		}
	}

}
