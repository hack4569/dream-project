package com.book.interceptor;

import com.book.session.SessionConst;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginCheckInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();

        if (session != null && session.getAttribute(SessionConst.LOGIN_MEMBER) != null) {
             response.sendRedirect("/");
             return false;
        }
        return true;
    }
}
