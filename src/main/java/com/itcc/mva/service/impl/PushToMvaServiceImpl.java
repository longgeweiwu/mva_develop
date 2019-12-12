package com.itcc.mva.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.itcc.mva.common.utils.Constant;
import com.itcc.mva.common.utils.GenSign;
import com.itcc.mva.common.utils.HttpUtil;
import com.itcc.mva.common.utils.Tools;
import com.itcc.mva.entity.IntelligentAsrEntity;
import com.itcc.mva.mapper.IntelligentAsrMapper;
import com.itcc.mva.mapper.PushToMvaMapper;
import com.itcc.mva.service.IPushToMvaService;
import com.itcc.mva.vo.MvaOutVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class PushToMvaServiceImpl implements IPushToMvaService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final String url = "http://wsxf.mva.gov.cn:8090/letter_test/service/letterPhoneRegister/incomingTelReg";

    private static final String recordUrl="";
    @Autowired
    private PushToMvaMapper pushToMvaMapper;

    @Autowired
    private IntelligentAsrMapper intelligentAsrMapper;

    @Override
    public void singleSendToMvaService(IntelligentAsrEntity intelligentAsrEntity) {
        /**
         * 查询callid对应信息存储
         */
        MvaOutVo mvaOutVo = pushToMvaMapper.queryByCallid(intelligentAsrEntity.getCallid());

        Map<String, Object> headers = new HashMap<String, Object>();
        Map<String, Object> postparams = GenSign.getValidSign();

        JSONObject jsonObject= new JSONObject();
        jsonObject.put("extIdcard",mvaOutVo.getId());//身份证号码
        /**
         * 一级主要述求
         * 1：退役安置类
         * 2：优待抚恤类
         * 3：就业创业类
         * 4：褒扬纪念类
         * 5：军休服务类
         * 6：帮扶援助类
         * 7：党员管理类
         * 8：咨询建议类
         * 9：其他
         *
         * 传 数字
         */
        jsonObject.put("regMainAppealOne",mvaOutVo.getQuestionType());
        jsonObject.put("acceptItem","");//问题属地（行政区划码）这行为空
        jsonObject.put("regAppealContent",intelligentAsrEntity.getJsonparseResult());//主要述求详情
        //录音路径还有问题，需要注意
        jsonObject.put("regRecordFileUri",recordUrl+intelligentAsrEntity.getFullPath().split("/")[3]+intelligentAsrEntity.getVoiceFilename());//录音文件地址


        Map<String, Object> validSign = GenSign.getValidSign();
        postparams.put("data", jsonObject.toJSONString());
        postparams.put("sign", validSign.get("sign"));
        postparams.put("t", validSign.get("t"));
        logger.info(">>> 推送准备 请求时候的参数为 [URL]:"+url+" [params data]:"+postparams.get("data")+" [params sign]:"+postparams.get("sign")+" [params t]:"+postparams.get("t"));

        String resultPost= HttpUtil.httpPost(url, headers, null, postparams, Constant.HTTP_TIMEOUT, false);
        /**
         * 这块做逻辑处理，失败啥的等等吧。暂时按照文档写
         */
        if(null != resultPost && Tools.isJSONValid(resultPost)){
            JSONObject httpResult= JSON.parseObject(resultPost);
            if(1==httpResult.getInteger("code")){
                logger.info(">>> 推送成功 请求时候的参数为 [URL]:"+url+" [params data]:"+postparams.get("data")+" [params sign]:"+postparams.get("sign")+" [params t]:"+postparams.get("t"));
                //只有等于1 的时候说明推送成功
                IntelligentAsrEntity result = new IntelligentAsrEntity();
                result.setJsonparseStatus(Constant.SEND_SUCCESS);
                intelligentAsrMapper.update(result, new QueryWrapper<IntelligentAsrEntity>().eq("CALLID", intelligentAsrEntity.getCallid()));
            }
        }else{
            logger.info(">>> 推送失败 请求时候的参数为 [URL]:"+url+" [params data]:"+postparams.get("data")+" [params sign]:"+postparams.get("sign")+" [params t]:"+postparams.get("t"));
            //等于空说明推送失败
            IntelligentAsrEntity result = new IntelligentAsrEntity();
            result.setJsonparseStatus(Constant.SEND_FAIL);
            intelligentAsrMapper.update(result, new QueryWrapper<IntelligentAsrEntity>().eq("CALLID", intelligentAsrEntity.getCallid()));
        }
    }
}
