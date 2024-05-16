package com.book.member;

import com.book.model.Member;
import com.book.policy.QueryType;
import com.book.user.repository.MemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class MemberTest {

    @Autowired
    private MemberRepository memberRepository;

    /**
     * queryType 저장 테스트
     */
    @Test
    public void queryTypeSaveTest() {
        Member member = new Member();
        member = memberRepository.findMemberByLoginId("admin").orElse(null);
        member.setQueryType(QueryType.BESTSELLER);
        memberRepository.save(member);
        QueryType queryType = member.getQueryType();
        Assertions.assertEquals(QueryType.BESTSELLER, queryType);
    }
}
