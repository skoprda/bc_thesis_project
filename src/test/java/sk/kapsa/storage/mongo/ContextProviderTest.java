package sk.kapsa.storage.mongo;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;

import org.junit.jupiter.api.Test;

import com.mongodb.BasicDBList;

import sk.kapsa.storage.mongo.entities.Wrapper;
import sk.kapsa.storage.mongo.services.WrapperService;

class ContextProviderTest {

	@Test
	void testGetWrapperService() {
		WrapperService wrapperService = ContextProvider.CONTEXT.getWrapperService();
		Wrapper wrapper = new Wrapper();
		BasicDBList items = new BasicDBList();
		wrapper.setItems(items);
		wrapper.setUrl("abc");
		Set<String> urlsFromWrapper = wrapperService.getSeedUrlsFromWrapper(wrapper);
		assertEquals(1, urlsFromWrapper.size());
	}

}
