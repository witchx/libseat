package com.libseat.server.web.service.impl;

import com.libseat.server.web.dto.BasicCompanyInfo;
import com.libseat.server.web.mapper.UserMapper;
import com.libseat.server.web.service.HomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HomeServiceImpl implements HomeService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public List<BasicCompanyInfo> getBasicCompanyInfo(Integer id) {
        return userMapper.getBasicCompanyInfo(id);
    }
}
