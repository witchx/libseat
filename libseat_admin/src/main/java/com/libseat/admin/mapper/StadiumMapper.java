package com.libseat.admin.mapper;

import com.libseat.api.entity.StadiumEntity;
import com.libseat.api.mapper.MyBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface StadiumMapper extends MyBaseMapper<StadiumEntity> {

    List<StadiumEntity> getStadiumList(@Param("userId") Integer userId,
                                       @Param("name") String name,
                                       @Param("companyName") String companyName,
                                       @Param("address") String address);
}
