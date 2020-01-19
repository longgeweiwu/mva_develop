package com.itcc.mva.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @author whoami
 */
@Data
@TableName(value="MVA_TX_INTELLIGENT_ASR")
public class TxAsrEntity {

    @TableId(value = "PID")
    private String pid;
    @TableField(value = "LOG_ID")
    private String logId;
    @TableField(value = "LEAV_WORD_TIME")
    private String leavWordTime;
    @TableField(value = "ANI")
    private String ani;
    @TableField(value = "LEAVEWORD_PATH")
    private String leaveWordpath;
    @TableField(value = "VOICE_FILE_NAME")
    private String voiceFileName;
    @TableField(value = "CALLID")
    private String callid;
    @TableField(value = "FULL_PATH")
    private String fullPath;
    @TableField(value = "TXFILEFLAG")
    private Integer txfileflag;
    @TableField(value = "ASRFLAG")
    private Integer asrflag;
    @TableField(value = "TASKID")
    private String taskid;
    @TableField(value = "TX_RESULT")
    private String txResult;
    @TableField(value = "INSERT_TIME")
    private Date insertTime;
    @TableField(value = "TXPARSE_STATUS")
    private Integer txparseStatus;
    @TableField(value = "ISSUBMIT")
    private Integer issubmit;

}
