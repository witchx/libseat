package com.libseat.api.entity;

import com.swpu.libseat.dto.Seat;
import lombok.Data;
import org.apache.ibatis.type.Alias;

import javax.persistence.*;

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
    private Integer libraryId;
    @Column
    private Integer line;
    @Column
    private Integer row;
    @Column
    private String deleteFlag;
    @Transient
    private Seat[] seat;
    @Transient
    private Integer availableSeatCount;
    @Transient
    private Integer totalSeatCount;
}