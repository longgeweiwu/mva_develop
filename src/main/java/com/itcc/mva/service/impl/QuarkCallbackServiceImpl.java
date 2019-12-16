package com.itcc.mva.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.itcc.mva.common.utils.Constant;
import com.itcc.mva.common.utils.Tools;
import com.itcc.mva.entity.QuarkCallbackEntity;
import com.itcc.mva.mapper.QuarkCallbackMapper;
import com.itcc.mva.service.IQuarkCallbackService;
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

    @Autowired
    private QuarkCallbackMapper quarkCallbackMapper;

    @Override
    public List<QuarkCallbackEntity> queryIflyPendingTop(int top) {
        return quarkCallbackMapper.queryIflyPendingTopMapper(top);
    }

    @Override
    public void addIflyTask(QuarkCallbackEntity quarkCallbackEntity) {
        String wavcid = quarkCallbackEntity.getCallid();
        //生成唯一的消息通知地址
        String task_notyfy_url= Constant.NOTIFYURL+"/"+wavcid;
        String waitingUrl=Constant.AUDIO+quarkCallbackEntity.getFullPath().split("\\/")[3]+"/"+quarkCallbackEntity.getVoiceFilename();
        //添加任务
        Tools.addTask(wavcid, Constant.URL,waitingUrl,task_notyfy_url);
    }

    @Override
    public void addRmaIflyTask(QuarkCallbackEntity quarkCallbackEntity) {
        String wavcid = quarkCallbackEntity.getCallid();
        //生成唯一的消息通知地址
        String audioUrl=Constant.AUDIO+quarkCallbackEntity.getFullPath().split("\\/")[3]+"/"+quarkCallbackEntity.getVoiceFilename();
        //添加任务
        Tools.addRmaTask(wavcid,Constant.RMAURL,Constant.RMANOTIFYURL,Constant.UPLOADFILE+"/"+quarkCallbackEntity.getVoiceFilename().split("\\.")[0],audioUrl);

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
