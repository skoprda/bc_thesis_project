package sk.kapsa.storage.crawling;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SiteToCrawl {

	//TODO co s redirektmi?? 301 302 ?? - nejake aliasy?
	private String url;
	private Set<SiteToCrawl> parents = new HashSet<>();
	private Map<String, SiteToCrawl> xPathsToChildren = new HashMap<>();
	private boolean assigned;
	private boolean crawled;
	private boolean detialPage;
	
	public SiteToCrawl(String url) {
		this.url = url;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Set<SiteToCrawl> getParents() {
		return parents;
	}

	public Map<String, SiteToCrawl> getxPathsToChildren() {
		return xPathsToChildren;
	}

	public boolean isAssigned() {
		return assigned;
	}

	public void setAsAssigned() {
		this.assigned = true;
	}

	public boolean isCrawled() {
		return crawled;
	}

	public void setAsCrawled() {
		this.crawled = true;
	}

	public boolean isDetialPage() {
		return detialPage;
	}

	public void setAsDetialPage() {
		this.detialPage = true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((url == null) ? 0 : url.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SiteToCrawl other = (SiteToCrawl) obj;
		if (url == null) {
			if (other.url != null)
				return false;
		} else if (!url.equals(other.url))
			return false;
		return true;
	}
}
