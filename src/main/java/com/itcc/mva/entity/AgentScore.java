package com.itcc.mva.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value="T_QC_AGENTSCORE")
public class AgentScore {

    @TableId(value = "PID")
    private String pid;

}
