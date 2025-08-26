package com.book.common.utils;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpSession;

public class SessionUtils {
    private static final String MEMBER_ID = "memberId";

    private static HttpSession getSession() {
        ServletRequestAttributes attr =
                (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        return attr.getRequest().getSession(true); // true: 없으면 새로 생성
    }

    public static void setMemberId(long memberId) {
        getSession().setAttribute(MEMBER_ID, memberId);
    }

    public static long getMemberId() {
        return (Long) getSession().getAttribute(MEMBER_ID);
    }

    public static void removeMemberId() {
        getSession().removeAttribute(MEMBER_ID);
    }
}
