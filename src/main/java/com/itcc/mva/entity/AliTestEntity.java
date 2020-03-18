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
@TableName(value="MVA_ALI_TEST_ASR")
public class AliTestEntity {

    @TableField(value = "FILENAME")
    private String filename;

    //原型
    @TableField(value = "ORITASKID")
    private String oritaskid;
    @TableField(value = "ORI_RESULT")
    private String oriResult;

    //语音模型
    @TableField(value = "VMTASKID")
    private String vmtaskid;
    @TableField(value = "VM_RESULT")
    private String vmResult;

    //语音+标注
    @TableField(value = "VHMTASKID")
    private String vhmtaskid;
    @TableField(value = "VHM_RESULT")
    private String vhmResult;


}
