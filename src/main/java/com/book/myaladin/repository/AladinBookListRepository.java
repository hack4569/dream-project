package com.book.myaladin.repository;

import com.book.model.AladinBookList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;
import java.util.Optional;

public interface AladinBookListRepository extends JpaRepository<AladinBookList, Integer>, QuerydslPredicateExecutor<AladinBookList> {
    List<AladinBookList> findAllByItemIdIn(List<Integer> itemIds);
}
