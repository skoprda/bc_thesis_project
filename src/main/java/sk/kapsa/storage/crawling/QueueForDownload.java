package sk.kapsa.storage.crawling;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sk.kapsa.storage.crawling.beans.ExagoCrawlTask;
import sk.kapsa.storage.crawling.beans.ExagoCrawledUrl;
import sk.kapsa.storage.crawling.beans.ExagoFoundUrl;
import sk.kapsa.storage.mongo.entities.Download;

public class QueueForDownload {

	public static final Logger logger = LoggerFactory.getLogger(QueueForDownload.class);
	private static final long TIMEOUT = 10000;
	private Download download;
	private List<String> regexesToMatchURL = new ArrayList<>();
	private Map<String, SiteToCrawl> sites = new HashMap<>();
	private LinkedHashSet<SiteToCrawl> sitesToCrawl = new LinkedHashSet<>();
	private LinkedHashMap<SiteToCrawl, LocalDateTime> assignedSites = new LinkedHashMap<>();

	public QueueForDownload(Set<String> seedUrls, Download download) {
		this.download = download;
		for (String seedUrl : seedUrls) {
			SiteToCrawl seed = new SiteToCrawl(seedUrl);
			sites.put(seedUrl, seed);
			sitesToCrawl.add(seed);
		}
	}

	public synchronized void addCrawledUrl(ExagoCrawledUrl ecu) throws IllegalArgumentException {
		String parentUrl = ecu.getUrl();
		if (parentUrl == null)
			throw new IllegalArgumentException("url cannot be null");
		SiteToCrawl parent = sites.get(parentUrl);
		if (parent == null)
			throw new IllegalArgumentException("the url " + parent + " is unknown ...");
		if (parent.isCrawled())
			return;
		for (ExagoFoundUrl child : ecu.getFoundUrls()) {
			addChildUrl(parent, child.getxPath(), child.getLink());
		}
		setAsCrawled(parent);
	}

	public synchronized void registerAsDetailPage(String url) {
		SiteToCrawl child = sites.get(url);
		if (child != null) {
			child.setAsDetialPage();
			setAsCrawled(child);
		}
	}

	/**
	 * Returns a list of urls to crawl. If all URLs was assigned, empty list is
	 * returned. If download is finished, null is returned.
	 * 
	 * @param maxCount
	 * @return
	 */
	public synchronized ExagoCrawlTask getNextURLs(int maxCount) {
		if (sitesToCrawl.size() == 0 && assignedSites.size() == 0)
			return null;
		List<String> result = new ArrayList<>(maxCount);
		Iterator<SiteToCrawl> it = sitesToCrawl.iterator();
		while (it.hasNext() && maxCount > result.size()) {
			SiteToCrawl next = it.next();
			next.setAsAssigned();
			result.add(next.getUrl());
			assignedSites.put(next, LocalDateTime.now());
			it.remove();
		}
		if (maxCount > result.size()) {
			Iterator<Entry<SiteToCrawl, LocalDateTime>> it2 = assignedSites.entrySet().iterator();
			while (it2.hasNext() && maxCount > result.size()) {
				Entry<SiteToCrawl, LocalDateTime> next = it2.next();
				if (next.getValue().plus(TIMEOUT, ChronoUnit.MILLIS).isBefore(LocalDateTime.now())) {
					result.add(next.getKey().getUrl());
					next.setValue(LocalDateTime.now());
				} else {
					break;
				}
			}
		}
		return new ExagoCrawlTask(download.getId(), result);
	}

	private void setAsCrawled(SiteToCrawl site) {
		LocalDateTime time = assignedSites.get(site);
		if (time != null) {
			logger.debug(
					"URL " + site.getUrl() + " crawled in " + (Duration.between(time, LocalDateTime.now()).toMillis() / 1000.0) + " seconds");
			assignedSites.remove(site);
		}
		site.setAsCrawled();
	}

	private void addChildUrl(SiteToCrawl parent, String xPath, String childUrl) throws IllegalArgumentException {
		if (xPath == null)
			throw new IllegalArgumentException("xPath cannot be null");
		if (childUrl == null)
			throw new IllegalArgumentException("childUrl cannot be null");

		SiteToCrawl child = sites.get(childUrl);
		if (child == null) {
			boolean ok = true;
			for (String regex : regexesToMatchURL) {
				if (!childUrl.matches(regex)) {
					ok = false;
					break;
				}
			}
			if (ok) {
				child = new SiteToCrawl(childUrl);
				sites.put(childUrl, child);
				sitesToCrawl.add(child);
			} else {
				return; // child is not from the same source as parent
			}
		}
		child.getParents().add(parent);
		parent.getxPathsToChildren().put(xPath, child);
	}

	public Download getDownload() {
		return download;
	}
	
	// TODO toto queue bude vymazane ak vratime true, treba este ulozit
	// statistiky o grafe crawlovania na buduce downloady
	public synchronized boolean endDownloadIfFinished() {
		return sitesToCrawl.size() == 0 && assignedSites.size() == 0;
	}
}
