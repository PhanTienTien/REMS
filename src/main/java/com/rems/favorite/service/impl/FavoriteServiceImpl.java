package com.rems.favorite.service.impl;

import com.rems.common.transaction.TransactionManager;
import com.rems.favorite.dao.FavoriteDAO;
import com.rems.favorite.dao.impl.FavoriteDAOImpl;
import com.rems.favorite.service.FavoriteService;
import com.rems.property.dto.PropertyCardDTO;

import java.util.List;

public class FavoriteServiceImpl implements FavoriteService {

    private final TransactionManager txManager = new TransactionManager();
    private final FavoriteDAO favoriteDAO = new FavoriteDAOImpl();

    @Override
    public void addFavorite(Long customerId,
                            Long propertyId) {

        txManager.execute(conn -> {

            boolean exists =
                    favoriteDAO.exists(conn,
                            customerId,
                            propertyId);

            if (!exists) {

                favoriteDAO.insert(conn,
                        customerId,
                        propertyId);
            }

            return null;
        });
    }

    @Override
    public void removeFavorite(Long customerId, Long propertyId) {

        txManager.executeWithoutResult(conn -> {

            boolean exists = favoriteDAO.exists(conn, customerId, propertyId);

            if (exists) {
                favoriteDAO.delete(conn, customerId, propertyId);
            }
        });
    }

    @Override
    public List<PropertyCardDTO> getFavorites(Long customerId) {

        return txManager.execute(conn ->
                favoriteDAO.findByCustomer(conn, customerId)
        );
    }

    @Override
    public boolean isFavorite(Long customerId, Long propertyId) {

        return txManager.execute(conn ->
                favoriteDAO.exists(conn, customerId, propertyId)
        );
    }
}
