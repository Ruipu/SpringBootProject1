package net.javaguides.ems.service;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NotificationServiceTest {

    private final NotificationService notificationService = new NotificationService();

    @Test
    @DisplayName("sendNotification - should complete without exception")
    void sendNotification_Success() {
        assertDoesNotThrow(() -> notificationService.sendNotification());
    }

    @Test
    @DisplayName("sendNotification - should handle interrupt")
    void sendNotification_Interrupted() {
        Thread.currentThread().interrupt();
        assertDoesNotThrow(() -> notificationService.sendNotification());
    }
}
