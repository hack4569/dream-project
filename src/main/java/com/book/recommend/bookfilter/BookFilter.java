package com.book.recommend.bookfilter;

import com.book.aladin.domain.AladinBook;
import com.book.recommend.dto.BookFilterDto;

import java.util.List;

public interface BookFilter {
    void customFilteredList(List<AladinBook> aladinBooks, BookFilterDto bookFilterDto);
}
