package com.book.aladin.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    //프리미엄
    private String fullDescription;
    private String fullDescription2;
    @OneToMany(mappedBy = "aladinBook", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    public List<Review> reviewList;
    private String toc;
    @OneToOne(mappedBy = "aladinBook", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private SubInfo subInfo;


}
