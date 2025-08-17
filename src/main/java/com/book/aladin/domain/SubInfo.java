package com.book.aladin.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "sub_info")
public class SubInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long subInfoId;
    //책속에서 - 책 속 문장
    @OneToMany(mappedBy = "subInfo", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Phrase> phraseList;
    //편집장 책추천
    @OneToMany(mappedBy = "subInfo", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MdRecommend> mdRecommendList;
    private String toc;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private AladinBook aladinBook;
}
