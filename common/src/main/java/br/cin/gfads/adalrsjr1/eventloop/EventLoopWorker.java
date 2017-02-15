package br.cin.gfads.adalrsjr1.eventloop;

import java.util.concurrent.BlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.cin.gfads.adalrsjr1.eventloop.events.EventLoopEvent;

abstract public class EventLoopWorker implements Runnable {
	protected static final Logger log = LoggerFactory.getLogger(EventLoopWorker.class);

	private EventLoopWorkerListener listener;

	protected boolean stoped = false;
	protected BlockingQueue buffer;

	protected EventLoopWorker(BlockingQueue buffer) {
		this.buffer = buffer;
	}

	void notifyListener(EventLoopEvent event) {
		if (listener != null) {
			listener.handle(event);
		}
	}

	void setListener(EventLoopWorkerListener listener) {
		this.listener = listener;
	}

	void unsetListener() {
		listener = null;
	}

	void stop() {
		log.info("stoping EventLoopWorker " + this);
		stoped = true;
		notifyListener(new EventLoopEvent.StopEvent(this));
		unsetListener();
	}

	void resume() {
		log.info("resuming EventLoopWorker " + this);
		stoped = false;
	}

	@Override
	public void run() {
		log.info("running EventLoopWorker " + this);
		Object obj = null;
		while (!stoped && !Thread.currentThread().isInterrupted()) {
			try {
				obj = buffer.take();
				process(obj);
			} catch (InterruptedException e) {
				log.error(obj.toString() + " caused::");
				log.error(e.getMessage());
				notifyListener(new EventLoopEvent.ExceptionEvent(this, e));
				Thread.currentThread().interrupt();
				stop();
			} catch (RuntimeException e) {
				log.error(obj.toString() + " caused::");
				log.error(e.getMessage());
				notifyListener(new EventLoopEvent.ExceptionEvent(this, e));
				continue;
			}
		}
	}

	abstract protected void process(Object object);
}
