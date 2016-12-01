package br.cin.gfads.adalrsjr1.adaptationqueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.cin.gfads.adalrsjr1.common.events.ChangePlanEvent;
import br.cin.gfads.adalrsjr1.endpoint.rabbitmq.RabbitMQProducer;

public class RabbitMQRemoteAdaptationPriorityQueueClientImpl implements AdaptationPriorityQueueClient {

	private static final Logger log = LoggerFactory.getLogger(RabbitMQRemoteAdaptationPriorityQueueClientImpl.class);

	private RabbitMQProducer producer;
	private RabbitMQProducer.Builder builder;
	
	public RabbitMQRemoteAdaptationPriorityQueueClientImpl(String host, int port, boolean durable, String queueName) {
		builder = RabbitMQProducer.builder()
				                   .withDurable(durable)
				                   .withHost(host)
				                   .withPort(port)
				                   .withQueue(queueName);
	}
	
	@Override
	public void enqueue(ChangePlanEvent changePlan) {
		producer.send(changePlan.serialize());
	}

	@Override
	public void start() {
		producer = builder.build();
	}

	@Override
	public void stop() {
		producer.stop();
	}

}
