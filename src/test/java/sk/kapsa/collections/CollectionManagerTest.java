package sk.kapsa.collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.bson.Document;
import org.junit.jupiter.api.Test;

import com.mongodb.client.MongoCollection;

import sk.kapsa.storage.mongo.CollectionManager;

public class CollectionManagerTest {

	CollectionManager cmTest = new CollectionManager("KapsaDB");

//	@Test
	public void getCollectionByWrapperTest() {
//		Wrapper wrapper = new Wrapper("{'key': 1}");
//		wrapper.setId("abcd");
//		MongoCollection<Document> collection = cmTest.getCollectionByWrapper(wrapper);
//		assertTrue(collection.getNamespace().toString().contains("abcd"));
//		assertEquals(0, collection.countDocuments());
	}

//	@Test
	public void getCollectionByWrapperIdTest() {
//		Wrapper wrapper = new Wrapper("{'key': 1}");
//		wrapper.setId("abcd");
//		MongoCollection<Document> collection = cmTest.getCollectionByWrapperId(wrapper.getId());
//		assertTrue(collection.getNamespace().toString().contains("abcd"));
//		assertEquals(0, collection.countDocuments());
	}
	
	@Test
	public void getCollectionByNameTest() {
		MongoCollection<Document> collection = cmTest.getCollectionByWrapperId("SomeName");
		assertTrue(collection.getNamespace().toString().contains("SomeName"));
		assertEquals(0, collection.countDocuments());
	}
	
	@Test
	public void createCollectionTest() {
		MongoCollection<Document> collectionCreated = cmTest.createCollection("OurTestCollection");
		assertTrue(collectionCreated.countDocuments() == 0);
		collectionCreated.insertOne(new Document("key", "value"));
		assertTrue(collectionCreated.countDocuments() == 1);
		collectionCreated.drop();
	}
	
	@Test
	public void deleteCollectionTest() {
		MongoCollection<Document> collection = cmTest.createCollection("Collection1");
		collection.insertOne(new Document("key", "value"));
		assertTrue(collection.countDocuments() == 1);
		cmTest.deleteCollection(collection);
		MongoCollection<Document> anotherCollection = cmTest.getCollectionByName("Collection1");
		assertTrue(anotherCollection.countDocuments() == 0); 
	}
	

}
