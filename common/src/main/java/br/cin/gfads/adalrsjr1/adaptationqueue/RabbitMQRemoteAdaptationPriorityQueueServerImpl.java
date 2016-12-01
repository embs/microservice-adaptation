package br.cin.gfads.adalrsjr1.adaptationqueue;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.cin.gfads.adalrsjr1.common.Util;
import br.cin.gfads.adalrsjr1.common.events.ChangePlanEvent;
import br.cin.gfads.adalrsjr1.endpoint.rabbitmq.RabbitMQConsumer;

public class RabbitMQRemoteAdaptationPriorityQueueServerImpl implements AdaptationPriorityQueue {

	private static final Logger log = LoggerFactory.getLogger(RabbitMQRemoteAdaptationPriorityQueueServerImpl.class);

	final private AdaptationPriorityQueue queue = new AdaptationPrioriryQueueImpl();
	
	private ExecutorService tPool = Executors.newSingleThreadExecutor(Util.threadFactory("adaptation-priority-queue-server")); 
	private RabbitMQConsumer consumer;
	private RabbitMQConsumer.Builder builder;
	private BlockingQueue<byte[]> buffer;
	
	private boolean stoped = false;
	
	public RabbitMQRemoteAdaptationPriorityQueueServerImpl(String host, int port, String queueName, boolean durable, BlockingQueue<byte[]> buffer) {
		this.buffer = buffer;
		builder = RabbitMQConsumer.builder()
				                   .withBuffer(buffer)
				                   .withDurable(durable)
				                   .withHost(host)
				                   .withPort(port)
				                   .withQueue(queueName);
	}
	
	@Override
	public void start() {
		consumer = builder.build();
		tPool.execute(() -> {
			ChangePlanEvent changePlan = new ChangePlanEvent();
			while(!stoped && !Thread.currentThread().isInterrupted()) {
				byte[] message;
				try {
					message = buffer.take();
					enqueue((ChangePlanEvent) changePlan.deserialize(message));
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
					throw new RuntimeException(e);
				}
			}
		}); 
	}
	
	@Override
	public void enqueue(ChangePlanEvent changePlan) {
		queue.enqueue(changePlan);
	}

	@Override
	public void stop() {
		consumer.stop();
		stoped = true;
		tPool.shutdown();
	}

	@Override
	public ChangePlanEvent remove() {
		return queue.remove();
	}

	@Override
	public ChangePlanEvent take() throws InterruptedException {
		return queue.take();
	}

}
