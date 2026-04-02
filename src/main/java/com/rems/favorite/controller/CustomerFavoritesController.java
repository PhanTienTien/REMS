package com.rems.favorite.controller;

import com.rems.common.util.Factory;
import com.rems.favorite.service.FavoriteService;
import com.rems.property.dto.PropertyCardDTO;
import com.rems.user.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/customer/profile/favorites")
public class CustomerFavoritesController extends HttpServlet {

    private final FavoriteService favoriteService = Factory.getFavoriteService();

    @Override
    protected void doGet(HttpServletRequest req,
                         HttpServletResponse resp)
            throws ServletException, IOException {

        User user = (User) req.getSession().getAttribute("currentUser");

        if (user == null) {

            resp.sendRedirect(
                    req.getContextPath() + "/auth"
            );

            return;
        }

        Long customerId = user.getId();

        List<PropertyCardDTO> favorites =
                favoriteService.getFavorites(customerId);

        req.setAttribute("favorites", favorites);

        req.getRequestDispatcher(
                "/views/customer/profile/favorites.jsp"
        ).forward(req, resp);
    }
}
