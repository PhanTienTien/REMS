package com.rems.common.filter;

import com.rems.common.util.SecurityUtil;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

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
        String contextPath = req.getContextPath();

        if (isPublicResource(uri, contextPath)) {
            chain.doFilter(request, response);
            return;
        }

        if (!SecurityUtil.isLoggedIn(req)) {
            res.sendRedirect(contextPath + "/auth");
            return;
        }

        if (isAdminUri(uri, contextPath)) {
            if (!SecurityUtil.isAdminOrStaff(req)) {
                res.sendError(HttpServletResponse.SC_FORBIDDEN, "Admin or Staff access required");
                return;
            }
        }

        chain.doFilter(request, response);
    }

    private boolean isPublicResource(String uri, String contextPath) {
        String path = uri.substring(contextPath.length());

        return uri.equals(contextPath + "/")
                || path.startsWith("/home")
                || path.startsWith("/customer/about")
                || path.startsWith("/services")
                || path.startsWith("/customer/contact")
                || path.startsWith("/customer/properties")
                || path.startsWith("/property-detail")
                || path.startsWith("/auth")
                || path.startsWith("/logout")
                || path.startsWith("/uploads")
                || path.startsWith("/assets")
                || path.startsWith("/css")
                || path.startsWith("/js")
                || path.startsWith("/images")
                || path.startsWith("/favicon.ico");
    }

    private boolean isAdminUri(String uri, String contextPath) {
        String path = uri.substring(contextPath.length());
        return path.startsWith("/admin/");
    }
}
