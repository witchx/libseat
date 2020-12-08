package com.libseat.api.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Table(name = "lib_admin")
public class AdminEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column
    private String loginName;
    @Column
    private String password;
    @Column
    private String role;
    @Column
    private String deleteFlag;
}
