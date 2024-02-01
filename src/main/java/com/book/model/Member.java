package com.book.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="MEMBER")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="member_id")
    private Long id;

    @NotEmpty
    private String loginId;
    @NotEmpty
    private String password;
    private String sessionId;

    @JsonIgnore
    @OneToMany(mappedBy = "member")
    private List<History> histories = new ArrayList<>();
}
