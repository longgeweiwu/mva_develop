package com.itcc.mva.service;

import com.itcc.mva.entity.IntelligentAsrEntity;
import com.itcc.mva.entity.QuarkCallbackEntity;

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
     * @param num JT查询待发送多少条
     * @return
     */
    List<IntelligentAsrEntity> queryWaitingSendJt(int num);
    /**
     *
     * @param num KD查询待发送多少条
     * @return
     */
    List<QuarkCallbackEntity> queryWaitingSendKd(int num);
    /**
     *
     * @param num AL查询待发送多少条
     * @return
     */
    List<IntelligentAsrEntity> queryWaitingSendAl(int num);
}
