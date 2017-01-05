package br.cin.gfads.adalrsjr1.adaptionmanager;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Stopwatch;

import br.cin.gfads.adalrsjr1.adaptationqueue.AdaptationPriorityQueue;
import br.cin.gfads.adalrsjr1.adaptationqueue.RabbitMQRemoteAdaptationPriorityQueueServerImpl;
import br.cin.gfads.adalrsjr1.adaptionmanager.commands.Script;
import br.cin.gfads.adalrsjr1.adaptionmanager.engine.ScriptEngine;
import br.cin.gfads.adalrsjr1.adaptionmanager.engine.context.Context;
import br.cin.gfads.adalrsjr1.adaptionmanager.engine.context.ContextImpl;
import br.cin.gfads.adalrsjr1.adaptionmanager.engine.context.events.ContextEvent;
import br.cin.gfads.adalrsjr1.adaptionmanager.engine.context.events.ContextListener;
import br.cin.gfads.adalrsjr1.adaptionmanager.repository.ActionsRepository;
import br.cin.gfads.adalrsjr1.common.Util;
import br.cin.gfads.adalrsjr1.common.events.ChangePlanEvent;

public class AdaptationManager implements AutoCloseable, ContextListener {

	private static class AdaptationQueueWorker implements Runnable, AutoCloseable {
		private boolean stopped = false;
		private AdaptationManager manager;
		
		private AdaptationQueueWorker(AdaptationManager manager) {
			this.manager = manager;
		}
		
		public void stop() {
			stopped = true;
		}

		@Override
		public void run() {
			while(!stopped && !Thread.currentThread().isInterrupted()) {
				try {
					ChangePlanEvent changePlan = manager.queue.take();
					manager.execute(changePlan);
				}
				catch (InterruptedException e) {
					Thread.currentThread().interrupt();
					log.warn(e.getMessage());
				}
			}
		}

		@Override
		public void close() throws Exception {
			stop();
		}
	}
	
	private static final Logger log = LoggerFactory.getLogger(AdaptationManager.class);
	public static final AdaptationManagerConfiguration CONFIG = AdaptationManagerConfiguration.getInstance();
	
	
	private AdaptationPriorityQueue queue;
	private ScriptEngine engine;
	private ActionsRepository repository;
	
	private AdaptationQueueWorker worker;
	private ExecutorService tPool;
	private Context context;
	
	public AdaptationManager(AdaptationPriorityQueue queue, Context context, ActionsRepository repository) {
		Stopwatch watch = Stopwatch.createStarted();
		this.queue = queue;
		this.repository = repository;
		this.context = context;
		this.engine = new ScriptEngine(this.context);
		worker = new AdaptationQueueWorker(this);
		
		tPool = Executors.newCachedThreadPool(Util.threadFactory("adaptation-manager-%d"));
		log.info("Adaptaiton manager instantiated in {}", watch.stop());
	}
	
	public void init() {
		this.context.addListener(this);
		repository.load();
		tPool.execute(worker);
		queue.start();
		log.info("Adaptation manager started");
	}
	
	public void stop() {
		queue.stop();
		context.removeListener(this);
		worker.stop();
		tPool.shutdown();
		log.info("Adaptation manager stopped");
	}
	
	@Override
	public void close() throws Exception {
		stop();
	}
	
	private void execute(ChangePlanEvent changePlan) {
		Stopwatch watch = Stopwatch.createStarted();
		
		Script script = repository.getAdaptationScript(changePlan.getAdaptationScript());
		System.err.println((System.currentTimeMillis()-changePlan.getTime()));
		Util.mavericLog(log, this.getClass(), "execute-workflow-before-adapt", (System.currentTimeMillis()-changePlan.getTime()));
		if(script != null) {	
			engine.execute(script);
		}
		else {
			log.warn("No adaptation script with name {}", changePlan.getAdaptationScript());
		}
		Util.mavericLog(log, this.getClass(), "execute-adaption", watch.stop());
	}
	
	@Override
	public void process(ContextEvent event) {
		System.err.println(event.toString());
	}
	
	public static void main(String[] args) throws Exception {
		Stopwatch watch = Stopwatch.createStarted();
		BlockingQueue<byte[]> buffer = new LinkedBlockingQueue<>();
		
		AdaptationPriorityQueue queue = 
				new RabbitMQRemoteAdaptationPriorityQueueServerImpl(CONFIG.host, 
																	CONFIG.port, 
																	CONFIG.rabbitmqPriorityQueueName, 
																	CONFIG.rabbitmqPriorityDurable, 
																	buffer);
		queue.start();
		
		Context context = new ContextImpl();
		ActionsRepository repository = new ActionsRepository();
		
		AdaptationManager manager = new AdaptationManager(queue, context, repository);
		manager.init();
		log.info("AdatationManager started in {}", watch.stop());
	}

	

}
