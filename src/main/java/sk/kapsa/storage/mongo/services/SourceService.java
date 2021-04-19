package sk.kapsa.storage.mongo.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sk.kapsa.storage.mongo.IDAssigner;
import sk.kapsa.storage.mongo.SourceRepository;
import sk.kapsa.storage.mongo.entities.Source;
//TODO dokumentacia sourceservice
//TODO otestovat source service
@Service
public class SourceService {

	@Autowired
	private SourceRepository sourceRepository;

	public Source saveSource(Source source) {
		return sourceRepository.insert(source);
	}
	
	public List<Source> getAll(){
		return sourceRepository.findAll();
	}
	
	public long getSourceIdBySite(String site) {
		Long id = null;
		try{
			id = sourceRepository.findBySite(site).getId();
			return id;
		}catch (NullPointerException e) {
			Source newSource = createNewSource(site);
			return newSource.getId();
		}
	}
	
	private Source createNewSource(String site) {
		Source newSource = new Source(site, site);
		Long newId = IDAssigner.assignNewSourceId();
		newSource.setId(newId);
		return sourceRepository.insert(newSource);
	}
}
