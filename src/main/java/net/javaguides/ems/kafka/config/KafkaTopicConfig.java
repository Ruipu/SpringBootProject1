package net.javaguides.ems.kafka.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    public static final String EMPLOYEE_EVENTS_TOPIC = "employee-events";
    public static final String EMPLOYEE_EVENTS_DLT = "employee-events.DLT";

    @Bean
    public NewTopic employeeEventsTopic() {
        return TopicBuilder.name(EMPLOYEE_EVENTS_TOPIC)
                .partitions(3)
                .replicas(3)
                .build();
    }

    @Bean
    public NewTopic employeeEventsDlt() {
        return TopicBuilder.name(EMPLOYEE_EVENTS_DLT)
                .partitions(3)
                .replicas(3)
                .build();
    }
}