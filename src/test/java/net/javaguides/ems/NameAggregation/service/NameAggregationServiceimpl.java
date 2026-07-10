package net.javaguides.ems.NameAggregation.service;


import com.sun.net.httpserver.HttpServer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    @Test
    @DisplayName("aggregate - should call downstream service and return names from response")
    void aggregate_Success() throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(0), 0);
        int port = server.getAddress().getPort();

        server.createContext("/v1/name/aggregation", exchange -> {
            String responseBody = "{\"name\":[\"Jessica\",\"Jocelyn\",\"Simon\"]}";
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, responseBody.getBytes().length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(responseBody.getBytes());
            }
        });
        server.start();

        try {
            ReflectionTestUtils.setField(nameAggregationService, "myName", "Simon");
            ReflectionTestUtils.setField(nameAggregationService, "downstreamUrl",
                    "http://localhost:" + port + "/v1/name/aggregation");

            List<String> incomingNames = Arrays.asList("Jessica", "Jocelyn");

            List<String> result = nameAggregationService.aggregate(incomingNames);

            assertNotNull(result);
            assertEquals(3, result.size());
            assertTrue(result.contains("Simon"));
        } finally {
            server.stop(0);
        }
    }
}