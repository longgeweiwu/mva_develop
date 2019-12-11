package com.itcc.mva.service.impl;

import com.itcc.mva.entity.IntelligentAsrEntity;
import com.itcc.mva.mapper.IntelligentAsrMapper;
import com.itcc.mva.service.IAsrJsonParseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AsrJsonParseServiceImpl implements IAsrJsonParseService {

    @Autowired
    private IntelligentAsrMapper intelligentAsrMapper;

    @Override
    public List<IntelligentAsrEntity> queryPendingTop(int top) {
        return intelligentAsrMapper.queryPendingTopMapper(top);
    }
}
