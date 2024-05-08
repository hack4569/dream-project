package com.book.category.service;

import com.book.category.repository.CategoryRepository;
import com.book.model.Category;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CategoryService {

    @Value("${aladin.accept-category-depth1}")
    String aladinAcceptCategory;

    private final CategoryRepository categoryRepository;

    @Transactional
    public HashSet<Integer> findCategories() {
        HashSet<Integer> cids = new HashSet<>();
        List<String> depth1s = Arrays.stream(aladinAcceptCategory.split(",")).collect(Collectors.toList());
        List<Category> categories = categoryRepository.findCategoriesByDepth1In(depth1s);
        List<Integer> cidList = categories.stream().map(category -> category.getCid()).collect(Collectors.toList());
        for (Integer cid : cidList) {
            cids.add(cid);
        }
        return cids;
    }
}
