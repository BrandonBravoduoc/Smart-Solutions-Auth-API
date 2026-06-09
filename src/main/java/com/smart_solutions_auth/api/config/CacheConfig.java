package com.smart_solutions_auth.api.config;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.cache.Cache;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.cache.interceptor.SimpleCacheErrorHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

@Configuration
@EnableCaching
public class CacheConfig {

    private static final Logger log = LoggerFactory.getLogger(CacheConfig.class);

    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {

        GenericJackson2JsonRedisSerializer jsonSerializer = new GenericJackson2JsonRedisSerializer();

        RedisCacheConfiguration defaultCacheConfig = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(10))
                .disableCachingNullValues()
                .serializeKeysWith(
                        RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(jsonSerializer));
        Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>();

        cacheConfigurations.put("users", defaultCacheConfig.entryTtl(Duration.ofMinutes(15)));
        cacheConfigurations.put("regions", defaultCacheConfig.entryTtl(Duration.ofHours(12)));
        cacheConfigurations.put("communes", defaultCacheConfig.entryTtl(Duration.ofHours(12)));
        cacheConfigurations.put("addresses", defaultCacheConfig.entryTtl(Duration.ofHours(12)));
        cacheConfigurations.put("roles", defaultCacheConfig.entryTtl(Duration.ofMinutes(10)));

        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(defaultCacheConfig)
                .withInitialCacheConfigurations(cacheConfigurations)
                .build();
    }

    @Bean
    public CacheErrorHandler errorHandler() {
        return new SimpleCacheErrorHandler() {

            @Override
            public void handleCacheGetError(@NonNull RuntimeException exception, @NonNull Cache cache,
                    @NonNull Object key) {
                log.error("Error al LEER. Info -> Tabla {}, Llave: {}, Error: {}", cache.getName(), key, exception);
            }

            @Override
            public void handleCachePutError(@NonNull RuntimeException exception, @NonNull Cache cache,
                    @NonNull Object key, @Nullable Object value) {
                log.error("Redis caído al ESCRIBIR. Info -> Tabla: {}, Llave: {}. Error: {}",
                        cache.getName(), key, exception.getMessage());
            }

            @Override
            public void handleCacheEvictError(@NonNull RuntimeException exception, @NonNull Cache cache,
                    @NonNull Object key) {
                log.error("Error crítico al borrar caché. Relanzando error para evitar inconsistencias.");
                throw exception;
            }

        };
    }

    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        config.setHostName("${REDIS_HOST:smart-solutions-cache-po4yvy.serverless.sae1.cache.amazonaws.com}");
        config.setPort(6379);
        config.setUsername("${REDIS_USERNAME:auth-api-user}");
        config.setPassword("${REDIS_PASSWORD}");

        LettuceClientConfiguration clientConfig = LettuceClientConfiguration.builder()
                .useSsl() // Activa TLS
                .disablePeerVerification() // Crucial: AWS Serverless a veces falla si esto no está desactivado
                .build();

        return new LettuceConnectionFactory(config, clientConfig);

    }

    @Bean
    CommandLineRunner checkRedisConnection(RedisConnectionFactory connectionFactory) {
        return args -> {
            try {
                connectionFactory.getConnection().ping();
                log.info("--- ¡CONEXIÓN A REDIS EXITOSA EN EL ARRANQUE! ---");
            } catch (Exception e) {
                log.error("--- ¡ERROR FATAL DE CONEXIÓN A REDIS EN EL ARRANQUE! ---", e);
            }
        };
    }
}
