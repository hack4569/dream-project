package com.book.aladin.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SubInfo {
    //책속에서 - 책 속 문장
    private List<Phrase> phraseList;
    //편집장 책추천
    private List<MdRecommend> mdRecommendList;
}
