package br.cin.gfads.adalrsjr1.verifier.processingunits;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Stopwatch;

import br.cin.gfads.adalrsjr1.common.events.ChangeRequestEvent;
import br.cin.gfads.adalrsjr1.common.events.SymptomEvent;
import br.cin.gfads.adalrsjr1.verifier.PropertyInstance;

abstract public class AbstractProcessingUnit implements ProcessingUnit {

	private static final Logger log = LoggerFactory.getLogger(AbstractProcessingUnit.class);

	protected BlockingQueue<SymptomEvent> symptomBuffer;
	protected List<ProcessingUnitListener> listeners;

	protected AtomicInteger count;
	protected Stopwatch watch;
	protected CountDownLatch latch;
	protected boolean stoped;

	protected PropertyInstance property;

	public AbstractProcessingUnit(PropertyInstance property) {
		this(Integer.MAX_VALUE, property);
	}

	public AbstractProcessingUnit(int bufferSize, PropertyInstance property) {
		latch = new CountDownLatch(1);
		stoped = true;
		count = new AtomicInteger();

		listeners = new ArrayList<>();
		symptomBuffer = new LinkedBlockingQueue<>(bufferSize);

		this.property = property;

		watch = Stopwatch.createUnstarted();
		log.trace("Processing Unit waiting to be started...");
	}


	@Override
	public void addListener(ProcessingUnitListener listener) {
		listeners.add(listener);
	}

	@Override
	public void removeListener(ProcessingUnitListener listener) {
		listeners.remove(listener);
	}

	@Override
	public synchronized void start() {
		watch.start();
		stoped = false;
		latch.countDown();

		log.trace("Processing Unit started...");
	}

	@Override
	public synchronized void stop() {
		stoped = true;
		watch.stop();
		log.trace("Processing Unit stoped.");
	}

	@Override
	public void run() {

		try {
			latch.await();

			while(!stoped && !Thread.currentThread().isInterrupted()) {
				Stopwatch watch = Stopwatch.createStarted();
				ChangeRequestEvent changeRequest;
				
				if(symptomBuffer.isEmpty()) {
					synchronized (this) {
						log.trace("buffer empty, waiting for event...");
						this.wait();
					}
				}
				changeRequest = evaluate();
				log.trace("evaluation result {}",changeRequest);
				if(changeRequest != ChangeRequestEvent.getNullChangeRequestEvent()) {
					listeners.stream().forEach(l -> {
						l.notify(changeRequest);
					});
				}
				log.info("evaluation of property {} performed in {}", property.getName(), watch.stop());
			}

		}
		catch(InterruptedException e) {
			Thread.currentThread().interrupt();
			log.error(e.getMessage());
		}
	}

	@Override
	public synchronized boolean toEvaluate(SymptomEvent symptom) {
		
		if(symptomBuffer.isEmpty()) {
			log.trace("buffer is not empty, unblocking thread...");
			this.notify();
		}
		
		boolean result = symptomBuffer.offer(symptom);
		if(result) {
			count.incrementAndGet();
		}
		
		return result;
	}

	@Override
	abstract public ChangeRequestEvent evaluate() throws InterruptedException;

	@Override
	public int getCounter() {
		return count.get();
	}

	@Override
	public void resetCounter() {
		count.set(0);
	}

	@Override
	public void setCounter(int n) {
		count.set(n);
	}

	@Override
	public long elapsedTime() {
		return watch.elapsed(TimeUnit.MILLISECONDS);
	}

	@Override
	public void resetTimer() {
		watch.reset();
		watch.start();
	}

}
