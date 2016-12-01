package br.cin.gfads.adalrsjr1.adaptionmanager.commands.impl.rabbitmq

import org.slf4j.Logger
import org.slf4j.LoggerFactory

import com.rabbitmq.client.Channel
import com.rabbitmq.client.Connection

import br.cin.gfads.adalrsjr1.adaptionmanager.commands.impl.AbstractCommandImpl
import br.cin.gfads.adalrsjr1.adaptionmanager.commands.impl.rabbitmq.util.RabbitmqAdapter
import metamodel.ICommand

class CommandPublishMessage extends AbstractCommandImpl {

    private static final Logger log = LoggerFactory.getLogger(CommandPublishMessage)
	
	@Override
	public Object execute(ICommand metaCommand) {
		Map map = parameterToMap(metaCommand.getCommandParameter())
		
		RabbitmqAdapter adapter = RabbitmqAdapter.factoryBuilder()
		               .username(map["username"].getValue())
					   .password(map["password"].getValue())
					   .virtualHost(map["virtualHost"].getValue())
					   .host(map["host"].getValue())
					   .port(map["port"].getValue())
					   .build()
		
		Connection connection = adapter.getConnection()
		Channel channel = connection.createChannel()
		channel.exchangeDeclare(map["exchangeName"].getValue(),
			                    map["type"].getValue(),
								map["durable"].getValue() as Boolean)
							
		channel.basicPublish(map["exchangeName"].getValue(),
			                 map["routingKey"].getValue(),
							 null,
							 map["message"].getValue())
		channel.close()
		connection.close()
    }
}
