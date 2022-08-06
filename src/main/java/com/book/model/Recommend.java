package com.book.model;

import lombok.*;

import javax.persistence.*;


@Entity
@Table(name="BK_RECOMMEND")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Recommend {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String itemId;

    @Column
    private String bkIsbn;

    @Column
    private String commentBrief;

    @Column
    private String commentDetail;

    @Column
    private String approval;

    @Column
    private String userId;

    @Column
    private String createDate;

    @Column
    private String updateDate;
}
