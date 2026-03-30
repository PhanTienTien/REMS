package com.rems.favorite.controller;

import com.rems.favorite.service.FavoriteService;
import com.rems.common.util.Factory;
import com.rems.user.model.User;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/customer/favorites/add")
public class CustomerAddFavoriteController extends HttpServlet {

    private final FavoriteService favoriteService = Factory.getFavoriteService();

    @Override
    protected void doPost(HttpServletRequest req,
                          HttpServletResponse resp)
            throws IOException {

        User user = (User) req.getSession().getAttribute("currentUser");

        Long propertyId =
                Long.parseLong(req.getParameter("propertyId"));

        favoriteService.addFavorite(
                user.getId(),
                propertyId
        );

        resp.sendRedirect(
                req.getHeader("referer")
        );
    }
}
