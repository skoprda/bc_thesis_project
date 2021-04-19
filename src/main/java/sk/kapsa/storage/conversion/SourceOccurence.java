package sk.kapsa.storage.conversion;

import java.util.List;

public class SourceOccurence {

	private List<Attribute> sources;
	private Attribute destination;
	
	public SourceOccurence(List<Attribute> sources, Attribute destination) {
		super();
		this.sources = sources;
		this.destination = destination;
	}

	public List<Attribute> getSources() {
		return sources;
	}

	public void setSources(List<Attribute> sources) {
		this.sources = sources;
	}

	public Attribute getDestination() {
		return destination;
	}

	public void setDestination(Attribute destination) {
		this.destination = destination;
	}

	@Override
	public String toString() {
		return "SourceOccurence [sources=" + sources + ", destination=" + destination + "]";
	}
	
	
	
}
