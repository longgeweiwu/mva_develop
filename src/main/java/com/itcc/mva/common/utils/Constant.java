package com.itcc.mva.common.utils;

/**
 * Title. 常量类 <br>
 * Description. 常量类
 *
 * <p>
 * Author: LCL
 * <p>
 * Version: 1.0
 * <p>
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
    public static final int lockAtMostForTime = 1000;
    /**
     *Lastly, you can set lockAtLeastFor attribute which specifies minimum amount of time for which the lock should be kept.
     *  Its main purpose is to prevent execution from multiple nodes in case of really short tasks and clock difference between the nodes.
     *  lockAtLeastFor：锁的最小时间单位为毫秒
     */
    public static final int lockAtLeastForTime = 1000;
}
