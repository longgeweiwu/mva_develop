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
     * @param top 查询多少条
     * @return
     */
    List<QuarkCallbackEntity> queryIstIflyPendingTop(int top);

    /**
     * create job visit ifly web
     * 单条解析文本
     */
    void addIflyTask(QuarkCallbackEntity quarkCallbackEntity);
    /**
     * create job visit ifly web
     * 单条解析文本
     */
    void addistIflyTask(QuarkCallbackEntity quarkCallbackEntity);

    /**
     * create job visit ifly web
     * 单条转码录音
     */
    void addRmaIflyTask(QuarkCallbackEntity quarkCallbackEntity);

    void updateRmaVoiceName(String voiceFileName,String rmaVoiceName);

    void updateRmaVoiceFlag(String aid,int rmaFlag);

    String getVoidPath(String voiceFileName);

    /**
     * create job visit ifly web
     * @param top 查询待转码多少条
     * @return
     */
    List<QuarkCallbackEntity> pushToIflyAudioTop(int top);

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
