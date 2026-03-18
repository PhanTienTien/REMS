package com.rems.favorite.service;

import com.rems.property.dto.PropertyCardDTO;

import java.util.List;

public interface FavoriteService {

    void addFavorite(Long customerId, Long propertyId);

    void removeFavorite(Long customerId, Long propertyId);

    List<PropertyCardDTO> getFavorites(Long customerId);

    boolean isFavorite(Long customerId, Long propertyId);
}
