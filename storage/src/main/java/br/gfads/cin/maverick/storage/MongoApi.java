package br.gfads.cin.maverick.storage;

import java.util.ArrayList;
import java.util.List;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

public class MongoApi {
	static final String HOST = "localhost"; // docker
	// to use spring boot
	static final int PORT = 27017;
	
	// getOrCreateDatabase()
	// policies
	
	void storePolicy() { }
	void storeAdaptation() { }
	void storeModuleState() { }
	void queryPolicy() { }
	void queryAdaptation() { }
	void queryModuleState() { }
	void resetModuleState() { }
	void updatePolicy() { }
	void updateAdaptation() { }
	void updateModuleState() { }
	void removePolicy() { }
	void removeAdaptation() { }
	void removeModuleState() { }
	
	public static void main(String[] args) {
		
		MongoClient mongoClient = new MongoClient(HOST, PORT);
		MongoDatabase database = mongoClient.getDatabase("test2");
//		database.createCollection("test2Collection");
		
		BasicDBObject document = new BasicDBObject("x", 1);
		MongoCollection<BasicDBObject> collection = database.getCollection("test2Collection",BasicDBObject.class);
//		collection.insertOne(document);
		document.append("x", 2).append("y", 3);
		
		collection.replaceOne (Filters.eq("_id", document.get("_id")),document);

		List<BasicDBObject> foundDocument = collection.find().into(new ArrayList<BasicDBObject>());
		
		foundDocument.stream().forEach(System.out::println);
		
		mongoClient.close();
	}
}
