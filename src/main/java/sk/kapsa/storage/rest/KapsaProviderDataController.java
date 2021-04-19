package sk.kapsa.storage.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;

import sk.kapsa.storage.conversion.DataConverter;
import sk.kapsa.storage.mongo.CollectionManager;
import sk.kapsa.storage.mongo.entities.RuleWrapper;
import sk.kapsa.storage.mongo.services.ExtractedObjectsCollectionsService;
import sk.kapsa.storage.mongo.services.RuleWrapperService;

@RestController
@CrossOrigin
@RequestMapping("/data")
public class KapsaProviderDataController {
	
	@Autowired
	private ExtractedObjectsCollectionsService extractedObjCollService;
	@Autowired
	private RuleWrapperService ruleWrapperService;

	private DataConverter converter = new DataConverter();

	@CrossOrigin
	@GetMapping("/download/{downloadId}/rule-up-to/{upTo}")
	public ResponseEntity<DataResponse> getDataByDownload(@PathVariable long downloadId, @PathVariable int upTo,
			@RequestParam(value = "pageNumber", required = true) int pageNumber,
			@RequestParam(value = "pageSize", required = true) int pageSize,
			@RequestParam(value = "filter", required = false) Optional<String> filter,
			@RequestParam(value = "sortBy", required = false) Optional<String> sortBy,
			@RequestParam(value = "descending", required = false) Optional<Boolean> descending) {
		List<Document> data = new ArrayList<>();
		Pair<FindIterable<Document>, Long> dataCountPair = getDataCollection(downloadId, filter, sortBy, descending);
		FindIterable<Document> dataIterable = dataCountPair.getFirst();
		long count = dataCountPair.getSecond();
		int toSkip = pageNumber * pageSize;
		dataIterable = dataIterable.skip(toSkip).limit(pageSize);
		Optional<RuleWrapper> ruleWrapper = ruleWrapperService.getRuleWrapperByDownloadId(downloadId);
		if (ruleWrapper.isPresent()) {
			if(ruleWrapper.get().getWrapperId().equals("")) {
				DataResponse response = new DataResponse(new ArrayList<>(), 0);
				return new ResponseEntity<>(response, HttpStatus.OK);
			}
			data = converter.process(dataIterable, upTo, ruleWrapper.get());
		} else {	
			dataIterable.into(data);
		}
		DataResponse response = new DataResponse(data, count);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	private Pair<FindIterable<Document>, Long> getDataCollection(long downloadId, 
			Optional<String> filter, 
			Optional<String> sortBy,
			Optional<Boolean> descending) {
		MongoCollection<Document> dataCollection = extractedObjCollService.getCollectionByDownloadId(downloadId);
		Bson byDownloadId = Filters.eq("downloadId", downloadId);
		Bson searchFilter = null;
		if(filter.isPresent()) {
			String filterString = filter.get();
			Document options = new Document();
			options.put("$search", filterString);
			options.put("$caseSensitive", false);
			options.put("$diacriticSensitive", false);
			Bson fullText = new Document("$text", options);
			searchFilter = Filters.and(byDownloadId, fullText);
		}else {
			searchFilter = byDownloadId;
		}
		long count = dataCollection.countDocuments(searchFilter);
		FindIterable<Document> data = dataCollection.find(searchFilter);
		if(sortBy.isPresent() && descending.isPresent()) {
			int direction = descending.get() ? -1 : 1;
			Bson sortCondition = new Document(sortBy.get(), direction);
			data = data.sort(sortCondition);
		}
		return Pair.of(data, count);
	}

	private class DataResponse {
		
		private List<Document> data;
		private long length;
		
		public DataResponse(List<Document> data, long length) {
			super();
			this.data = data;
			this.length = length;
		}

		public List<Document> getData() {
			return data;
		}

		public long getLength() {
			return length;
		}
	}
}


