package com.itcc.mva.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "Mva信息集合")
public class MvaOutVo {
    @ApiModelProperty(value = "电话编号")
    private String callid;
    @ApiModelProperty(value = "电话开始时间")
    private String startTime;
    @ApiModelProperty(value = "电话结束时间")
    private String leavWordTime;
    @ApiModelProperty(value = "电话号码")
    private String callno;
    @ApiModelProperty(value = "电话主叫号码")
    private String ani;
    @ApiModelProperty(value = "身份证号码")
    private String id;
    @ApiModelProperty(value = "联系电话")
    private String phoneno;
    @ApiModelProperty(value = "问题类型")
    private String questionType;
    @ApiModelProperty(value = "标志位 1 有效 0 无效")
    private String flag;
}
