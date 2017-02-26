package br.cin.gfads.adalrsjr1.endpoint.rabbitmq;

import static br.cin.gfads.adalrsjr1.endpoint.rabbitmq.ExchangeType.FANOUT;

import java.io.IOException;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import br.cin.gfads.adalrsjr1.endpoint.SenderEndpoint;
import br.cin.gfads.adalrsjr1.endpoint.rabbitmq.RabbitMQSubscriber.Builder;

public class RabbitMQPublisher implements SenderEndpoint {

	private static final Logger log = LoggerFactory.getLogger(RabbitMQPublisher.class);

	public static class Builder {
		private String host;
		private String routingKey;
		private String exchangeName;
		private boolean exchangeDurable;
		private ExchangeType exchangeType;
		private int port = 5672;

		public int getPort() {
			return port;
		}

		public Builder withPort(int port) {
			this.port = port;
			return this;
		}

		public RabbitMQPublisher build() {
			RabbitMQPublisher publisher = null;
			try {
				publisher = new RabbitMQPublisher(this);
			} catch (RuntimeException e) {
				Thread.sleep(1000);
				publisher = build();
			} finally {
				return publisher;
			}

		}

		public String getHost() {
			return host;
		}

		public String getRoutingKey() {
			return routingKey;
		}

		public String getExchangeName() {
			return exchangeName;
		}

		public boolean isExchangeDurable() {
			return exchangeDurable;
		}

		public ExchangeType getExchangeType() {
			return exchangeType;
		}

		public Builder withHost(String host) {
			this.host = host;
			return this;
		}

		public Builder withRoutingKey(String routingKey) {
			this.routingKey = routingKey;
			return this;
		}

		public Builder withExchangeName(String exchangeName) {
			this.exchangeName = exchangeName;
			return this;
		}

		public Builder withExchangeDurable(boolean exchangeDurable) {
			this.exchangeDurable = exchangeDurable;
			return this;
		}

		public Builder withExchangeType(ExchangeType exchangeType) {
			this.exchangeType = exchangeType;
			return this;
		}
	}

	public static RabbitMQPublisher.Builder builder() {
		return new RabbitMQPublisher.Builder();
	}

	private ConnectionFactory factory;
	private Connection connection;
	private Channel channel;

	private String exchangeName;
	private String routingKey;

	private RabbitMQPublisher(RabbitMQPublisher.Builder builder) {
		this.exchangeName = builder.getExchangeName();
		this.routingKey = builder.getRoutingKey();
		factory = new ConnectionFactory();
		factory.setAutomaticRecoveryEnabled(true);
		factory.setHost(builder.getHost());
		factory.setPort(builder.getPort());

		try {
			connection = factory.newConnection();
			channel = connection.createChannel();

			channel.exchangeDeclare(getExchangeName(), builder.getExchangeType()
				.name()
				.toLowerCase(), builder.isExchangeDurable());
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new RuntimeException(e);
		}
	}

	@Override
	public void stop() {
		try {
			channel.close();
			connection.close();
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new RuntimeException(e);
		}
	}

	@Override
	public Object send(byte[] message) {
		boolean result = false;
		try {
			channel.basicPublish(getExchangeName(), getRoutingKey(), null, message);
			result = true;
		} catch (IOException e) {
			log.error(e.getMessage());
			throw new RuntimeException(e);
		} finally {
			return result;
		}
	}

	public String getExchangeName() {
		return exchangeName;
	}

	public String getRoutingKey() {
		return routingKey;
	}

	public static void main(String[] args) {
		// ("10.0.75.1", "", "java-test", FANOUT, false);
		RabbitMQPublisher pub = RabbitMQPublisher.builder()
			.withExchangeDurable(false)
			.withExchangeName("java-test")
			.withExchangeType(FANOUT)
			.withRoutingKey("")
			.withHost("10.0.75.1")
			.build();

		try (Scanner scanner = new Scanner(System.in)) {
			while (scanner.hasNext()) {
				String message = scanner.nextLine();
				if (message.equals("quit"))
					break;
				for (int i = 0; i < 100; i++)
					pub.send(("" + i).getBytes());
				System.err.println("[X] Sent >>> " + message);
			}
		} finally {
			pub.stop();
		}
	}

	@Override
	public Object send(byte[] message, int priority) {
		throw new UnsupportedOperationException("priority still is not implemented yet");
	}

}
