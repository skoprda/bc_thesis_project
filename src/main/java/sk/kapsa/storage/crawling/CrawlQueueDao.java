package sk.kapsa.storage.crawling;

import sk.kapsa.storage.crawling.beans.ExagoCrawlTask;
import sk.kapsa.storage.crawling.beans.ExagoCrawledUrl;

public interface CrawlQueueDao {

	Long addSiteToDownload(String siteUrl) throws IllegalArgumentException;

	Long addWrapperToDownload(String wrapperId) throws IllegalArgumentException;

	ExagoCrawlTask getNextURLs(int maxCount);

	ExagoCrawlTask getNextURLs(int maxCount, long downloadId);

	void addCrawledUrl(ExagoCrawledUrl ecu, long downloadId) throws IllegalArgumentException;

	void registerAsDetailPage(String url, long downloadId);

	void endDownloadIfFinished(long downloadId);

}