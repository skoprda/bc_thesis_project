package sk.kapsa.storage.crawling;

import java.io.IOException;
import java.util.Iterator;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoCollection;

import sk.kapsa.storage.crawling.beans.ExagoCrawlTask;
import sk.kapsa.storage.crawling.beans.ExagoCrawledUrl;
import sk.kapsa.storage.crawling.beans.ExagoExtractedObject;
import sk.kapsa.storage.mongo.services.ExtractedObjectsCollectionsService;

@Service
public class ExtractionResultService {

	public static final Logger logger = LoggerFactory.getLogger(ExtractionResultService.class);

	@Autowired
	private ExtractedObjectsCollectionsService extractedObjectsCollectionsService;
	private MongoCollection<Document> mongoCollection;
	@Autowired
	private CrawlQueueDao crawlQueueDao;
	private ObjectMapper jacksonMapper = new ObjectMapper();
	
	public void saveExtractionResult(String json) throws JsonProcessingException, IOException, IllegalArgumentException {
		Long downloadId = null;
		JsonNode tree = jacksonMapper.readTree(json);
		JsonNode downloadIdNode = tree.get("downloadId");
		if (downloadIdNode != null && downloadIdNode.canConvertToLong()) {
			downloadId = downloadIdNode.asLong();
		} else {
			logger.warn("Result JSON does no contain downloadId and cannot be saved");
			throw new IllegalArgumentException("Result JSON does no contain downloadId and cannot be saved");
		}
		mongoCollection = extractedObjectsCollectionsService.getCollectionByDownloadId(downloadId);
		if (mongoCollection == null) {
			logger.warn("No collection named by wrapper in download with id = " + downloadId);
			throw new IllegalArgumentException("No collection named by wrapper in download with id = " + downloadId);
		}
		JsonNode urlsCrawled = tree.get("crawledUrls");
		int numberOfReceivedURLs = 0;
		if (urlsCrawled != null && urlsCrawled.isArray()) {
			Iterator<JsonNode> elements = urlsCrawled.elements();
			while (elements.hasNext()) {
				try {
					ExagoCrawledUrl ecurl = jacksonMapper.convertValue(elements.next(), ExagoCrawledUrl.class); // throws IllegalArgumentException
					crawlQueueDao.addCrawledUrl(ecurl, downloadId);
					numberOfReceivedURLs += ecurl.getFoundUrls().size();
					logger.debug("URL crawled: " + ecurl);
				} catch (IllegalArgumentException e) {
					logger.warn("Bad structured urlsCrawled object received from Exago: {}", e.getMessage());
				}
			}
		}
		int numberOfReceivedObjects = 0;
		JsonNode extractedObjects = tree.get("extractedObjects");
		if (extractedObjects != null && extractedObjects.isArray()) {
			Iterator<JsonNode> extractedObjectsIterator = extractedObjects.elements();
			while (extractedObjectsIterator.hasNext()) {
				try {
					ExagoExtractedObject eeo = jacksonMapper.convertValue(extractedObjectsIterator.next(), ExagoExtractedObject.class); // throws IllegalArgumentException
					crawlQueueDao.registerAsDetailPage(eeo.getUrl(), downloadId);
					numberOfReceivedObjects++;
					eeo.setDownloadId(downloadId);
					saveExagoExtractedObject(eeo);
				} catch (IllegalArgumentException e) {
					logger.warn("Bad structured product received from Exago: {}", e.getMessage());
				}
			}
		}
		logger.info("New exago result received: {} new URLs to crawl, {} extracted objects, {} bad structured objects .",
				numberOfReceivedURLs, numberOfReceivedObjects);
		crawlQueueDao.endDownloadIfFinished(downloadId);
	}

	private void saveExagoExtractedObject(ExagoExtractedObject eeo) {
		try {
			mongoCollection.insertOne(eeo.toDocument());
		} catch (Exception e) {
			logger.error("Object from URL {} not saved. Exception: {}", eeo.getUrl(), e.toString());
		}
	}

	public ExagoCrawlTask getNextURLs(int maxCount) {
		return crawlQueueDao.getNextURLs(maxCount);
	}
	
	public ExagoCrawlTask getNextURLs(int maxCount, long downloadId) {
		return crawlQueueDao.getNextURLs(maxCount, downloadId);
	}

	public Long startCrawlByWrapperId(String wrapperId) {
		return crawlQueueDao.addWrapperToDownload(wrapperId);
	}

	public Long startCrawlBySite(String siteUrl) {
		return crawlQueueDao.addSiteToDownload(siteUrl);
	}
	
}
