package sk.kapsa.storage.crawling.beans;

import java.util.List;

public class ExagoCrawledUrl {
	private String url;
	private List<ExagoFoundUrl> foundUrls;
	public ExagoCrawledUrl() {
		super();
	}
	public ExagoCrawledUrl(String url, List<ExagoFoundUrl> foundUrls) {
		super();
		this.url = url;
		this.foundUrls = foundUrls;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public List<ExagoFoundUrl> getFoundUrls() {
		return foundUrls;
	}
	public void setFoundUrls(List<ExagoFoundUrl> foundUrls) {
		this.foundUrls = foundUrls;
	}
	@Override
	public String toString() {
		return "ExagoCrawledUrl [url=" + url + ", foundUrls=" + foundUrls + "]";
	}
}
