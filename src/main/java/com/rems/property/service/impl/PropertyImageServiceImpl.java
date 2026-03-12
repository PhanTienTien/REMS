package com.rems.property.service.impl;

import com.rems.common.transaction.TransactionManager;
import com.rems.property.dao.PropertyImageDAO;
import com.rems.property.dao.impl.PropertyImageDAOImpl;
import com.rems.property.model.PropertyImage;
import com.rems.property.service.PropertyImageService;

import java.util.List;

public class PropertyImageServiceImpl implements PropertyImageService {

    private final PropertyImageDAO propertyImageDAO = new PropertyImageDAOImpl();
    private final TransactionManager txManager = new TransactionManager();

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
    public void addImages(Long propertyId, List<String> imageUrls) {

        txManager.executeWithoutResult(conn ->
                propertyImageDAO.insertMultiple(conn, propertyId, imageUrls)
        );
    }
}