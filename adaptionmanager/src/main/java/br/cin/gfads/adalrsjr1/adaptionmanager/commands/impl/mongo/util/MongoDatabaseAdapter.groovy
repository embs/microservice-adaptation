package br.cin.gfads.adalrsjr1.adaptionmanager.commands.impl.mongo.util

import org.slf4j.Logger
import org.slf4j.LoggerFactory

import br.cin.gfads.adalrsjr1.adaptionmanager.commands.ScriptCommand
import br.cin.gfads.adalrsjr1.adaptionmanager.commands.impl.mongo.CommandFindAllMongo
import br.cin.gfads.adalrsjr1.adaptionmanager.commands.impl.mongo.CommandFindByQuery
import br.cin.gfads.adalrsjr1.adaptionmanager.commands.impl.mongo.CommandFindFirstMongo

import com.mongodb.MongoClient
import com.mongodb.client.MongoCursor
import com.mongodb.client.MongoDatabase
import com.mongodb.client.model.Filters

class MongoDatabaseAdapter {

	private static final Logger log = LoggerFactory.getLogger(MongoDatabaseAdapter)

	private MongoClient client
	
	String hostAddress
	String databaseName
	int port

	private MongoDatabaseAdapter() {}

	MongoClient getClient() {
		client
	}

	static Builder builder() {
		new Builder(new MongoDatabaseAdapter())
	}

	static class Builder {
		private MongoDatabaseAdapter database

		Builder(MongoDatabaseAdapter database) {
			this.database = database
			assert this.database != null
		}

		MongoDatabase build() {
			database.client = new MongoClient(database.hostAddress, database.port)
			return database.client.getDatabase(database.databaseName)
		}

		Builder hostAddress(String hostAddress) {
			database.hostAddress = hostAddress
			return this
		}

		Builder port(int port) {
			database.port = port
			return this
		}

		Builder databaseName(String name) {
			database.databaseName = name
			return this
		}

	}


	static void main(String[] args) {
		MongoDatabase db = MongoDatabaseAdapter.builder()
				.databaseName("products")
				.hostAddress("localhost")
				.port(27017)
				.build()

		ScriptCommand findFirst = CommandFindFirstMongo.builder()
											.collection("events")
											.database(db)
											.build()
											
		println findFirst.execute().toJson()	
		
		ScriptCommand findAll = CommandFindAllMongo.builder() 
											.collection("events")
											.database(db)
											.build()
											
		ScriptCommand findByQuery = CommandFindByQuery.builder()
											.collection("events")
											.database(db)
											.filter(Filters.gt("aggregateIdentifier","1"))
											.build()
							
		MongoCursor cursor = findByQuery.execute()
		
		while(cursor.hasNext()) {
			
			println ">>>" + cursor.next().toJson()
		}
		cursor.close()
		
		
								
	}
}
