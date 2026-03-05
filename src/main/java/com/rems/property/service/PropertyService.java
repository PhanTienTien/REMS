package com.rems.property.service;

import com.rems.property.model.dto.CreatePropertyDTO;
import com.rems.property.model.dto.UpdatePropertyDTO;
import com.rems.common.constant.Role;

import java.sql.Connection;

public interface PropertyService {

    void approveProperty(Long propertyId, Long staffId);

    void reserveProperty(Long propertyId, Connection conn);

    void completeSale(Long propertyId, Connection conn);

    void completeRent(Long propertyId, Connection conn);

    void failTransaction(Long propertyId, Connection conn);

    void deactivateProperty(Long propertyId, Long staffId);

    void restoreProperty(Long propertyId, Long staffId);
}