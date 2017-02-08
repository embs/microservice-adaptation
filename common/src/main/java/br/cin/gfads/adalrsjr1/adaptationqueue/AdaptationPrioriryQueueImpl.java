package br.cin.gfads.adalrsjr1.adaptationqueue;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.cin.gfads.adalrsjr1.common.events.ChangePlanEvent;

public class AdaptationPrioriryQueueImpl implements AdaptationPriorityQueue {

	private static final Logger log = LoggerFactory.getLogger(AdaptationPrioriryQueueImpl.class);

	private BlockingQueue<ChangePlanEvent> queue = new PriorityBlockingQueue<>();

	public AdaptationPrioriryQueueImpl() { }

	public void enqueue(ChangePlanEvent changePlan) {
		queue.offer(changePlan);
	}

	public void clear() {
		queue.clear();
	}
	
	public ChangePlanEvent remove() {
		ChangePlanEvent changePlan = null;
		try {
			changePlan = take();
		}
		catch(InterruptedException e) {
			log.error(e.getMessage());
			throw new RuntimeException(e);
		}
		finally {
			return changePlan;
		}
	}

	public ChangePlanEvent take() throws InterruptedException {
		return queue.take();
	}

	@Override
	public void start() {
		throw new RuntimeException(new NoSuchMethodException("method start useless at " + this.getClass()));
	}

	@Override
	public void stop() {
		queue.clear();
	}

}
