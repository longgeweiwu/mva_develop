package com.itcc.mva.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName(value="MVA_IFLY_INTELLIGENT_ASR")
public class QuarkCallbackEntity {

    @TableId(value = "PID")
    private String pid;
    @TableField(value = "LOG_ID")
    private String logId;
    @TableField(value = "LEAV_WORD_TIME")
    private String leavWorktime;
    @TableField(value = "ANI")
    private String ani;
    @TableField(value = "LEAVEWORD_PATH")
    private String leaveWordpath;
    @TableField(value = "VOICE_FILE_NAME")
    private String voiceFilename;
    @TableField(value = "CALLID")
    private String callid;
    @TableField(value = "FULL_PATH")
    private String fullPath;
    @TableField(value = "AID")
    private String aid;
    @TableField(value = "IFLY_RESULT")
    private String iflyResult;
    @TableField(value = "IFLYPARSE_STATUS")
    private Integer iflyparseStatus;
    @TableField(value = "INSERT_TIME")
    private Date insertTime;

}
