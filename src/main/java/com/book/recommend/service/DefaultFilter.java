package com.book.recommend.service;

import com.book.aladin.domain.AladinBook;
import com.book.common.utils.LocalDateUtils;
import com.book.model.History;
import com.book.recommend.dto.BookFilterDto;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Slf4j
public class DefaultFilter implements FilterService {
    @Override
    public List<AladinBook> filter(List<AladinBook> aladinBooks, BookFilterDto bookFilterDto) {
        // 허용된 카테고리만 필터링하는 Predicate
        Predicate<AladinBook> categoryFilter = book -> 
            bookFilterDto.getFinalCids()
                .orElse(new HashSet<>())
                .contains(book.getCategoryId());

        // 1년 이내 출간된 책을 필터링하는 Predicate
        Predicate<AladinBook> publicationDateFilter = book ->
            Integer.parseInt(bookFilterDto.getAnchorDate().orElse("0")) > 
            Integer.parseInt(LocalDateUtils.getCustomDate(book.getPubDate()));

        // 히스토리에 없는 책을 필터링하는 Predicate
        Predicate<AladinBook> historyFilter = book -> {
            List<History> histories = bookFilterDto.getHistories().orElseGet(ArrayList::new);
            return histories.stream().noneMatch(history ->
                book.getItemId() == history.getItemId() &&
                LocalDate.now().isEqual(history.getCreated().toLocalDate())
            );
        };

        // 모든 Predicate를 조합하여 필터링
        aladinBooks = aladinBooks.stream()
            .filter(categoryFilter)
            .filter(publicationDateFilter)
            .filter(historyFilter)
            .collect(Collectors.toList());

        log.info("필터링 후 책 수: {}", aladinBooks.size());
        return aladinBooks;
    }
}
