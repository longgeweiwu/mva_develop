package com.itcc.mva.common.utils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WriteJson {

    public static void writeLog(String taskId,String jsonStr){
        log.info("{}.wav \n{}",taskId,jsonStr);
    }
}
