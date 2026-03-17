package com.rems.favorite.service;

import com.rems.property.model.Property;

import java.util.List;

public interface FavoriteService {

    void addFavorite(Long customerId, Long propertyId);

    void removeFavorite(Long customerId, Long propertyId);

    List<Property> getFavorites(Long customerId);

    boolean isFavorite(Long customerId, Long propertyId);
}
