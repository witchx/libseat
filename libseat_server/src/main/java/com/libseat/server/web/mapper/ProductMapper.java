package com.libseat.server.web.mapper;

import com.libseat.api.entity.ProductEntity;
import com.libseat.api.mapper.MyBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface ProductMapper extends MyBaseMapper<ProductEntity> {

}
