package com.libseat.admin.mapper;

import com.libseat.api.entity.CustomerEntity;
import com.libseat.api.mapper.MyBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.List;

@Mapper
@Component
public interface CustomerMapper extends MyBaseMapper<CustomerEntity> {
    List<CustomerEntity> getCustomerList(@Param("username") String username,
                                         @Param("sex") Integer sex,
                                         @Param("userId") Integer userId,
                                         @Param("createTimeStart") Timestamp createTimeStart,
                                         @Param("createTimeEnd") Timestamp createTimeEnd,
                                         @Param("lastLoginTimeStart") Timestamp lastLoginTimeStart,
                                         @Param("lastLoginTimeEnd")  Timestamp lastLoginTimeEnd);

    Integer insertCustomerBatch(@Param("customerEntities") List<CustomerEntity> customerEntities);

    void deleteCustomerBatch(@Param("customerEntities") List<CustomerEntity> customerEntities);

    List<CustomerEntity> getYesterdayCustomerByUserId(Integer userId, Timestamp yesStartTime, Timestamp yesEndTime);
}
