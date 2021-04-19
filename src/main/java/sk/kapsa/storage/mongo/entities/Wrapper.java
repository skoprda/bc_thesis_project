package sk.kapsa.storage.mongo.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.mongodb.BasicDBList;

@Document(collection = "Wrapper")
public class Wrapper{

	@Id
	private String id;
	private Long sourceId;
	private String type;
	private String url;
	private String site;
	private Long languageId;
	private Long countryId;
	private BasicDBList items;

	public Wrapper() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Long getSourceId() {
		return sourceId;
	}

	public void setSourceId(Long sourceId) {
		this.sourceId = sourceId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site;
	}

	public Long getLanguageId() {
		return languageId;
	}
	
	public void setLanguageId(Long languageId) {
		this.languageId = languageId;
	}
	
	public Long getCountryId() {
		return countryId;
	}

	public void setCountryId(Long countryId) {
		this.countryId = countryId;
	}

	public BasicDBList getItems() {
		return items;
	}

	public void setItems(BasicDBList items) {
		this.items = items;
	}
	
}
