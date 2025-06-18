package com.book.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "book_like_count")
@Getter
@Entity
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class BookLikeCount {
    @Id
    private long itemId;
    private long likeCount;
    public void increasse() {
        this.likeCount++;
    }
    public void decreasse() {
        this.likeCount--;
    }
}
