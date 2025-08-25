package com.book.recommend.service;

import com.book.aladin.domain.AladinBook;
import com.book.common.utils.LocalDateUtils;
import com.book.model.History;
import com.book.recommend.dto.BookFilterDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Slf4j
public class DefaultFilter implements FilterService {
    @Override
    public List<AladinBook> filter(List<AladinBook> aladinBooks, BookFilterDto bookFilterDto) {
        // 모든 Predicate를 조합하여 필터링
        aladinBooks = (List<AladinBook>) aladinBooks.stream()
            .filter(categoryFilter(bookFilterDto.getFinalCids()))
            .filter(publicationDateFilter(bookFilterDto.getAnchorDate()))
            .filter(historyFilter(bookFilterDto.getHistories()))
            .collect(Collectors.toList());

        log.info("필터링 후 책 수: {}", aladinBooks.size());
        return aladinBooks;
    }

    public List<AladinBook> filterForShow(List<AladinBook> aladinBooks, BookFilterDto bookFilterDto) {
        // 모든 Predicate를 조합하여 필터링
        aladinBooks = (List<AladinBook>) aladinBooks.stream()
                .filter(historyFilter(bookFilterDto.getHistories()))
                .collect(Collectors.toList());

        log.info("필터링 후 책 수: {}", aladinBooks.size());
        return aladinBooks;
    }

    private Predicate categoryFilter(Set finalCids) {
        if (finalCids == null) return book -> true;
        // 허용된 카테고리만 필터링하는 Predicate
        Predicate<AladinBook> categoryFilter = book ->
                finalCids.contains(book.getCategoryId());
        return categoryFilter;
    }

    private Predicate publicationDateFilter(String publicationDate) {
        if (!StringUtils.hasText(publicationDate)) return book -> false;
        // 1년 이내 출간된 책을 필터링하는 Predicate
        Predicate<AladinBook> publicationDateFilter = book ->
                Integer.parseInt(publicationDate) >
                        Integer.parseInt(LocalDateUtils.getCustomDate(book.getPubDate()));
        return publicationDateFilter;
    }

    private Predicate historyFilter(List<History> histories) {
        // 히스토리에 없는 책을 필터링하는 Predicate
        if (ObjectUtils.isEmpty(histories)) return book -> true;

        // 히스토리에 없는 책을 필터링하는 Predicate
        Predicate<AladinBook> historyFilter = book -> {

            return histories.stream().noneMatch(history ->
                    book.getItemId() == history.getItemId() &&
                            LocalDate.now().isEqual(history.getCreated().toLocalDate())
            );
        };
        return historyFilter;
    }
}
