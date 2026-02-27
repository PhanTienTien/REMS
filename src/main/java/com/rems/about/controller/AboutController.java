package com.rems.about.controller;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/about")
public class AboutController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req,
                         HttpServletResponse resp)
            throws IOException {

        try {

            // Sau này có thể load data từ DB
            req.setAttribute("companyName", "REMS System");
            req.setAttribute("mission",
                    "We provide secure real estate transactions.");

            req.getRequestDispatcher("/views/about.jsp")
                    .forward(req, resp);

        } catch (Exception e) {
            resp.getWriter().println("Error loading about page");
        }
    }
}
