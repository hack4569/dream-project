package com.book.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name="BK_CATEGORY")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int cid;
    @Column
    private String subCid;
    @Column
    private String name;
    @Column
    private String mall;
    @Column
    private String depth1;
}
