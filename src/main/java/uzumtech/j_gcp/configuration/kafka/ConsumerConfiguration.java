package uzumtech.j_gcp.configuration.kafka;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.support.serializer.JacksonJsonDeserializer;
import uzumtech.j_gcp.configuration.props.KafkaProps;
import uzumtech.j_gcp.dto.response.UserResponseDto;
import uzumtech.j_gcp.handler.KafkaExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ConsumerConfiguration {

    KafkaProps kafkaProps;
    KafkaExceptionHandler kafkaErrorHandler;

    public Map<String, Object> consumerConfigs() {
        Map<String, Object> props = new HashMap<>();

        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProps.getBootstrapServers());
        props.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaProps.getGroupId());

        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        props.put(ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG, 3000);
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, 60000);
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 100);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, kafkaProps.getAutoOffsetResetConfig());

        return props;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, UserResponseDto> userKafkaFactory() {
        return buildContainerFactory(UserResponseDto.class);
    }

    private <T> ConcurrentKafkaListenerContainerFactory<String, T> buildContainerFactory(Class<T> type) {
        // Настройка десериализатора JSON для Kafka
        JacksonJsonDeserializer<T> jsonDeserializer = new JacksonJsonDeserializer<>(type, false);

        jsonDeserializer.addTrustedPackages("uzumtech.j_gcp.dto.response");

        DefaultKafkaConsumerFactory<String, T> consumerFactory = new DefaultKafkaConsumerFactory<>(
                consumerConfigs(),
                new StringDeserializer(),
                jsonDeserializer
        );

        ConcurrentKafkaListenerContainerFactory<String, T> factory = new ConcurrentKafkaListenerContainerFactory<>();

        factory.setConsumerFactory(consumerFactory);
        factory.setBatchListener(false);

        // MANUAL_IMMEDIATE: Подтверждаем получение сообщения вручную после обработки
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);

        // Обработка ошибок при десериализации или выполнении метода
        factory.setCommonErrorHandler(kafkaErrorHandler);

        return factory;
    }
}