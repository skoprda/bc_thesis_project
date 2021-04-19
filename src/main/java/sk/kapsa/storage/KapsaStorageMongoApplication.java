package sk.kapsa.storage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import sk.kapsa.storage.crawling.redis.RedisConfig;
import sk.kapsa.storage.mongo.MongoConfig;
@SpringBootApplication
@EnableMongoRepositories({ "sk.kapsa.storage" })
@ComponentScan(value = {"sk.kapsa.storage"}, excludeFilters = {@Filter(type = FilterType.ASSIGNABLE_TYPE,
value = {MongoConfig.class ,RedisConfig.class})})
public class KapsaStorageMongoApplication {	
	public static void main(String[] args) {
		SpringApplication.run(KapsaStorageMongoApplication.class, args);	
	}

}
