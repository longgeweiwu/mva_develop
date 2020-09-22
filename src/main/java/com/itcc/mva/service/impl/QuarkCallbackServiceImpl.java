package com.itcc.mva.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.itcc.mva.common.utils.Constant;
import com.itcc.mva.common.utils.Tools;
import com.itcc.mva.entity.QuarkCallbackEntity;
import com.itcc.mva.mapper.QuarkCallbackMapper;
import com.itcc.mva.service.IQuarkCallbackService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author whoami
 */
@Service
public class QuarkCallbackServiceImpl implements IQuarkCallbackService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private QuarkCallbackMapper quarkCallbackMapper;

    @Override
    public List<QuarkCallbackEntity> queryIflyPendingTop(int top) {
        return quarkCallbackMapper.queryIflyPendingTopMapper(top);
    }

    @Override
    public List<QuarkCallbackEntity> queryIstIflyPendingTop(int top) {
        return quarkCallbackMapper.queryIstIflyPendingTopMapper(top);
    }

    @Override
    public void addIflyTask(QuarkCallbackEntity quarkCallbackEntity) {
        String wavcid = quarkCallbackEntity.getCallid();
        //生成唯一的消息通知地址
        String task_notyfy_url= Constant.NOTIFYURL+"/"+wavcid;
        String waitingUrl=Constant.AUDIO+quarkCallbackEntity.getFullPath().split("\\/")[3]+"/16k_"+quarkCallbackEntity.getVoiceFileName();
        //添加任务
        Tools.addTask(wavcid, Constant.URL,waitingUrl,task_notyfy_url);
        preIflyParse(wavcid);
    }

    @Override
    public void addistIflyTask(QuarkCallbackEntity quarkCallbackEntity) {
        String wavcid = quarkCallbackEntity.getCallid();
        //生成唯一的消息通知地址
        String task_notyfy_url= Constant.NOTISTIFYURL;
        String waitingUrl=Constant.AUDIO+quarkCallbackEntity.getFullPath().split("\\/")[3]+"/"+quarkCallbackEntity.getVoiceFileName();
        //添加任务
        logger.info("回调地址为:"+task_notyfy_url + "-----> 录音地址为:"+waitingUrl);
        if(Tools.addIstTask(wavcid, Constant.ISTURL,waitingUrl,task_notyfy_url)==1){
            preIflyParse(wavcid);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void preIflyParse(String callid) {
        QuarkCallbackEntity quarkCallbackEntity = new QuarkCallbackEntity();
        quarkCallbackEntity.setAid(callid);
        quarkCallbackEntity.setInsertTime(new Date());
        quarkCallbackEntity.setIflyparseStatus(Constant.ASRPARSER_REQIFLY_SUCCESS);
        quarkCallbackMapper.update(quarkCallbackEntity, new QueryWrapper<QuarkCallbackEntity>().eq("CALLID", callid));
    }

    @Override
    public void addRmaIflyTask(QuarkCallbackEntity quarkCallbackEntity) {
        String wavcid = quarkCallbackEntity.getCallid();
        //生成唯一的消息通知地址
        String audioUrl=Constant.AUDIO+quarkCallbackEntity.getFullPath().split("\\/")[3]+"/"+quarkCallbackEntity.getVoiceFileName();
        //添加任务
        Tools.addRmaTask(wavcid,Constant.RMAURL,Constant.RMANOTIFYURL,Constant.UPLOADFILE+"/"+quarkCallbackEntity.getVoiceFileName().split("\\.")[0],audioUrl);

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateRmaVoiceName(String voiceFileName, String rmaVoiceName) {
        QuarkCallbackEntity quarkCallbackEntity = new QuarkCallbackEntity();
        quarkCallbackEntity.setVoiceFileName(voiceFileName);
        quarkCallbackEntity.setRmavoiceFileName(rmaVoiceName);
        quarkCallbackMapper.update(quarkCallbackEntity, new QueryWrapper<QuarkCallbackEntity>().eq("VOICE_FILE_NAME", voiceFileName));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateRmaVoiceFlag(String aid, int rmaFlag) {
        QuarkCallbackEntity quarkCallbackEntity = new QuarkCallbackEntity();
        quarkCallbackEntity.setRmaflag(rmaFlag);
        quarkCallbackMapper.update(quarkCallbackEntity, new QueryWrapper<QuarkCallbackEntity>().eq("CALLID", aid));
    }

    @Override
    public String getVoidPath(String voiceFileName) {
        QuarkCallbackEntity quarkCallbackEntity = quarkCallbackMapper.selectOne(new QueryWrapper<QuarkCallbackEntity>().eq("VOICE_FILE_NAME", voiceFileName));
        return quarkCallbackEntity.getLeavewordPath();
    }

    @Override
    public List<QuarkCallbackEntity> pushToIflyAudioTop(int top) {
        return quarkCallbackMapper.pushToIflyAudioTopMapper(top);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void uiflyresultQuarkCall(String callid,String iflyresult) {
        QuarkCallbackEntity quarkCallbackEntity = new QuarkCallbackEntity();

        quarkCallbackEntity.setAid(callid);
        quarkCallbackEntity.setIflyResult(iflyresult);
        quarkCallbackEntity.setInsertTime(new Date());
        quarkCallbackEntity.setIflyparseStatus(Constant.ASRPARSER_IFLY_SUCCESS);

        quarkCallbackMapper.update(quarkCallbackEntity, new QueryWrapper<QuarkCallbackEntity>().eq("CALLID", callid));

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void modifyIflyParse(String callid) {
        //修改基表 科讯飞 解析完成
        QuarkCallbackEntity result = quarkCallbackMapper.selectOne(new QueryWrapper<QuarkCallbackEntity>().eq("CALLID", callid));

        if(null == result.getIflyparseStatus()){
            result.setIflyparseStatus(Constant.ASRPARSER_IFLY_FAIL);
        }else{
            result.setIflyparseStatus(result.getIflyparseStatus()+1);
        }
        quarkCallbackMapper.update(result, new QueryWrapper<QuarkCallbackEntity>().eq("CALLID", callid));
    }

    @Override
    public void generateIflyBaseTable() {
        quarkCallbackMapper.generateIflyBaseTable();
    }

}
