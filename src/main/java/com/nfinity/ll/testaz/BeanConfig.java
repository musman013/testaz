package com.nfinity.ll.testaz;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.cache.transaction.TransactionAwareCacheManagerProxy;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories
public class BeanConfig {

	@Autowired 
    private Environment environment; 
    
	@Bean
    JedisConnectionFactory jedisConnectionFactory() {
    	RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration(environment.getProperty("redis.server.address"), Integer.parseInt(environment.getProperty("redis.server.port")));
        return new JedisConnectionFactory(redisStandaloneConfiguration);
    }
 
    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
         RedisTemplate<String, Object> template = new RedisTemplate<>();
         JedisConnectionFactory jc = jedisConnectionFactory();
         jc.getPoolConfig().setMaxIdle(30);
         jc.getPoolConfig().setMinIdle(10);
         template.setConnectionFactory(jc);
	         return template;
    }

    @Bean
    public CacheManager cacheManager() {
        SimpleCacheManager cacheManager = new SimpleCacheManager();

        List<Cache> caches = new ArrayList<>();
        caches.add(new ConcurrentMapCache("User"));
        caches.add(new ConcurrentMapCache("Role"));
        caches.add(new ConcurrentMapCache("Permission"));
        caches.add(new ConcurrentMapCache("Rolepermission"));
        caches.add(new ConcurrentMapCache("Userrole"));
        caches.add(new ConcurrentMapCache("Userpermission"));
		caches.add(new ConcurrentMapCache("Types"));
		caches.add(new ConcurrentMapCache("Pets"));
		caches.add(new ConcurrentMapCache("Specialties"));
		caches.add(new ConcurrentMapCache("Visits"));
		caches.add(new ConcurrentMapCache("Vets"));
		caches.add(new ConcurrentMapCache("VetSpecialties"));
		caches.add(new ConcurrentMapCache("Owners"));

        cacheManager.setCaches(caches );

        // manually call initialize the caches as our SimpleCacheManager is not declared as a bean
        cacheManager.initializeCaches();

        return new TransactionAwareCacheManagerProxy( cacheManager );
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
 
}
