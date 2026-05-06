package com.rems.common.filter;

import com.rems.common.util.CsrfUtil;
import com.rems.common.util.SecurityUtil;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Set;

@WebFilter("/*")
public class CsrfFilter implements Filter {

    private static final Set<String> SAFE_METHODS = Set.of("GET", "HEAD", "OPTIONS", "TRACE");
    private static final Set<String> EXCLUDED_PATHS = Set.of(
            "/auth",
            "/logout",
            "/uploads",
            "/assets",
            "/css",
            "/js",
            "/images"
    );

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        String method = req.getMethod();
        String path = req.getRequestURI().substring(req.getContextPath().length());

        if (SAFE_METHODS.contains(method) || isExcludedPath(path)) {
            chain.doFilter(request, response);
            return;
        }

        HttpSession session = req.getSession(false);

        // Skip CSRF validation for unauthenticated requests
        if (!SecurityUtil.isLoggedIn(req)) {
            chain.doFilter(request, response);
            return;
        }

        // Ensure CSRF token exists for logged-in users
        if (CsrfUtil.getToken(session) == null) {
            CsrfUtil.generateToken(session);
        }

        // Validate CSRF token
        if (!CsrfUtil.validateToken(req)) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid or missing CSRF token");
            return;
        }

        chain.doFilter(request, response);
    }

    private boolean isExcludedPath(String path) {
        return EXCLUDED_PATHS.stream().anyMatch(path::startsWith);
    }
}
