package com.book.aladin.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "book_comment")
public class BookComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookCommentId;

    @Lob
    private String comment;

    private String type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private AladinBook aladinBook;

    private int aladinItemId;

    public static BookComment create(String comment, String type) {
        BookComment bookComment = new BookComment();
        bookComment.setComment(comment);
        bookComment.setType(type);
        return bookComment;
    }
}
