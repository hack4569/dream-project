package com.book;

import com.book.book.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BookApplicationTests {

	@Autowired
	CategoryRepository categoryRepository;

	@Test
	void contextLoads() {


	}

}
