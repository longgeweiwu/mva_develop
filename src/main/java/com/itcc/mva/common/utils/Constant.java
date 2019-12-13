package com.itcc.mva.common.utils;

/**
 * 根据github项目中，您必须将lockAtMostFor设置为比正常执行时间长得多的值。这个时间小于轮训时间
 * 否则会出问题
 */
public class Constant {
    /**
     *You can also set lockAtMostFor attribute which specifies how long the lock should be kept in case the executing node dies.
     *  This is just a fallback, under normal circumstances the lock is released as soon the tasks finishes.
     *  You have to set lockAtMostFor to a value which is much longer than normal execution time.
     *  If the task takes longer than lockAtMostFor the resulting behavior may be unpredictable
     *  (more then one process will effectively hold the lock).
     *  lockAtMostFor：锁的最大时间单位为毫秒
     */
    public static final String lockAtMostForTime = "1s";
    /**
     *Lastly, you can set lockAtLeastFor attribute which specifies minimum amount of time for which the lock should be kept.
     *  Its main purpose is to prevent execution from multiple nodes in case of really short tasks and clock difference between the nodes.
     *  lockAtLeastFor：锁的最小时间单位为毫秒
     */
    public static final String lockAtLeastForTime = "1s";

    public static final int HTTP_TIMEOUT=60000;//默认超时时间

    public static final int NO_PARSER_IFLY = 25; //解析数量
    public static final int ASRPARSER_IFLY_SUCCESS = 1; //成功
    public static final int ASRPARSER_IFLY_FAIL = 2; //失败


    public static final int NO_PARSER = 25; //解析数量
    public static final int NO_SENDER = 25; //发送数量

    public static final int ASRPARSER_SUCCESS = 1; //成功
    public static final int ASRPARSER_FAIL = 2; //失败

    public static final int SEND_SUCCESS = 1; //成功
    public static final int SEND_FAIL = 2; //失败

    public static final String TYPE_JSON = "application/json";

    //转写音频回调地址
    public static String NOTIFYURL="http://192.168.102.115:52113/quark/callback";
    //离线转写引擎服务器地址
    public static String URL="http://172.16.12.231:9505/quark_procer";
    //待转写音频下载地址
    public static String AUDIO="http://192.168.102.115:52113/";
}
