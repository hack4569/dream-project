package com.book.recommend;

import com.book.model.History;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface HistoryRepository extends JpaRepository<History, Long>, QuerydslPredicateExecutor<History> {
    List<History> findHistoryByLoginId(String loginId);
    List<History> findHistoriesByLoginIdAndItemId(String loginId, long itemId);
}
