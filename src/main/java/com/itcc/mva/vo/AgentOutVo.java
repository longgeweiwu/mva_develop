package com.itcc.mva.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "员工得分信息")
public class AgentOutVo {
    @ApiModelProperty(value = "录音编号")
    private String recordPid;
    @ApiModelProperty(value = "组织机构")
    private String organization;
    @ApiModelProperty(value = "任务名称")
    private String mission;
    @ApiModelProperty(value = "呼叫类型")
    private String callDirection;
    @ApiModelProperty(value = "工单编号")
    private String workorderPid;
    @ApiModelProperty(value = "录音时间")
    private String callBegin;
    @ApiModelProperty(value = "主叫号码")
    private String callNo;
    @ApiModelProperty(value = "质检得分")
    private String totalScore;
    @ApiModelProperty(value = "质检员")
    private String scorerId;
    @ApiModelProperty(value = "申诉状态")
    private String appealState;
    @ApiModelProperty(value = "坐席编号")
    private String agentId;
    @ApiModelProperty(value = "单据编号")
    private String workOrderUrl;
}
