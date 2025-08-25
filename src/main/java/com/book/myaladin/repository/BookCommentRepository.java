package com.book.myaladin.repository;

import com.book.aladin.domain.AladinBook;
import com.book.aladin.domain.BookComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;
import java.util.Optional;

public interface BookCommentRepository extends JpaRepository<BookComment, Long>, QuerydslPredicateExecutor<BookComment> {
    List<BookComment> findBookCommentsByAladinItemId(int aladinItemId);
}
