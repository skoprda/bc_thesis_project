package sk.kapsa.storage.mongo;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
//TODO dokumentacia idassigner
public class IDAssigner {
  // pri rucnom pridavani sourceId do db treba ako hodnotu napisat NumberLong(1)
	
	private static CollectionManager collectionManager = new CollectionManager("KapsaDB");
	private static MongoCollection<Document> assignerCollection = collectionManager.getCollectionByName("IDAssigner");
	
	private static long assignNewId(String idValue) {
		Bson filter = Filters.gt(idValue, 0);
		Document updateValue = new Document(idValue, 1);
		Document update = new Document("$inc", updateValue);
		Document doc = assignerCollection.findOneAndUpdate(filter, update);
		return (long)doc.get(idValue);
		
	}
	
	public static long assignNewSourceId() {
		return assignNewId("sourceId");
	}
	
	public static long assignNewDownloadId() {
		return assignNewId("downloadId");
	}
	
	
	
	
	
}
