package com.book.session;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SessionManager {

    private Map<String, Object> sessionStore = new HashMap<>();
    public static final String SESSOIN_COOKIE_NAME = "mySessionId";

    public void createSession(Object value, HttpServletResponse response){
        //세션 id를 생성하고, 값을 세션에 저장
        String sessionId = UUID.randomUUID().toString();
        sessionStore.put(sessionId, value);

        //쿠키 생성
        Cookie mySessionCookie = new Cookie(SESSOIN_COOKIE_NAME, sessionId);
        response.addCookie(mySessionCookie);
    }

    public void expire(HttpServletRequest request){
        Cookie sessionCookie = findCookie(request, SESSOIN_COOKIE_NAME);
        if(sessionCookie != null){
            sessionStore.remove(sessionCookie.getValue());
        }
    }

    public Object getSession(HttpServletRequest request){
        Cookie sessionCookie = findCookie(request, SESSOIN_COOKIE_NAME);
        if(sessionCookie ==null){
            return null;
        }
        return sessionStore.get(sessionCookie.getValue());
//        Cookie[] cookies = request.getCookies();
//        if(cookies == null){
//            return null;
//        }
//        for(Cookie cookie : cookies){
//            if(cookie.getName().equals(SESSOIN_COOKIE_NAME)){
//                return sessionStore.get(cookie.getValue());
//            }
//        }
        //return null;
    }

    public Cookie findCookie(HttpServletRequest request, String cookieName){
        if(request.getCookies() == null){
            return null;
        }
        return Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals(cookieName))
                .findAny()
                .orElse(null);
    }


}
