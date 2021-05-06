package com.libseat.server.web.service.impl;

import com.libseat.api.entity.ProductEntity;
import com.libseat.server.web.mapper.ProductMapper;
import com.libseat.server.web.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductMapper productMapper;

    @Override
    public ProductEntity getProductById(Integer productId) {
        ProductEntity productEntity = new ProductEntity();
        productEntity.setId(productId);
        return productMapper.selectByPrimaryKey(productEntity);
    }
}
