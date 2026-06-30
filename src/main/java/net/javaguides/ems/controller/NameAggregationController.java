package net.javaguides.ems.controller;

import lombok.RequiredArgsConstructor;
import net.javaguides.ems.exception.InvalidRequestException;
import net.javaguides.ems.service.NameAggregationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class NameAggregationController {

    private final NameAggregationService nameAggregationService;

    @PostMapping("/name/aggregation")
    public ResponseEntity<?> aggregate(@RequestBody Map<String, List<String>> request) {
        List<String> names = request.get("name");
        if (names == null || names.isEmpty()) {
            throw new InvalidRequestException("Request body must contain a non-empty 'name' field");
        }
        List<String> result = nameAggregationService.aggregate(names);
        return ResponseEntity.ok(Map.of("name", result));
    }
}