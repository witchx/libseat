package com.libseat.admin.service;


import com.libseat.api.entity.RankEntity;
import com.libseat.api.entity.RankInfo;

import java.util.List;

public interface RankService {

    void initRank();

    Boolean clearRank(String flag);

    void updateRank(String flag, Double time, String rank);

    Integer insertRank(RankEntity rankEntity);

    RankEntity getRankByCustomerId(Integer id);

    void addStudyTime(Integer orderId);

    List<RankEntity> getRankListDB();

    List<RankInfo> getRankInfoListDB();

    RankEntity getRankDBById(Integer id);

    Integer updateRankDB(Integer customerId, Double addTime, Integer addDay);

    void clearWeekRank();
}
