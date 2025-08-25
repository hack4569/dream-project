package com.book.batch;

import com.book.aladin.service.AladinService;
import com.book.category.service.CategoryService;
import com.book.myaladin.repository.AladinBookRepository;
import com.book.recommend.service.RecommendService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BookBatchSchedulerTest {
    @Autowired
    private AladinService aladinService;
    @Autowired
    private RecommendService recommendService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private AladinBookRepository aladinBookRepository;
    @Autowired
    private BookBatchScheduler bookBatchScheduler;

    @Test
    void scheduleBestSellerBatch() {
        bookBatchScheduler.scheduleBestSellerBatch();
    }
}