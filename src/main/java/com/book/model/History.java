package com.book.model;

import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Entity
@Table(name="histories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class History extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "history_id")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name = "item_id")
    private long itemId;

    public void setMember(Member member) {
        this.member = member;
        member.getHistories().add(this);
    }

    public static History createHistory(long bookId, Member member) {
        History history = new History();
        history.setItemId(bookId);
        history.setMember(member);
        return history;
    }
}
