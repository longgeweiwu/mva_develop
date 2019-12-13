package com.itcc.mva.entity;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName(value="MVA_IFLY_INTELLIGENT_ASR")
public class QuarkCallbackEntity {

    @TableId(value = "PID")
    private String pid;
    @TableId(value = "CALLID")
    private String callid;
    @TableId(value = "IFLY_RESULT")
    private String iflyResult;
    @TableId(value = "INSERT_TIME")
    private Date insertTime;

}
