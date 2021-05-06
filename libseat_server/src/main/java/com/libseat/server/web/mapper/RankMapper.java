package com.libseat.server.web.mapper;


import com.libseat.api.entity.RankEntity;
import com.libseat.api.mapper.MyBaseMapper;
import com.libseat.server.web.dto.RankInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface RankMapper extends MyBaseMapper<RankEntity> {

    RankInfo getCustomerRankById(@Param("customerId") Integer customerId);

    RankInfo getCustomerRankByCustomerId(Integer id);
}
