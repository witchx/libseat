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
    public PageResult<ProductEntity> getProductList(Integer id, String no, String name, String des, String companyName, Integer page, Integer pageSize) {
        PageHelper.startPage(page, pageSize);
        List<ProductEntity> productList = productMapper.getProductList(id, no, name, des, companyName);
        PageInfo pageInfo = new PageInfo(productList);
        return new PageResult<>(pageInfo.getTotal(),pageInfo.getList());
    }

    @Override
    public Integer insertProductBatch(List<ProductEntity> productEntities) {
        return productMapper.insertList(productEntities);
    }

    @Override
    public void deleteProduct(ProductEntity productEntity) {
        productMapper.deleteByPrimaryKey(productEntity);
    }

    @Override
    public ProductEntity getProductById(Integer productId) {
        ProductEntity productEntity = new ProductEntity();
        productEntity.setId(productId);
        return productMapper.selectByPrimaryKey(productEntity);
    }

    @Override
    public List<ProductEntity> getProductByIdBatch(List<Integer> collect) {
        return null;
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

}
