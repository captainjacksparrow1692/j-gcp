package uzumtech.j_gcp.configuration;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import uzumtech.j_gcp.configuration.props.RedisProps;

import java.time.Duration;
import java.util.Map;

import static uzumtech.j_gcp.constant.Constant.USER_CACHE;

@EnableCaching
@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RedisConfiguration {

    RedisProps props;


    // Сериализатор для превращения объектов в JSON внутри Redis.
    @Bean
    RedisSerializer<Object> redisJsonSerializer() {
        return RedisSerializer.json();
    }


    // Фабрика соединений через библиотеку Lettuce (асинхронный драйвер).
    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        var configuration = new RedisStandaloneConfiguration();
        configuration.setHostName(props.getHost());
        configuration.setPort(props.getPort());
        configuration.setPassword(props.getPassword());
        configuration.setDatabase(props.getDatabase());

        var lettuceClientConfiguration = LettuceClientConfiguration
                .builder()
                .commandTimeout(Duration.ofMillis(props.getTimeout()))
                .shutdownTimeout(Duration.ofMillis(props.getShutdownTimeout()))
                .build();

        return new LettuceConnectionFactory(configuration, lettuceClientConfiguration);
    }


    //  Менеджер кэша. Определяет настройки времени жизни (TTL) для разных групп данных.
    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory factory, RedisSerializer<Object> redisJsonSerializer) {
        var defaultConfiguration = RedisCacheConfiguration
                .defaultCacheConfig()
                .entryTtl(Duration.ofMillis(props.getDefaultTtl())) // Стандартное время жизни
                .disableCachingNullValues() // Не кэшируем null-результаты
                .serializeKeysWith(
                        RedisSerializationContext.SerializationPair
                                .fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(
                        RedisSerializationContext.SerializationPair
                                .fromSerializer(redisJsonSerializer));

        // Настраиваем разное TTL (время жизни) для разных категорий кэша
        Map<String, RedisCacheConfiguration> perCacheTtl = Map.of(
                USER_CACHE, defaultConfiguration.entryTtl(Duration.ofMillis(props.getGcpTtl()))
        );

        return RedisCacheManager
                .builder(factory)
                .cacheDefaults(defaultConfiguration)
                .withInitialCacheConfigurations(perCacheTtl)
                .transactionAware() // Чтобы кэш обновлялся только после успешного завершения транзакции
                .build();
    }
}