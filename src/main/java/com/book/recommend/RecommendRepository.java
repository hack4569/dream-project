package com.book.recommend;

import com.book.model.Recommend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface RecommendRepository extends JpaRepository<Recommend, Long>, QuerydslPredicateExecutor<Recommend> {
}
