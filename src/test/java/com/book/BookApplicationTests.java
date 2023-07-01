package com.book;

import com.book.aladin.domain.AladinBook;
import com.book.book.BookFilterDto;
import com.book.book.CategoryRepository;
import com.book.model.History;
import com.book.recommend.HistoryRepository;
import com.book.recommend.RecommendService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class BookApplicationTests {

	@Autowired
	CategoryRepository categoryRepository;

	@Autowired
	HistoryRepository historyRepository;

	@Autowired
	RecommendService recommendService;

	@Test
	void contextLoads() {
		String loginId = "admin";
		List<History> histories = historyRepository.findHistoryByLoginId(loginId);

		BookFilterDto bookFilterDto = new BookFilterDto();
		List<AladinBook> bestBooks = recommendService.bestSellerList(bookFilterDto);
		long itemId = 307208992;
		for(History history : histories){
			bestBooks = bestBooks.stream().filter(bB->bB.getItemId() != history.getItemId()).collect(Collectors.toList());
		}
	}

}
