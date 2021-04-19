package sk.kapsa.storage.conversion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.bson.Document;

public class AttributeSearcher {

	public static List<Object> getSourceAttributes(Document item, List<Attribute> sources) {
		List<Object> resultSourceAttributes = new ArrayList<>();
		for (Attribute path : sources) {
			Object attribute = getSourceAttribute(item, path);
			resultSourceAttributes.add(attribute);
		}
		return resultSourceAttributes;
	}

	private static Object getSourceAttribute(Document item, Attribute source) {
		Object partialResult = item;
		for (int i = 0; i < source.size(); i++) {
			partialResult = findPartialAttr(partialResult, source, i);
		}
		return partialResult;
	}

	public static Document injectAttribute(Document newItem, Attribute destination, Object destinationAttrValue) {
		String attrName = destination.getLast();
		Object partialResult = newItem;
		for (int i = 0; i < destination.size() - 1; i++) {
			partialResult = findPartialAttr(partialResult, destination, i);
		}
		if (partialResult instanceof Document) {
			((Document) partialResult).put(attrName, destinationAttrValue);
		} else if (partialResult instanceof List<?>) {
			try {
				int index = Integer.parseInt(attrName);
				List listResult = ((List<?>) partialResult);
				listResult.get(index);
				listResult.set(index, destinationAttrValue);

			} catch (IndexOutOfBoundsException oobe) {
				System.err.println("index: " + attrName + " neexistuje");
				throw oobe;
			}
		} else {
			System.err.println("nevedel som zaradit: " + partialResult);
		}

		return newItem;
	}

	public static Document deleteAttributes(Document item, List<Attribute> attrsToDelete) {
		Document newItem = item;
		for (Attribute toDelete : attrsToDelete) {
			newItem = deleteAttribute(newItem, toDelete);
		}
		return newItem;
	}

	public static Map<Attribute, Integer> getListsSizesOnPath(Document item, Attribute path) {
		Map<Attribute, Integer> listsSizes = new LinkedHashMap<>();
		Object partialResult = item;
		for (int actualIndex = 0; actualIndex < path.size(); actualIndex++) {
			partialResult = findPartialAttr(partialResult, path, actualIndex);
			if (partialResult instanceof List<?> && path.size() > actualIndex + 1) {
				int size = ((List<?>) partialResult).size();
				Attribute listPath = path.subPath(0, actualIndex + 1);
				listsSizes.put(listPath, size);
			}
		}
		return listsSizes;
	}

	public static int getListsCountOnPath(Document item, Attribute path) {
		int count = 0;
		Object partialResult = item;
		for (int actualIndex = 0; actualIndex < path.size(); actualIndex++) {
			partialResult = findPartialAttr(partialResult, path, actualIndex);

			if (partialResult instanceof List<?> && path.size() > actualIndex + 1) {
				count++;
			}

		}
		return count;
	}
	
	public static List<Attribute> getListsAttributesOnPath(Document item, Attribute attribute){
		List<Attribute> listsOnPath = new ArrayList<>();
		Object partialResult = item;
		for (int actualIndex = 0; actualIndex < attribute.size(); actualIndex++) {
			partialResult = findPartialAttr(partialResult, attribute, actualIndex);
			
			if (partialResult instanceof List<?> && attribute.size() > actualIndex + 1) {
				listsOnPath.add(attribute.subPath(0, actualIndex + 1));
			}
		}
		return listsOnPath;
	}

	private static Document deleteAttribute(Document item, Attribute toDelete) {
		Object partialResult = item;
		String last = toDelete.getLast();
		for (int i = 0; i < toDelete.size() - 1; i++) {
			partialResult = findPartialAttr(partialResult, toDelete, i);
		}
		if (partialResult instanceof Document) {
			((Document) partialResult).remove(last);
		} else if (partialResult instanceof List<?>) {
			try {
				List listResult = ((List<?>) partialResult);
				listResult.removeIf(listItem -> true);
			} catch (IndexOutOfBoundsException oobe) {
				System.err.println("index: " + last + " neexistuje (Mazanie)");
				throw oobe;
			}
		} else {
			System.err.println("nevedel som zaradit: " + partialResult);
		}

		return item;
	}

	private static Object findPartialAttr(Object item, Attribute path, int actualIndex) {
		Object partialResult = item;
		String sourceItem = path.get(actualIndex);
		if (partialResult instanceof Document) {
			partialResult = ((Document) partialResult).get(sourceItem);
		} else if (partialResult instanceof List<?>) {
			int index = Integer.parseInt(path.get(actualIndex));
			try {
				partialResult = ((List<?>) partialResult).get(index);
			} catch (IndexOutOfBoundsException oobe) {
				return null;
			}
		} else {
			System.err.println("nevedel som zaradit: " + partialResult);
		}
		return partialResult;
	}

}
