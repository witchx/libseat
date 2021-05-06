package com.libseat.server.web.dto;

import lombok.Data;

@Data
public class BasicCompanyInfo {
    private Integer userId;
    private Integer stadiumId;
    private String companyName;
    private String stadiumName;
    private String detailAddress;
    private String icon;
}
