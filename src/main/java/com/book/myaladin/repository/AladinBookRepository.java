package com.book.myaladin.repository;

import com.book.aladin.domain.AladinBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface AladinBookRepository extends JpaRepository<AladinBook, Integer>, QuerydslPredicateExecutor<AladinBook> {
    List<AladinBook> findAllByItemIdIn(List<Integer> itemIds);
    List<AladinBook> findAllByItemIdNotIn(List<Integer> itemIds);
    //List<AladinBookList> findAll();
}
