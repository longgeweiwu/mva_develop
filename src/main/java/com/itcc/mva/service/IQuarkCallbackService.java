package com.itcc.mva.service;

import com.itcc.mva.entity.IntelligentAsrEntity;

import java.util.List;

public interface IQuarkCallbackService {
    /**
     *
     * @param top 查询多少条
     * @return
     */
    List<IntelligentAsrEntity> queryIflyPendingTop(int top);
    /**
     * 单条解析文本
     */
    void addIflyTask(IntelligentAsrEntity intelligentAsrEntity);

    void insertQuarkCall(String callid,String iflyresult);

}
