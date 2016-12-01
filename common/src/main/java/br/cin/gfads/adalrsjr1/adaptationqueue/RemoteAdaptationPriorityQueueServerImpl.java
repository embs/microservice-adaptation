package br.cin.gfads.adalrsjr1.adaptationqueue;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zeromq.ZContext;

import br.cin.gfads.adalrsjr1.common.Util;
import br.cin.gfads.adalrsjr1.common.events.ChangePlanEvent;
import br.cin.gfads.adalrsjr1.jeromq.ZmqResponserRunnable;
import br.cin.gfads.adalrsjr1.jeromq.ZmqRunnable;
import br.cin.gfads.adalrsjr1.jeromq.ZmqRunnable.ZmqCommunicationPattern;
import br.cin.gfads.adalrsjr1.jeromq.ZmqRunnable.ZmqProtocol;
import br.cin.gfads.adalrsjr1.jeromq.worker.AbstractWorker;

public class RemoteAdaptationPriorityQueueServerImpl implements AdaptationPriorityQueue {

	private static class QueueWorker extends AbstractWorker<byte[], ChangePlanEvent> {
		private RemoteAdaptationPriorityQueueServerImpl server;
		public QueueWorker(RemoteAdaptationPriorityQueueServerImpl server) {
			super(server.channel, "adaptation-queue-worker");
			this.server = server;
		}

		@Override
		protected ChangePlanEvent process(byte[] toProcess) {
			server.enqueue((ChangePlanEvent) new ChangePlanEvent().deserialize((byte[])toProcess));
			return null;
		}
		
	}
	
	private static final Logger log = LoggerFactory.getLogger(RemoteAdaptationPriorityQueueServerImpl.class);

	final private AdaptationPriorityQueue queue = new AdaptationPrioriryQueueImpl(); 
	
	private ZContext zmqContext;
	private BlockingQueue<byte[]> channel;
	private ZmqResponserRunnable responser; 
	
	private AbstractWorker<byte[], ChangePlanEvent> worker;
	private ExecutorService tPool = Executors.newFixedThreadPool(2, Util.threadFactory("adaptation-priority-queue-server"));
	
	public RemoteAdaptationPriorityQueueServerImpl(String host, int port, ZmqProtocol protocol) {
		zmqContext = new ZContext(1);
		channel = new LinkedBlockingQueue<>();
		responser = (ZmqResponserRunnable) ZmqRunnable.builder(zmqContext, channel)
				                               .setId("remote-adaptation-priority-queue-server")
											   .setHost(host)
											   .setPort(port)
											   .setProtocol(protocol)
											   .setCommunicationPattern(ZmqCommunicationPattern.AREP)
											   .build();
		worker = new QueueWorker(this);
		

	}
	
	public void start() {
		tPool.execute(responser);
		tPool.execute(worker);
	}
	
	@Override
	public void enqueue(ChangePlanEvent changePlan) {
		queue.enqueue(changePlan);
	}

	@Override
	public ChangePlanEvent remove() {
		return queue.remove();
	}

	@Override
	public ChangePlanEvent take() throws InterruptedException {
		return queue.take();
	}
	
	@Override
	public void stop() {
		responser.shutdown();
		worker.stop();
		zmqContext.close();
	}

	public static void main(String[] args) {
		AdaptationPriorityQueueClient client = new RemoteAdaptationPriorityQueueClientImpl("localhost", 5679, ZmqProtocol.TCP);
		RemoteAdaptationPriorityQueueServerImpl server = new RemoteAdaptationPriorityQueueServerImpl("localhost", 5679, ZmqProtocol.TCP);
		server.start();
		
		Random r = new Random();
		new Thread(() -> {
			while(true) {
				int i = r.nextInt(10);
				client.enqueue(new ChangePlanEvent(null, i, "teste"+i));
			}
		}).start();
		
		new Thread(() -> {
			while(true) {
				try {
					System.err.println(server.take());
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();
		
	}

	
}
