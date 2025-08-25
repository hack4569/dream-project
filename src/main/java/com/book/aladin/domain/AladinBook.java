package com.book.aladin.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.junit.Ignore;

import javax.persistence.*;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="aladin_book")
public class AladinBook {

    @Id
    private int itemId;
    private String title;
    private String link;
    private String author;
    private String pubDate;
    private String description;
    private String isbn;
    private String isbn13;
    private int priceSales;
    private int priceStandard;
    private String mallType;
    private String stockStatus;
    private int mileage;
    private String cover;
    private int categoryId;
    private String categoryName;
    private String publisher;
    private int salesPoint;
    private Boolean adult;
    private Boolean fixedPrice;
    private int customerReviewRank;
    private int bestRank;

    @Transient
    private SubInfo subInfo;
    //프리미엄
    private String fullDescription;
    private String fullDescription2;
    @OneToMany(mappedBy = "aladinBook", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    public List<Review> reviewList;
    private String toc;
    @OneToMany(mappedBy = "aladinBook", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<BookComment> bookCommentList;

    public void setBookCommentList(List<BookComment> bookCommentList) {
        bookCommentList.forEach(i -> i.setAladinItemId(itemId));
        this.bookCommentList = bookCommentList;
    }
}
