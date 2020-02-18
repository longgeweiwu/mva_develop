package com.itcc.mva.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.itcc.mva.feign.IntelligentAsrFeign;
import com.itcc.mva.mapper.IntelligentAsrMapper;
import com.itcc.mva.service.IIntelligentTransferService;
import com.itcc.mva.vo.RecordNameAndPathVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Service
public class IntelligentAsrServiceImpl implements IIntelligentTransferService {
    private Logger log = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private IntelligentAsrMapper intelligentAsrMapper;
    @Autowired
    private IntelligentAsrFeign intelligentAsrFeign;

//    @Value("${asrParams.recordDir}")
//    private String recordDir;
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
    private boolean enableAddPunc;
    @Value("${asrParams.enableDiarization}")
    private boolean enableDiarization;
    @Value("${asrParams.enableEmotion}")
    private boolean enableEmotion;
    @Value("${asrParams.enableSpeed}")
    private boolean enableSpeed;
    @Value("${asrParams.isPathdir}")
    private boolean isPathdir;
    @Value("${asrParams.enableCallback}")
    private boolean enableCallback;
    @Value("${asrParams.enableDigitnorm}")
    private boolean enableDigitnorm;

    @Override
    public void generateBaseTable() {
        intelligentAsrMapper.generateBaseTable();
    }

    @Override
    public void asr() {
        List<RecordNameAndPathVo> records = intelligentAsrMapper.getRecordsToAsr();
        if(records.size()!=0){

        JSONObject recordsDetail = new JSONObject();
        JSONArray audioPaths = new JSONArray();
        JSONArray fileExtension = new JSONArray();
        String resultPath = null;
        for(RecordNameAndPathVo record:records){
            audioPaths.add("file://"+record.getFullPath()+record.getVoiceFilename());
            resultPath = "file://"+record.getFullPath();
        }
        recordsDetail.put("audio_paths",audioPaths);
        recordsDetail.put("is_path_dir",isPathdir);
        recordsDetail.put("result_path",resultPath);
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
        recordsDetail.put("enable_callback",enableCallback);
        recordsDetail.put("enable_digit_norm",enableDigitnorm);
        log.info("发送参数如下，到ASR引擎"+recordsDetail.toJSONString());
        try {
            log.info("准备发送json参数给asr引擎--------------------------");
            JSONObject responseResult = intelligentAsrFeign.asrResult(recordsDetail);
            log.info("获得asr引擎的返回信息--------------------------");
            JSONArray files = new JSONArray();
            for(RecordNameAndPathVo record:records){
                files.add(record.getVoiceFilename());
            }
            String task_no = responseResult.getString("task_no");
            log.info("获得asr引擎的返回信息--------------------------\n任务ID为:"+task_no);
            JSONObject statusDetail = new JSONObject();
            statusDetail.put("task_no",task_no);
            statusDetail.put("files",files);
            log.info("发送json参数给asr引擎，获取任务状态--------------------------");
            JSONObject responseStatus = intelligentAsrFeign.asrStatus(statusDetail);
            String res_msg = responseStatus.getString("res_msg");
            log.info("任务状态："+res_msg+"--------------------------");
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
            int i = 1;
            while (fileStatus.hasNext()) {
                log.info("遍历每个文件转写结果");
                JSONObject ob = (JSONObject) fileStatus.next();
                String filePath = ob.getString("file");
                log.info("第"+i+"个文件路径"+filePath);
                String[] filePathSplit = filePath.split("/");
                String fileName = "path"+i+"_"+filePathSplit[filePathSplit.length-1]+"."+resultType.toLowerCase();
                log.info("第"+i+"个文件名"+fileName);
                int fileNameLength = filePathSplit[filePathSplit.length-1].length();
                log.info("第"+i+"个文件名长度"+fileNameLength);
                Map<String,String> params = new HashMap<>();
                params.put("recordName",filePathSplit[filePathSplit.length-1]);
                params.put("filePath",filePath.substring(7,filePath.length()-fileNameLength));
                params.put("fileName",fileName);
                params.put("task_no",task_no);
                params.put("resultPath",resultPath);
                if("success".equals(ob.getString("msg"))){
                    intelligentAsrMapper.asrSuccess(params);
                    i = i + 1;
                }else{
                    intelligentAsrMapper.asrFail(filePathSplit[filePathSplit.length-1]);
                    i = i + 1;
                }

            }

        }catch(Exception e){
            log.info("ASR解析错误，请核对发送参数是否符合要求。");
            e.printStackTrace();
        }
        }else{
            log.info("解析任务为0，不存在ASR解析。");
        }
    }

    @Override
    public void asrThreads() {

    }
}
