package br.cin.gfads.adalrsjr1.jeromq.worker;

import java.util.UUID;
import java.util.concurrent.BlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractWorker<I,O> implements Runnable {
	
	private static final Logger log = LoggerFactory.getLogger(AbstractWorker.class);
	
	protected final String id;
	
	protected BlockingQueue<I> inputChannel;
	protected BlockingQueue<O> outputChannel;
	
	protected boolean stopped = true;
	
	public AbstractWorker(BlockingQueue<I> inputBuffer, BlockingQueue<O> outputBuffer) {
		this(inputBuffer, outputBuffer, UUID.randomUUID().toString());
	}
	
	public AbstractWorker(BlockingQueue<I> inputBuffer) {
		this(inputBuffer, null, UUID.randomUUID().toString());
	}
	
	public AbstractWorker(BlockingQueue<I> inputChannel, BlockingQueue<O> outputChannel, String id) {
		log.info("Creating Worker with id: {}", id);
		this.id = id;
		this.inputChannel = inputChannel;
		this.outputChannel = outputChannel;
	}
	
	public AbstractWorker(BlockingQueue<I> inputChannel, String id) {
		this(inputChannel, null, id);
	}
	
	public boolean isRunning() {
		return !stopped;
	}
	
	public void stop() {
		stopped = true;
		log.info("Worker {} stoped.", id);
	}
	
	public String getId() {
		return id;
	}
	
	public void run() {
		log.info("Worker {} running at {}", id, Thread.currentThread().getName());
		while(isRunning() && !Thread.interrupted()) {
			I toProcess;
			try {
				toProcess = inputChannel.take();
				O toSend = process(toProcess);
				if(outputChannel != null) {
					outputChannel.offer(toSend);
				}
			} 
			catch(Exception e) {
				if(e instanceof InterruptedException) {
					Thread.currentThread().interrupt();
					log.warn(e.getClass().getSimpleName());
				} 
				else {
					e.printStackTrace();
				}
			}
		}
	}
	
	protected abstract O process(I toProcess); 
}
