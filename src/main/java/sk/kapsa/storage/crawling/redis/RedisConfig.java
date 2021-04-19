package sk.kapsa.storage.crawling.redis;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.ReactiveRedisConnection;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;

@Configuration
public class RedisConfig {
	static String HOSTNAME = "c.kapsa.sk";
	static String PASSWORD = "dh38fhw0238923df89djkd93la9fjs0mq9gjflv9jkddj934df90rj";

	@Bean
	public RedisStandaloneConfiguration getRedisStandaloneConfiguration() {
		RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration(HOSTNAME);
		configuration.setPassword(RedisPassword.of(PASSWORD));
		return configuration;
	}

	@Primary
	@Bean
	public ReactiveRedisConnectionFactory reactiveConnectionFactory(RedisStandaloneConfiguration configuration) {
		return new LettuceConnectionFactory(configuration);
	}

	@Bean
	public ReactiveRedisConnection reactiveRedisConnection(ReactiveRedisConnectionFactory connectionFactory) {
		return connectionFactory.getReactiveConnection();
	}

	@Bean
	public ReactiveRedisTemplate<String, String> reactiveRedisTemplate(ReactiveRedisConnectionFactory connectionFactory) {
		return new ReactiveRedisTemplate<>(connectionFactory, RedisSerializationContext.string());
	}
}
