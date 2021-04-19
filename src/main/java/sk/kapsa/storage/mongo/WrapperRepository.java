package sk.kapsa.storage.mongo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import sk.kapsa.storage.mongo.entities.Wrapper;

@Repository
public interface WrapperRepository extends MongoRepository<Wrapper, String>{
	
	@Query(value = "{ 'site' : {'$regex': '?0$'} }")
	public List<Wrapper> findByUrl(String site);
	
	public List<Wrapper> findBySite(String site);
}
