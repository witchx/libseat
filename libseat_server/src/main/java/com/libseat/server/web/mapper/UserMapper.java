package com.libseat.server.web.mapper;


import com.libseat.api.entity.UserEntity;
import com.libseat.api.mapper.MyBaseMapper;
import com.libseat.server.web.dto.BasicCompanyInfo;
import com.libseat.server.web.dto.CompanyInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface UserMapper extends MyBaseMapper<UserEntity> {

    List<BasicCompanyInfo> getBasicCompanyInfo(@Param("id") Integer id);

    List<CompanyInfo> getAllCompany();
}
