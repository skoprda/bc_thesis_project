package sk.kapsa.storage.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import sk.kapsa.storage.mongo.entities.Country;

@Repository
public interface CountryRepository extends MongoRepository<Country, String> {

}
