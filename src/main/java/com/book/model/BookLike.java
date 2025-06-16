package com.book.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;


@Table(name = "book_like")
@Getter
@Entity
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class BookLike {
    @Id
    @Column(name = "book_like_id")
    private Long bookLikeId;
    @Column(name = "item_id")
    private Long itemId;
    @Column(name = "login_id")
    private String loginId;
    private LocalDateTime created;
}
