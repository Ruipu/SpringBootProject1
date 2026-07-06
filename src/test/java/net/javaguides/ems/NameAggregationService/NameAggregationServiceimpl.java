package net.javaguides.ems.NameAggregationService;


import net.javaguides.ems.NameAggregation.service.impl.NameAggregationServiceimpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class NameAggregationServiceimplTest {

    @InjectMocks
    private NameAggregationServiceimpl nameAggregationService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(nameAggregationService, "myName", "Simon");
        ReflectionTestUtils.setField(nameAggregationService, "downstreamUrl", "http://localhost:8080/v1/name/aggregation");
    }

    // ==================== aggregateFallback Tests ====================

    @Test
    @DisplayName("aggregateFallback - should return names with fallback suffix")
    void aggregateFallback_Success() {
        List<String> incomingNames = Arrays.asList("Jessica", "Jocelyn");
        Throwable throwable = new RuntimeException("Connection refused");

        List<String> result = nameAggregationService.aggregateFallback(incomingNames, throwable);

        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals("Jessica", result.get(0));
        assertEquals("Jocelyn", result.get(1));
        assertEquals("Simon (fallback - downstream unavailable)", result.get(2));
    }

    @Test
    @DisplayName("aggregateFallback - should handle empty incoming list")
    void aggregateFallback_EmptyList() {
        List<String> incomingNames = new ArrayList<>();
        Throwable throwable = new RuntimeException("Timeout");

        List<String> result = nameAggregationService.aggregateFallback(incomingNames, throwable);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Simon (fallback - downstream unavailable)", result.get(0));
    }

    @Test
    @DisplayName("aggregateFallback - should preserve all incoming names")
    void aggregateFallback_PreservesAllNames() {
        List<String> incomingNames = Arrays.asList("Jessica", "Jocelyn", "Suzy", "April");
        Throwable throwable = new RuntimeException("Service down");

        List<String> result = nameAggregationService.aggregateFallback(incomingNames, throwable);

        assertEquals(5, result.size());
        assertEquals("Jessica", result.get(0));
        assertEquals("April", result.get(3));
        assertEquals("Simon (fallback - downstream unavailable)", result.get(4));
    }
}