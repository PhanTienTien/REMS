package com.rems.common.util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.security.SecureRandom;
import java.util.Base64;

public class CsrfUtil {

    private static final String CSRF_TOKEN_ATTR = "CSRF_TOKEN";
    private static final SecureRandom secureRandom = new SecureRandom();

    private CsrfUtil() {
    }

    public static String generateToken(HttpSession session) {
        byte[] tokenBytes = new byte[32];
        secureRandom.nextBytes(tokenBytes);
        String token = Base64.getUrlEncoder().withoutPadding().encodeToString(tokenBytes);
        session.setAttribute(CSRF_TOKEN_ATTR, token);
        return token;
    }

    public static String getToken(HttpSession session) {
        if (session == null) {
            return null;
        }
        Object token = session.getAttribute(CSRF_TOKEN_ATTR);
        return token != null ? token.toString() : null;
    }

    public static boolean validateToken(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return false;
        }

        String sessionToken = getToken(session);
        if (sessionToken == null) {
            return false;
        }

        String requestToken = request.getParameter("_csrf");
        if (requestToken == null) {
            requestToken = request.getHeader("X-CSRF-Token");
        }

        if (requestToken == null) {
            return false;
        }

        return sessionToken.equals(requestToken);
    }

    public static void removeToken(HttpSession session) {
        if (session != null) {
            session.removeAttribute(CSRF_TOKEN_ATTR);
        }
    }
}
