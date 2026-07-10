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
public class EmployeeAuditConsumer {

    private static final Logger log = LoggerFactory.getLogger(EmployeeAuditConsumer.class);

    @KafkaListener(
            topics = "employee-events",
            groupId = "audit-service",
            concurrency = "3"
    )
    public void onEvent(
            @Payload EmployeeEvent event,
            @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
            @Header(KafkaHeaders.OFFSET) long offset,
            Acknowledgment ack) {

        log.info("[audit-service] Consumed event={} partition={} offset={}",
                event, partition, offset);

        // Simulated side effect: write an audit trail entry for every event type.
        log.info("[audit-service] Recording audit trail: employeeId={} eventType={} timestamp={}",
                event.getEmployeeId(), event.getEventType(), event.getTimestamp());

        ack.acknowledge();
    }
}