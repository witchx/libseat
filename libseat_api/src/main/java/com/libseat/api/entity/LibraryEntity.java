package com.libseat.api.entity;

import lombok.Data;
import org.apache.ibatis.type.Alias;

import javax.persistence.*;

@Data
@Table(name = "lib_library")
@Alias(value = "LibraryEntity")
public class LibraryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column
    private String name;
    @Column
    private Integer universityId;
    @Column
    private String deleteFlag;

    @Transient
    private String universityName;
}
