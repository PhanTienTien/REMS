package com.rems.common.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter("/*")
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String uri = req.getRequestURI();

        boolean isPublic = uri.contains("/auth")
                || uri.contains("/assets")
                || uri.contains("/css")
                || uri.contains("/js");

        HttpSession session = req.getSession(false);

        boolean loggedIn = session != null
                && session.getAttribute("currentUser") != null;

        if (isPublic || loggedIn) {
            chain.doFilter(request, response);
        } else {
            res.sendRedirect(req.getContextPath() + "/auth");
        }
    }
}