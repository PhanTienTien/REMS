package com.rems.property.service.impl;

import com.rems.common.transaction.TransactionManager;
import com.rems.property.dao.PropertyImageDAO;
import com.rems.property.dao.impl.PropertyImageDAOImpl;
import com.rems.property.model.PropertyImage;
import com.rems.property.service.PropertyImageService;

import java.sql.Connection;
import java.util.List;

public class PropertyImageServiceImpl implements PropertyImageService {

    private final PropertyImageDAO propertyImageDAO;
    private final TransactionManager txManager;

    public PropertyImageServiceImpl() {
        this(new PropertyImageDAOImpl(), new TransactionManager());
    }

    public PropertyImageServiceImpl(PropertyImageDAO propertyImageDAO, TransactionManager txManager) {
        this.propertyImageDAO = propertyImageDAO;
        this.txManager = txManager;
    }

    @Override
    public List<PropertyImage> getByPropertyId(Long propertyId) {

        return txManager.execute(conn ->
                propertyImageDAO.findByPropertyId(conn, propertyId)
        );
    }

    @Override
    public String getThumbnail(Long propertyId) {

        return txManager.execute(conn ->
                propertyImageDAO.getThumbnail(conn, propertyId)
        );
    }

    @Override
    public void addImages(Connection conn,
                          Long propertyId,
                          List<String> imageUrls) {

        propertyImageDAO.insertMultiple(conn, propertyId, imageUrls);
    }

    @Override
    public void addImagesAction(Long propertyId, List<String> imageUrls) {

        txManager.executeWithoutResult(conn -> {

            propertyImageDAO.resetThumbnail(conn, propertyId);

            propertyImageDAO.insertMultiple(conn, propertyId, imageUrls);

        });
    }

    @Override
    public void deleteImage(Long imageId) {

        txManager.executeWithoutResult(conn ->
                propertyImageDAO.deleteById(conn, imageId)
        );
    }
}
