package sk.kapsa.storage.conversion;

import java.util.List;

public class Rule {
	
	private String ruleTypeSelector;
	private String alias;
	private List<Attribute> source;
	private Attribute destination;
	private Object ruleSpecific;
	
	public String getRuleTypeSelector() {
		return ruleTypeSelector;
	}
	public void setRuleTypeSelector(String ruleType) {
		this.ruleTypeSelector = ruleType;
	}
	
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	
	public List<Attribute> getSource() {
		return source;
	}
	public void setSource(List<Attribute> source) {
		this.source = source;
	}
	public Attribute getDestination() {
		return destination;
	}
	public void setDestination(Attribute destination) {
		this.destination = destination;
	}
	public Object getRuleSpecific() {
		return ruleSpecific;
	}
	public void setRuleSpecific(Object ruleSpecific) {
		this.ruleSpecific = ruleSpecific;
	}
	
	@Override
	public String toString() {
		return "Rule [ruleTypeSelector=" + ruleTypeSelector + ", alias=" + alias + ", source=" + source
				+ ", destination=" + destination + ", ruleSpecific=" + ruleSpecific + "]";
	}
	
	
	
}	
