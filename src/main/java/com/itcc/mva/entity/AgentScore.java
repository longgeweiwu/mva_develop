package com.itcc.mva.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@TableName(value="T_QC_AGENTSCORE")
public class AgentScore {

    @ApiModelProperty(value = "id")
    @TableId(value = "PID")
    private String pid;

}
