package sk.kapsa.storage.mongo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import sk.kapsa.storage.mongo.entities.Download;
import sk.kapsa.storage.mongo.entities.State;

@Repository
public interface DownloadRepository extends MongoRepository<Download, Long>{
	
	public List<Download> findByActualState(State actualState);
	@Query("{'wrapper._id': ?0}")
	public List<Download> findByWrapperId(String wrapperId);
	@Query("{'wrapper.site': ?0}")
	public List<Download> findBySite(String site);
}
