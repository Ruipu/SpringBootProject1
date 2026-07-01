package net.javaguides.ems.NameAggregation.service.impl;

import net.javaguides.ems.NameAggregation.service.NameAggregationService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class NameAggregationServiceimpl implements NameAggregationService {

    @Value("${node.name}")
    private String myName;

    @Value("${downstream.url}")
    private String downstreamUrl;

    @CircuitBreaker(name = "downstreamService", fallbackMethod = "aggregateFallback")
    @Retry(name = "downstreamService")
    @Override
    public List<String> aggregate(List<String> incomingNames) {
        List<String> updatedNames = new ArrayList<>(incomingNames);
        updatedNames.add(myName);
        log.info("[{} Node Log] After adding my name: {}", myName, updatedNames);

        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(3000);
        factory.setReadTimeout(3000);
        RestTemplate restTemplate = new RestTemplate(factory);
        Map<String, List<String>> requestBody = Map.of("name", updatedNames);
        Map response = restTemplate.postForObject(downstreamUrl, requestBody, Map.class);
        return (List<String>) response.get("name");
    }
    public List<String> aggregateFallback(List<String> incomingNames, Throwable t) {
        log.error("Circuit breaker fallback triggered. Reason: {}", t.getMessage());
        List<String> fallbackList = new ArrayList<>(incomingNames);
        fallbackList.add(myName + " (fallback - downstream unavailable)");
        return fallbackList;
    }
}