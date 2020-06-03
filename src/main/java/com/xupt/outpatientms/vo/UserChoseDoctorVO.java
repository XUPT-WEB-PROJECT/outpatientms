package com.xupt.outpatientms.vo;

import com.xupt.outpatientms.enumeration.GenderEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "患者所见的医生信息")
public class UserChoseDoctorVO {

    @ApiModelProperty(value = "doctorId")
    private String doctorId;//姓名

    @ApiModelProperty(value = "医生姓名")
    private String doctorName;//姓名

    @ApiModelProperty(value = "医生性别(0：男，1：女，2：保密)")
    private GenderEnum doctorGender;//性别(0：男，1：女，2：保密)

    @ApiModelProperty(value = "医生职称")
    private String doctorTitle;//职称

    @ApiModelProperty(value = "医生照片url")
    private String doctorPhoto;//照片url

    @ApiModelProperty(value = "医生简介")
    private String doctorInfo;//医生简介

    @ApiModelProperty(value = "上午剩余预约名额")
    private Integer amQuota;

    @ApiModelProperty(value = "下午剩余预约名额")
    private Integer pmQuota;

}
