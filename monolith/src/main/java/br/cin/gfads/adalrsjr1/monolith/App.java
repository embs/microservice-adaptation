package br.cin.gfads.adalrsjr1.monolith;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Stopwatch;

import br.cin.gfads.adalrsjr1.adaptationqueue.AdaptationPriorityQueue;
import br.cin.gfads.adalrsjr1.adaptationqueue.AdaptationPriorityQueueClient;
import br.cin.gfads.adalrsjr1.adaptationqueue.RabbitMQRemoteAdaptationPriorityQueueClientImpl;
import br.cin.gfads.adalrsjr1.adaptationqueue.RabbitMQRemoteAdaptationPriorityQueueServerImpl;
import br.cin.gfads.adalrsjr1.adaptionmanager.AdaptationManager;
import br.cin.gfads.adalrsjr1.adaptionmanager.AdaptationManagerConfiguration;
import br.cin.gfads.adalrsjr1.adaptionmanager.engine.context.Context;
import br.cin.gfads.adalrsjr1.adaptionmanager.engine.context.ContextImpl;
import br.cin.gfads.adalrsjr1.adaptionmanager.repository.ActionsRepository;
import br.cin.gfads.adalrsjr1.common.Util;
import br.cin.gfads.adalrsjr1.common.events.ChangePlanEvent;
import br.cin.gfads.adalrsjr1.common.events.SymptomEvent;
import br.cin.gfads.adalrsjr1.planner.Planner;
import br.cin.gfads.adalrsjr1.planner.policy.PolicyRepository;
import br.cin.gfads.adalrsjr1.planner.policy.PolicyRepositoryFileImpl;
import br.cin.gfads.adalrsjr1.verifier.PropertyInstance;
import br.cin.gfads.adalrsjr1.verifier.processingunits.instances.OneByOneProcessingUnit;
import br.cin.gfads.adalrsjr1.verifier.processingunits.wrappers.RabbitMQProcessingUnitWrapper;
import br.cin.gfads.adalrsjr1.verifier.properties.ReqRespProperty;
import br.cin.gfads.adalrsjr1.verifier.properties.ResponseTimeProperty;

public class App {
	private static final Logger log = LoggerFactory.getLogger(AdaptationManager.class);
	public static final AdaptationManagerConfiguration CONFIG = AdaptationManagerConfiguration.getInstance();
	
	static void newAdaptationManager() {
		Stopwatch watch = Stopwatch.createStarted();
		BlockingQueue<byte[]> buffer = new LinkedBlockingQueue<>();
		
		AdaptationPriorityQueue queue = 
				new RabbitMQRemoteAdaptationPriorityQueueServerImpl("10.66.66.22", 
																	5672, 
																	"adaptationmanager.priorityqueue", 
																	false, 
																	buffer);
		queue.start();
		
		Context context = new ContextImpl();
		ActionsRepository repository = new ActionsRepository();
		
		AdaptationManager manager = new AdaptationManager(queue, context, repository);
		manager.init();
		log.info("AdatationManager started in {}", watch.stop());
	}
	
	static void newPlanner() {
		AdaptationPriorityQueueClient queue = 
				new RabbitMQRemoteAdaptationPriorityQueueClientImpl("10.66.66.22", 
																    5672, 
																    false, 
																    "adaptationmanager.priorityqueue");
		queue.start();
		queue.enqueue(new ChangePlanEvent());
		PolicyRepository repository = new PolicyRepositoryFileImpl();

		Planner planner = new Planner(queue, repository);
		planner.start();
	}
	
	static void newRequestResponse() throws InterruptedException {
		BlockingQueue<byte[]> buffer = new ArrayBlockingQueue<>(100);
		PropertyInstance p = new ReqRespProperty(
				"G(((client:(.)*) && (container_name:.client(.)*)) ->F((client:(.)*) && (container_name:.server(.)*)))");
		OneByOneProcessingUnit pu = new OneByOneProcessingUnit(p);
		RabbitMQProcessingUnitWrapper wrapper = new RabbitMQProcessingUnitWrapper(
				pu);

		ExecutorService executor = Executors.newSingleThreadExecutor(
				Util.threadFactory("rabbitmq-wrapper-processing-unit-reqresp"));
		executor.execute(wrapper);

		while (true)
			pu.toEvaluate(new SymptomEvent(buffer.take()));
	}
	
	static void newResponseTime() throws InterruptedException {
		BlockingQueue<byte[]> buffer = new ArrayBlockingQueue<>(100);

		PropertyInstance p = new ResponseTimeProperty(10);
		OneByOneProcessingUnit pu = new OneByOneProcessingUnit(p);

		RabbitMQProcessingUnitWrapper wrapper = new RabbitMQProcessingUnitWrapper(pu);
		
		ExecutorService executor = Executors.newSingleThreadExecutor(Util.threadFactory("rabbitmq-wrapper-processing-unit-processingtime"));
		executor.execute(wrapper);

		while(true)
			pu.toEvaluate(new SymptomEvent(buffer.take()));
	}
	
	public static void main(String[] args) {
		ExecutorService tPool = Executors.newFixedThreadPool(4);
		
		tPool.execute(() -> {
			newAdaptationManager();
		});
		
		tPool.execute(() -> {
			newPlanner();
		});
		
		tPool.execute(() -> {
			try {
				newRequestResponse();
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		});
		
		tPool.execute(() -> {
			try {
				newResponseTime();
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		});
	}
}
