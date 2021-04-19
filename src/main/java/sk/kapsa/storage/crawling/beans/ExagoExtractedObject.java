package sk.kapsa.storage.crawling.beans;


import org.bson.Document;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.mongodb.BasicDBList;

public class ExagoExtractedObject {
	
	@JsonAlias(value = "_id")
	private Object id;
	private String url;
	private BasicDBList data;
	private long downloadId;

	public ExagoExtractedObject() {
	}

	public ExagoExtractedObject(String url, BasicDBList data) {
		this.url = url;
		this.data = data;
	}

	public Object getId() {
		return id;
	}

	public void setId(Object id) {
		this.id = id;
	}
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public BasicDBList getData() {
		return data;
	}

	public void setData(BasicDBList data) {
		this.data = data;
	}

	public long getDownloadId() {
		return downloadId;
	}

	public void setDownloadId(long downloadId) {
		this.downloadId = downloadId;
	}
	
	public Document toDocument() {
		Document doc = new Document();
		doc.append("url", url);
		doc.append("downloadId", downloadId);
		doc.append("data", data);
		return doc;
	}

	

	
}
