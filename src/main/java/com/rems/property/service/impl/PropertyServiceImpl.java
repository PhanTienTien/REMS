package com.rems.property.service.impl;

import com.rems.common.constant.PropertyStatus;
import com.rems.common.constant.PropertyType;
import com.rems.common.transaction.TransactionManager;
import com.rems.property.dao.PropertyDAO;
import com.rems.property.dao.PropertyImageDAO;
import com.rems.property.dao.impl.PropertyDAOImpl;
import com.rems.property.dao.impl.PropertyImageDAOImpl;
import com.rems.property.dto.CreatePropertyDTO;
import com.rems.property.dto.PropertyCardDTO;
import com.rems.property.dto.PropertySearchDTO;
import com.rems.property.dto.UpdatePropertyDTO;
import com.rems.property.model.Property;
import com.rems.property.service.PropertyImageService;
import com.rems.property.service.PropertyService;

import java.math.BigDecimal;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class PropertyServiceImpl implements PropertyService {

    private final PropertyDAO propertyDAO = new PropertyDAOImpl();
    private final TransactionManager txManager = new TransactionManager();
    private final PropertyImageDAO propertyImageDAO = new PropertyImageDAOImpl();
    private final PropertyImageService propertyImageService = new PropertyImageServiceImpl();

    @Override
    public Long createProperty(CreatePropertyDTO dto,
                               Long staffId,
                               List<String> imageUrls) {

        return txManager.execute(conn -> {

            Property property = new Property();

            property.setTitle(dto.getTitle());
            property.setAddress(dto.getAddress());
            property.setDescription(dto.getDescription());
            property.setPrice(dto.getPrice());
            property.setType(dto.getType());

            property.setStatus(PropertyStatus.DRAFT);
            property.setCreatedBy(staffId);

            Long propertyId = propertyDAO.insert(conn, property);
            propertyImageService.addImages(propertyId, imageUrls);

            if (imageUrls != null) {

                for (String url : imageUrls) {

                    propertyImageDAO.insert(conn, propertyId, url);

                }

            }

            return propertyId;
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

    @Override
    public List<Property> searchApproved(String address, String type) {

        return txManager.execute(conn ->
                propertyDAO.searchApproved(conn, address, type)
        );
    }

    @Override
    public List<Property> searchCustomer(
            String address,
            String type,
            Integer minPrice,
            Integer maxPrice,
            String sort,
            int page,
            int size) {

        return txManager.execute(conn ->
                propertyDAO.searchCustomer(
                        conn,
                        address,
                        type,
                        minPrice,
                        maxPrice,
                        sort,
                        page,
                        size
                )
        );
    }

    @Override
    public int countCustomer(
            String address,
            String type,
            Integer minPrice,
            Integer maxPrice) {

        return txManager.execute(conn ->
                propertyDAO.countCustomer(
                        conn,
                        address,
                        type,
                        minPrice,
                        maxPrice
                )
        );
    }

    @Override
    public List<Property> searchAvailable(PropertySearchDTO dto) {

        return txManager.execute(conn ->
                propertyDAO.searchAvailable(conn, dto)
        );

    }

    @Override
    public List<Property> findSimilar(Property property) {

        long minPrice = property.getPrice()
                .multiply(BigDecimal.valueOf(0.8))
                .longValue();

        long maxPrice = property.getPrice()
                .multiply(BigDecimal.valueOf(1.2))
                .longValue();

        return txManager.execute(conn ->
                propertyDAO.findSimilar(
                        conn,
                        String.valueOf(property.getType()),
                        property.getAddress(),
                        minPrice,
                        maxPrice
                )
        );
    }

    @Override
    public List<PropertyCardDTO> searchAvailableCard(PropertySearchDTO dto) {

        return txManager.execute(conn ->
                propertyDAO.searchAvailableCard(conn, dto)
        );
    }

}