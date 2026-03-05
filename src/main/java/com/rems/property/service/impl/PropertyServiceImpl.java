package com.rems.property.service.impl;

import com.rems.common.constant.PropertyStatus;
import com.rems.common.constant.PropertyType;
import com.rems.property.dao.PropertyDAO;
import com.rems.property.dao.impl.PropertyDAOImpl;
import com.rems.property.model.*;
import com.rems.property.model.dto.*;
import com.rems.property.service.PropertyService;
import com.rems.common.constant.Role;
import com.rems.common.exception.BusinessException;
import com.rems.common.exception.ErrorCode;
import com.rems.common.util.DBUtil;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class PropertyServiceImpl implements PropertyService {

    private final PropertyDAO propertyDAO = new PropertyDAOImpl();

    @Override
    public void approveProperty(Long propertyId, Long staffId) {

        try (Connection conn = DBUtil.getConnection()) {

            conn.setAutoCommit(false);

            try {

                Property property = propertyDAO
                        .findByIdForUpdate(conn, propertyId)
                        .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND));

                if (property.getStatus() != PropertyStatus.DRAFT)
                    throw new BusinessException(ErrorCode.INVALID_STATE_TRANSITION);

                propertyDAO.updateStatus(conn, propertyId, PropertyStatus.AVAILABLE);
                propertyDAO.updateApproval(conn, propertyId, LocalDateTime.now());

                conn.commit();

            } catch (Exception e) {
                conn.rollback();
                throw e;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void reserveProperty(Long propertyId, Connection conn) {

        Property property = propertyDAO
                .findByIdForUpdate(conn, propertyId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND));

        if (property.getStatus() != PropertyStatus.AVAILABLE)
            throw new BusinessException(ErrorCode.PROPERTY_NOT_AVAILABLE);

        propertyDAO.updateStatus(conn, propertyId, PropertyStatus.RESERVED);
    }

    @Override
    public void completeSale(Long propertyId, Connection conn) {

        Property property = propertyDAO
                .findByIdForUpdate(conn, propertyId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND));

        if (property.getStatus() != PropertyStatus.RESERVED)
            throw new BusinessException(ErrorCode.INVALID_STATE_TRANSITION);

        propertyDAO.updateStatus(conn, propertyId, PropertyStatus.SOLD);
    }

    @Override
    public void completeRent(Long propertyId, Connection conn) {

        Property property = propertyDAO
                .findByIdForUpdate(conn, propertyId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND));

        if (property.getStatus() != PropertyStatus.RESERVED)
            throw new BusinessException(ErrorCode.INVALID_STATE_TRANSITION);

        propertyDAO.updateStatus(conn, propertyId, PropertyStatus.RENTED);
    }

    @Override
    public void failTransaction(Long propertyId, Connection conn) {

        Property property = propertyDAO
                .findByIdForUpdate(conn, propertyId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND));

        if (property.getStatus() != PropertyStatus.RESERVED)
            throw new BusinessException(ErrorCode.INVALID_STATE_TRANSITION);

        propertyDAO.updateStatus(conn, propertyId, PropertyStatus.AVAILABLE);
    }

    @Override
    public void deactivateProperty(Long propertyId, Long staffId) {

        try (Connection conn = DBUtil.getConnection()) {

            conn.setAutoCommit(false);

            try {

                Property property = propertyDAO
                        .findByIdForUpdate(conn, propertyId)
                        .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND));

                if (property.getStatus() == PropertyStatus.SOLD ||
                        property.getStatus() == PropertyStatus.RENTED)
                    throw new BusinessException(ErrorCode.INVALID_STATE_TRANSITION);

                propertyDAO.updateStatus(conn, propertyId, PropertyStatus.INACTIVE);

                conn.commit();

            } catch (Exception e) {
                conn.rollback();
                throw e;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void restoreProperty(Long propertyId, Long staffId) {

        try (Connection conn = DBUtil.getConnection()) {

            conn.setAutoCommit(false);

            try {

                Property property = propertyDAO
                        .findByIdForUpdate(conn, propertyId)
                        .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND));

                if (property.getStatus() != PropertyStatus.INACTIVE)
                    throw new BusinessException(ErrorCode.INVALID_STATE_TRANSITION);

                propertyDAO.updateStatus(conn, propertyId, PropertyStatus.AVAILABLE);

                conn.commit();

            } catch (Exception e) {
                conn.rollback();
                throw e;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
