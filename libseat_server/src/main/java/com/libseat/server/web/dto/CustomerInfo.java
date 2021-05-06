package com.libseat.server.web.dto;

import lombok.Data;

import javax.persistence.*;

@Data
public class CustomerInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column
    private String icon;
    @Column
    private String nickname;
    @Column
    private String username;
    @Column
    private Integer sex;
    @Column
    private String tel;
    @Column
    private String email;
    @Column
    private Integer userId;
    @Transient
    private String companyName;
}
