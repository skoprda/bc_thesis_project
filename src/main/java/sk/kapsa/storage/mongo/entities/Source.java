package sk.kapsa.storage.mongo.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Source")
public class Source {

	@Id
	private Long id;
	private String site;
	private String name;

	public Source() {
		super();
	}
	
	public Source(String site, String name) {
		this.site = site;
		this.name = name;
	}
	
	public Source(Long id, String site, String name) {
		this(site, name);
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
