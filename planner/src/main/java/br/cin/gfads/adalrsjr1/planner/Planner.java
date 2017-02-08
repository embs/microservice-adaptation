package br.cin.gfads.adalrsjr1.planner;

import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Stopwatch;

import br.cin.gfads.adalrsjr1.adaptationqueue.AdaptationPriorityQueueClient;
import br.cin.gfads.adalrsjr1.adaptationqueue.RabbitMQRemoteAdaptationPriorityQueueClientImpl;
import br.cin.gfads.adalrsjr1.common.Util;
import br.cin.gfads.adalrsjr1.common.events.ChangePlanEvent;
import br.cin.gfads.adalrsjr1.common.events.ChangeRequestEvent;
import br.cin.gfads.adalrsjr1.endpoint.ReceiverEndpoint;
import br.cin.gfads.adalrsjr1.endpoint.rabbitmq.RabbitMQConsumer;
import br.cin.gfads.adalrsjr1.planner.policy.Policy;
import br.cin.gfads.adalrsjr1.planner.policy.PolicyRepository;
import br.cin.gfads.adalrsjr1.planner.policy.PolicyRepositoryFileImpl;

@Singleton
public class Planner  {

	private static final Logger log = LoggerFactory.getLogger(Planner.class);
	public static final PlannerConfiguration CONFIG = PlannerConfiguration.getInstance();

	private AdaptationPriorityQueueClient queue;
	private PolicyRepository repository;

	//static final private BlockingQueue<byte[]> channel = new LinkedBlockingQueue<>();
	static final private BlockingQueue<byte[]> channel = new LinkedBlockingQueue<>(100);

	private ReceiverEndpoint endpoint;
	private boolean stopped = true;
	//private ExecutorService tPool = Executors.newSingleThreadExecutor(Util.threadFactory("planner-tPool-%d"));
	private ExecutorService tPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() + 1, Util.threadFactory("planner-tPool-%d"));

	public Planner(AdaptationPriorityQueueClient queue, PolicyRepository repository) {
		Stopwatch watch = Stopwatch.createStarted();
		this.queue = queue;
		this.repository = repository;

		endpoint = RabbitMQConsumer.builder()
				.withBuffer(channel)
				.withDurable(CONFIG.rabbitmqQueueDurable)
				.withHost(CONFIG.host)
				.withPort(CONFIG.port)
				.withQueue(CONFIG.rabbitmqQueueName)
				.build();
		log.info("Planner created in {}", watch.stop());
	}

	public void start() {
		ChangeRequestEvent changeEvent = new ChangeRequestEvent();
		log.info("Planner started");
		stopped = false;
		while(!stopped) {
			byte[] input;
			try {
				input = channel.take();
				tPool.execute(() -> {
					try {

						if(input != null) {
							Object obj = changeEvent.deserialize(input);
							handle((ChangeRequestEvent) obj);
						}
					} catch (NullPointerException ex) {
						log.error(ex.getMessage());
						throw new RuntimeException(ex);
					}
				});
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				log.error(e.getMessage());
				throw new RuntimeException(e);
			}
		}

	}

	public void stop() {
		endpoint.stop();
		stopped = true;
		tPool.shutdown();
		log.info("Planner stopped");
	}

	public void handle(ChangeRequestEvent changeRequest) {
		Stopwatch watch = Stopwatch.createStarted();
		log.debug("changeRequest: {}", changeRequest.getName());
		List<Policy> policies = repository.fetchAdaptationPlans(changeRequest);
		while(!policies.isEmpty()) {
			Policy policy = policies.remove(0);
			ChangePlanEvent event = new ChangePlanEvent(policy, policy.getPriority(), policy.getAction());
			event.setTime(changeRequest.getTime());
			enqueue(event);
		}
		Util.mavericLog(log, this.getClass(), "handle", watch.stop());
	}

	void enqueue(ChangePlanEvent changePlan) {
		queue.enqueue(changePlan);
	}

	public static void main(String[] args) throws Exception {
		AdaptationPriorityQueueClient queue = 
				new RabbitMQRemoteAdaptationPriorityQueueClientImpl(CONFIG.host, 
						5672, 
						false, 
						"adaptationmanager.priorityqueue");
		queue.start();
		PolicyRepository repository = new PolicyRepositoryFileImpl();

		Planner planner = new Planner(queue, repository);
		planner.start();

	}



}
