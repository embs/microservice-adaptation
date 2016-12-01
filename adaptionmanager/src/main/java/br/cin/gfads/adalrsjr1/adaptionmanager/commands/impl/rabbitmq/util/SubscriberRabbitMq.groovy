package br.cin.gfads.adalrsjr1.adaptionmanager.commands.impl.rabbitmq.util

import org.slf4j.Logger
import org.slf4j.LoggerFactory

import com.rabbitmq.client.AMQP
import com.rabbitmq.client.Channel
import com.rabbitmq.client.Connection
import com.rabbitmq.client.ConnectionFactory
import com.rabbitmq.client.Consumer
import com.rabbitmq.client.DefaultConsumer
import com.rabbitmq.client.Envelope

class SubscriberRabbitMq {

    private static final Logger log = LoggerFactory.getLogger(SubscriberRabbitMq)

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
		
		Channel channel = conn.createChannel()
		channel.exchangeDeclare(exchangeName, "fanout", true)
		String queueName = channel.queueDeclare().getQueue()
		channel.queueBind(queueName, exchangeName, "")
		
		log.info "[*] Waiting for messages. To exit press CTRL+C"
		
		Consumer consumer = new DefaultConsumer(channel) {
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope,
									   AMQP.BasicProperties properties, byte[] body) throws IOException {
			  String message = new String(body, "UTF-8");
			  log.info (" [x] Received '" + message + "'");
			}
		}
		
		channel.basicConsume(queueName, true, consumer)
	}
}
