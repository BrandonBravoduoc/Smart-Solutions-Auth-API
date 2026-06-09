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
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.beans.factory.annotation.Value;

import io.lettuce.core.ClientOptions;

@Configuration
@EnableCaching
public class CacheConfig {

    private static final Logger log = LoggerFactory.getLogger(CacheConfig.class);

    private static final Duration DEFAULT_COMMAND_TIMEOUT = Duration.ofSeconds(5);
    private static final Duration DEFAULT_SHUTDOWN_TIMEOUT = Duration.ofMillis(200);

    @Value("${spring.data.redis.host}")
    private String redisHost;

    @Value("${spring.data.redis.port}")
    private int redisPort;

    @Value("${spring.data.redis.username}")
    private String redisUsername;

    @Value("${spring.data.redis.password:}")
    private String redisPassword;

    @Value("${spring.data.redis.ssl.enabled:true}")
    private boolean redisSslEnabled;

    @Value("${spring.data.redis.timeout:5s}")
    private Duration redisCommandTimeout;

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
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(redisHost, redisPort);
        if (redisUsername != null && !redisUsername.isBlank()) {
            config.setUsername(redisUsername);
        }
        if (redisPassword != null && !redisPassword.isBlank()) {
            config.setPassword(redisPassword);
        }

        LettuceClientConfiguration.LettuceClientConfigurationBuilder clientConfigBuilder = LettuceClientConfiguration.builder();
        if (redisSslEnabled) {
            clientConfigBuilder.useSsl();
        }
        clientConfigBuilder
                .commandTimeout(redisCommandTimeout != null ? redisCommandTimeout : DEFAULT_COMMAND_TIMEOUT)
                .shutdownTimeout(DEFAULT_SHUTDOWN_TIMEOUT)
                .clientOptions(ClientOptions.builder()
                        .autoReconnect(true)
                        .build());

        LettuceClientConfiguration clientConfig = clientConfigBuilder.build();

        return new LettuceConnectionFactory(config, clientConfig);

    }

    @Bean
    CommandLineRunner checkRedisConnection(RedisConnectionFactory connectionFactory) {
        return args -> {
            try (RedisConnection connection = connectionFactory.getConnection()) {
                String pong = connection.ping();
                log.info("Conexión Redis/Valkey verificada en el arranque: {}", pong);
            } catch (Exception e) {
                log.error("No se pudo verificar la conexión inicial a Redis/Valkey. Revisa host, puerto, TLS, usuario ACL y contraseña.", e);
            }
        };
    }
}
