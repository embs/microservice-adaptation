package br.cin.gfads.adalrsjr1.endpoint.rabbitmq;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeoutException;

import org.ietf.jgss.MessageProp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.MessageProperties;

import br.cin.gfads.adalrsjr1.endpoint.Endpoint;
import br.cin.gfads.adalrsjr1.endpoint.ReceiverEndpoint;
import br.cin.gfads.adalrsjr1.endpoint.SenderEndpoint;
import br.cin.gfads.adalrsjr1.endpoint.builder.EndpointBuilder;
import br.cin.gfads.adalrsjr1.endpoint.builder.EndpointCallback;
import br.cin.gfads.adalrsjr1.endpoint.builder.EndpointDriver;
import br.cin.gfads.adalrsjr1.endpoint.builder.TypeEndpoint;
import br.cin.gfads.adalrsjr1.endpoint.rabbitmq.callbacks.ConsumerCallback;

public class RabbitmqDriver<T extends Endpoint> implements EndpointDriver<T> {

	public static class ReceiverEndpointImpl implements ReceiverEndpoint {

		private Channel channel;
		private EndpointCallback<DefaultConsumer> callback;

		private ReceiverEndpointImpl(Channel channel, EndpointCallback<DefaultConsumer> callback) {
			this.channel = channel;
			this.callback = callback;
		}

		private static ReceiverEndpoint newConsumer(Channel channel, EndpointCallback<DefaultConsumer> callback) {
			return new ReceiverEndpointImpl(channel, callback);
		}

		private static ReceiverEndpoint newSubscriber(Channel channel, EndpointCallback<DefaultConsumer> callback) {
			return new ReceiverEndpointImpl(channel, callback);
		}

		@Override
		public void stop() {
			try {
				callback.stop();
				channel.close();
			} catch (IOException | TimeoutException e) {
				e.printStackTrace();
			}
		}

	}

	public static class SenderEndpointImpl implements SenderEndpoint {
		private Channel channel;
		private TypeEndpoint type;
		private EndpointBuilder builder;

		private SenderEndpointImpl(Channel channel, TypeEndpoint type, EndpointBuilder builder) {
			this.channel = channel;
			this.type = type;
			this.builder = builder;
		}

		private static SenderEndpoint newProducer(Channel channel, EndpointBuilder builder) {
			return new SenderEndpointImpl(channel, TypeEndpoint.PRODUCER, builder);
		}

		private static SenderEndpoint newPublisher(Channel channel, EndpointBuilder builder) {
			return new SenderEndpointImpl(channel, TypeEndpoint.PUBLISHER, builder);
		}

		@Override
		public void stop() {
			try {
				channel.close();
			} catch (IOException | TimeoutException e) {
				e.printStackTrace();
			}
		}

		@Override
		public Object send(byte[] message, int priority) {
			switch (type) {
			case PRODUCER:
				return sendProducer(message, priority);
			case PUBLISHER:
				return sendPublisher(message, priority);
			default:
				return null;
			}
		}
		
		@Override
		public Object send(byte[] message) {
			return send(message, 0);
		}

		private Object sendProducer(byte[] message, int priority) {
			boolean result = false;
			try {

				BasicProperties messageProperty = new BasicProperties("text/plain", null, null, 2, priority, null, null,
						null, null, null, null, null, null, null);
				channel.basicPublish("", builder.getQueueName(), messageProperty, message);
				result = true;
			} catch (IOException e) {
				log.error(e.getMessage());
				throw new RuntimeException(e);
			} finally {
				return result;
			}
		}

		private Object sendPublisher(byte[] message, int priority) {
			boolean result = false;
			try {
				BasicProperties messageProperty = new BasicProperties("text/plain", null, null, 2, priority, null, null,
						null, null, null, null, null, null, null);
				channel.basicPublish(builder.getQueueName(), builder.getTopic(),
						messageProperty, message);
				result = true;
			} catch (IOException e) {
				log.error(e.getMessage());
				throw new RuntimeException(e);
			} finally {
				return result;
			}
		}
	}

	private static final Logger log = LoggerFactory.getLogger(RabbitmqDriver.class);
	private ConnectionFactory factory = new ConnectionFactory();
	private Connection connection;
	private Channel channel;
	private EndpointBuilder<T> builder;

	@Override
	public void shutdown() {

		try {
			channel.close();
			connection.close();
			if (builder.getCallback() != null) {
				builder.getCallback()
					.stop();
			}
		} catch (IOException | TimeoutException e) {
			e.printStackTrace();
		}

	}

	@Override
	public T create(EndpointBuilder<T> builder) {
		this.builder = builder;
		factory.setAutomaticRecoveryEnabled(true);
		factory.setHost(builder.getAddress());
		factory.setPort(builder.getPort());
		factory.setHandshakeTimeout(5000);

		T endpoint = null;

		try {
			connection = factory.newConnection();
			channel = connection.createChannel();
			switch (builder.getType()) {
			case CONSUMER:
				createConsumer(builder.getQueueName(), builder.isDurable(), (DefaultConsumer) builder.getCallback()
					.getImpl(), builder.getMaxPriority());
				endpoint = (T) ReceiverEndpointImpl.newConsumer(channel, builder.getCallback());
				break;
			case PRODUCER:
				createProducer(builder.getQueueName(), builder.isDurable(), builder.getMaxPriority());
				endpoint = (T) SenderEndpointImpl.newProducer(channel, builder);
				break;
			case PUBLISHER:
				createPublisher(builder.getQueueName(), builder.isDurable(), builder.getMaxPriority());
				endpoint = (T) SenderEndpointImpl.newPublisher(channel, builder);
				break;
			case SUBSCRIBER:
				createSubscriber(builder.getQueueName(), builder.isDurable(), (DefaultConsumer) builder.getCallback()
					.getImpl(), builder.getTopic(), builder.getMaxPriority());
				endpoint = (T) ReceiverEndpointImpl.newSubscriber(channel, builder.getCallback());
				break;
			default:
				log.warn("Rabbitmq driver it was not set");
				break;
			}

		} catch (IOException | TimeoutException e) {
			e.printStackTrace();
		}

		return endpoint;
	}

