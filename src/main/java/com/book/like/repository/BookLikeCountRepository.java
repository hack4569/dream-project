package com.book.like.repository;

import com.book.model.BookLikeCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookLikeCountRepository extends JpaRepository<BookLikeCount, Long> {
    @Query(
            value = "update book_like_count set like_count = like_count + 1 where item_id = :itemId",
            nativeQuery = true
    )
    @Modifying
    int increase(@Param("itemId") Long itemId);

    @Query(
            value = "update book_like_count set like_count = like_count - 1 where item_id = :itemId",
            nativeQuery = true
    )
    @Modifying
    int decrease(@Param("itemId") Long itemId);
}
