package sk.kapsa.storage.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import sk.kapsa.storage.mongo.entities.RuleWrapper;

@Repository
public interface RuleWrapperRepository extends MongoRepository<RuleWrapper, String>{

	RuleWrapper findByWrapperId(String wrapperId);
}
