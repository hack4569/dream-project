package com.book.recommend.service;

import com.book.aladin.domain.AladinBook;
import com.book.recommend.dto.BookFilterDto;

import java.util.List;

public interface FilterService {
    List<AladinBook> filter(List<AladinBook> aladinBooks, BookFilterDto bookFilterDto);
}
