package com.itcc.mva.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.itcc.mva.common.utils.*;
import com.itcc.mva.entity.*;
import com.itcc.mva.mapper.*;
import com.itcc.mva.service.IPushToMvaService;
import com.itcc.mva.vo.MvaOutVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
/**
 * @author whoami
 */
@Service
public class PushToMvaServiceImpl implements IPushToMvaService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private PushToMvaMapper pushToMvaMapper;

    @Autowired
    private IntelligentAsrMapper intelligentAsrMapper;

    @Autowired
    private QuarkCallbackMapper quarkCallbackMapper;

    @Autowired
    private IdCardMapper idCardMapper;

    @Autowired
    private AliTransferMapper aliTransferMapper;

    @Autowired
    private TxMapper txMapper;

    @Override
    public void singleSendToMvaServiceJt(IntelligentAsrEntity intelligentAsrEntity) {
        /**
         * 查询callid对应信息存储
         */
        MvaOutVo mvaOutVo = pushToMvaMapper.queryByCallid(intelligentAsrEntity.getCallid());
        if (null != mvaOutVo) {
            if (Tools.isLegal(mvaOutVo.getId())) {
                Map<String, Object> headers = new HashMap<String, Object>();
                headers.put("Content-Type", "application/x-www-form-urlencoded");

                Map<String, Object> postparams = GenSign.getValidSign(mvaOutVo.getId());

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("extIdcard", mvaOutVo.getId());//身份证号码
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
                jsonObject.put("extMobileOne", mvaOutVo.getPhoneno());
                jsonObject.put("regMainAppealOne", mvaOutVo.getQuestionType());
                String mvaAdd = getJudgeMvaId(mvaOutVo.getId());
                if (null != mvaAdd) {
                    jsonObject.put("extDomicileAddress", mvaAdd);//户籍地址
                } else {
                    String code = mvaOutVo.getId().substring(0, 6);
                    IdCardEntity fCode = idCardMapper.selectOne(new QueryWrapper<IdCardEntity>().eq("F_CODE", code));
                    if (null != fCode) {
                        jsonObject.put("extDomicileAddress", fCode.getFProvince() + fCode.getFCity() + fCode.getFArea());//户籍地址
                    }
                }
                if (jsonObject.containsKey("extDomicileAddress")) {
                    jsonObject.put("acceptItem", "");//问题属地（行政区划码）这行为空
                    jsonObject.put("regAppealContent", intelligentAsrEntity.getJsonparseResult());//主要述求详情
                    //录音路径还有问题，需要注意
                    jsonObject.put("regRecordFileUri", Constant.RECORDURL + intelligentAsrEntity.getFullPath().split("/")[3] + "/" + intelligentAsrEntity.getVoiceFileName());//录音文件地址
                    Map<String, Object> validSign = GenSign.getPushValidSign(jsonObject.toJSONString());
                    postparams.put("data", jsonObject.toJSONString());
                    postparams.put("sign", validSign.get("sign"));
                    postparams.put("t", validSign.get("t"));
                    logger.info(">>> 推送准备 请求时候的参数为 [URL]:" + Constant.MVAURL + " [params data]:" + postparams.get("data") + " [params sign]:" + postparams.get("sign") + " [params t]:" + postparams.get("t"));

                    String resultPost = HttpUtil.httpPost(Constant.MVAURL, headers, null, postparams, Constant.HTTP_TIMEOUT, false);
                    /**
                     * 这块做逻辑处理，失败啥的等等吧。暂时按照文档写
                     */
                    if (null != resultPost && Tools.isJSONValid(resultPost)) {
                        JSONObject httpResult = JSON.parseObject(resultPost);
                        if (1 == httpResult.getInteger("code")) {
                            logger.info(">>> 推送成功 请求时候的参数为 [URL]:" + Constant.MVAURL + " [params data]:" + postparams.get("data") + " [params sign]:" + postparams.get("sign") + " [params t]:" + postparams.get("t"));
                            //只有等于1 的时候说明推送成功
                            IntelligentAsrEntity result = new IntelligentAsrEntity();
                            result.setIssubmit(Constant.SEND_SUCCESS);
                            intelligentAsrMapper.update(result, new QueryWrapper<IntelligentAsrEntity>().eq("CALLID", intelligentAsrEntity.getCallid()));
                        } else {
                            logger.info(">>> 推送成功 请求时候的参数为 [URL]:" + Constant.MVAURL + " [params data]:" + postparams.get("data") + " [params sign]:" + postparams.get("sign") + " [params t]:" + postparams.get("t") + "接口返回参数为： " + httpResult);
                            //只有等于1 的时候说明推送成功
                            IntelligentAsrEntity result = new IntelligentAsrEntity();
                            result.setIssubmit(Constant.SEND_NOTEXIST);
                            intelligentAsrMapper.update(result, new QueryWrapper<IntelligentAsrEntity>().eq("CALLID", intelligentAsrEntity.getCallid()));
                        }
                    } else {
                        logger.info(">>> 推送失败 请求时候的参数为 [URL]:" + Constant.MVAURL + " [params data]:" + postparams.get("data") + " [params sign]:" + postparams.get("sign") + " [params t]:" + postparams.get("t"));
                        //等于空说明推送失败
                        IntelligentAsrEntity result = new IntelligentAsrEntity();
                        result.setIssubmit(Constant.SEND_FAIL);
                        intelligentAsrMapper.update(result, new QueryWrapper<IntelligentAsrEntity>().eq("CALLID", intelligentAsrEntity.getCallid()));
                    }
                } else {
                    logger.info(">>> 推送失败 请求时候的主要因为身份证参数 : " + mvaOutVo.getId());
                    //等于空说明推送失败
                    IntelligentAsrEntity result = new IntelligentAsrEntity();
                    result.setIssubmit(Constant.ID_SEND_FAIL);
                    intelligentAsrMapper.update(result, new QueryWrapper<IntelligentAsrEntity>().eq("CALLID", intelligentAsrEntity.getCallid()));
                }
            } else {
                logger.info(">>> 推送失败 请求时候的主要因为身份证是非法的 : " + mvaOutVo.getId());
                //等于空说明推送失败
                IntelligentAsrEntity result = new IntelligentAsrEntity();
                result.setIssubmit(Constant.ID_ILLEGAL_FAIL);
                intelligentAsrMapper.update(result, new QueryWrapper<IntelligentAsrEntity>().eq("CALLID", intelligentAsrEntity.getCallid()));
            }
        } else {
            logger.info(">>> 推送失败 请求时候的主要因为在ICD数据库没有找到相关要素信息");
            //等于空说明推送失败
            IntelligentAsrEntity result = new IntelligentAsrEntity();
            result.setIssubmit(Constant.ICD_INFO_FAIL);
            intelligentAsrMapper.update(result, new QueryWrapper<IntelligentAsrEntity>().eq("CALLID", intelligentAsrEntity.getCallid()));
        }
    }

    @Override
    public void singleSendToMvaServiceKd(QuarkCallbackEntity quarkCallbackEntity) {
        /**
         * 查询callid对应信息存储
         */
        MvaOutVo mvaOutVo = pushToMvaMapper.queryByCallid(quarkCallbackEntity.getCallid());
        if (null != mvaOutVo) {
            if (Tools.isLegal(mvaOutVo.getId())) {

                Map<String, Object> headers = new HashMap<String, Object>();
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                Map<String, Object> postparams = GenSign.getValidSign(mvaOutVo.getId());

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("extIdcard", mvaOutVo.getId());//身份证号码
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
                String extNumber=mvaOutVo.getPhoneno();
                if(!(extNumber.indexOf("0")==0) && !(extNumber.indexOf("1")==0)){
                    extNumber="010-"+extNumber;
                    logger.info("01开头号码："+ extNumber);
                }else if(extNumber.indexOf("02")==0){
                    extNumber=extNumber.substring(0, 3)+"-"+extNumber.substring(3);
                    logger.info("02开头号码:"+extNumber);
                }else if(!(extNumber.indexOf("02")==0)&&!(extNumber.indexOf("01")==0)&&!(extNumber.indexOf("1")==0)){
                    extNumber=extNumber.substring(0, 4)+"-"+extNumber.substring(4);
                    logger.info("非02和01:"+extNumber);
                }else {
                    logger.info("手机号:"+extNumber);
                }

                jsonObject.put("extMobileOne", extNumber);
                jsonObject.put("regMainAppealOne", mvaOutVo.getQuestionType());
                String mvaAdd = getJudgeMvaId(mvaOutVo.getId());
                if (null != mvaAdd) {
                    jsonObject.put("extDomicileAddress", mvaAdd);//户籍地址
                } else {
                    String code = mvaOutVo.getId().substring(0, 6);
                    IdCardEntity fCode = idCardMapper.selectOne(new QueryWrapper<IdCardEntity>().eq("F_CODE", code));
                    if (null != fCode) {
                        jsonObject.put("extDomicileAddress", fCode.getFProvince() + fCode.getFCity() + fCode.getFArea());//户籍地址
                    }
                }
                if (jsonObject.containsKey("extDomicileAddress")) {
                    jsonObject.put("acceptItem", "");//问题属地（行政区划码）这行为空
                    jsonObject.put("regAppealContent", quarkCallbackEntity.getIflyResult());//主要述求详情
                    //录音路径还有问题，需要注意
                    jsonObject.put("regRecordFileUri", Constant.RECORDURL + quarkCallbackEntity.getFullPath().split("/")[3] + "/" + quarkCallbackEntity.getVoiceFileName());//录音文件地址


                    Map<String, Object> validSign = GenSign.getPushValidSign(jsonObject.toJSONString());
                    postparams.put("data", AESEncryptUtils.encrypt(jsonObject.toJSONString()));
                    postparams.put("sign", validSign.get("sign"));
                    postparams.put("t", validSign.get("t"));
                    logger.info(">>> 推送准备 请求时候的参数为 [URL]:" + Constant.MVAURL + " [params data]:" + postparams.get("data") + " [params sign]:" + postparams.get("sign") + " [params t]:" + postparams.get("t"));

                    String resultPost = HttpUtil.httpPost(Constant.MVAURL, headers, null, postparams, Constant.HTTP_TIMEOUT, false);
                    /**
                     * 这块做逻辑处理，失败啥的等等吧。暂时按照文档写
                     */
                    if (null != resultPost && Tools.isJSONValid(resultPost)) {
                        JSONObject httpResult = JSON.parseObject(resultPost);
                        if (1 == httpResult.getInteger("code")) {
                            logger.info(">>> 推送成功 请求时候的参数为 [URL]:" + Constant.MVAURL + " [params data]:" + postparams.get("data") + " [params sign]:" + postparams.get("sign") + " [params t]:" + postparams.get("t"));
                            //只有等于1 的时候说明推送成功
                            QuarkCallbackEntity result = new QuarkCallbackEntity();
                            result.setIssubmit(Constant.SEND_SUCCESS);
                            quarkCallbackMapper.update(result, new QueryWrapper<QuarkCallbackEntity>().eq("CALLID", quarkCallbackEntity.getCallid()));
                        } else {
                            logger.info(">>> 推送成功 请求时候的参数为 [URL]:" + Constant.MVAURL + " [params data]:" + postparams.get("data") + " [params sign]:" + postparams.get("sign") + " [params t]:" + postparams.get("t") + "接口返回参数为： " + httpResult);
                            //只有等于1 的时候说明推送成功
                            QuarkCallbackEntity result = new QuarkCallbackEntity();
                            result.setIssubmit(Constant.SEND_NOTEXIST);
                            quarkCallbackMapper.update(result, new QueryWrapper<QuarkCallbackEntity>().eq("CALLID", quarkCallbackEntity.getCallid()));
                        }
                    } else {
                        logger.info(">>> 推送失败 请求时候的参数为 [URL]:" + Constant.MVAURL + " [params data]:" + postparams.get("data") + " [params sign]:" + postparams.get("sign") + " [params t]:" + postparams.get("t"));
                        //等于空说明推送失败
                        QuarkCallbackEntity result = new QuarkCallbackEntity();
                        result.setIssubmit(Constant.SEND_FAIL);
                        quarkCallbackMapper.update(result, new QueryWrapper<QuarkCallbackEntity>().eq("CALLID", quarkCallbackEntity.getCallid()));
                    }
                } else {
                    logger.info(">>> 推送失败 请求时候的主要因为身份证参数 : " + mvaOutVo.getId());
                    //等于空说明推送失败
                    QuarkCallbackEntity result = new QuarkCallbackEntity();
                    result.setIssubmit(Constant.ID_SEND_FAIL);
                    quarkCallbackMapper.update(result, new QueryWrapper<QuarkCallbackEntity>().eq("CALLID", quarkCallbackEntity.getCallid()));
                }
            } else {
                logger.info(">>> 推送失败 请求时候的主要因为身份证是非法的 : " + mvaOutVo.getId());
                //等于空说明推送失败
                QuarkCallbackEntity result = new QuarkCallbackEntity();
                result.setIssubmit(Constant.ID_ILLEGAL_FAIL);
                quarkCallbackMapper.update(result, new QueryWrapper<QuarkCallbackEntity>().eq("CALLID", quarkCallbackEntity.getCallid()));
            }
        } else {
            logger.info(">>> 推送失败 请求时候的主要因为在ICD数据库没有找到相关要素信息");
            //等于空说明推送失败
            QuarkCallbackEntity result = new QuarkCallbackEntity();
            result.setIssubmit(Constant.ICD_INFO_FAIL);
            quarkCallbackMapper.update(result, new QueryWrapper<QuarkCallbackEntity>().eq("CALLID", quarkCallbackEntity.getCallid()));
        }
    }

    @Override
    public void singleSendToMvaServiceAl(AliAsrEntity aliAsrEntity) {
        /**
         * 查询callid对应信息存储
         */
        MvaOutVo mvaOutVo = pushToMvaMapper.queryByCallid(aliAsrEntity.getCallid());
        if (null != mvaOutVo) {
            if (Tools.isLegal(mvaOutVo.getId())) {
                Map<String, Object> headers = new HashMap<String, Object>();
                headers.put("Content-Type", "application/x-www-form-urlencoded");

                Map<String, Object> postparams = GenSign.getValidSign(mvaOutVo.getId());

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("extIdcard", mvaOutVo.getId());//身份证号码
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
                jsonObject.put("extMobileOne", mvaOutVo.getPhoneno());
                jsonObject.put("regMainAppealOne", mvaOutVo.getQuestionType());
                String mvaAdd = getJudgeMvaId(mvaOutVo.getId());
                if (null != mvaAdd) {
                    jsonObject.put("extDomicileAddress", mvaAdd);//户籍地址
                } else {
                    String code = mvaOutVo.getId().substring(0, 6);
                    IdCardEntity fCode = idCardMapper.selectOne(new QueryWrapper<IdCardEntity>().eq("F_CODE", code));
                    if (null != fCode) {
                        jsonObject.put("extDomicileAddress", fCode.getFProvince() + fCode.getFCity() + fCode.getFArea());//户籍地址
                    }
                }
                if (jsonObject.containsKey("extDomicileAddress")) {
                    jsonObject.put("acceptItem", "");//问题属地（行政区划码）这行为空
                    jsonObject.put("regAppealContent", aliAsrEntity.getAliResult());//主要述求详情
                    //录音路径还有问题，需要注意
                    jsonObject.put("regRecordFileUri", Constant.RECORDURL + aliAsrEntity.getFullPath().split("/")[3] + "/" + aliAsrEntity.getVoiceFileName());//录音文件地址
                    Map<String, Object> validSign = GenSign.getPushValidSign(jsonObject.toJSONString());
                    postparams.put("data", jsonObject.toJSONString());
                    postparams.put("sign", validSign.get("sign"));
                    postparams.put("t", validSign.get("t"));
                    logger.info(">>> 推送准备 请求时候的参数为 [URL]:" + Constant.MVAURL + " [params data]:" + postparams.get("data") + " [params sign]:" + postparams.get("sign") + " [params t]:" + postparams.get("t"));

                    String resultPost = HttpUtil.httpPost(Constant.MVAURL, headers, null, postparams, Constant.HTTP_TIMEOUT, false);
                    /**
                     * 这块做逻辑处理，失败啥的等等吧。暂时按照文档写
                     */
                    if (null != resultPost && Tools.isJSONValid(resultPost)) {
                        JSONObject httpResult = JSON.parseObject(resultPost);
                        if (1 == httpResult.getInteger("code")) {
                            logger.info(">>> 推送成功 请求时候的参数为 [URL]:" + Constant.MVAURL + " [params data]:" + postparams.get("data") + " [params sign]:" + postparams.get("sign") + " [params t]:" + postparams.get("t"));
                            //只有等于1 的时候说明推送成功
                            AliAsrEntity result = new AliAsrEntity();
                            result.setIssubmit(Constant.SEND_SUCCESS);
                            aliTransferMapper.update(result, new QueryWrapper<AliAsrEntity>().eq("CALLID", aliAsrEntity.getCallid()));
                        } else {
                            logger.info(">>> 推送成功 请求时候的参数为 [URL]:" + Constant.MVAURL + " [params data]:" + postparams.get("data") + " [params sign]:" + postparams.get("sign") + " [params t]:" + postparams.get("t") + "接口返回参数为： " + httpResult);
                            //只有等于1 的时候说明推送成功
                            AliAsrEntity result = new AliAsrEntity();
                            result.setIssubmit(Constant.SEND_NOTEXIST);
                            aliTransferMapper.update(result, new QueryWrapper<AliAsrEntity>().eq("CALLID", aliAsrEntity.getCallid()));
                        }
                    } else {
                        logger.info(">>> 推送失败 请求时候的参数为 [URL]:" + Constant.MVAURL + " [params data]:" + postparams.get("data") + " [params sign]:" + postparams.get("sign") + " [params t]:" + postparams.get("t"));
                        //等于空说明推送失败
                        AliAsrEntity result = new AliAsrEntity();
                        result.setIssubmit(Constant.SEND_FAIL);
                        aliTransferMapper.update(result, new QueryWrapper<AliAsrEntity>().eq("CALLID", aliAsrEntity.getCallid()));
                    }
                } else {
                    logger.info(">>> 推送失败 请求时候的主要因为身份证参数 : " + mvaOutVo.getId());
                    //等于空说明推送失败
                    AliAsrEntity result = new AliAsrEntity();
                    result.setIssubmit(Constant.ID_SEND_FAIL);
                    aliTransferMapper.update(result, new QueryWrapper<AliAsrEntity>().eq("CALLID", aliAsrEntity.getCallid()));
                }
            } else {
                logger.info(">>> 推送失败 请求时候的主要因为身份证是非法的 : " + mvaOutVo.getId());
                //等于空说明推送失败
                AliAsrEntity result = new AliAsrEntity();
                result.setIssubmit(Constant.ID_ILLEGAL_FAIL);
                aliTransferMapper.update(result, new QueryWrapper<AliAsrEntity>().eq("CALLID", aliAsrEntity.getCallid()));
            }
        } else {
            logger.info(">>> 推送失败 请求时候的主要因为在ICD数据库没有找到相关要素信息");
            //等于空说明推送失败
            AliAsrEntity result = new AliAsrEntity();
            result.setIssubmit(Constant.ICD_INFO_FAIL);
            aliTransferMapper.update(result, new QueryWrapper<AliAsrEntity>().eq("CALLID", aliAsrEntity.getCallid()));
        }
    }

    @Override
    public void singleSendToMvaServiceTx(TxAsrEntity txAsrEntity) {
        /**
         * 查询callid对应信息存储
         */
        MvaOutVo mvaOutVo = pushToMvaMapper.queryByCallid(txAsrEntity.getCallid());
        if (null != mvaOutVo) {
            if (Tools.isLegal(mvaOutVo.getId())) {
                Map<String, Object> headers = new HashMap<String, Object>();
                headers.put("Content-Type", "application/x-www-form-urlencoded");

                Map<String, Object> postparams = GenSign.getValidSign(mvaOutVo.getId());

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("extIdcard", mvaOutVo.getId());//身份证号码
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
                jsonObject.put("extMobileOne", mvaOutVo.getPhoneno());
                jsonObject.put("regMainAppealOne", mvaOutVo.getQuestionType());
                String mvaAdd = getJudgeMvaId(mvaOutVo.getId());
                if (null != mvaAdd) {
                    jsonObject.put("extDomicileAddress", mvaAdd);//户籍地址
                } else {
                    String code = mvaOutVo.getId().substring(0, 6);
                    IdCardEntity fCode = idCardMapper.selectOne(new QueryWrapper<IdCardEntity>().eq("F_CODE", code));
                    if (null != fCode) {
                        jsonObject.put("extDomicileAddress", fCode.getFProvince() + fCode.getFCity() + fCode.getFArea());//户籍地址
                    }
                }
                if (jsonObject.containsKey("extDomicileAddress")) {
                    jsonObject.put("acceptItem", "");//问题属地（行政区划码）这行为空
                    jsonObject.put("regAppealContent", txAsrEntity.getTxResult());//主要述求详情
                    //录音路径还有问题，需要注意
                    jsonObject.put("regRecordFileUri", Constant.RECORDURL + txAsrEntity.getFullPath().split("/")[3] + "/" + txAsrEntity.getVoiceFileName());//录音文件地址
                    Map<String, Object> validSign = GenSign.getPushValidSign(jsonObject.toJSONString());
                    postparams.put("data", jsonObject.toJSONString());
                    postparams.put("sign", validSign.get("sign"));
                    postparams.put("t", validSign.get("t"));
                    logger.info(">>> 推送准备 请求时候的参数为 [URL]:" + Constant.MVAURL + " [params data]:" + postparams.get("data") + " [params sign]:" + postparams.get("sign") + " [params t]:" + postparams.get("t"));

                    String resultPost = HttpUtil.httpPost(Constant.MVAURL, headers, null, postparams, Constant.HTTP_TIMEOUT, false);
                    /**
                     * 这块做逻辑处理，失败啥的等等吧。暂时按照文档写
                     */
                    if (null != resultPost && Tools.isJSONValid(resultPost)) {
                        JSONObject httpResult = JSON.parseObject(resultPost);
                        if (1 == httpResult.getInteger("code")) {
                            logger.info(">>> 推送成功 请求时候的参数为 [URL]:" + Constant.MVAURL + " [params data]:" + postparams.get("data") + " [params sign]:" + postparams.get("sign") + " [params t]:" + postparams.get("t"));
                            //只有等于1 的时候说明推送成功
                            TxAsrEntity result = new TxAsrEntity();
                            result.setIssubmit(Constant.SEND_SUCCESS);
                            txMapper.update(result, new QueryWrapper<TxAsrEntity>().eq("CALLID", txAsrEntity.getCallid()));
                        } else {
                            logger.info(">>> 推送成功 请求时候的参数为 [URL]:" + Constant.MVAURL + " [params data]:" + postparams.get("data") + " [params sign]:" + postparams.get("sign") + " [params t]:" + postparams.get("t") + "接口返回参数为： " + httpResult);
                            //只有等于1 的时候说明推送成功
                            TxAsrEntity result = new TxAsrEntity();
                            result.setIssubmit(Constant.SEND_NOTEXIST);
                            txMapper.update(result, new QueryWrapper<TxAsrEntity>().eq("CALLID", txAsrEntity.getCallid()));
                        }
                    } else {
                        logger.info(">>> 推送失败 请求时候的参数为 [URL]:" + Constant.MVAURL + " [params data]:" + postparams.get("data") + " [params sign]:" + postparams.get("sign") + " [params t]:" + postparams.get("t"));
                        //等于空说明推送失败
                        TxAsrEntity result = new TxAsrEntity();
                        result.setIssubmit(Constant.SEND_FAIL);
                        txMapper.update(result, new QueryWrapper<TxAsrEntity>().eq("CALLID", txAsrEntity.getCallid()));
                    }
                } else {
                    logger.info(">>> 推送失败 请求时候的主要因为身份证参数 : " + mvaOutVo.getId());
                    //等于空说明推送失败
                    TxAsrEntity result = new TxAsrEntity();
                    result.setIssubmit(Constant.ID_SEND_FAIL);
                    txMapper.update(result, new QueryWrapper<TxAsrEntity>().eq("CALLID", txAsrEntity.getCallid()));
                }
            } else {
                logger.info(">>> 推送失败 请求时候的主要因为身份证是非法的 : " + mvaOutVo.getId());
                //等于空说明推送失败
                TxAsrEntity result = new TxAsrEntity();
                result.setIssubmit(Constant.ID_ILLEGAL_FAIL);
                txMapper.update(result, new QueryWrapper<TxAsrEntity>().eq("CALLID", txAsrEntity.getCallid()));
            }
        } else {
            logger.info(">>> 推送失败 请求时候的主要因为在ICD数据库没有找到相关要素信息");
            //等于空说明推送失败
            TxAsrEntity result = new TxAsrEntity();
            result.setIssubmit(Constant.ICD_INFO_FAIL);
            txMapper.update(result, new QueryWrapper<TxAsrEntity>().eq("CALLID", txAsrEntity.getCallid()));
        }
    }

    @Override
    public String illegalId(String id) {
        logger.info(">>> 调用API查询身份证号是否在MVA的数据库里，此时身份号码为: " + id);
        String result = getJudgeMvaId(id);
        if (null != result) {
            return formVxml("1");
        } else {
            return formVxml("0");
        }
    }

    private String formVxml(String num) {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\" ?> \n" +
                "<vxml version=\"1.0\">\n" +
                "<var name=\"ILLEGA\" expr=\"" + num + "\" /> \n" +
                "\t<form>\n" +
                "\t\t<block>\n" +
                "\t\t\t<return namelist=\"ILLEGA\" /> \n" +
                "\t\t</block>\n" +
                "\t</form>\n" +
                "</vxml>";
    }

    /**
     * 通过ID查询户籍归属地
     *
     * @param id
     * @return 默认是NUll
     */
    public String getJudgeMvaId(String id) {
        if (id.length() > 14) {
            Map<String, Object> getParams = GenSign.getValidSign(id);
            Map<String, Object> validSign = GenSign.getValidSign(id);
            getParams.put("idCard", id);
            getParams.put("sign", validSign.get("sign"));
            getParams.put("t", validSign.get("t"));
            logger.info(">>> 身份接口准备 请求时候的参数为 [URL]:" + Constant.IDURL + " [params data]:" + getParams.get("idCard") + " [params sign]:" + getParams.get("sign") + " [params t]:" + getParams.get("t"));
            try {
                String resultGet = HttpUtil.get(Constant.IDURL, getParams);
                if (null != resultGet && Tools.isJSONValid(resultGet)) {
                    JSONObject httpResult = JSON.parseObject(resultGet);
                    if (1 == httpResult.getInteger("code")) {
                        String add = httpResult.getJSONObject("data").getString("extDomicileAddress");
                        if (add.contains("区")) {
                            return add.substring(0, add.lastIndexOf("区") + 1);
                        } else {
                            return add;
                        }
                    } else {
                        return null;
                    }
                } else {
                    return null;
                }
            }catch(Exception e){
                logger.info(">>> 身份接口异常有可能是网络问题: " + e.getMessage());
                return null;
            }
        } else {
            logger.info(">>> 身份接口异常 [身份证长度不足15位] 身份证号码为: " + id);
            return null;
        }
    }

}
