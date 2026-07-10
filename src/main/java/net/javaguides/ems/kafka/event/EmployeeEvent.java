package net.javaguides.ems.kafka.event;

import java.time.LocalDateTime;

public class EmployeeEvent {

    public enum EventType {
        CREATED, UPDATED, DELETED
    }

    private EventType eventType;
    private Long employeeId;
    private String firstName;
    private String lastName;
    private String email;
    private LocalDateTime timestamp;

    public EmployeeEvent() {
    }

    public EmployeeEvent(EventType eventType, Long employeeId, String firstName, String lastName, String email) {
        this.eventType = eventType;
        this.employeeId = employeeId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.timestamp = LocalDateTime.now();
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "EmployeeEvent{" +
                "eventType=" + eventType +
                ", employeeId=" + employeeId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}