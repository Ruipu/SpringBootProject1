package net.javaguides.ems.service;

import java.util.List;

public interface NameAggregationService {
    List<String> aggregate(List<String> incomingNames);
}
