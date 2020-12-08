package com.libseat.api.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Table(name = "lib_university")
public class UniversityEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column
    private String name;
    @Column
    private String deleteFlag;
}
