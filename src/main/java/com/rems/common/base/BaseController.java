package com.rems.common.base;

import jakarta.servlet.http.HttpServlet;

public abstract class BaseController extends HttpServlet {

    protected void redirect(jakarta.servlet.http.HttpServletResponse response,
                            String path) throws java.io.IOException {
        response.sendRedirect(path);
    }
}
