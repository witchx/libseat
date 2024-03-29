package com.libseat.admin.mapper;

import com.libseat.api.entity.CouponEntity;
import com.libseat.api.entity.ProductEntity;
import com.libseat.api.entity.VipCardEntity;
import com.libseat.api.mapper.MyBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProductMapper extends MyBaseMapper<ProductEntity> {

    List<ProductEntity> getProductList(@Param("id") Integer id,
                                       @Param("no") String no,
                                       @Param("name") String name,
                                       @Param("des") String des,
                                       @Param("companyName") String companyName);

    void deleteProductBatch(@Param("ids") List<Integer> ids);
}
