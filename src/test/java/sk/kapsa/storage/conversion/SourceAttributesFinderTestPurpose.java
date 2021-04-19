package sk.kapsa.storage.conversion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bson.Document;

public class SourceAttributesFinderTestPurpose extends SourceAttributesFinder {

	public SourceAttributesFinderTestPurpose(Document item, List<Attribute> exampleSources, Attribute exampleDestination) {
		super(item, exampleSources, exampleDestination);
	}
	
	public Attribute getLongestPrefix() {
		return super.getLongestPrefix();
	}
	
	public Map<Attribute, Integer> calculateCommonPrefixListCount() {
		return super.calculateCommonPrefixListCount();
	}
	
	public SourceOccurence getSourceDestinationFromSelection() {
		return super.getSourceDestinationFromSelection();
	}
	
	public Attribute getPathFromSelection(Attribute path, int upTo) {
		return super.getPathFromSelection(path, upTo);
	}
	
	public int[] getSizes() {
		return super.getSizes();
	}

}
