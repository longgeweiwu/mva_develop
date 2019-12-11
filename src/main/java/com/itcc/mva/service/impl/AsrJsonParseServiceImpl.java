package com.itcc.mva.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.itcc.mva.common.utils.Constant;
import com.itcc.mva.common.utils.JsonTools;
import com.itcc.mva.entity.IntelligentAsrEntity;
import com.itcc.mva.mapper.AsrJsonParseMapper;
import com.itcc.mva.mapper.IntelligentAsrMapper;
import com.itcc.mva.service.IAsrJsonParseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author whoami
 */
@Service
public class AsrJsonParseServiceImpl implements IAsrJsonParseService {

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
        String path = intelligentAsrEntity.getOutputFilepath()+intelligentAsrEntity.getOutputFilename();
        try {
            String readJsonFile = JsonTools.readJsonFile(path);
            /**
             * 处理内容
             */
            IntelligentAsrEntity result = new IntelligentAsrEntity();
            result.setJsonparseStatus(Constant.ASRPARSER_SUCCESS);
            result.setJsonparseResult("");
            intelligentAsrMapper.update(result, new QueryWrapper<IntelligentAsrEntity>().eq("CALLID", ""));
        }catch(Exception e){
            IntelligentAsrEntity result = new IntelligentAsrEntity();
            result.setJsonparseStatus(Constant.ASRPARSER_FAIL);
            intelligentAsrMapper.update(result, new QueryWrapper<IntelligentAsrEntity>().eq("CALLID", ""));
        }
    }
}
