package br.cin.gfads.adalrsjr1.jeromq;

import java.util.UUID;
import java.util.concurrent.BlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Socket;

public abstract class ZmqRunnable implements Runnable {

	private static final Logger log = LoggerFactory.getLogger(ZmqRunnable.class);

	public static enum ZmqProtocol {
		IPC("ipc"),
		TCP("tcp");
		
		private final String protocol;
		
		ZmqProtocol(String protocol) {
			this.protocol = protocol;
		}
		
		public String protocolName() {
			return this.protocol;
		}
		
		public ZmqProtocol protocol() {
			return this;
		}
		
	}
	
	public static enum ZmqCommunicationPattern {
		PUB(ZMQ.PUB),
		SUB(ZMQ.SUB),
		AREQ(ZMQ.ROUTER), // async request
		AREP(ZMQ.DEALER); // async reply
		
		private final int pattern;
		ZmqCommunicationPattern(int pattern) {
			this.pattern = pattern;
		}
		
		public int pattern() {
			return this.pattern;
		}
	}

	public static class ZmqRunnableBuilder {
		private BlockingQueue channel;
		private String id;
		private ZContext context;
		private String host;
		private int port;
		private ZmqProtocol protocol;
		private String topic;
		private ZmqCommunicationPattern commPattern;

		private ZmqRunnableBuilder(ZContext context, BlockingQueue channel) {
			this.context = context;
			this.channel = channel;
		}

		public ZContext getContext() {
			return context;
		}

		public BlockingQueue getChannel() {
			return channel;
		}
		
		public String getId() {
			return id == null ? UUID.randomUUID().toString() : id;
		}

		public String getHost() {
			return host;
		}

		public int getPort() {
			return port;
		}

		public ZmqProtocol getProtocol() {
			return protocol.protocol();
		}
		
		public String getTopic() {
			return topic;
		}
		
		public String getAddress() {
			return getProtocol().protocolName() + "://" + host + (getProtocol() == ZmqProtocol.TCP ? ":" + port : "");
		}
		
		public int communicationPattern() {
			return commPattern.pattern();
		}

		public ZmqRunnableBuilder setId(String id) {
			this.id = id;
			return this;
		}
		
		public ZmqRunnableBuilder setHost(String host) {
			this.host = host;
			return this;
		}

		public ZmqRunnableBuilder setPort(int port) {
			this.port = port;
			return this;
		}
		
		public ZmqRunnableBuilder setTopic(String topic) {
			this.topic = topic;
			return this;
		}

		public ZmqRunnableBuilder setProtocol(ZmqProtocol protocol) {
			this.protocol = protocol;
			return this;
		}

		public ZmqRunnableBuilder setCommunicationPattern(ZmqCommunicationPattern pattern) {
			this.commPattern = pattern;
			return this;
		}	

		public ZmqRunnable build() {
			switch(commPattern) {
			case PUB: return new ZmqPublisherRunnable(this);
			case SUB: return new ZmqSubscriberRunnable(this);
			case AREQ: return new ZmqRequestorRunnable(this);
			case AREP: return new ZmqResponserRunnable(this);
			default : return null;
			}
		}
	}
	
	protected boolean contextTerminated;
	protected Socket socket;
	protected ZContext context;
	protected BlockingQueue channel;
	protected String host;
	protected int port;
	public final String id;

	protected boolean stoped;

	protected ZmqRunnable(ZmqRunnableBuilder builder) {
		this.stoped = false;
		this.context = builder.getContext();
		this.channel = builder.getChannel();
		this.host = builder.getHost();
		this.port = builder.getPort();
		this.id = builder.getId();
	}

	public void shutdown() {
		stoped = true;
		socket.close();
		context.destroySocket(socket);
	}

	public static ZmqRunnableBuilder builder(ZContext context, BlockingQueue channel) {
		return new ZmqRunnableBuilder(context, channel);
	}
	
	public abstract void run();

}
