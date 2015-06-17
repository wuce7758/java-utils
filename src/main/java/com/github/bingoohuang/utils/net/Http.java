package com.github.bingoohuang.utils.net;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class Http {
    public static void respondJSON(HttpServletResponse rsp, String json) {
        if (json == null) return;

        try {
            rsp.setHeader("Content-Type", "application/json; charset=UTF-8");
            rsp.setCharacterEncoding("UTF-8");
            PrintWriter writer = rsp.getWriter();
            writer.write(json);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean isAjax(HttpServletRequest request) {
        return "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
    }

    public static void respondText(HttpServletResponse rsp, String text) {
        if (text == null) return;

        try {
            rsp.setHeader("Content-Type", "text/plain; charset=UTF-8");
            rsp.setCharacterEncoding("UTF-8");
            PrintWriter writer = rsp.getWriter();
            writer.write(text);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void error(HttpServletResponse response, int statusCode, Throwable ex) {
        response.setStatus(statusCode);
        String message = ex.getMessage();
        respondText(response, message != null ? message : ex.toString());
    }


    public static void error(HttpServletResponse response, int statusCode, String message) {
        response.setStatus(statusCode);
        respondText(response, message);
    }
}
