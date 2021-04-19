package sk.kapsa.storage.mongo.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sk.kapsa.storage.mongo.RuleWrapperRepository;
import sk.kapsa.storage.mongo.entities.RuleWrapper;
import sk.kapsa.storage.mongo.entities.Wrapper;

@Service
public class RuleWrapperService {
	
	@Autowired
	private RuleWrapperRepository ruleWrapperRepository;
	@Autowired
	private DownloadService downloadService;
	
	public RuleWrapper save(RuleWrapper ruleWrapper) {
		return ruleWrapperRepository.save(ruleWrapper);
	}
	
	public RuleWrapper getByWrapperId(String wrapperId) {
		return ruleWrapperRepository.findByWrapperId(wrapperId);
	}
	
	public Optional<RuleWrapper> getRuleWrapperByDownloadId(long downloadId) {
		Optional<Wrapper> wrapper = downloadService.getWrapperByDownloadId(downloadId);
		if(wrapper.isEmpty()) {
			RuleWrapper ruleWrapper = new RuleWrapper();
			ruleWrapper.setWrapperId("");
			return Optional.of(ruleWrapper);
		}else {
			String wrapperId = wrapper.get().getId();	
			return Optional.ofNullable(getByWrapperId(wrapperId)); 
		}
	}
}
