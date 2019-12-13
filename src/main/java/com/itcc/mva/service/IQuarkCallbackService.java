package com.itcc.mva.service;

import com.itcc.mva.entity.QuarkCallbackEntity;

import java.util.List;

public interface IQuarkCallbackService {
    /**
     *
     * @param top 查询多少条
     * @return
     */
    List<QuarkCallbackEntity> queryIflyPendingTop(int top);
    /**
     * 单条解析文本
     */
    void addIflyTask(QuarkCallbackEntity quarkCallbackEntity);

    void insertQuarkCall(String callid,String iflyresult);

    void modifyIflyParse(String callid);

    void generateIflyBaseTable();
}
