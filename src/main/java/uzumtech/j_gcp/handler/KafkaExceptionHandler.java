package uzumtech.j_gcp.handler; // Твой пакет

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.TopicPartition;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.CommonErrorHandler;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class KafkaExceptionHandler implements CommonErrorHandler {

    private final DeadLetterPublishingRecoverer recoverer;

    public KafkaExceptionHandler(@Qualifier("kafkaTemplate") KafkaTemplate<String, Object> kafkaTemplate) {
        this.recoverer = new DeadLetterPublishingRecoverer(kafkaTemplate,
                (record, ex) -> {
                    log.info("Пересылка сообщения в DLQ для топика: {}", record.topic());
                    return new TopicPartition(record.topic() + "-DLT", record.partition());
                });
    }

    @Override
    public boolean handleOne(Exception ex,
                             ConsumerRecord<?, ?> record,
                             Consumer<?, ?> consumer,
                             MessageListenerContainer container) {
        log.error("Ошибка при обработке сообщения из топика [{}], смещение [{}]: {}",
                record.topic(), record.offset(), ex.getMessage());

        // Отправляем в DLQ
        recoverer.accept(record, ex);

        return true; // true означает, что ошибка "обработана" и мы идем дальше к следующему сообщению
    }

    @Override
    public void handleOtherException(Exception ex,
                                     Consumer<?, ?> consumer,
                                     MessageListenerContainer container,
                                     boolean batchListener) {
        log.error("Глобальная ошибка Kafka Listener (Consumer Error): {}", ex.getMessage());
    }
}