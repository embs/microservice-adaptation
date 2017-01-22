package br.cin.gfads.adalrsjr1.planner;

import java.util.List;
import java.util.concurrent.BlockingQueue;
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

	static final private BlockingQueue<byte[]> channel = new LinkedBlockingQueue<>();

	private ReceiverEndpoint endpoint;
	private boolean stopped = true;
	private ExecutorService tPool = Executors.newSingleThreadExecutor(Util.threadFactory("planner-tPool-%d"));

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
		tPool.execute(() -> {
			ChangeRequestEvent changeEvent = new ChangeRequestEvent();
			log.info("Planner started");
			while(!stopped) {
				byte[] input;
				try {
					input = channel.take();
					System.err.println(input);
					if(input != null) {
						handle((ChangeRequestEvent) changeEvent.deserialize(input));
					}
				} catch (InterruptedException e) {
					throw new RuntimeException(e);
				} catch (NullPointerException ex) {
					log.warn(ex.getMessage());
					continue;
				}
				
			}
		});
		stopped = false;
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

	public static void main(String[] args) throws InterruptedException {
		AdaptationPriorityQueueClient queue = 
				new RabbitMQRemoteAdaptationPriorityQueueClientImpl(CONFIG.adaptationHost, 
																    CONFIG.port, 
																    false, 
																    "adaptationmanager.priorityqueue");
		queue.start();
//		queue.enqueue(new ChangePlanEvent());
		PolicyRepository repository = new PolicyRepositoryFileImpl();

		Planner planner = new Planner(queue, repository);
		planner.start();

//		RabbitMQProducer producer = RabbitMQProducer.builder()
//				.withDurable(CONFIG.rabbitmqQueueDurable)
//				.withHost(CONFIG.host)
//				.withPort(CONFIG.port)
//				.withQueue(CONFIG.rabbitmqQueueName) 
//				.build();
//
//		ExecutorService tpool = Executors.newCachedThreadPool();
//
//		tpool.execute(() -> {
//			Random r = new Random();
//			long x = 1000;
//			for(;;) {
//				int n = r.nextInt(2);
//				System.out.println("changeRequest"+n);
//				ChangeRequestEvent ev = new ChangeRequestEvent("changeRequest"+n, null, new HashMap<>());
//				ev.setTime(System.nanoTime());
//				producer.send(ev.serialize());
//				try {
//					Thread.sleep(x);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//			}
//		});
//
//		tpool.shutdown();
	}



}
