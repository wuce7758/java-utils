package com.github.bingoohuang.utils.net;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class Http {
    public static void respondJSON(HttpServletResponse rsp, String json) {
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

    public static void respondText(HttpServletResponse rsp, String text) {
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

    public static boolean isAjax(HttpServletRequest request) {
        return "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
    }
}
