package com.itcc.mva.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName(value="T_ID_CARD")
public class IdCardEntity {
    @TableId(value = "F_CODE")
    private Integer F_CODE;
    @TableField(value = "F_PROVINCE")
    private String F_PROVINCE;
    @TableField(value = "F_CITY")
    private String F_CITY;
    @TableField(value = "F_AREA")
    private String F_AREA;
}
