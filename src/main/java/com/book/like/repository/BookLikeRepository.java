package com.book.like.repository;

import com.book.model.BookLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookLikeRepository extends JpaRepository<BookLike, Long> {
    Optional<BookLike> findByItemIdAndLoginId(Long itemId, String loginId);
}
