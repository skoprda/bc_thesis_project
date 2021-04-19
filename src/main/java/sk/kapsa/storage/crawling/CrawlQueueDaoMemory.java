package sk.kapsa.storage.crawling;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sk.kapsa.storage.crawling.beans.ExagoCrawlTask;
import sk.kapsa.storage.crawling.beans.ExagoCrawledUrl;
import sk.kapsa.storage.mongo.entities.Download;
import sk.kapsa.storage.mongo.entities.State;
import sk.kapsa.storage.mongo.entities.Wrapper;
import sk.kapsa.storage.mongo.services.DownloadService;
import sk.kapsa.storage.mongo.services.WrapperService;

@Service
public class CrawlQueueDaoMemory implements CrawlQueueDao {

	public static final Logger logger = LoggerFactory.getLogger(CrawlQueueDaoMemory.class);
	
	@Autowired
	private WrapperService wrapperService;
	@Autowired
	private DownloadService downloadService;

	private Map<String, QueueForDownload> wrapperIdsToQueues = new LinkedHashMap<>();
	private Map<Long, QueueForDownload> downloadIdsToQueues = new LinkedHashMap<>();
	
	public CrawlQueueDaoMemory() {
//		wrapperService = ContextProvider.CONTEXT.getWrapperService();
	}
	
	private Wrapper getWrapperBySite(String site) throws NoSuchElementException {
		Wrapper wrapper = wrapperService.getWrapperBySite(site);
		if (wrapper == null)
			throw new NoSuchElementException("no wrapper for site " + site);
		return wrapper;
	}

	@Override
	public Long addSiteToDownload(String site) throws IllegalArgumentException {
		try {
			Wrapper wrapper = getWrapperBySite(site);
			if (wrapperIdsToQueues.containsKey(wrapper.getId())) {
				logger.warn("Wrapper is already in process of downloading");
				return wrapperIdsToQueues.get(wrapper.getId()).getDownload().getId();
			}
			return addWrapperToDownload(getWrapperBySite(site));
		} catch (NoSuchElementException e) {
			throw new IllegalArgumentException(e);
		}
	}
	
	@Override
	public Long addWrapperToDownload(String wrapperId) throws IllegalArgumentException {
		if (wrapperIdsToQueues.containsKey(wrapperId)) {
			logger.warn("Wrapper is already in process of downloading");
			return wrapperIdsToQueues.get(wrapperId).getDownload().getId();
		}
		try {
		 return addWrapperToDownload(wrapperService.getWrapperById(wrapperId));
		} catch (NoSuchElementException e) {
			throw new IllegalArgumentException(e);
		}
	}

	private Long addWrapperToDownload(Wrapper wrapper) {
		Download initialDownload = new Download(wrapper);
		Download download = downloadService.save(initialDownload);
		Set<String> seedUrls = wrapperService.getSeedUrlsFromWrapper(wrapper); 
		QueueForDownload queueForDownload = new QueueForDownload(seedUrls, download);
		wrapperIdsToQueues.put(wrapper.getId(), queueForDownload);
		downloadIdsToQueues.put(download.getId(), queueForDownload);
		return download.getId();
	}

	@Override
	public void addCrawledUrl(ExagoCrawledUrl ecu, long downloadId) throws IllegalArgumentException {
		QueueForDownload queueForDownload = downloadIdsToQueues.get(downloadId);
		if (queueForDownload != null) {
			queueForDownload.addCrawledUrl(ecu);
		}
	}
	
	@Override
	public void registerAsDetailPage(String url, long downloadId) {
		QueueForDownload queueForDownload = downloadIdsToQueues.get(downloadId);
		if (queueForDownload != null) {
			queueForDownload.registerAsDetailPage(url);
		}
	}

	@Override
	public ExagoCrawlTask getNextURLs(int maxCount) {
		if (downloadIdsToQueues.isEmpty()) {
			return null;
		}
		ExagoCrawlTask task = null;
		for (QueueForDownload q: downloadIdsToQueues.values()) {
			task = q.getNextURLs(maxCount);
			if (task.getUrlsToCrawl().size() > 0)
				return task;
		}
		return task;
	}

	@Override
	public ExagoCrawlTask getNextURLs(int maxCount, long downloadId) {
		QueueForDownload queueForDownload = downloadIdsToQueues.get(downloadId);
		return (queueForDownload == null) ? null : queueForDownload.getNextURLs(maxCount);
	}
	
	@Override
	public void endDownloadIfFinished(long downloadId) {
		QueueForDownload queueForDownload = downloadIdsToQueues.get(downloadId);
		if (queueForDownload != null) {
			boolean finished = queueForDownload.endDownloadIfFinished();
			if (finished) {
				Download download = queueForDownload.getDownload();
				downloadService.saveWithState(download, State.CRAWLED);
				downloadIdsToQueues.remove(downloadId);
				wrapperIdsToQueues.remove(download.getWrapper().getId());
				logger.info("Download with id {} finished.", downloadId);
			}
		}
	}

}
