package com.itcc.mva.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.itcc.mva.common.utils.Constant;
import com.itcc.mva.common.utils.Tools;
import com.itcc.mva.entity.IntelligentAsrEntity;
import com.itcc.mva.entity.QuarkCallbackEntity;
import com.itcc.mva.job.IflyAsrJob;
import com.itcc.mva.mapper.IntelligentAsrMapper;
import com.itcc.mva.mapper.QuarkCallbackMapper;
import com.itcc.mva.service.IQuarkCallbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class QuarkCallbackServiceImpl implements IQuarkCallbackService {

    @Autowired
    private QuarkCallbackMapper quarkCallbackMapper;

    @Autowired
    private IntelligentAsrMapper intelligentAsrMapper;

    @Override
    public List<IntelligentAsrEntity> queryIflyPendingTop(int top) {
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertQuarkCall(String callid,String iflyresult) {
        QuarkCallbackEntity quarkCallbackEntity = new QuarkCallbackEntity();
        quarkCallbackEntity.setPid(Tools.getUniqueID());
        quarkCallbackEntity.setCallid(callid);
        quarkCallbackEntity.setIflyResult(iflyresult);
        quarkCallbackEntity.setInsertTime(new Date());
        quarkCallbackMapper.insert(quarkCallbackEntity);

        //修改基表 科讯飞 解析完成
        IntelligentAsrEntity result = new IntelligentAsrEntity();
        result.setJsonparseStatus(Constant.ASRPARSER_FAIL);
        intelligentAsrMapper.update(result, new QueryWrapper<IntelligentAsrEntity>().eq("CALLID", callid));
    }
}
