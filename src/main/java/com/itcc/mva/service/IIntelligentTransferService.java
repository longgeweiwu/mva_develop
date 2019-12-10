package com.itcc.mva.service;

import java.util.List;

public interface IIntelligentTransferService {

    /**
     * 生成录音基表
     */
    void generateBaseTable();

    /**
     * 解析录音文件
     */
    void asr();

    /**
     * 多线程
     */
    void asrThreads();

}
