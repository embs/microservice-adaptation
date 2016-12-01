package br.cin.gfads.adalrsjr1.jeromq;

import java.nio.channels.ClosedByInterruptException;
import java.util.concurrent.BlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;
import org.zeromq.ZMQException;
import org.zeromq.ZMQException.IOException;
import org.zeromq.ZSocket;

public class ZmqSubscriberRunnable extends ZmqRunnable {

    private static final Logger log = LoggerFactory.getLogger(ZmqSubscriberRunnable.class);

    private String topic;
    private String address;
    ZmqSubscriberRunnable(ZmqRunnableBuilder builder) {
    	super(builder);
    	this.topic = builder.getTopic();
    	socket = context.createSocket(builder.communicationPattern());
    	socket.subscribe(topic.getBytes());
    	address = builder.getAddress();
    	socket.connect(address);
    }
    
	public void run() {
		log.info("Subscriber[{}] {} running at thread {} in {}", topic, id, Thread.currentThread().getName(), address);
		while(!stoped && !Thread.currentThread().isInterrupted()) {
			try {
				// do something
				// recv(0) : blocking
				// recv(ZMQ.DONTWAIT : non-blocking
				byte[] raw = socket.recv(0);
				while(raw != null && raw.length > 0) {
					channel.offer(raw);
					raw = null;
				}
			}
			catch(Exception e) {
				log.warn(e.getClass().getSimpleName());
				if(e instanceof InterruptedException || e instanceof ClosedByInterruptException || e instanceof RuntimeException) {
					Thread.currentThread().interrupt();
				}
				else {
					e.printStackTrace();
				}
				socket.close();
			}
		}
	}

}
