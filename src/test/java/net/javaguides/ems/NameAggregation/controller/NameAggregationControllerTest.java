package net.javaguides.ems.NameAggregation.controller;


import net.javaguides.ems.NameAggregation.service.NameAggregationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NameAggregationControllerTest {

    @Mock
    private NameAggregationService nameAggregationService;

    @InjectMocks
    private NameAggregationController nameAggregationController;

    @Test
    @DisplayName("aggregate - should return OK with aggregated names")
    void aggregate_Success() {
        List<String> inputNames = Arrays.asList("Jessica", "Jocelyn");
        List<String> resultNames = Arrays.asList("Jessica", "Jocelyn", "Simon");
        Map<String, List<String>> request = Map.of("name", inputNames);

        when(nameAggregationService.aggregate(inputNames)).thenReturn(resultNames);

        ResponseEntity<?> response = nameAggregationController.aggregate(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        Map<String, List<String>> body = (Map<String, List<String>>) response.getBody();
        assertEquals(3, body.get("name").size());
        assertEquals("Simon", body.get("name").get(2));
        verify(nameAggregationService, times(1)).aggregate(inputNames);
    }

    @Test
    @DisplayName("aggregate - should handle empty name list")
    void aggregate_EmptyList() {
        List<String> inputNames = List.of();
        List<String> resultNames = List.of("Simon");
        Map<String, List<String>> request = Map.of("name", inputNames);

        when(nameAggregationService.aggregate(inputNames)).thenReturn(resultNames);

        ResponseEntity<?> response = nameAggregationController.aggregate(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, List<String>> body = (Map<String, List<String>>) response.getBody();
        assertEquals(1, body.get("name").size());
        verify(nameAggregationService, times(1)).aggregate(inputNames);
    }
}
