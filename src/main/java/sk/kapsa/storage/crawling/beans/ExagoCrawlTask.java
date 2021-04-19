package sk.kapsa.storage.crawling.beans;

import java.util.ArrayList;
import java.util.List;

public class ExagoCrawlTask {
	private long downloadId;
	private List<String> urlsToCrawl = new ArrayList<>();
	public ExagoCrawlTask() {
	}
	public ExagoCrawlTask(long downloadId, List<String> urlsToCrawl) {
		this.downloadId = downloadId;
		this.urlsToCrawl = urlsToCrawl;
	}
	public long getDownloadId() {
		return downloadId;
	}
	public void setDownloadId(long downloadId) {
		this.downloadId = downloadId;
	}
	public List<String> getUrlsToCrawl() {
		return urlsToCrawl;
	}
}
