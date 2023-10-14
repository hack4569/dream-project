package com.book.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name="HISTORY")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class History {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String loginId;
    private long itemId;
    private String createdDate;
}
