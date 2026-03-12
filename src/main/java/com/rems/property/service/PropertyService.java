package com.rems.property.service;

import com.rems.common.constant.PropertyStatus;
import com.rems.common.constant.PropertyType;
import com.rems.property.dto.CreatePropertyDTO;
import com.rems.property.dto.PropertyCardDTO;
import com.rems.property.dto.PropertySearchDTO;
import com.rems.property.dto.UpdatePropertyDTO;
import com.rems.property.model.Property;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.List;

public interface PropertyService {

    Long createProperty(CreatePropertyDTO dto,
                        Long staffId,
                        List<String> imageUrls);

    void updateProperty(UpdatePropertyDTO dto);

    Property getPropertyById(Long id);

    List<Property> getAllProperties();

    List<Property> getPropertiesByStatus(PropertyStatus status);

    List<Property> search(String address,
                          PropertyType type,
                          BigDecimal minPrice,
                          BigDecimal maxPrice);

    void approveProperty(Long propertyId, Long staffId);

    void reserveProperty(Long propertyId, Connection conn);

    void completeSale(Long propertyId, Connection conn);

    void completeRent(Long propertyId, Connection conn);

    void failTransaction(Long propertyId, Connection conn);

    void deactivateProperty(Long propertyId, Long staffId);

    void restoreProperty(Long propertyId, Long staffId);

    void deleteProperty(Long propertyId);

    List<Property> searchApproved(String address, String type);

    List<Property> searchCustomer(
            String address,
            String type,
            Integer minPrice,
            Integer maxPrice,
            String sort,
            int page,
            int size
    );

    int countCustomer(
            String address,
            String type,
            Integer minPrice,
            Integer maxPrice
    );

    List<Property> searchAvailable(PropertySearchDTO dto);

    List<Property> findSimilar(Property property);

    List<PropertyCardDTO> searchAvailableCard(PropertySearchDTO dto);

}