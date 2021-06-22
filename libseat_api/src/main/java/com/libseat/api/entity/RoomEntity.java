package com.libseat.api.entity;


import com.libseat.api.model.Seat;
import lombok.Data;
import org.apache.ibatis.type.Alias;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Table(name = "lib_room")
@Alias(value = "RoomEntity")
public class RoomEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column
    private String name;
    @Column
    private Integer stadiumId;
    @Column
    private Integer line;
    @Column
    private Integer row;
    @Column
    private Timestamp createTime;
    @Column
    private Timestamp modifyTime;
    @Transient
    private Seat[] seat;
    @Transient
    private Integer availableSeatCount;
    @Transient
    private Integer totalSeatCount;
    @Transient
    private String stadiumName;
    @Transient
    private String CompanyName;
}
