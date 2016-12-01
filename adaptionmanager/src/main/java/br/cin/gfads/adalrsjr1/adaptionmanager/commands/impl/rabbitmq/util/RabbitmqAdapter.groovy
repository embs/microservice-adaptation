package br.cin.gfads.adalrsjr1.adaptionmanager.commands.impl.rabbitmq.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rabbitmq.client.ConnectionFactory
import com.rabbitmq.client.Connection

import groovy.transform.builder.Builder

@Builder
public class RabbitmqAdapter {

	private static final Logger log = LoggerFactory.getLogger(RabbitmqAdapter.class);

	String username
	String password
	String virtualHost
	String exchangeName
	String host
	int port
	
	ConnectionFactory factory
	RabbitmqAdapter() { }
	
	@Builder(builderClassName="FactoryBuilder", builderMethodName="factoryBuilder")
	RabbitmqAdapter(username, password, virtualHost, exchangeName, host, port) {
		factory = new ConnectionFactory()
		factory.setUsername(username)
		factory.setPassword(password)
		factory.setVirtualHost(virtualHost)
		factory.setHost(host)
		factory.setPort(port)
	}

	Connection getConnection() {
		factory.newConnection()
	}
}
