package sk.kapsa.storage.rest;

import java.io.IOException;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;

import sk.kapsa.storage.crawling.ExtractionResultService;
import sk.kapsa.storage.crawling.beans.ExagoCrawlTask;

@Controller
public class CrawlingController {

	public static final Logger logger = LoggerFactory.getLogger(CrawlingController.class);
	
	@Autowired
	private ExtractionResultService extractionResultService;
	
	@RequestMapping(value = "/start-crawl", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<ExagoCrawlTask> startCrawl(
			@RequestParam(value = "siteUrl", required = false) Optional<String> siteUrl,
			@RequestParam(value = "wrapperId", required = false) Optional<String> wrapperId) {
		logger.info("starting crawl and returning downloadId");
		Long downloadId = null;
		if (wrapperId.isPresent()) {
			downloadId = extractionResultService.startCrawlByWrapperId(wrapperId.get());
		}
		if (siteUrl.isPresent()) {
			downloadId = extractionResultService.startCrawlBySite(siteUrl.get());
		}
		if (downloadId == null) {
			return new ResponseEntity<ExagoCrawlTask>(HttpStatus.FORBIDDEN);
		}

		Integer max = 10;
		ExagoCrawlTask ect = extractionResultService.getNextURLs(max, downloadId);
		return (ect == null) ? new ResponseEntity<ExagoCrawlTask>(HttpStatus.GONE) : new ResponseEntity<ExagoCrawlTask>(ect, HttpStatus.OK);
	}

	@RequestMapping(value = "/urls-to-crawl", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<ExagoCrawlTask> getNextURLs(
			@RequestParam(value = "downloadId", required = false) Optional<Long> downloadId,
			@RequestParam(value = "maxCount", required = false) Optional<Integer> maxCount) {
		logger.info("requesting URLs to crawl");
		Integer max = maxCount.orElse(10);
		ExagoCrawlTask ect = null;
		if (downloadId.isPresent()) {
			ect = extractionResultService.getNextURLs(max, downloadId.get());
		} else {
			ect = extractionResultService.getNextURLs(max);
		}
		return (ect == null) ? new ResponseEntity<ExagoCrawlTask>(HttpStatus.GONE) : new ResponseEntity<ExagoCrawlTask>(ect, HttpStatus.OK);
	}

	@RequestMapping(value = "/extraction-result", method = RequestMethod.POST, consumes = "application/json")
	public @ResponseBody ResponseEntity<Void> post(@RequestBody String json) {
		try {
			extractionResultService.saveExtractionResult(json);
			return new ResponseEntity<Void>(HttpStatus.CREATED);
		} catch (JsonProcessingException e) {
			return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
		} catch (IOException e) {
			return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
		}
	}

}
