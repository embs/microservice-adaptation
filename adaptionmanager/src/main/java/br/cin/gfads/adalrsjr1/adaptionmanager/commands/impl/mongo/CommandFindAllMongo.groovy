package br.cin.gfads.adalrsjr1.adaptionmanager.commands.impl.mongo

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase

import br.cin.gfads.adalrsjr1.adaptionmanager.commands.impl.AbstractCommandImpl
import br.cin.gfads.adalrsjr1.adaptionmanager.commands.impl.mongo.util.MongoDatabaseAdapter
import metamodel.ICommand
import metamodel.ICommandParameter

class CommandFindAllMongo extends AbstractCommandImpl {

    private static final Logger log = LoggerFactory.getLogger(CommandFindAllMongo)

	@Override
	public Object execute(ICommand metaCommand) throws Exception {
		ICommandParameter params = metaCommand.getCommandParameter()
		
		Map map = parameterToMap(params.getValue())
		
		MongoDatabase database = MongoDatabaseAdapter.builder()
		                    .databaseName(map["databaseName"].getValue())
							.hostAddress(map["hostAddress"].getValue())
							.port(map["port"].getValue() as Integer)
							.build()

		MongoCollection collection = database.getCollection(map["collection"].getValue())
		return collection.find().limit(map["collectionLimit"].getValue() as Integer).iterator()							
	}
}
