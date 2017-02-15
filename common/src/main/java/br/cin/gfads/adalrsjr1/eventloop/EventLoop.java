package br.cin.gfads.adalrsjr1.eventloop;

import java.util.List;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.cin.gfads.adalrsjr1.common.Util;
import br.cin.gfads.adalrsjr1.eventloop.events.EventLoopEvent;

public class EventLoop implements EventLoopWorkerListener {

	private static final Logger log = LoggerFactory.getLogger(EventLoop.class);

	private ExecutorService tPool;
	private List<EventLoopWorker> workers;
	public final String name;

	public EventLoop(String name, int numberOfThreads, List<EventLoopWorker> workers) {
		this.name = name;
		tPool = Executors.newFixedThreadPool(numberOfThreads + 1, Util.threadFactory("eventLoop-" + name + "-%d"));

		this.workers = workers;

		if (workers.size() > numberOfThreads + 1) {
			log.warn("the number of workers [" + workers.size() + "] exceeds the number of [" + (numberOfThreads + 1)
					+ "] available threads ");
			log.warn("removing [" + (workers.size() - numberOfThreads + 1) + "] workers");
			for (int i = 0; i < workers.size() - numberOfThreads + 1; i++) {
				workers.remove(0);
			}
		}

		for (EventLoopWorker worker : this.workers) {
			worker.setListener(this);
			tPool.execute(worker);
		}
	}

	public EventLoop(String name, List<EventLoopWorker> workers) {
		this(name, Runtime.getRuntime().availableProcessors()+1, workers);
	}

	public void stop() {
		log.info("stoping EventLoop " + name);
		for (EventLoopWorker worker : this.workers) {
			worker.stop();
		}
	}

	public void stop(EventLoopWorker worker) {
		worker.stop();
	}

	public void resume() {
		log.info("resuming EventLoop " + name);
		for (EventLoopWorker worker : this.workers) {
			worker.setListener(this);
			worker.resume();
			tPool.execute(worker);
		}
	}

	public void resume(EventLoopWorker worker) {
		worker.resume();
		worker.setListener(this);
		tPool.execute(worker);
	}

	public void shutdown() {
		stop();
		log.info("shutdown EventLoop " + name);
		tPool.shutdown();
	}

	@Override
	public void handle(EventLoopEvent event) {
		if (event instanceof EventLoopEvent.ExceptionEvent) {
			if (!(((EventLoopEvent.ExceptionEvent) event).exception instanceof RuntimeException)) {
				EventLoopWorker worker = event.getSource();
				worker.resume();
				tPool.execute(worker);
			}
		} else {
			log.trace(event.toString());
		}
	}

	private static class TestEventLoopWorker extends EventLoopWorker {

		protected TestEventLoopWorker(BlockingQueue buffer) {
			super(buffer);
		}

		@Override
		protected void process(Object object) {
			Random u = new Random();
			double lambda = 1; // rate
			double t = Math.log(1 - u.nextDouble()) / (-1 * lambda);
			try {
				Thread.sleep((long) t);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			notifyListener(new EventLoopEvent.MessageEvent(this, (String) object + " [" + t + "] "));
		}

	}
}
