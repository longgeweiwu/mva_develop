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

    public static final int NO_PARSER = 25; //解析数量
}
