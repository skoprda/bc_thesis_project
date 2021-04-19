package sk.kapsa.storage.mongo.entities;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import sk.kapsa.storage.conversion.Rule;

@Document("RuleWrapper")
public class RuleWrapper {
	
	@Id
	private String id;
	private String wrapperId;
	private List<Rule> rules;
	
	public List<Rule> getRulesUpTo(int upTo){
		return rules.subList(0, upTo);
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getWrapperId() {
		return wrapperId;
	}
	public void setWrapperId(String wrapperId) {
		this.wrapperId = wrapperId;
	}
	public List<Rule> getRules() {
		return rules;
	}
	public void setRules(List<Rule> rules) {
		this.rules = rules;
	}
	
	
}
