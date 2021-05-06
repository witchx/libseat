package com.libseat.server.web.dto;

import lombok.Data;

@Data
public class RankInfo {
    private Integer rank;
    private String icon;
    private String nickname;
    private Double hoursByWeek;
    private Integer daysByWeek;
    private Double hoursByMonth;
    private Integer daysByMonth;
    private Double hoursByYear;
    private Integer daysByYear;

}
