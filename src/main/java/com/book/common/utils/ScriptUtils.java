package com.book.common.utils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class ScriptUtils {
    public static void init(HttpServletResponse response) {
        response.setContentType("text/html; charset=utf-8");
        response.setCharacterEncoding("utf-8");
    }

    public static void alert(HttpServletResponse response, String msg) throws IOException {
        init(response);
        PrintWriter out = response.getWriter();
        out.println("<script>alert('"+msg+"')</script>");
        out.flush();
    }

    public static void rdirect(HttpServletResponse response, String msg, String nextPage) throws IOException {
        init(response);
        response.sendRedirect(nextPage);
    }

    public static void alertAndBack(HttpServletResponse response, String msg) throws IOException {
        init(response);
        PrintWriter out = response.getWriter();
        out.println("<script>alert('"+msg+"'); history.go(-1);</script> ");
        out.flush();
    }
}
