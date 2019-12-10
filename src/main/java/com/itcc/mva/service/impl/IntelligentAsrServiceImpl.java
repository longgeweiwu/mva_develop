package com.itcc.mva.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.itcc.mva.feign.IntelligentAsrFeign;
import com.itcc.mva.mapper.IntelligentAsrMapper;
import com.itcc.mva.service.IIntelligentTransferService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;

@Service
public class IntelligentAsrServiceImpl implements IIntelligentTransferService {
    private Logger log = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private IntelligentAsrMapper intelligentAsrMapper;
    @Autowired
    private IntelligentAsrFeign intelligentAsrFeign;

    @Value("${asrParams.parallel}")
    private int parallel;
    @Value("${asrParams.channelType}")
    private String channelType;
    @Value("${asrParams.audioFormat}")
    private String audioFormat;
    @Value("${asrParams.property}")
    private String property;
    @Value("${asrParams.resultType}")
    private String resultType;
    @Value("${asrParams.wordsInfo}")
    private String wordsInfo;
    @Value("${asrParams.enableAddPunc}")
    private String enableAddPunc;
    @Value("${asrParams.enableDiarization}")
    private String enableDiarization;
    @Value("${asrParams.enableEmotion}")
    private String enableEmotion;
    @Value("${asrParams.enableSpeed}")
    private String enableSpeed;


    @Override
    public void generateBaseTable() {
        intelligentAsrMapper.generateBaseTable();
    }

    @Override
    public void asr() {
        List<String> records = intelligentAsrMapper.getRecordsToAsr();
        JSONObject recordsDetail = new JSONObject();
        JSONArray audioPaths = new JSONArray();
        JSONArray fileExtension = new JSONArray();

        for(String record:records){
            audioPaths.add(record);
        }
        recordsDetail.put("audio_paths",audioPaths);
        recordsDetail.put("is_path_dir",false);
        fileExtension.add(".wav");
        fileExtension.add(".mp3");
        recordsDetail.put("file_extensions",fileExtension);
        recordsDetail.put("audio_format",audioFormat);
        recordsDetail.put("property",property);
        recordsDetail.put("result_type",resultType);
        recordsDetail.put("channel_type",channelType);
        recordsDetail.put("words_info",wordsInfo);
        recordsDetail.put("enable_add_punc",enableAddPunc);
        recordsDetail.put("enable_diarization",enableDiarization);
        recordsDetail.put("enable_emotion",enableEmotion);
        recordsDetail.put("enable_speed",enableSpeed);

        try {
            JSONObject responseResult = intelligentAsrFeign.asrResult(recordsDetail);
            String task_no = responseResult.getString("task_no");
            JSONObject statusDetail = new JSONObject();
            statusDetail.put("task_no",task_no);
            JSONObject responseStatus = intelligentAsrFeign.asrStatus(statusDetail);
            String res_msg = responseStatus.getString("res_msg");



            while(!"finished".equals(res_msg)){
                responseStatus = intelligentAsrFeign.asrStatus(statusDetail);
                res_msg = responseStatus.getString("res_msg");
                Thread.sleep(5000);
                log.info("任务"+task_no+"正在转写中，状态为:"+res_msg);
            }
            log.info("任务状态为"+res_msg);
            log.info("任务转写完成");
            responseStatus = intelligentAsrFeign.asrStatus(statusDetail);
            JSONArray status_list = responseStatus.getJSONArray("status_list");
            Iterator<Object> fileStatus = status_list.iterator();
            while (fileStatus.hasNext()) {
                JSONObject ob = (JSONObject) fileStatus.next();
                //打印出遍历出的jsonObject
                System.out.println(ob);
            }


        }catch(Exception e){
            log.info("ASR解析错误，请核对发送参数是否符合要求。");
        }


    }

    @Override
    public void asrThreads() {

    }
}
