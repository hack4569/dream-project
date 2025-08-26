package com.book.history.repository;

import com.book.model.History;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface HistoryRepository extends JpaRepository<History, Long>, QuerydslPredicateExecutor<History> {
    List<History> findHistoryByMemberId(long memberId);
    List<History> findHistoriesByMemberIdAndItemId(long memberId, long itemId);
    void deleteHistoriesByMemberId(long memberId);
}
