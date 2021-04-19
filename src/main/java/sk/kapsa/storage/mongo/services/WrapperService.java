package sk.kapsa.storage.mongo.services;

import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sk.kapsa.storage.mongo.WrapperRepository;
import sk.kapsa.storage.mongo.entities.Wrapper;

@Service
public class WrapperService {

	private static final Logger logger = LoggerFactory.getLogger(WrapperService.class);

	@Autowired
	private WrapperRepository wrapperRepository;
	@Autowired
	private SourceService sourceService;

	
	public List<Wrapper> getAll(){
		return wrapperRepository.findAll();
	}
	
	public Wrapper getWrapperByUrl(String url) {
		List<Wrapper> wrappers = wrapperRepository.findAll();
		List<Wrapper> wrappersFitUrl= wrappers.stream().filter((wrapper) ->  url.contains(wrapper.getSite())).collect(Collectors.toList());

		if (wrappersFitUrl.size() == 0) {
			logger.warn("There is no wrapper for a given url: " + url);
			return null;
		} else {
			int biggestSite = 0;
			Wrapper actualBestFitWrapper = wrappersFitUrl.get(0);
			for (Wrapper wrapper : wrappersFitUrl) {
				if (wrapper.getSite().length() > biggestSite) {
					actualBestFitWrapper = wrapper;
					biggestSite = wrapper.getSite().length();
				}
			}
			return actualBestFitWrapper;
		}
	}

	public Wrapper save(Wrapper wrapper) throws WrapperMalformedException {
		if (wrapper.getId() == null) {
			String site = wrapper.getSite();
			if (site == null || site.isBlank()) {
				logger.error("wrapper contains no site attribute");
				throw new WrapperMalformedException("wrapper contains no site attribute");
			}
			Long sourceId = sourceService.getSourceIdBySite(site);
			wrapper.setSourceId(sourceId);
			Wrapper wrapperWithId = wrapperRepository.insert(wrapper);
			logger.info("New wrapper created succesfully");
			return wrapperWithId;
		} else {
			wrapperRepository.save(wrapper);
			logger.info("Wrapper updated succesfully");
			return wrapper;
		}
	}

	public Set<String> getSeedUrlsFromWrapper(Wrapper wrapper) throws WrapperMalformedException {
		try {
			Set<String> urls = new HashSet<>();
			JSONArray tabs = new JSONArray(wrapper.getItems());
			if (tabs != null && tabs.length() > 0) {
				for (int i = 0; i < tabs.length(); i++) {
					JSONObject tab = tabs.optJSONObject(i);
					if (tab != null && tab.optString("target").equals("crawlerRules")) {
						JSONArray items = tab.optJSONArray("items");
						if (items != null && items.length() > 0) {
							for (int j = 0; j < items.length(); j++) {
								JSONObject rule = items.optJSONObject(j);
								if (! rule.optBoolean("negative")) {
									String regex = rule.optString("regex");
									if (regex.startsWith("http") && ! regex.contains("(")) {
										try
									    {
									        URL url = new URL(regex);
									        url.toURI();
									        urls.add(regex);
									    } catch (Exception exception){}
									}
								}
							}
						}
					}
				}
			}
			if (urls.size() == 0)
				urls.add(wrapper.getUrl());
			return urls;
		} catch (JSONException e) {
			logger.error("Data error: Wrapper for site {} does not have seed url.", wrapper.getSite());
			throw new WrapperMalformedException(e);
		}
	}

	public Wrapper getWrapperById(String wrapperId) throws NoSuchElementException {
		Optional<Wrapper> optional = wrapperRepository.findById(wrapperId);
		if (optional.isPresent())
			return optional.get();
		String error = "wrapper with id " + wrapperId + " not found";
		logger.error(error);
		throw new NoSuchElementException(error); 
	}

	public Wrapper getWrapperBySite(String site) {
		List<Wrapper> list = wrapperRepository.findByUrl(site);
		if (list == null || list.isEmpty()) {
			return null;
		}
		return list.get(0);
	}
	
	public List<Wrapper> getWrappersBySite(String site){
		return wrapperRepository.findBySite(site);
	}

}
