package net.javaguides.ems.kafka.consumer;

import net.javaguides.ems.kafka.event.EmployeeEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class EmployeeEventConsumer {

    private static final Logger log = LoggerFactory.getLogger(EmployeeEventConsumer.class);

    @KafkaListener(
            topics = "employee-events",
            groupId = "notification-service",
            concurrency = "3"
    )
    public void onEvent(
            @Payload EmployeeEvent event,
            @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
            @Header(KafkaHeaders.OFFSET) long offset,
            Acknowledgment ack) {

        // Simulated side effect: send a welcome email on CREATED events.
        log.info("[notification-service] Consumed event={} partition={} offset={}",
                event, partition, offset);

        if (event.getEventType() == EmployeeEvent.EventType.CREATED) {
            log.info("[notification-service] Sending welcome email to {}", event.getEmail());
        }

        // Manually commit the offset only after processing succeeds.
        ack.acknowledge();
    }
}