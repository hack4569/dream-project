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
@Table(name = "md_recommend")
public class MdRecommend {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mdRecommendId;
    private String title;
    private String comment;
    private String mdName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sub_info_id")
    private SubInfo subInfo;
}


