package com.itcc.mva.service;

import com.itcc.mva.entity.IntelligentAsrEntity;

import java.util.List;

public interface IAsrJsonParseService {
    /**
     *
     * @param top 查询多少条
     * @return
     */
    List<IntelligentAsrEntity> queryPendingTop(int top);
}
