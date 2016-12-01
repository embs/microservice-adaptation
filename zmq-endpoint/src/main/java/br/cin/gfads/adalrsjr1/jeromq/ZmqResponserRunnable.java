package br.cin.gfads.adalrsjr1.jeromq;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.nio.channels.ClosedByInterruptException;
import java.util.concurrent.BlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;
import org.zeromq.ZMQException;
import org.zeromq.ZMQException.IOException;
import org.zeromq.ZMsg;

public class ZmqResponserRunnable extends ZmqRunnable{

	private static final Logger log = LoggerFactory.getLogger(ZmqResponserRunnable.class);
	private String address;
	private String identity;
	ZmqResponserRunnable(ZmqRunnableBuilder builder) {
		super(builder);
		this.socket = context.createSocket(builder.communicationPattern()); // async reply
		this.address = builder.getAddress();
		this.socket.bind(address);
		try {
			this.identity = Inet4Address.getLocalHost().getHostAddress();
//			this.identity = System.nanoTime() + "";
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		socket.setIdentity(this.identity.getBytes());
	}
	
	public void run() {
		log.info("Responser {} running at thread {} in {}", id, Thread.currentThread().getName(), address);
		while(!stoped && !Thread.currentThread().isInterrupted()) {
			try {
				// http://zguide.zeromq.org/php:chapter3
				// http://zguide.zeromq.org/java:rtdealer
				socket.send(identity);
				ZMsg message = ZMsg.recvMsg(socket,0);
				byte[] content = message.getLast().getData();
				channel.offer(content);
				message.destroy();
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
