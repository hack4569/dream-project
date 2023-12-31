package com.book.interceptor;

import com.book.model.Member;
import com.book.session.SessionConst;
import com.book.user.login.member.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.util.WebUtils;

import javax.inject.Inject;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class RememberMeInterceptor implements HandlerInterceptor {
    @Inject
    private MemberRepository memberRepository;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession httpSession = request.getSession();
        Cookie loginCookie = WebUtils.getCookie(request, SessionConst.LOGIN_MEMBER);
        if (loginCookie != null) {
             String sessionId = loginCookie.getValue();
             Member member = memberRepository.findMemberBySessionId(sessionId).orElse(null);
             if (member != null) {
                httpSession.setAttribute(SessionConst.LOGIN_MEMBER, member);
             }
        }
        return true;
    }
}
