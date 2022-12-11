package com.book.user.login;

import com.book.model.Member;
import com.book.user.login.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoginService {
    private final MemberRepository memberRepository;

    public Member login(String loginId, String password){
        return memberRepository.findMemberByLoginId(loginId).filter(m->m.getPassword().equals(password)).orElse(null);
    }

    public Optional<Member> findByLoginId(Long loginId) {
        return memberRepository.findById(loginId);
    }
}
