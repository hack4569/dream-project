package com.book.like.service.response;

import com.book.model.BookLike;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@ToString
public class BookLikeResponse {
    private Long bookLikeId;
    private Long itemId;
    private String loginId;
    private LocalDateTime createdAt;

    public static BookLikeResponse create(BookLike bookLike) {
        BookLikeResponse response = new BookLikeResponse();
        response.bookLikeId = bookLike.getBookLikeId();
        response.itemId = bookLike.getItemId();
        response.loginId = bookLike.getLoginId();
        response.createdAt = bookLike.getCreated();
        return response;
    }
}
