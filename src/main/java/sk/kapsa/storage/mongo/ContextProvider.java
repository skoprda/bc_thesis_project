package sk.kapsa.storage.mongo;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import sk.kapsa.storage.mongo.services.DownloadService;
import sk.kapsa.storage.mongo.services.SourceService;
import sk.kapsa.storage.mongo.services.WrapperService;

public enum ContextProvider {
	CONTEXT;
	
	private AnnotationConfigApplicationContext context;
	
	private ContextProvider() {
		context = new AnnotationConfigApplicationContext(MongoConfig.class);
	}
	
	/**
	 * 
	 * @return DownloadService which provides functionality to manipulate downloads
	 */
	public DownloadService getDownloadService() {
		return this.context.getBean(DownloadService.class);
	}
	
	/**
	 * 
	 * @return SourceService which provides functionality to manipulate sources
	 */
	public SourceService getSourceService() {
		return this.context.getBean(SourceService.class);
	}
	

	public WrapperService getWrapperService() {
		return context.getBean(WrapperService.class);
	}
	
}
