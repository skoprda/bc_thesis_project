package sk.kapsa.storage.mongo.entities;

public enum State {
	SCHEDULED, CRAWLING, CRAWLED, PROCESSING_RAW_DATA, UNIFICATION, FINISHED, CANCELLED

}
