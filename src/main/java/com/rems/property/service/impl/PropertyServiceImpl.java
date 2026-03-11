package com.rems.property.service.impl;

import com.rems.common.constant.PropertyStatus;
import com.rems.common.constant.PropertyType;
import com.rems.common.transaction.TransactionManager;
import com.rems.property.dao.PropertyDAO;
import com.rems.property.dao.impl.PropertyDAOImpl;
import com.rems.property.model.Property;
import com.rems.property.model.dto.CreatePropertyDTO;
import com.rems.property.model.dto.UpdatePropertyDTO;
import com.rems.property.service.PropertyService;

import java.math.BigDecimal;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class PropertyServiceImpl implements PropertyService {

    private final PropertyDAO propertyDAO = new PropertyDAOImpl();
    private final TransactionManager txManager = new TransactionManager();

    @Override
    public Long createProperty(CreatePropertyDTO dto, Long staffId) {

        return txManager.execute(conn -> {

            Property property = new Property();

            property.setTitle(dto.getTitle());
            property.setAddress(dto.getAddress());
            property.setDescription(dto.getDescription());
            property.setPrice(dto.getPrice());
            property.setType(dto.getType());

            property.setStatus(PropertyStatus.DRAFT);
            property.setCreatedBy(staffId);

            return propertyDAO.insert(conn, property);
        });
    }

    @Override
    public void updateProperty(UpdatePropertyDTO dto) {

        txManager.executeWithoutResult(conn -> {

            Property property = propertyDAO
                    .findByIdForUpdate(conn, dto.getId())
                    .orElseThrow(() -> new RuntimeException("Property not found"));

            if (property.getStatus() == PropertyStatus.SOLD ||
                    property.getStatus() == PropertyStatus.RENTED) {

                throw new RuntimeException("Cannot update completed property");
            }

            property.setTitle(dto.getTitle());
            property.setAddress(dto.getAddress());
            property.setDescription(dto.getDescription());
            property.setPrice(dto.getPrice());
            property.setType(dto.getType());

            propertyDAO.update(conn, property);
        });
    }

    @Override
    public Property getPropertyById(Long id) {

        return propertyDAO
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Property not found"));
    }

    @Override
    public List<Property> getAllProperties() {

        return propertyDAO.findAll();
    }

    @Override
    public List<Property> getPropertiesByStatus(PropertyStatus status) {

        return propertyDAO.findAll()
                .stream()
                .filter(p -> p.getStatus() == status)
                .collect(Collectors.toList());
    }

    @Override
    public List<Property> search(String address,
                                 PropertyType type,
                                 BigDecimal minPrice,
                                 BigDecimal maxPrice) {

        return propertyDAO.search(address, type, minPrice, maxPrice);
    }

    @Override
    public void approveProperty(Long propertyId, Long staffId) {

        txManager.executeWithoutResult(conn -> {

            Property property = propertyDAO
                    .findByIdForUpdate(conn, propertyId)
                    .orElseThrow(() -> new RuntimeException("Property not found"));

            if (property.getStatus() != PropertyStatus.DRAFT) {
                throw new RuntimeException("Property is not in DRAFT state");
            }

            propertyDAO.updateStatus(conn, propertyId, PropertyStatus.AVAILABLE);

            propertyDAO.updateApproval(conn, propertyId, LocalDateTime.now());
        });
    }

    @Override
    public void reserveProperty(Long propertyId, Connection conn) {

        Property property = propertyDAO
                .findByIdForUpdate(conn, propertyId)
                .orElseThrow(() -> new RuntimeException("Property not found"));

        if (property.getStatus() != PropertyStatus.AVAILABLE) {
            throw new RuntimeException("Property not available");
        }

        propertyDAO.updateStatus(conn, propertyId, PropertyStatus.RESERVED);
    }

    @Override
    public void completeSale(Long propertyId, Connection conn) {

        propertyDAO.updateStatus(conn, propertyId, PropertyStatus.SOLD);
    }

    @Override
    public void completeRent(Long propertyId, Connection conn) {

        propertyDAO.updateStatus(conn, propertyId, PropertyStatus.RENTED);
    }

    @Override
    public void failTransaction(Long propertyId, Connection conn) {

        propertyDAO.updateStatus(conn, propertyId, PropertyStatus.AVAILABLE);
    }

    @Override
    public void deactivateProperty(Long propertyId, Long staffId) {

        txManager.executeWithoutResult(conn ->
                propertyDAO.updateStatus(conn, propertyId, PropertyStatus.INACTIVE)
        );
    }

    @Override
    public void restoreProperty(Long propertyId, Long staffId) {

        txManager.executeWithoutResult(conn ->
                propertyDAO.updateStatus(conn, propertyId, PropertyStatus.AVAILABLE)
        );
    }

    @Override
    public void deleteProperty(Long propertyId) {

        txManager.executeWithoutResult(conn ->
                propertyDAO.updateStatus(conn, propertyId, PropertyStatus.INACTIVE)
        );
    }

}