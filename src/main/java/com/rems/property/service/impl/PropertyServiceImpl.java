package com.rems.property.service.impl;

import com.rems.activitylog.dao.impl.ActivityLogDAOImpl;
import com.rems.activitylog.service.ActivityLogService;
import com.rems.activitylog.service.impl.ActivityLogServiceImpl;
import com.rems.common.constant.ActivityLogAction;
import com.rems.common.constant.PropertyStatus;
import com.rems.common.constant.PropertyType;
import com.rems.common.exception.BusinessException;
import com.rems.common.exception.ErrorCode;
import com.rems.common.transaction.TransactionManager;
import com.rems.common.util.PageResult;
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
import java.util.Optional;

public class PropertyServiceImpl implements PropertyService {

    private final PropertyDAO propertyDAO;
    private final TransactionManager txManager;
    private final PropertyImageDAO propertyImageDAO;
    private final PropertyImageService propertyImageService;
    private final ActivityLogService activityLogService;

    public PropertyServiceImpl() {
        this(
                new PropertyDAOImpl(),
                new TransactionManager(),
                new PropertyImageDAOImpl(),
                new PropertyImageServiceImpl(),
                new ActivityLogServiceImpl(new TransactionManager(), new ActivityLogDAOImpl())
        );
    }

    public PropertyServiceImpl(PropertyDAO propertyDAO,
                               TransactionManager txManager,
                               PropertyImageDAO propertyImageDAO,
                               PropertyImageService propertyImageService,
                               ActivityLogService activityLogService) {
        this.propertyDAO = propertyDAO;
        this.txManager = txManager;
        this.propertyImageDAO = propertyImageDAO;
        this.propertyImageService = propertyImageService;
        this.activityLogService = activityLogService;
    }

