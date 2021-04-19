package sk.kapsa.storage.mongo.services;

import java.util.NoSuchElementException;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.IndexOptions;

import sk.kapsa.storage.mongo.CollectionManager;
import sk.kapsa.storage.mongo.DownloadRepository;
import sk.kapsa.storage.mongo.entities.Download;

@Service
public class ExtractedObjectsCollectionsService {
	// TODO neviem ako pouzit to co je v konfiguraku application.properties
	private static final String DB_NAME = "KapsaDB";

	private CollectionManager collectionManager = new CollectionManager(DB_NAME);

	@Autowired
	private DownloadRepository downloadRepository;

	public MongoCollection<Document> getCollectionByDownloadId(Long downloadId) {
		try {
			Download download = downloadRepository.findById(downloadId).get();
			MongoCollection<Document> collection = collectionManager.getCollectionByWrapper(download.getWrapper());
			createTextIndexIfNotExists(collection);
			return collection;
		} catch (NoSuchElementException e) {
			return null;
		}
	}

	// na fulltext vyhladavanie
	private void createTextIndexIfNotExists(MongoCollection<?> collection) {
		MongoCursor<Document> iterator = collection.listIndexes().iterator();
		while (iterator.hasNext()) {
			Document nextIndex = iterator.next();
			boolean isText = nextIndex.containsValue("text_index");
			if (isText) {
				return;
			}
		}
		Bson textIndex = new Document("$**", "text");
		IndexOptions options = new IndexOptions();
		options.name("text_index");
		collection.createIndex(textIndex, options);
	}
}
