package com.rems.common.filter;

import com.rems.user.model.User;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

public class RoleFilter implements Filter {

    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpSession session = req.getSession(false);

        User user = (User) session.getAttribute("currentUser");

        String uri = req.getRequestURI();

        if (uri.contains("/admin") && !user.getRole().equals("ADMIN")) {
            ((HttpServletResponse) response).sendError(403);
            return;
        }

        if (uri.contains("/staff") && !user.getRole().equals("STAFF")) {
            ((HttpServletResponse) response).sendError(403);
            return;
        }

        chain.doFilter(request, response);
    }
}