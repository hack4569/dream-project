package com.book.recommend.factory;

import com.book.recommend.service.DefaultFilter;
import com.book.recommend.service.FilterService;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class FilterFactory {
    final static Map<String, Supplier<FilterService>> map = new HashMap<>();
    static {
        map.put("default", DefaultFilter::new);
    }
    public static FilterService createFilter(String filterName) {
        Supplier<FilterService> filterService = map.get(filterName);
        if (filterService == null) {
            throw new IllegalArgumentException("No such filter: " + filterName);
        }
        return filterService.get();
    }
}
