package com.book.login;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.UUID;

public class LogFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException{
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String requestUri = httpRequest.getRequestURI();

        String uuid = UUID.randomUUID().toString();
        try{
            //log.info("REQUEST [{]]");
            chain.doFilter(request,response);
        }catch (Exception e){
            throw e;
        }finally {
            //log.info("RESPONSE");
        }

    }
}
