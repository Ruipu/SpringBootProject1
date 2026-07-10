package net.javaguides.ems.kafka.producer;

import net.javaguides.ems.kafka.config.KafkaTopicConfig;
import net.javaguides.ems.kafka.event.EmployeeEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

@Component
public class EmployeeEventProducer {

    private static final Logger log = LoggerFactory.getLogger(EmployeeEventProducer.class);

    @Autowired
    private KafkaTemplate<String, EmployeeEvent> kafkaTemplate;

    public void publish(EmployeeEvent event) {
        // Key by employeeId so all events for the same employee land on the
        // same partition and are processed in the order they were produced.
        String key = String.valueOf(event.getEmployeeId());

        kafkaTemplate.send(KafkaTopicConfig.EMPLOYEE_EVENTS_TOPIC, key, event)
                .whenComplete((result, ex) -> {
                    if (ex == null) {
                        logSuccess(result);
                    } else {
                        log.error("Failed to publish event={} key={}", event, key, ex);
                    }
                });
    }

    private void logSuccess(SendResult<String, EmployeeEvent> result) {
        log.info("Published event={} partition={} offset={}",
                result.getProducerRecord().value(),
                result.getRecordMetadata().partition(),
                result.getRecordMetadata().offset());
    }
}