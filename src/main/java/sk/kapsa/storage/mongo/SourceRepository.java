package sk.kapsa.storage.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import sk.kapsa.storage.mongo.entities.Source;

@Repository
public interface SourceRepository extends MongoRepository<Source, String>{
	
	public Source findBySite(String site);
}
