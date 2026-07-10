package net.javaguides.ems.NameAggregation.controller;

import net.javaguides.ems.NameAggregation.service.NameAggregationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class NameAggregationController {

    private final NameAggregationService nameAggregationService;

    @PostMapping("v1/name/aggregation")
    public ResponseEntity<?> aggregate(@RequestBody Map<String, List<String>> request) {
        List<String> names = request.get("name");
        List<String> result = nameAggregationService.aggregate(names);
        return ResponseEntity.ok(Map.of("name", result));
    }
}