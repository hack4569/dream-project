package com.book.recommend.service;

import com.book.aladin.domain.AladinBook;
import com.book.common.utils.LocalDateUtils;
import com.book.model.History;
import com.book.recommend.dto.BookFilterDto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class DefaultFilter implements FilterService{
    @Override
    public List<AladinBook> filter(List<AladinBook> aladinBooks, BookFilterDto bookFilterDto) {
        //필터1 : 허용 카테고리만
        aladinBooks = aladinBooks.stream().filter(i -> bookFilterDto.getFinalCids().orElse(new HashSet<>()).contains(i.getCategoryId())).collect(Collectors.toList());

        //필터2 : 1년도 안된 책 out
        aladinBooks = aladinBooks.stream().filter(i -> Integer.parseInt(bookFilterDto.getAnchorDate().orElse("0") ) > Integer.parseInt(LocalDateUtils.getCustomDate(i.getPubDate()))).collect(Collectors.toList());

        //필터3 : history에 없는 데이터
        for (History history : bookFilterDto.getHistories().orElseGet(() -> new ArrayList<>())) {
            aladinBooks = aladinBooks.stream().filter(bB ->
            {
                return
                        (
                                bB.getItemId() == history.getItemId()
                                        &&
                                        LocalDate.now().isEqual(history.getCreated().toLocalDate())
                        )
                                || bB.getItemId() != history.getItemId();
            }).collect(Collectors.toList());
        }
        return aladinBooks;
    }
}
