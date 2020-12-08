package com.libseat.api.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Table(name = "lib_student")
public class StudentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column
    private String sid;
    @Column
    private String name;
    @Column
    private String password;
    /**
     * 1：男
     * 2：女
     */
    @Column
    private String sex;
    @Column
    private String tel;
    @Column
    private Integer universityId;
    @Column
    private String deleteFlag;
}
