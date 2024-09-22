package com.book.model;

import com.book.policy.QueryType;
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
    private long id;

    @NotEmpty
    private String loginId;

    @NotEmpty
    private String password;

    @Column(name = "member_type")
    private String memberType;
    @Column(name="session_id")
    private String sessionId;

    @Column(length = 20, name = "query_type")
    private String queryType;

    @Column(length = 20, name = "filter_type")
    private String fiterType;

    @JsonIgnore
    @OneToMany(mappedBy = "member")
    private List<History> histories = new ArrayList<>();
}
