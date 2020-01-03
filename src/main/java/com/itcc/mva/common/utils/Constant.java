package com.itcc.mva.common.utils;

/**
 * 根据github项目中，您必须将lockAtMostFor设置为比正常执行时间长得多的值。这个时间小于轮训时间
 * 否则会出问题
 */
public class Constant {
    /**
     * 部委来电信访登记
     */
    public static final String MVAURL = "https://wsxf.mva.gov.cn:8090/letter_test/service/letterPhoneRegister/incomingTelReg";
    /**
     * 部委信息采集身份核验
     */
    public static final String IDURL = "https://wsxf.mva.gov.cn:8090/letter_test/service/letterPhoneRegister/identityVerify";

    public static final String RECORDURL="http://172.16.12.178:52111/";
    /**
     * 引擎选择
     */
    public static final int ENGINETYPE_JT = 1; //捷通
    public static final int ENGINETYPE_KD = 1; //科大
    public static final int ENGINETYPE_AL = 1; //阿里

    public static final int HTTP_TIMEOUT=60000;//默认超时时间

    public static final int NO_RMA_IFLY = 25; //科大讯飞解析数量
    public static final int NO_PARSER_IFLY = 25; //科大讯飞解析数量
    public static final int NO_PARSER_ALI = 25; //阿里解析数量
    public static final int NO_PARSER = 25; //捷通解析数量

    public static final int ASRPARSER_SUCCESS = 1; //捷通成功
    public static final int ASRPARSER_FAIL = 2; //捷通失败
    public static final int ASRPARSER_IFLY_SUCCESS = 1; //科大成功
    public static final int ASRPARSER_IFLY_FAIL = 2; //科大失败
    public static final int ASRPARSER_ALI_SUCCESS = 1; //阿里成功
    public static final int ASRPARSER_ALI_FAIL = 2; //阿里失败

    public static final int ASRPARSER_QUERYALI_SUCCESS = 1; //阿里成功
    public static final int ASRPARSER_QUERYRESULTALI_FAIL = 3; //阿里请将如上响应的J SON字符串反馈给对接⼈
    public static final int ASRPARSER_SERIOUS_FAIL = 4; //阿里失败
    public static final int ASRPARSER_EXCEPTION_FAIL = 2; //阿里查询结果为空 可能原因 网络

    public static final int NO_SENDER = 25; //部委发送数量
    public static final int SEND_SUCCESS = 1; //部委成功
    public static final int SEND_FAIL = 2; //部委接口问题

    public static final int SEND_NOTEXIST = 3; //部委返回errorcode=0
    public static final int ID_SEND_FAIL = 4; //身份号找不到归属地，请查验
    public static final int ID_ILLEGAL_FAIL = 5; //身份号不合法
    public static final int ICD_INFO_FAIL = 6; //话务平台成功标志不是1 主要看ICD数据库

    //转科大写音频回调地址
    public static String NOTIFYURL="http://172.16.12.178:52113/quark/callback";
    //科大离线转写引擎服务器地址
    public static String URL="http://172.16.12.231:9505/quark_procer";
    //科大待转写音频下载地址
    public static String AUDIO="http://172.16.12.178:52111/";

    //转码文件存储地址
    public static final  String RMAPATH = "D:\\AAA\\voice-engine-quark-javademo\\src\\main\\resources\\static\\RM";
    //转写音频回调地址
    public static String RMANOTIFYURL="http://172.16.12.178:52113/rma/callback";
    //离线转写引擎服务器地址
    public static String RMAURL="http://172.16.12.231:9505/rma_procer";
    //转码结果文件上传地址
    public static final String UPLOADFILE = "http://172.16.12.178:52113/rma/upload";
    public static final int RMA_IFLY_SUCCESS = 1; //科大转码成功
    public static final int RMA_IFLY_FAIL = 2; //科大转码失败


    /**
     * 阿里常量字段，请勿修改
     */
    public static final String KEY_APPKEY = "appkey";
    public static final String KEY_TOKEN = "token";
    public static final String KEY_FILE_LINK = "file_link";

    public static final String KEY_TASK_ID = "task_id";
    public static final String STATUS_SUCCESS = "SUCCESS";
    public static final String STATUS_RUNNING = "RUNNING";
    public static final String FILE_DOWNLOAD_FAILED = "FILE_DOWNLOAD_FAILED";

    public static final String KEY_ALIASR = "http://172.16.12.233:8101/stream/v1/filetrans";
    public static final String KEY_QUERYALIASR = "http://172.16.12.233:8101/stream/v1/filetrans";
}
