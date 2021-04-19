package sk.kapsa.storage.mongo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;


@Configuration
@ComponentScan(basePackages = {"sk.kapsa.storage.mongo"})
@EnableMongoRepositories
public class MongoConfig extends AbstractMongoConfiguration{
   public static final String DB_NAME = "KapsaDB";
	public static final String DB_HOST = "localhost";
	public static final int DB_PORT = 27017;
	
	
	@Override
	protected String getDatabaseName() {
		return DB_NAME;
	}

	@Override
	@Bean
	public MongoClient mongoClient() {
		System.out.println("mongo client metoda :)");
		return new MongoClient(new ServerAddress(DB_HOST, DB_PORT));
	}
	
	
}
