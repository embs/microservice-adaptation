package br.cin.gfads.adalrsjr1.adaptationqueue;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zeromq.ZContext;

import br.cin.gfads.adalrsjr1.common.Util;
import br.cin.gfads.adalrsjr1.common.events.ChangePlanEvent;
import br.cin.gfads.adalrsjr1.jeromq.ZmqRequestorRunnable;
import br.cin.gfads.adalrsjr1.jeromq.ZmqRunnable;
import br.cin.gfads.adalrsjr1.jeromq.ZmqRunnable.ZmqCommunicationPattern;
import br.cin.gfads.adalrsjr1.jeromq.ZmqRunnable.ZmqProtocol;

public class RemoteAdaptationPriorityQueueClientImpl implements AdaptationPriorityQueueClient {

	private static final Logger log = LoggerFactory.getLogger(RemoteAdaptationPriorityQueueClientImpl.class);

	private ZContext zmqContext;
	private BlockingQueue<byte[]> channel;
	private ZmqRequestorRunnable requestor; 
	private ExecutorService tPool = Executors.newFixedThreadPool(1, Util.threadFactory("adaptation-priority-queue-client"));
	
	public RemoteAdaptationPriorityQueueClientImpl(String host, int port, ZmqProtocol protocol) {
		zmqContext = new ZContext(1);
		channel = new LinkedBlockingQueue<>();
		requestor = (ZmqRequestorRunnable) ZmqRunnable.builder(zmqContext, channel)
											   .setId("remote-adaptation-priority-queue-client")
											   .setHost(host)
											   .setPort(port)
											   .setProtocol(protocol)
											   .setCommunicationPattern(ZmqCommunicationPattern.AREQ)
											   .build();
	}

	@Override
	public void enqueue(ChangePlanEvent changePlan) {
		channel.offer(changePlan.serialize());
	}
	
	public void stop() {
		requestor.shutdown();
		zmqContext.close();
	}

	@Override
	public void start() {
		tPool.execute(requestor);
	}

}
