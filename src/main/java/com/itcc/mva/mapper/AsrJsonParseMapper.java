package com.itcc.mva.mapper;

import com.itcc.mva.entity.IntelligentAsrEntity;

import java.util.List;

public interface AsrJsonParseMapper {
    /**
     *
     * @param top 查询多少条
     * @return
     */
    public List<IntelligentAsrEntity> queryPendingTopMapper(int top);

    public List<IntelligentAsrEntity> queryWaitingSend(int num);

}
