package sk.kapsa.storage.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import sk.kapsa.storage.mongo.entities.Language;

@Repository
public interface LanguageRepository extends MongoRepository<Language, String> {

}
