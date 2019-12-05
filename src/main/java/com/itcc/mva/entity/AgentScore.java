package com.itcc.mva.entity;

import com.baomidou.mybatisplus.annotation.TableField;
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

    @ApiModelProperty(value = "录音编号")
    @TableField(value = "RECORD_PID")
    private String recordPid;

    @ApiModelProperty(value = "工单编号")
    @TableField(value = "WORKORDER_PID")
    private String workOrderPid;

    @ApiModelProperty(value = "组织编号")
    @TableField(value = "DEPARTMENT_PID")
    private String departmentPid;

    @ApiModelProperty(value = "坐席编号")
    @TableField(value = "AGENT_ID")
    private String agentId;

    @ApiModelProperty(value = "总得分")
    @TableField(value = "TOTAL_SCORE")
    private Integer totalScord;

    @ApiModelProperty(value = "申诉状态  0 未申诉  1 已提交申诉  2 申诉通过  3 申诉驳回")
    @TableField(value = "APPEAL_STATE")
    private String appealState;

}
