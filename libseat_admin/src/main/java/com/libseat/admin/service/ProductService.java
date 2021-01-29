package com.libseat.admin.service;

import com.libseat.api.entity.ProductEntity;
import com.libseat.utils.page.PageResult;

import java.util.List;

public interface ProductService {
    PageResult<ProductEntity> getProductList(Integer id, String no, String name, Integer type, String companyName, Integer conType, Integer page, Integer pageSize);

    Integer insertProductBatch(List<ProductEntity> productEntities);

    Integer insertProduct(ProductEntity productEntity);

    Integer updateProduct(ProductEntity productEntity);

    void deleteProductBatch(List<Integer> ids);

    Integer deleteProduct(ProductEntity productEntity);
}
