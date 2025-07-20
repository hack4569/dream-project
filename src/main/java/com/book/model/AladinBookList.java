package com.book.model;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;


@Table(name = "aladin_book_list")
@Getter
@Entity
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class AladinBookList {
    @Id
    @Column(name = "item_id")
    private int itemId;
    @Column(name = "pub_date")
    private String pubDate;
    @Column(name = "category_id")
    private int categoryId;
    private String isbn13;
    @Column(name = "query_type")
    private String queryType;

    public static AladinBookList create(int itemId, String pubDate, int categoryId, String isbn13, String queryType) {
        return AladinBookList.builder().itemId(itemId).pubDate(pubDate).categoryId(categoryId).isbn13(isbn13).queryType(queryType).build();
    }
}
