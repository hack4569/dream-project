package com.book.like.service;

import com.book.common.snowflake.Snowflake;
import com.book.like.repository.BookLikeCountRepository;
import com.book.like.repository.BookLikeRepository;
import com.book.like.service.response.BookLikeResponse;
import com.book.model.BookLike;
import com.book.model.BookLikeCount;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BookLikeService {
    private final BookLikeRepository bookLikeRepository;
    private final BookLikeCountRepository bookLikeCountRepository;
    private final Snowflake snowflake;

    public BookLikeResponse read(Long bookId, String loginId) {
        return bookLikeRepository.findByItemIdAndLoginId(bookId, loginId)
                .map(BookLikeResponse::create)
                .orElse(null);
    }

    @Transactional
    public void likeBook(Long bookId, String loginId) {

        bookLikeRepository.save(
                BookLike.builder().itemId(bookId).loginId(loginId).bookLikeId(snowflake.generateId()).build()
        );

        int result = bookLikeCountRepository.increase(bookId);
        if (result == 0) {
            bookLikeCountRepository.save(
                    BookLikeCount.builder().itemId(bookId).likeCount(0L).build()
            );
        }
    }

    public void unlikeBook(Long bookId, String loginId) {
        bookLikeRepository.findByItemIdAndLoginId(bookId, loginId)
                .ifPresent(bookLike -> {
                    bookLikeRepository.delete(bookLike);
                    bookLikeCountRepository.decrease(bookLike.getItemId());
                });
    }

    public Long count(Long bookId) {
        return bookLikeCountRepository.findById(bookId)
                .map(BookLikeCount::getLikeCount)
                .orElse(0L);
    }
}
