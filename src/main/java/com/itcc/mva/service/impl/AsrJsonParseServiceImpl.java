package com.itcc.mva.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.itcc.mva.common.utils.Constant;
import com.itcc.mva.common.utils.JsonTools;
import com.itcc.mva.common.utils.Tools;
import com.itcc.mva.entity.AliAsrEntity;
import com.itcc.mva.entity.IntelligentAsrEntity;
import com.itcc.mva.entity.QuarkCallbackEntity;
import com.itcc.mva.entity.TxAsrEntity;
import com.itcc.mva.mapper.AsrJsonParseMapper;
import com.itcc.mva.mapper.IntelligentAsrMapper;
import com.itcc.mva.service.IAsrJsonParseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author whoami
 */
@Service
public class AsrJsonParseServiceImpl implements IAsrJsonParseService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private AsrJsonParseMapper asrJsonParseMapper;

    @Autowired
    private IntelligentAsrMapper intelligentAsrMapper;

    @Override
    public List<IntelligentAsrEntity> queryPendingTop(int top) {
        return asrJsonParseMapper.queryPendingTopMapper(top);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void jsonSigle(IntelligentAsrEntity intelligentAsrEntity) {
        //生产path要修改！！！
        try {
            String dirPath = intelligentAsrEntity.getOutputFilepath().replace("file:///home/data/", "z:\\leaveword\\jt\\");
            String path = dirPath + "\\" + Tools.findTrueFname(dirPath, Tools.getSplitMaxValue(intelligentAsrEntity.getOutputFilename(), "\\_"));
            StringBuffer bfText = new StringBuffer();
            String readJsonFile = JsonTools.readJsonFile(path);
            JSONObject jsonObj = JSON.parseObject(readJsonFile);
            int spit = 0;
            try {
                JSONArray resultArr = jsonObj.getJSONObject("result").getJSONArray("sentence_list");
                for (int i = 0; i < resultArr.size(); i++) {
                    bfText.append(((JSONObject) resultArr.get(i)).get("text"));
                    if (++spit < resultArr.size()) {
                        bfText.append("");
                    }
                }
            } catch (Exception e) {
                logger.error(">>> [解析]出错，请检查文件 ：" + path);
            }
            IntelligentAsrEntity result = new IntelligentAsrEntity();
            result.setJsonparseStatus(Constant.ASRPARSER_SUCCESS);
            result.setJsonparseResult(bfText.toString());
            intelligentAsrMapper.update(result, new QueryWrapper<IntelligentAsrEntity>().eq("CALLID", intelligentAsrEntity.getCallid()));
        } catch (Exception e) {
            IntelligentAsrEntity result = new IntelligentAsrEntity();
            result.setJsonparseStatus(Constant.ASRPARSER_FAIL);
            intelligentAsrMapper.update(result, new QueryWrapper<IntelligentAsrEntity>().eq("CALLID", intelligentAsrEntity.getCallid()));
        }
    }

    @Override
    public List<IntelligentAsrEntity> queryWaitingSendJt(int num) {
        return asrJsonParseMapper.queryWaitingSendJt(num);
    }

    @Override
    public List<QuarkCallbackEntity> queryWaitingSendKd(int num) {
        return asrJsonParseMapper.queryWaitingSendKd(num);
    }

    @Override
    public List<AliAsrEntity> queryWaitingSendAl(int num) {
        return asrJsonParseMapper.queryWaitingSendAl(num);
    }

    @Override
    public List<TxAsrEntity> queryWaitingSendTx(int num) {
        return asrJsonParseMapper.queryWaitingSendTx(num);
    }
}
