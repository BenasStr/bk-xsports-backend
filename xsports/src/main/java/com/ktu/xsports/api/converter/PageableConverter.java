package com.ktu.xsports.api.converter;

import org.springframework.data.domain.Page;

import java.util.HashMap;
import java.util.Map;

public class PageableConverter {
    static public <T> Map<String, Object> convert(int pageNumber, int size, Page<T> page) {
        Map<String, Object> response = new HashMap<>();
        response.put("items", page.getContent());
        response.put("items_per_page", size);
        response.put("page_index", pageNumber);
        response.put("total_items", page.getTotalElements());
        return Map.of("data", response);
    }
}
