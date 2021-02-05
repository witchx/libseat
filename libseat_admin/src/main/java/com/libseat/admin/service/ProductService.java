package com.libseat.admin.service;

import com.libseat.api.entity.CouponEntity;
import com.libseat.api.entity.ProductEntity;
import com.libseat.api.entity.VipCardEntity;
import com.libseat.utils.page.PageResult;

import java.util.List;

public interface ProductService {

    PageResult<ProductEntity> getProductList(Integer id, String no, String name, String des, String companyName, Integer page, Integer pageSize);

    Integer insertProduct(ProductEntity productEntity);

    Integer updateProduct(ProductEntity productEntity);

    void deleteProductBatch(List<Integer> ids);

    Integer insertProductBatch(List<ProductEntity> productEntities);

    void deleteProduct(ProductEntity productEntity);
}
