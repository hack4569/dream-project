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
@Table(name = "phrase")
public class Phrase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long phraseId;
    private String pageNo;
    private String phrase;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sub_info_id")
    private SubInfo subInfo;
}
