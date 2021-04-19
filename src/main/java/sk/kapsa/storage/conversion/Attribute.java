package sk.kapsa.storage.conversion;

import java.util.ArrayList;
import java.util.List;

public class Attribute {

	private List<String> pathItems;

	public Attribute() {
		pathItems = new ArrayList<>();
	}

	public Attribute(List<String> pathItems) {
		this.pathItems = pathItems;
	}

	public int size() {
		return pathItems.size();
	}

	public String getLast() {
		int itemsSize = pathItems.size();
		if(itemsSize == 0) 
			return null;
		String last = pathItems.get(itemsSize - 1);
		return last;
	}

	public String get(int index) {
		return pathItems.get(index);
	}

	public void set(int index, String value) {
		this.pathItems.set(index, value);
	}

	public Attribute subPath(int from, int to) {
		List<String> newPathItems = pathItems.subList(from, to);
		return new Attribute(newPathItems);
	}
	
	public Attribute commonPrefix(Attribute anotherPath) {
		// ak cesta [0]/a[0]/b[2]/c
		// a cesta [0]/a[0]/b[0]/d iteruje sa len cez [0]/a[0]
		Attribute shorterPath = this.size() <= anotherPath.size() ? this : anotherPath;
		Attribute longerPath = this.size() > anotherPath.size() ? this : anotherPath;
		Attribute commonPrefix = null;
		for(int i = 0; i < shorterPath.size(); i++) {
			String shorterItem = shorterPath.get(i);
			String longerItem = longerPath.get(i);
			if(!shorterItem.equals(longerItem)) {
				commonPrefix = shorterPath.subPath(0, i);
				return commonPrefix;
			}
		}
		commonPrefix = shorterPath.subPath(0, shorterPath.size());
		return commonPrefix;
	}

	public List<String> getPathItems() {
		return this.pathItems;
	}

	public void setPathItems(List<String> items) {
		this.pathItems = items;
	}

	@Override
	public String toString() {
		return "pathItems: " + pathItems.toString();
	}

}
