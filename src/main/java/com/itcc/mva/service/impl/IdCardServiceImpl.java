package com.itcc.mva.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.itcc.mva.feign.IntelligentAsrFeign;
import com.itcc.mva.mapper.IntelligentAsrMapper;
import com.itcc.mva.service.IIntelligentTransferService;
import com.itcc.mva.service.IdCardService;
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
public class IdCardServiceImpl implements IdCardService {
    private Logger log = LoggerFactory.getLogger(this.getClass());

}
