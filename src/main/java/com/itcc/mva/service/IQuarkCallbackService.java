package com.itcc.mva.service;

import com.itcc.mva.entity.QuarkCallbackEntity;

import java.util.List;
/**
 * @author whoami
 */
public interface IQuarkCallbackService {
    /**
     * create job visit ifly web
     * @param top 查询多少条
     * @return
     */
    List<QuarkCallbackEntity> queryIflyPendingTop(int top);

    /**
     * create job visit ifly web
     * 单条解析文本
     */
    void addIflyTask(QuarkCallbackEntity quarkCallbackEntity);

    // 上面方法是 离线请求
    // 下面是回调处理
    /**
     * update parse result
     * @param callid
     * @param iflyresult
     */
    void uiflyresultQuarkCall(String callid,String iflyresult);

    /**
     * modify ifly parse
     * @param callid
     */
    void modifyIflyParse(String callid);

    /**
     * copy to iflybasetable
     */
    void generateIflyBaseTable();
}
