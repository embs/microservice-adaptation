package br.cin.gfads.adalrsjr1.adaptionmanager.commands.impl.rabbitmq.util

import org.slf4j.Logger
import org.slf4j.LoggerFactory

import br.cin.gfads.adalrsjr1.adaptionmanager.commands.ScriptCommand
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

class PublisherRabbitMq {

    private static final Logger log = LoggerFactory.getLogger(PublisherRabbitMq)

	static void main(String[] args) {
		String user = "test"
		String pass = "password"
		String virtualHost = "/"
		String exchangeName = "product.events.fanout.exchange"
		
		ConnectionFactory factory = new ConnectionFactory()
		factory.setUsername(user)
		factory.setPassword(pass)
		factory.setVirtualHost(virtualHost)
		factory.setHost("localhost")
		factory.setPort(5672)
		
		Connection conn = factory.newConnection()
		
//		ScriptCommand publishMessage = CommandPublishMessage.builder()
//										.connection(conn)
//										.exchangeName(exchangeName)
//										.type("fanout")
//										.durable(true)
//										.message("teste123".bytes)
//										.build()
										
										
//		publishMessage.execute()
		conn.close()
		
//		
//		
//		Channel channel = conn.createChannel()
//		channel.exchangeDeclare(exchangeName, "fanout", true)
//		String message = "teste123"
//		channel.basicPublish(exchangeName, "", null, message.bytes)
//		channel.close()
//		conn.close()
	}
}
