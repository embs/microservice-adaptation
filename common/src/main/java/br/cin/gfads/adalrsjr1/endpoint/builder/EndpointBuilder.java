package br.cin.gfads.adalrsjr1.endpoint.builder;

import java.util.concurrent.BlockingQueue;

import br.cin.gfads.adalrsjr1.endpoint.Endpoint;
import br.cin.gfads.adalrsjr1.endpoint.rabbitmq.MaverickBuffer;

public class EndpointBuilder<T extends Endpoint> {

	private String address;
	private int port;
	private MaverickBuffer<byte[]> buffer;
	private String queueName;
	private boolean durable;
	private EndpointDriver<T> driver;
	private TypeEndpoint type;
	private String topic;
	private EndpointCallback callback;
	private int maxPriority;

	public String getAddress() {
		return address;
	}

	public int getPort() {
		return port;
	}

	public MaverickBuffer<byte[]> getBuffer() {
		return buffer;
	}

	public String getQueueName() {
		return queueName;
	}

	public boolean isDurable() {
		return durable;
	}
	
	public int getMaxPriority() {
		return maxPriority;
	}

	public EndpointDriver getDriver() {
		return driver;
	}

	public TypeEndpoint getType() {
		return type;
	}
	
	public String getTopic() {
		return topic;
	}

	public EndpointCallback getCallback() {
		return callback;
	}


	public EndpointBuilder<T> withAddress(String address) {
		this.address = address;
		return this;
	}

	public EndpointBuilder<T> withPort(int port) {
		this.port = port;
		return this;
	}

	public EndpointBuilder<T> withMaxPriority(int maxPriority) {
		this.maxPriority = maxPriority;
		return this;
	}
	
	public EndpointBuilder<T> withBuffer(MaverickBuffer<byte[]> buffer) {
		this.buffer = buffer;
		return this;
	}

	public EndpointBuilder<T> withQueueName(String queueName) {
		this.queueName = queueName;
		return this;
	}

	public EndpointBuilder<T> withDurable(boolean durable) {
		this.durable = durable;
		return this;
	}

	public EndpointBuilder<T> withDriver(EndpointDriver driver) {
		this.driver = driver;
		return this;
	}

	public EndpointBuilder<T> withType(TypeEndpoint type) {
		this.type = type;
		return this;
	}
	
	public EndpointBuilder<T> withTopic(String topic) {
		this.topic = topic;
		return this;
	}
	
	public EndpointBuilder<T> withCallback(EndpointCallback callback) {
		this.callback = callback;
		return this;
	}
	
	public T build() {
		return driver.create(this);		
	}

	

}
