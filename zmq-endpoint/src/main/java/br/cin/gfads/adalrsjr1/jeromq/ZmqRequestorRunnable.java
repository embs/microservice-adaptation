package br.cin.gfads.adalrsjr1.jeromq;

import java.nio.channels.ClosedByInterruptException;
import java.util.concurrent.BlockingQueue;

import org.codehaus.groovy.control.messages.SyntaxErrorMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Context;
import org.zeromq.ZMQ.Socket;
import org.zeromq.ZMQException.IOException;

import com.google.common.base.Stopwatch;

import org.zeromq.ZMQException;
import org.zeromq.ZSocket;

public class ZmqRequestorRunnable extends ZmqRunnable{

	private static final Logger log = LoggerFactory.getLogger(ZmqRequestorRunnable.class);
	private String address;
	private int communicationPattern;
	
	ZmqRequestorRunnable(ZmqRunnableBuilder builder) {
		super(builder);
		this.address = builder.getAddress();
		this.communicationPattern = builder.communicationPattern();
//		this.socket = context.createSocket(builder.communicationPattern()); // async request, doesn't wait for a reply
//		this.socket.connect(this.address);
//		this.socket = nextSocket();
	}
	
	private Socket nextSocket() {
		Stopwatch watch = Stopwatch.createStarted();
		Socket socket = context.createSocket(communicationPattern);
		socket.connect(address);
		log.info("socket created in {}", watch.stop());
		return socket;
	}
	//TODO: create a router to mult-services
	public void run() {
		log.info("Requestor {} running at thread {} in {}", id, Thread.currentThread().getName(), address);
		while(!stoped && !Thread.currentThread().isInterrupted()) {
			try {
				socket = nextSocket();
				String identity = socket.recvStr();
				System.err.println(identity);
				byte[] message = (byte[]) channel.take();
				socket.send(identity, ZMQ.SNDMORE);
				socket.send(message,0);
//				socket.connect(address);
				context.destroySocket(socket);
				
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
