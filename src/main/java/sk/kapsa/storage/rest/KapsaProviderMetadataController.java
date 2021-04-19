package sk.kapsa.storage.rest;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import sk.kapsa.storage.mongo.entities.Download;
import sk.kapsa.storage.mongo.entities.RuleWrapper;
import sk.kapsa.storage.mongo.entities.Source;
import sk.kapsa.storage.mongo.entities.Wrapper;
import sk.kapsa.storage.mongo.services.DownloadService;
import sk.kapsa.storage.mongo.services.RuleWrapperService;
import sk.kapsa.storage.mongo.services.SourceService;
import sk.kapsa.storage.mongo.services.WrapperService;

@RestController
@CrossOrigin
@RequestMapping("/metadata")
public class KapsaProviderMetadataController {
	
	@Autowired
	private WrapperService wrapperService;
	@Autowired
	private DownloadService downloadService;
	@Autowired
	private SourceService sourceService;
	@Autowired
	private RuleWrapperService ruleWrapperService;
	
	@GetMapping("/sites")
	public List<Source> getAllSources() {
		List<Source> sources = sourceService.getAll();
		return sources;
	}

	@GetMapping("/wrappers")
	public ResponseEntity<List<Wrapper>> getAllWrappers() {
		List<Wrapper> wrappers = wrapperService.getAll();
		return new ResponseEntity<>(wrappers, HttpStatus.OK);
	}

	@GetMapping("/wrappers/site/{site}")
	public ResponseEntity<List<Wrapper>> getWrappersBySite(@PathVariable String site) {
		List<Wrapper> wrappers = wrapperService.getWrappersBySite(site);
		return new ResponseEntity<>(wrappers, HttpStatus.OK);
	}

	@GetMapping("/downloads")
	public ResponseEntity<List<Download>> getAllDownloads() {
		List<Download> allDownloads = downloadService.getAll();
		return new ResponseEntity<>(allDownloads, HttpStatus.OK);
	}

	@GetMapping("/downloads/site/{site}")
	public ResponseEntity<List<Download>> getDownloadsBySite(@PathVariable String site) {
		List<Download> downloadsBySite = downloadService.getBySite(site);
		return new ResponseEntity<>(downloadsBySite, HttpStatus.OK);
	}

	@GetMapping("/downloads/wrapper/{wrapperId}")
	public ResponseEntity<List<Download>> getDownloadsByWrapper(@PathVariable String wrapperId) {
		List<Download> downloadsByWrapperId = downloadService.getByWrapperId(wrapperId);
		return new ResponseEntity<>(downloadsByWrapperId, HttpStatus.OK);
	}

	@GetMapping("/wrapper/download/{downloadId}")
	public ResponseEntity<Wrapper> getWrapperByDownloadId(@PathVariable long downloadId) {
		Optional<Download> download = downloadService.getById(downloadId);
		if(download.isEmpty()) {
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}
		else {
			Wrapper wrapper = download.get().getWrapper();
			return new ResponseEntity<>(wrapper, HttpStatus.OK);
		}
	}	
	@PostMapping("/rule-wrapper")
	public ResponseEntity<RuleWrapper> saveRuleWrapper(@RequestBody RuleWrapper ruleWrapper) {
		RuleWrapper savedRuleWrapper = ruleWrapperService.save(ruleWrapper);
		return new ResponseEntity<>(savedRuleWrapper, HttpStatus.OK);
	}
	@GetMapping("/rule-wrapper/wrapper-id/{wrapperId}")
	public ResponseEntity<RuleWrapper> getRuleWrapperByWrapperId(@PathVariable String wrapperId) {
		RuleWrapper ruleWrapperByWrapperId = ruleWrapperService.getByWrapperId(wrapperId);
		return new ResponseEntity<>(ruleWrapperByWrapperId, HttpStatus.OK);
		
	}
}