    @Override
    public Optional<Property> findById(Long id) {
        return propertyDAO.findById(id);
    }

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
            propertyImageService.addImages(conn, propertyId, imageUrls);
            log(conn, staffId, ActivityLogAction.CREATE_PROPERTY, propertyId);
            return propertyId;
        });
    }

    @Override
    public void updateProperty(UpdatePropertyDTO dto) {

        txManager.executeWithoutResult(conn -> {
            Property property = requireForUpdate(conn, dto.getId());

            if (property.getStatus() != PropertyStatus.DRAFT) {
                throw new BusinessException(ErrorCode.INVALID_STATE_TRANSITION);
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
        return propertyDAO.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.PROPERTY_NOT_FOUND));
    }

    @Override
    public List<Property> getAllProperties() {
        return propertyDAO.findAll();
    }

    @Override
    public List<Property> getPropertiesByStatus(PropertyStatus status) {
        return txManager.execute(conn -> propertyDAO.findByStatus(conn, status));
    }

    @Override
    public List<Property> search(String address,
                                 PropertyType type,
                                 BigDecimal minPrice,
                                 BigDecimal maxPrice) {
        return propertyDAO.search(address, type, minPrice, maxPrice);
    }

    @Override
    public void approveProperty(Long propertyId, Long approverId) {

        txManager.executeWithoutResult(conn -> {
            Property property = requireForUpdate(conn, propertyId);
            requireStatus(property, PropertyStatus.DRAFT);

            propertyDAO.updateStatus(conn, propertyId, PropertyStatus.AVAILABLE);
            propertyDAO.updateApproval(conn, propertyId, approverId, LocalDateTime.now());
            log(conn, approverId, ActivityLogAction.APPROVE_PROPERTY, propertyId);
        });
    }

    @Override
    public void reserveProperty(Long propertyId, Connection conn) {
        Property property = requireForUpdate(conn, propertyId);
        requireStatus(property, PropertyStatus.AVAILABLE);
        propertyDAO.updateStatus(conn, propertyId, PropertyStatus.RESERVED);
    }

    @Override
    public void completeSale(Long propertyId, Connection conn) {
        Property property = requireForUpdate(conn, propertyId);
        requireStatus(property, PropertyStatus.RESERVED);

        if (!property.getType().isSale()) {
            throw new BusinessException(ErrorCode.TYPE_MISMATCH);
        }

        propertyDAO.updateStatus(conn, propertyId, PropertyStatus.SOLD);
    }

    @Override
    public void completeRent(Long propertyId, Connection conn) {
        Property property = requireForUpdate(conn, propertyId);
        requireStatus(property, PropertyStatus.RESERVED);

        if (!property.getType().isRent()) {
            throw new BusinessException(ErrorCode.TYPE_MISMATCH);
        }

        propertyDAO.updateStatus(conn, propertyId, PropertyStatus.RENTED);
    }

    @Override
    public void failTransaction(Long propertyId, Connection conn) {
        Property property = requireForUpdate(conn, propertyId);
        requireStatus(property, PropertyStatus.RESERVED);
        propertyDAO.updateStatus(conn, propertyId, PropertyStatus.AVAILABLE);
    }

    @Override
    public void deactivateProperty(Long propertyId, Long staffId) {

        txManager.executeWithoutResult(conn -> {
            Property property = requireForUpdate(conn, propertyId);

            if (property.getStatus() == PropertyStatus.SOLD
                    || property.getStatus() == PropertyStatus.RENTED) {
                throw new BusinessException(ErrorCode.CANNOT_DEACTIVATE);
            }

            if (property.getStatus() != PropertyStatus.DRAFT
                    && property.getStatus() != PropertyStatus.AVAILABLE
                    && property.getStatus() != PropertyStatus.RESERVED) {
                throw new BusinessException(ErrorCode.INVALID_STATE_TRANSITION);
            }

            propertyDAO.updateStatus(conn, propertyId, PropertyStatus.INACTIVE);
            log(conn, staffId, ActivityLogAction.DEACTIVATE_PROPERTY, propertyId);
        });
    }

    @Override
    public void restoreProperty(Long propertyId, Long staffId) {

        txManager.executeWithoutResult(conn -> {
            Property property = requireForUpdate(conn, propertyId);
            requireStatus(property, PropertyStatus.INACTIVE);

            propertyDAO.updateStatus(conn, propertyId, PropertyStatus.AVAILABLE);
            log(conn, staffId, ActivityLogAction.RESTORE_PROPERTY, propertyId);
        });
    }

    @Override
    public void deleteProperty(Long propertyId, Long staffId) {
        txManager.executeWithoutResult(conn -> {
            requireForUpdate(conn, propertyId);
            propertyDAO.updateStatus(conn, propertyId, PropertyStatus.INACTIVE);
            log(conn, staffId, ActivityLogAction.DELETE_PROPERTY, propertyId);
        });
    }

    @Override
    public List<Property> searchApproved(String address, String type) {
        return txManager.execute(conn -> propertyDAO.searchApproved(conn, address, type));
    }

    @Override
    public List<Property> searchCustomer(String address,
                                         String type,
                                         Integer minPrice,
                                         Integer maxPrice,
                                         String sort,
                                         int page,
                                         int size) {

        return txManager.execute(conn ->
                propertyDAO.searchCustomer(conn, address, type, minPrice, maxPrice, sort, page, size)
        );
    }

    @Override
    public int countCustomer(String address,
                             String type,
                             Integer minPrice,
                             Integer maxPrice) {

        return txManager.execute(conn ->
                propertyDAO.countCustomer(conn, address, type, minPrice, maxPrice)
        );
    }

    @Override
    public List<Property> searchAvailable(PropertySearchDTO dto) {
        return txManager.execute(conn -> propertyDAO.searchAvailable(conn, dto));
    }

    @Override
    public List<Property> findSimilar(Property property) {

        long minPrice = property.getPrice().multiply(BigDecimal.valueOf(0.8)).longValue();
        long maxPrice = property.getPrice().multiply(BigDecimal.valueOf(1.2)).longValue();

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
        return txManager.execute(conn -> propertyDAO.searchAvailableCard(conn, dto));
    }

    @Override
    public PageResult<Property> searchAdminPage(String address,
                                                String type,
                                                Integer minPrice,
                                                Integer maxPrice,
                                                String sort,
                                                int page,
                                                int size) {

        return txManager.execute(conn -> {
            List<Property> data = propertyDAO.searchAdmin(conn, address, type, minPrice, maxPrice, sort, page, size);
            int total = propertyDAO.countAdmin(conn, address, type, minPrice, maxPrice);
            return new PageResult<>(data, page, size, total);
        });
    }

    @Override
    public int countAdmin(String address,
                          String type,
                          Integer minPrice,
                          Integer maxPrice) {
        return txManager.execute(conn ->
                propertyDAO.countAdmin(conn, address, type, minPrice, maxPrice)
        );
    }

    @Override
    public PageResult<Property> searchStaffPage(Long staffId,
                                                String address,
                                                String type,
                                                Integer minPrice,
                                                Integer maxPrice,
                                                String sort,
                                                int page,
                                                int size) {

        return txManager.execute(conn -> {
            List<Property> data = propertyDAO.searchAdminByCreator(
                    conn,
                    staffId,
                    address,
                    type,
                    minPrice,
                    maxPrice,
                    sort,
                    page,
                    size
            );
            int total = propertyDAO.countAdminByCreator(
                    conn,
                    staffId,
                    address,
                    type,
                    minPrice,
                    maxPrice
            );
            return new PageResult<>(data, page, size, total);
        });
    }

    @Override
    public int countStaff(Long staffId,
                          String address,
                          String type,
                          Integer minPrice,
                          Integer maxPrice) {
        return txManager.execute(conn ->
                propertyDAO.countAdminByCreator(conn, staffId, address, type, minPrice, maxPrice)
        );
    }

    private Property requireForUpdate(Connection conn, Long propertyId) {
        return propertyDAO.findByIdForUpdate(conn, propertyId)
                .orElseThrow(() -> new BusinessException(ErrorCode.PROPERTY_NOT_FOUND));
    }

    private void requireStatus(Property property, PropertyStatus expected) {
        if (property.getStatus() != expected) {
            throw new BusinessException(ErrorCode.INVALID_STATE_TRANSITION);
        }
    }

    private void log(Connection conn,
                     Long userId,
                     ActivityLogAction action,
                     Long propertyId,
                     Object... args) {
        activityLogService.log(conn, userId, action, propertyId, null, args);
    }
}