	private void createConsumer(String queue, boolean durable, DefaultConsumer consumer, int maxPriority)
			throws IOException {
		Map<String, Object> args = new HashMap<>();
		args.put("x-max-priority", maxPriority);
		channel.queueDeclare(queue, durable, false, false, maxPriority > 0 ? args : null);
		channel.basicQos(1);
		channel.basicConsume(queue, false, consumer);
	}

	private void createProducer(String queue, boolean durable, int maxPriority) throws IOException {
		Map<String, Object> args = new HashMap<>();
		args.put("x-max-priority", maxPriority);
		channel.queueDeclare(queue, durable, false, false, maxPriority > 0 ? args : null);
	}

	private void createPublisher(String queue, boolean durable, int maxPriority) throws IOException {
		Map<String, Object> args = new HashMap<>();
		args.put("x-max-priority", maxPriority);
		channel.exchangeDeclare(queue, "topic", durable, false, maxPriority > 0 ? args : null);
	}

	private void createSubscriber(String queue, boolean durable, DefaultConsumer consumer, String topic,
			int maxPriority) throws IOException {
		Map<String, Object> args = new HashMap<>();
		args.put("x-max-priority", maxPriority);
		channel.exchangeDeclare(queue, "topic", durable, false, maxPriority > 0 ? args : null);
		String iqueue = channel.queueDeclare()
			.getQueue();
		channel.queueBind(iqueue, queue, topic);
		channel.basicConsume(iqueue, true, consumer);
	}

	public Channel getChannel() {
		return channel;
	}

	public static void main(String[] args) throws InterruptedException {
		EndpointDriver<Endpoint> driver1 = new RabbitmqDriver<>();
		EndpointBuilder<SenderEndpoint> producer = new EndpointBuilder<>();
		SenderEndpoint sender = producer.withAddress("localhost")
			.withPort(5672)
			.withDurable(false)
			.withQueueName("p-s")
			.withTopic("") // only use in pub-sub
			.withType(TypeEndpoint.PRODUCER)
			.withCallback(null)
			.withDriver(driver1)
			.withBuffer(null)
			.build();

		MaverickBuffer<byte[]> buffer = new MaverickBuffer<>(10);
		ExecutorService tpool = Executors.newCachedThreadPool();

		EndpointDriver<Endpoint> driver2 = new RabbitmqDriver<>();
		EndpointBuilder<ReceiverEndpoint> consumer1 = new EndpointBuilder<>();
		EndpointCallback receiverCallback1 = new ConsumerCallback(driver2);
		receiverCallback1.setBuilder(consumer1);
		ReceiverEndpoint receiver1 = consumer1.withAddress("localhost")
			.withPort(5672)
			.withDurable(false)
			.withQueueName("p-s")
			.withTopic("")
			.withType(TypeEndpoint.CONSUMER)
			.withCallback(receiverCallback1)
			.withDriver(driver2)
			.withBuffer(buffer)
			.build();

		EndpointDriver<Endpoint> driver3 = new RabbitmqDriver<>();
		EndpointBuilder<ReceiverEndpoint> consumer2 = new EndpointBuilder<>();
		EndpointCallback receiverCallback2 = new ConsumerCallback(driver3);
		receiverCallback2.setBuilder(consumer2);
		ReceiverEndpoint receiver2 = consumer2.withAddress("localhost")
			.withPort(5672)
			.withDurable(false)
			.withQueueName("p-s")
			.withTopic("")
			.withType(TypeEndpoint.CONSUMER)
			.withCallback(receiverCallback2)
			.withDriver(driver3)
			.withBuffer(buffer)
			.build();

		// EndpointDriver<Endpoint> driver4 = new RabbitmqDriver<>();
		// EndpointBuilder<ReceiverEndpoint> consumer3 = new
		// EndpointBuilder<>();
		// EndpointCallback receiverCallback3 = new ConsumerCallback(driver4);
		// receiverCallback3.setBuilder(consumer3);
		// ReceiverEndpoint receiver3 = consumer3.withAddress("localhost")
		// .withPort(5672)
		// .withDurable(false)
		// .withQueueName("p-s")
		// .withTopic("")
		// .withType(TypeEndpoint.CONSUMER)
		// .withCallback(receiverCallback3)
		// .withDriver(driver4)
		// .withBuffer(buffer)
		// .build();

		// tpool.execute(() -> {
		// Random r = new Random();
		// while(true) {
		// sender.send((""+System.currentTimeMillis()).getBytes());
		// try {
		// Thread.sleep((long) (10 * r.nextDouble()));
		// } catch (InterruptedException e) {
		// e.printStackTrace();
		// }
		// }
		// });

		Random r = new Random();
		double sum = 0.0;
		double count = 1.0;
		while (true) {
			try {
				sum += buffer.size();
				log.trace(new String(buffer.take()) + " " + (sum / count));
				count++;
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			try {
				Thread.sleep((long) (10 * r.nextDouble()));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		// driver1.shutdown();
		// driver2.shutdown();
		// driver3.shutdown();
		// buffer.clear();

	}
}
