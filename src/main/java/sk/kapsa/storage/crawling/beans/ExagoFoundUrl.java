package sk.kapsa.storage.crawling.beans;

public class ExagoFoundUrl {
	private String xPath;
	private String link;
	public ExagoFoundUrl(String xPath, String link) {
		super();
		this.xPath = xPath;
		this.link = link;
	}
	public ExagoFoundUrl() {
		super();
	}
	public String getxPath() {
		return xPath;
	}
	public void setxPath(String xPath) {
		this.xPath = xPath;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	@Override
	public String toString() {
		return "\n  ExagoFoundUrl [xPath=" + xPath + ", link=" + link + "]";
	}
}
