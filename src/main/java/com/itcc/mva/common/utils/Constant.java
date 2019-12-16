package com.itcc.mva.common.utils;

/**
 * 根据github项目中，您必须将lockAtMostFor设置为比正常执行时间长得多的值。这个时间小于轮训时间
 * 否则会出问题
 */
public class Constant {
    /**
     * 部委来电信访登记
     */
    public static final String MVAURL = "http://wsxf.mva.gov.cn:8090/letter_test/service/letterPhoneRegister/incomingTelReg";
    /**
     * 部委信息采集身份核验
     */
    public static final String IDURL = "http://wsxf.mva.gov.cn:8090/letter_test/service/letterPhoneRegister/identityVerify";


    /**
     * 引擎选择
     */
    public static final int ENGINETYPE_JT = 1; //捷通
    public static final int ENGINETYPE_KD = 1; //科大
    public static final int ENGINETYPE_AL = 1; //阿里

    public static final int HTTP_TIMEOUT=60000;//默认超时时间

    public static final int NO_RMA_IFLY = 25; //科大讯飞解析数量
    public static final int NO_PARSER_IFLY = 25; //科大讯飞解析数量
    public static final int NO_PARSER = 25; //捷通解析数量

    public static final int ASRPARSER_SUCCESS = 1; //捷通成功
    public static final int ASRPARSER_FAIL = 2; //捷通失败
    public static final int ASRPARSER_IFLY_SUCCESS = 1; //科大成功
    public static final int ASRPARSER_IFLY_FAIL = 2; //科大失败


    public static final int NO_SENDER = 25; //部委发送数量
    public static final int SEND_SUCCESS = 1; //部委成功
    public static final int SEND_FAIL = 2; //部委失败

    //转科大写音频回调地址
    public static String NOTIFYURL="http://192.168.102.115:52113/quark/callback";
    //科大离线转写引擎服务器地址
    public static String URL="http://172.16.12.231:9505/quark_procer";
    //科大待转写音频下载地址
    public static String AUDIO="http://172.16.12.178:52111/";

    //转码文件存储地址
    public static final  String RMAPATH = "D:\\AAA\\voice-engine-quark-javademo\\src\\main\\resources\\static\\RM";
    //转写音频回调地址
    public static String RMANOTIFYURL="http://192.168.102.115:52113/rma/callback";
    //离线转写引擎服务器地址
    public static String RMAURL="http://172.16.12.231:9505/rma_procer";
    //转码结果文件上传地址
    public static final String UPLOADFILE = "http://192.168.102.115:52113/rma/upload";
    public static final int RMA_IFLY_SUCCESS = 1; //科大转码成功
    public static final int RMA_IFLY_FAIL = 2; //科大转码失败
}
