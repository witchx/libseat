package com.libseat.server.web.service;

import com.libseat.server.web.dto.BasicCompanyInfo;

import java.util.List;

public interface HomeService {
    List<BasicCompanyInfo> getBasicCompanyInfo(Integer id);
}
