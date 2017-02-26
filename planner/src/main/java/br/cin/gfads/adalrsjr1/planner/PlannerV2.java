package br.cin.gfads.adalrsjr1.planner;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Stopwatch;

import br.cin.gfads.adalrsjr1.adaptationqueue.AdaptationPriorityQueueClient;
import br.cin.gfads.adalrsjr1.adaptationqueue.RabbitMQRemoteAdaptationPriorityQueueClientImpl;
import br.cin.gfads.adalrsjr1.common.Util;
import br.cin.gfads.adalrsjr1.common.events.ChangePlanEvent;
import br.cin.gfads.adalrsjr1.common.events.ChangeRequestEvent;
import br.cin.gfads.adalrsjr1.endpoint.Endpoint;
import br.cin.gfads.adalrsjr1.endpoint.ReceiverEndpoint;
import br.cin.gfads.adalrsjr1.endpoint.builder.EndpointBuilder;
import br.cin.gfads.adalrsjr1.endpoint.builder.EndpointCallback;
import br.cin.gfads.adalrsjr1.endpoint.builder.EndpointDriver;
import br.cin.gfads.adalrsjr1.endpoint.builder.TypeEndpoint;
import br.cin.gfads.adalrsjr1.endpoint.rabbitmq.MaverickBuffer;
import br.cin.gfads.adalrsjr1.endpoint.rabbitmq.RabbitmqDriver;
import br.cin.gfads.adalrsjr1.endpoint.rabbitmq.callbacks.ConsumerCallback;
import br.cin.gfads.adalrsjr1.eventloop.EventLoop;
import br.cin.gfads.adalrsjr1.eventloop.EventLoopWorker;
import br.cin.gfads.adalrsjr1.planner.policy.Policy;
import br.cin.gfads.adalrsjr1.planner.policy.PolicyRepository;
import br.cin.gfads.adalrsjr1.planner.policy.PolicyRepositoryFileImpl;

public class PlannerV2 {
	private static final Logger log = LoggerFactory.getLogger(PlannerV2.class);
	public static final PlannerConfigurationV2 CONFIG = PlannerConfigurationV2.getInstance();

	private AdaptationPriorityQueueClient queue;
	private MaverickBuffer<byte[]> buffer;
	private PolicyRepository repository;

	private ReceiverEndpoint endpoint;
	private EventLoop eventLoop;

	public PlannerV2(AdaptationPriorityQueueClient queue, PolicyRepository repository) {
		Stopwatch watch = Stopwatch.createStarted();
		this.repository = repository;
		this.buffer = new MaverickBuffer<>(CONFIG.bufferCapacity);
		log.info("Planner created in {}", watch.stop());

		List<EventLoopWorker> workers = new ArrayList<>(CONFIG.plannerWorkers);

		for (int i = 0; i < CONFIG.plannerWorkers; i++) {
			workers.add(new PlannerWorker(buffer, repository, queue));
		}

		eventLoop = new EventLoop("planner", workers);

		EndpointDriver<Endpoint> driver = new RabbitmqDriver<>();
		EndpointBuilder<ReceiverEndpoint> consumer = new EndpointBuilder<>();
		EndpointCallback receiverCallback = new ConsumerCallback(driver);
		receiverCallback.setBuilder(consumer);
		endpoint = consumer.withAddress(CONFIG.rabbitmqHost)
			.withPort(CONFIG.rabbitmqPort)
			.withDurable(CONFIG.plannerQueueDurable)
			.withQueueName(CONFIG.plannerQueue)
			.withType(TypeEndpoint.CONSUMER)
			.withCallback(receiverCallback)
			.withDriver(driver)
			.withBuffer(buffer)
			.build();
	}

	public void shutdown() {
		endpoint.stop();
		eventLoop.stop();
	}

	public static class PlannerWorker extends EventLoopWorker {
		private static final ChangeRequestEvent changeEvent = new ChangeRequestEvent();

		private PolicyRepository repository;
		private AdaptationPriorityQueueClient queue;

		protected PlannerWorker(BlockingQueue buffer, PolicyRepository repository,
				AdaptationPriorityQueueClient queue) {
			super(buffer);
			this.repository = repository;
			this.queue = queue;
		}

		@Override
		protected void process(Object object) {
			if (object != null) {
				Object obj = changeEvent.deserialize((byte[]) object);
				handle((ChangeRequestEvent) obj);
			}
		}

		private void handle(ChangeRequestEvent changeRequest) {
			Stopwatch watch = Stopwatch.createStarted();
			log.debug("changeRequest: {}", changeRequest.getName());
			List<Policy> policies = repository.fetchAdaptationPlans(changeRequest);
			while (!policies.isEmpty()) {
				Policy policy = policies.remove(0);
				ChangePlanEvent event = new ChangePlanEvent(policy, policy.getPriority(), policy.getAction());
				event.setTime(changeRequest.getTime());
				enqueue(event);
			}
			Util.mavericLog(log, this.getClass(), "handle", watch.stop());
		}

		private void enqueue(ChangePlanEvent changePlan) {
			queue.enqueue(changePlan);
		}

	}
	
	public static void main(String[] args) {
		AdaptationPriorityQueueClient queue = 
				new RabbitMQRemoteAdaptationPriorityQueueClientImpl(CONFIG.rabbitmqHost, 
						CONFIG.rabbitmqPort, 
						false, 
						CONFIG.adaptationQueue);
		queue.start();
		PolicyRepository repository = new PolicyRepositoryFileImpl();
		PlannerV2 planner = new PlannerV2(queue, repository);
	}
}
