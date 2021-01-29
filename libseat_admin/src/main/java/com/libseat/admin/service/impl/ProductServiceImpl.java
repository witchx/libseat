package com.libseat.admin.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.libseat.admin.mapper.ProductMapper;
import com.libseat.admin.service.ProductService;
import com.libseat.api.entity.ProductEntity;
import com.libseat.utils.page.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductMapper productMapper;

    @Override
    public PageResult<ProductEntity> getProductList(Integer id, String no, String name, Integer type, String companyName, Integer conType, Integer page, Integer pageSize) {
        PageHelper.startPage(page, pageSize);
        List<ProductEntity> productList = productMapper.getProductList(id, no, name, type, companyName, conType);
        PageInfo pageInfo = new PageInfo(productList);
        return new PageResult<>(pageInfo.getTotal(),pageInfo.getList());
    }

    @Override
    public Integer insertProductBatch(List<ProductEntity> productEntities) {
        return productMapper.insertList(productEntities);
    }

    @Override
    public Integer insertProduct(ProductEntity productEntity) {
        return productMapper.insert(productEntity);
    }

    @Override
    public Integer updateProduct(ProductEntity productEntity) {
        return productMapper.updateByPrimaryKeySelective(productEntity);
    }

    @Override
    public void deleteProductBatch(List<Integer> ids) {
        productMapper.deleteProductBatch(ids);
    }

    @Override
    public Integer deleteProduct(ProductEntity productEntity) {
        return productMapper.deleteByPrimaryKey(productEntity);
    }
}
