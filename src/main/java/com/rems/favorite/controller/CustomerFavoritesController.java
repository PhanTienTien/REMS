package com.rems.favorite.controller;

import com.rems.common.constant.Role;
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

        if (user == null || user.getRole() != Role.CUSTOMER) {

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

    @Override
    protected void doPost(HttpServletRequest req,
                          HttpServletResponse resp)
            throws ServletException, IOException {

        User user = (User) req.getSession().getAttribute("currentUser");

        if (user == null || user.getRole() != Role.CUSTOMER) {
            resp.sendRedirect(req.getContextPath() + "/auth");
            return;
        }

        String propertyIdStr = req.getParameter("propertyId");
        String action = req.getParameter("action");

        if (propertyIdStr != null && action != null) {
            Long propertyId = Long.parseLong(propertyIdStr);
            Long customerId = user.getId();

            if ("add".equals(action)) {
                // Check if already favorited
                boolean isAlreadyFavorited = favoriteService.isFavorite(customerId, propertyId);
                
                if (isAlreadyFavorited) {
                    req.getSession().setAttribute("message", "Property saved");
                } else {
                    favoriteService.addFavorite(customerId, propertyId);
                    req.getSession().setAttribute("message", "Property added to favorites");
                }
            } else if ("remove".equals(action)) {
                favoriteService.removeFavorite(customerId, propertyId);
                req.getSession().setAttribute("message", "Property removed from favorites");
            }
        }

        // Redirect back to referring page or favorites list
        String referer = req.getHeader("referer");
        if (referer != null && !referer.isEmpty()) {
            resp.sendRedirect(referer);
        } else {
            resp.sendRedirect(req.getContextPath() + "/customer/profile/favorites");
        }
    }
}
