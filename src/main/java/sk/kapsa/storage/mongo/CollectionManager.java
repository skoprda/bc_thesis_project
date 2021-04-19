package sk.kapsa.storage.mongo;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import sk.kapsa.storage.mongo.entities.Wrapper;

public class CollectionManager {
	
	private static final Logger logger = LoggerFactory.getLogger(CollectionManager.class);
	private MongoClient mongoClient;
	private MongoDatabase kapsaDb;
	
	/**
	 * Creates CollectionManager to manage Kapsa data collections"
	 * 
	 * @param host   is a String containing address of your db server
	 * @param port   is a port where your db server listens
	 * @param dbName is a name of your mongodb database
	 *
	 **/
	public CollectionManager(String host, int port, String dbName) {
		this.mongoClient = new MongoClient(new ServerAddress(host, port));
		try {
			this.kapsaDb = mongoClient.getDatabase(dbName);
		} catch (IllegalArgumentException e) {
			logger.error("dbName: " + dbName + " don't exist.");
			return;
		}
	}

	/**
	 * Creates CollectionManager with <b>host</b>:<b>port</b> set to
	 * "localhost:27017" to manage Kapsa data collections
	 * 
	 * @param dbName is a name of your mongodb database
	 *
	 **/
	public CollectionManager(String dbName) {
		this("localhost", 27017, dbName);
	}

	/**
	 * 
	 * @param collectionName of collection to be returned
	 * @return collection by the given <b>collectionName</b>
	 */
	public MongoCollection<Document> getCollectionByName(String collectionName){
		MongoCollection<Document> collection = null;
		try {
			collection = kapsaDb.getCollection(collectionName);
			return collection;
		} catch (IllegalArgumentException e) {
			logger.error("collectionName: "+ collectionName +" isn't valid name.");
		}
		return collection;
	}
	
	/**
	 * 
	 * @param wrapper is wrapper which id corresponds to the name of data collection
	 *                for that wrapper
	 * @return the data collection for specified wrapper or null if fail
	 */
	public MongoCollection<Document> getCollectionByWrapper(Wrapper wrapper) {
		return getCollectionByWrapperId(wrapper.getId());
	}

	/**
	 * 
	 * @param wrapperId is an id of a given wrapper which corresponds to the name of
	 *                  data collection for that wrapper
	 * @return the data collection for specified wrapper or null if fail
	 */
	public MongoCollection<Document> getCollectionByWrapperId(String wrapperId) {
		return getCollectionByName(wrapperId);
	}

	/**
	 * Delete a collection with given name from the database specified earlier
	 * 
	 * @param collectionName name of collection to be deleted
	 */
	public void deleteCollection(String collectionName) {
		try {
			MongoCollection<Document> collection = kapsaDb.getCollection(collectionName);
			collection.drop();
		} catch (IllegalArgumentException e) {
			logger.error("collectionName: "+ collectionName +" isn't valid name.");
		}
	}

	/**
	 * Delete a <b>collection</b> from the database specified earlier
	 * 
	 * @param collection to be deleted
	 */
	public void deleteCollection(MongoCollection<Document> collection) {
		collection.drop();
	}

	/**
	 * 
	 * @param collectionName name of collection to be created
	 * @return created collection or null if fail
	 */
	public MongoCollection<Document> createCollection(String collectionName) {
		try {
			kapsaDb.createCollection(collectionName);
			MongoCollection<Document> collection = kapsaDb.getCollection(collectionName);
			return collection;
		} catch (IllegalArgumentException e) {
			logger.error("collectionName: "+ collectionName +" isn't valid name.");
		}
		return null;
	}

}
