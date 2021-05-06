package com.libseat.server.web.service;


import com.libseat.api.entity.CustomerEntity;
import com.libseat.server.web.dto.CustomerInfo;

import javax.servlet.http.HttpServletRequest;

public interface LoginService {
    CustomerInfo login(CustomerEntity customerEntity);

    void addCustomerToken(String token, String memberId);

    String jwtToken(CustomerInfo customer, HttpServletRequest request);
}
