package sk.kapsa.storage.conversion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bson.Document;

public class SourceAttributesFinder implements Iterator<SourceOccurence> {

	private Document item;
	private List<Attribute> exampleSources;
	private Attribute exampleDestination;

	private Attribute commonPrefix;
	private Map<Attribute, Integer> pathsCommonPrefixListCount;
	private Map<Attribute, Integer> listSizes;
	private int[] actualSelection;
	private boolean hasNext = true;

	public SourceAttributesFinder(Document item, List<Attribute> exampleSources, Attribute exampleDestination) {
		this.item = item;
		this.exampleSources = exampleSources;
		this.exampleDestination = exampleDestination;
		normalizeAttributes();
		this.commonPrefix = getLongestPrefix();
		this.listSizes = calculateListSizes();
		this.pathsCommonPrefixListCount = calculateCommonPrefixListCount();
		this.actualSelection = new int[listSizes.size()];
	}

	public SourceAttributesFinder(Document item, List<Attribute> exampleSources) {
		this(item, exampleSources, exampleSources.get(0));
	}

	@Override
	public boolean hasNext() {
		return hasNext;
	}

	@Override
	public SourceOccurence next() {
		int[] sizes = getSizes();
		for (int i = actualSelection.length - 1; i >= 0; i--) {
			if (actualSelection[i] + 1 < sizes[i]) {
				actualSelection[i]++;
				break;
			} else {
				actualSelection[i] = 0;
				if(i == 0) hasNext = false; // vynulovali sme posledny prvok teda koncime
			}
		}
		SourceOccurence sourceDestination = getSourceDestinationFromSelection();
		return sourceDestination;
		
	}

	protected Map<Attribute, Integer> calculateCommonPrefixListCount() {
		//LinkedHashMap kvoli zachovaniu poradia
		Map<Attribute, Integer> pathsCommonPrefixSize = new LinkedHashMap<>(); 
		for (Attribute source : exampleSources) {
			Attribute prefix = source.commonPrefix(this.commonPrefix);
			int numberOfListsOnPath = AttributeSearcher.getListsCountOnPath(item, prefix);
			pathsCommonPrefixSize.put(source, numberOfListsOnPath);
		}
		Attribute destinationPrefix = exampleDestination.commonPrefix(this.commonPrefix);
		int numberOfListsOnPath = AttributeSearcher.getListsCountOnPath(item, destinationPrefix);
		pathsCommonPrefixSize.put(exampleDestination, numberOfListsOnPath);
		return pathsCommonPrefixSize;
	}

	protected Attribute getLongestPrefix() {
		Attribute commonPrefix = new Attribute();
		for (Attribute source : exampleSources) {
			Attribute actualPrefix = source.commonPrefix(exampleDestination);
			if (actualPrefix.size() > commonPrefix.size()) {
				commonPrefix = actualPrefix;
			}
		}
		return commonPrefix;
	}

	protected SourceOccurence getSourceDestinationFromSelection() {
		List<Attribute> sources = new ArrayList<>();
		Attribute destination = null;
		int index = 0;
		int pathsCommonPrefixSizeLength = pathsCommonPrefixListCount.size();
		for (Entry<Attribute, Integer> entry : pathsCommonPrefixListCount.entrySet()) {
			Attribute path = entry.getKey();
			int upTo = entry.getValue();
			if (index < pathsCommonPrefixSizeLength - 1) {
				Attribute source = getPathFromSelection(path, upTo);
				sources.add(source);
			}else {
				destination = getPathFromSelection(path, upTo);		 
			}
			index++;
		}
		SourceOccurence sourceDestination = new SourceOccurence(sources, destination);
		return sourceDestination;
	}
	
	protected Attribute getPathFromSelection(Attribute path, int upTo) {
		int i = 0;
		for (Attribute listPath : listSizes.keySet()) {
			if(i == upTo) {
				break;
			}
			int index = listPath.size();
			int value = actualSelection[i];
			i++;
			path.set(index, Integer.toString(value));
		}
		return path;
	}

	protected int[] getSizes() {
		commonPrefix = getPathFromSelection(commonPrefix, commonPrefix.size());
		listSizes = calculateListSizes();
		int[] sizesArray = new int[listSizes.size()];
		int i = 0;
		for (int size : listSizes.values()) {
			sizesArray[i] = size;
			i++;
		}
		return sizesArray;
	}

	private Map<Attribute, Integer> calculateListSizes() {
		return AttributeSearcher.getListsSizesOnPath(item, commonPrefix);
	}
	
	private void normalizeAttributes() {
		for(Attribute source: exampleSources) {
			normalizeOneAttribute(source);
		}
		normalizeOneAttribute(exampleDestination);
	}

	private void normalizeOneAttribute(Attribute attribute) {
		List<Attribute> listsOnPath = AttributeSearcher.getListsAttributesOnPath(item, attribute);
		for(Attribute listOnPath: listsOnPath) {
			attribute.set(listOnPath.size(), "0");
		}
	}

	
}
