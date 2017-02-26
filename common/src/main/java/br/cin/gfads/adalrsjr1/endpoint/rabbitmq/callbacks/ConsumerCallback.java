package br.cin.gfads.adalrsjr1.endpoint.rabbitmq.callbacks;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import br.cin.gfads.adalrsjr1.endpoint.builder.EndpointCallback;
import br.cin.gfads.adalrsjr1.endpoint.builder.EndpointDriver;
import br.cin.gfads.adalrsjr1.endpoint.rabbitmq.MaverickBuffer;
import br.cin.gfads.adalrsjr1.endpoint.rabbitmq.RabbitmqDriver;

/**
 * JVM variables
 * 
 * timeout before a new try to put message into a buffer
 * -Drabbitmq.consumer.buffer.offer.timeout
 * 
 * number of attempts before drop down a message without put it into the buffer
 * -Drabbitmq.consumer.buffer.offer.attempts
 * 
 * @author adalrsjr1
 *
 */
public class ConsumerCallback extends EndpointCallback<DefaultConsumer> {
	private static final Logger log = LoggerFactory.getLogger(ConsumerCallback.class);
	private EndpointDriver driver;
	private Channel channel;
	
	public ConsumerCallback(EndpointDriver driver) {
		this.driver = driver;
	}
	
	@Override
	public DefaultConsumer getImpl() {
		DefaultConsumer consumer = new DefaultConsumer(channel) {
			private MaverickBuffer<byte[]> buffer = builder.getBuffer();
			private final int bufferOfferTimeout = Integer
					.parseInt(System.getProperty("rabbitmq.consumer.buffer.offer.timeout", "100"));
			private final int bufferAttempts = Integer
					.parseInt(System.getProperty("rabbitmq.consumer.buffer.offer.attempts", "3"));
			
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
					byte[] body) throws IOException {
				channel = ((RabbitmqDriver) driver).getChannel();
				try {
					int count = 0;
					while (!stoped && !buffer.offer(body, properties.getPriority(), bufferOfferTimeout, TimeUnit.MILLISECONDS)) {
						if (count < bufferAttempts) {
							count++;
							log.warn("trying to put a RabbitMQ message into buffer... try " + count);
						} else {
							log.error("message from RabbitMQ lost message::" + new String(body));
						}
					}
					 channel.basicAck(envelope.getDeliveryTag(), false);
				} catch (InterruptedException e) {
					log.error(e.getMessage());
					throw new RuntimeException(e);
				}
			}
		};

		return consumer;
	}

}
