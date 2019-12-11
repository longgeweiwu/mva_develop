package com.itcc.mva.service;

import com.itcc.mva.entity.IntelligentAsrEntity;

import java.util.List;

/**
 * @author whoami
 */
public interface IAsrJsonParseService {
    /**
     *
     * @param top 查询多少条
     * @return
     */
    List<IntelligentAsrEntity> queryPendingTop(int top);

    /**
     * 单条解析文本
     */
    void jsonSigle(IntelligentAsrEntity intelligentAsrEntity);


    /**
     *
     * @param num 查询待发送多少条
     * @return
     */
    List<IntelligentAsrEntity> queryWaitingSend(int num);
}
