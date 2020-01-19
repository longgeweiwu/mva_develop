package com.itcc.mva.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.itcc.mva.common.utils.Constant;
import com.itcc.mva.common.utils.HttpUtil;
import com.itcc.mva.common.utils.Tools;
import com.itcc.mva.entity.AliAsrEntity;
import com.itcc.mva.mapper.AliTransferMapper;
import com.itcc.mva.service.IAliTransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author whoami
 */
@Service
public class AliTransferServiceImpl implements IAliTransferService {

    @Autowired
    private AliTransferMapper aliTransferMapper;

    @Override
    public void generateAliBaseTable() {
        aliTransferMapper.generateAliBaseTable();
    }

    @Override
    public List<AliAsrEntity> queryAliPendingTop(int top) {
        return aliTransferMapper.queryAliPendingTopMapper(top);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addAliTask(AliAsrEntity aliAsrEntity) {
        String audioUrl= Constant.AUDIO+aliAsrEntity.getFullPath().split("\\/")[3]+"/"+aliAsrEntity.getVoiceFileName();

        Map<String, Object> headers = new HashMap<String, Object>();
        headers.put("Content-Type","application/json");

        JSONObject sendAliweb = new JSONObject();
        sendAliweb.put(Constant.KEY_APPKEY,"default");
        sendAliweb.put(Constant.KEY_TOKEN,"default");
        sendAliweb.put(Constant.KEY_FILE_LINK,audioUrl);
        sendAliweb.put(Constant.VOCABULARY_ID,"096d421ab4e74266b9ecd3c28e27e936");

        String response= HttpUtil.httpPost(Constant.KEY_ALIASR, headers,  sendAliweb.toJSONString(), null,Constant.HTTP_TIMEOUT, false);

        if(null!=response && Tools.isJSONValid(response)){
            JSONObject responseJson=JSONObject.parseObject(response);
            if(responseJson.containsKey("header")&&Constant.STATUS_SUCCESS.equals(responseJson.getJSONObject("header").getString("status_message"))){
                String taskId=responseJson.getJSONObject("header").getString("task_id");
                AliAsrEntity aliAsr = new AliAsrEntity();
                aliAsr.setTaskid(taskId);
                aliAsr.setAsrflag(Constant.ASRPARSER_ALI_SUCCESS);
                aliTransferMapper.update(aliAsr,new QueryWrapper<AliAsrEntity>().eq("CALLID", aliAsrEntity.getCallid()));
            }else{
                if(aliAsrEntity.getAsrflag()== null ){
                    AliAsrEntity aliAsr = new AliAsrEntity();
                    aliAsr.setAsrflag(Constant.ASRPARSER_ALI_FAIL);
                    aliTransferMapper.update(aliAsr,new QueryWrapper<AliAsrEntity>().eq("CALLID", aliAsrEntity.getCallid()));
                }else{
                    AliAsrEntity aliAsr = new AliAsrEntity();
                    aliAsr.setAsrflag(aliAsrEntity.getAsrflag()+1);
                    aliTransferMapper.update(aliAsr,new QueryWrapper<AliAsrEntity>().eq("CALLID", aliAsrEntity.getCallid()));
                }
            }
        }else{
            if(aliAsrEntity.getAsrflag()== null ){
                AliAsrEntity aliAsr = new AliAsrEntity();
                aliAsr.setAsrflag(Constant.ASRPARSER_ALI_FAIL);
                aliTransferMapper.update(aliAsr,new QueryWrapper<AliAsrEntity>().eq("CALLID", aliAsrEntity.getCallid()));
            }else{
                AliAsrEntity aliAsr = new AliAsrEntity();
                aliAsr.setAsrflag(aliAsrEntity.getAsrflag()+1);
                aliTransferMapper.update(aliAsr,new QueryWrapper<AliAsrEntity>().eq("CALLID", aliAsrEntity.getCallid()));
            }
        }
    }


    @Override
    public List<AliAsrEntity> queryAliResultTop(int top) {
        return aliTransferMapper.queryAliResultTopMapper(top);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void queryAndSetAliBase(AliAsrEntity aliAsrEntity) {

        StringBuffer aliResult=new StringBuffer();

        Map<String, Object> requestParams = new HashMap<String, Object>();
        requestParams.put(Constant.KEY_TASK_ID,aliAsrEntity.getTaskid());
        String response= HttpUtil.get(Constant.KEY_QUERYALIASR,requestParams);

        if(null!=response && Tools.isJSONValid(response)){
            JSONObject responseJson=JSONObject.parseObject(response);

            if(responseJson.containsKey("header")&&responseJson.containsKey("payload")){
                AliAsrEntity aliAsr = new AliAsrEntity();
                switch (responseJson.getJSONObject("header").getString("status_message")){
                    case Constant.STATUS_SUCCESS:
                        JSONObject payloadJson=responseJson.getJSONObject("payload");
                        if(payloadJson.containsKey("sentences")){
                            for(int i=0;i<payloadJson.getJSONArray("sentences").size();i++){
                                aliResult.append(((JSONObject)payloadJson.getJSONArray("sentences").get(i)).get("text"));
                            }
                        }
                        aliAsr.setAliResult(aliResult.toString());
                        aliAsr.setAliparseStatus(Constant.ASRPARSER_QUERYALI_SUCCESS);
                        aliAsr.setInsertTime(new Date());
                        aliTransferMapper.update(aliAsr,new QueryWrapper<AliAsrEntity>().eq("CALLID", aliAsrEntity.getCallid()));
                        break;
                    case Constant.STATUS_RUNNING:
                        break;
                    case Constant.FILE_DOWNLOAD_FAILED:
                        aliAsr.setAliparseStatus(Constant.ASRPARSER_SERIOUS_FAIL);
                        aliAsr.setInsertTime(new Date());
                        aliTransferMapper.update(aliAsr,new QueryWrapper<AliAsrEntity>().eq("CALLID", aliAsrEntity.getCallid()));
                        break;
                    default:
                        aliAsr.setAliparseStatus(Constant.ASRPARSER_QUERYRESULTALI_FAIL);
                        aliAsr.setInsertTime(new Date());
                        aliTransferMapper.update(aliAsr,new QueryWrapper<AliAsrEntity>().eq("CALLID", aliAsrEntity.getCallid()));
                }
            }
        }else{
            AliAsrEntity aliAsr = new AliAsrEntity();
            aliAsr.setAliparseStatus(Constant.ASRPARSER_EXCEPTION_FAIL);
            aliAsr.setInsertTime(new Date());
            aliTransferMapper.update(aliAsr,new QueryWrapper<AliAsrEntity>().eq("CALLID", aliAsrEntity.getCallid()));
        }
    }
}
