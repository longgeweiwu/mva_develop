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
    private Integer fCode;
    @TableField(value = "F_PROVINCE")
    private String fProvince;
    @TableField(value = "F_CITY")
    private String fCity;
    @TableField(value = "F_AREA")
    private String fArea;
}
