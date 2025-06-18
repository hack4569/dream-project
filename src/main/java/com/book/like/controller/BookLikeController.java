package com.book.like.controller;

import com.book.like.service.BookLikeService;
import com.book.like.service.response.BookLikeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class BookLikeController {
    private final BookLikeService bookLikeService;

    @GetMapping("/v1/book-likes/books/{itemId}/users/{loginId}")
    public BookLikeResponse read(
            @PathVariable("itemId") Long itemId,
            @PathVariable("loginId") String loginId
    ) {
        return bookLikeService.read(itemId, loginId);
    }

    @GetMapping("/v1/book-likes/books/{itemId}/count")
    public Long count(
            @PathVariable("itemId") Long itemId
    ) {
        return bookLikeService.count(itemId);
    }

    @PostMapping("/v1/books-likes/books/{itemId}/users/{loginId}")
    public void likeBook(
            @PathVariable("itemId") Long itemId,
            @PathVariable("loginId") String loginId
    ) {
        bookLikeService.likeBook(itemId, loginId);
    }

    @DeleteMapping("/v1/books-likes/books/{itemId}/users/{loginId}")
    public void unlikeBook(
            @PathVariable("itemId") Long itemId,
            @PathVariable("loginId") String loginId
    ) {
        bookLikeService.unlikeBook(itemId, loginId);
    }
}
