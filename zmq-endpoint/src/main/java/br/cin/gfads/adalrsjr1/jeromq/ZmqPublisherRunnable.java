package br.cin.gfads.adalrsjr1.jeromq;

import java.nio.channels.ClosedByInterruptException;
import java.util.concurrent.BlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;
import org.zeromq.ZMQException;
import org.zeromq.ZMQException.IOException;


public class ZmqPublisherRunnable extends ZmqRunnable {

    private static final Logger log = LoggerFactory.getLogger(ZmqPublisherRunnable.class);

    private String topic;
    private String address;
    ZmqPublisherRunnable(ZmqRunnableBuilder builder) {
    	super(builder);
    	this.topic = builder.getTopic();
 
    	socket = context.createSocket(builder.communicationPattern());
    	socket.setLinger(5000); // how long wait trying to send messages after socket close
    	address = builder.getAddress();
    	socket.bind(address);
    	
    }

	public void run() {
		log.info("Publisher[{}] {} running at thread {} in {}", topic, id, Thread.currentThread().getName(),
				                                                   address);
		byte[] topicByteArray = (topic + " ").getBytes();
		while(!stoped && !Thread.currentThread().isInterrupted()) {
			try {
				// send a message without use sendMore with topic
				byte[] rawMessage = (byte[]) channel.take();
				byte[] message = new byte[topicByteArray.length + rawMessage.length];
				System.arraycopy(topicByteArray, 0, message, 0, topicByteArray.length);
				System.arraycopy(rawMessage, 0, message, topicByteArray.length, rawMessage.length);
				socket.send(message);
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
