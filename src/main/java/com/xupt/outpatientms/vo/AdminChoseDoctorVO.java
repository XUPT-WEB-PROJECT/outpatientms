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
@ApiModel(description = "管理员排班时所见医生信息")
public class AdminChoseDoctorVO {

    @ApiModelProperty(value = "doctorId")
    private String doctorId;

    @ApiModelProperty(value = "医生姓名")
    private String doctorName;//姓名

    @ApiModelProperty(value = "医生性别(0：男，1：女，2：保密)")
    private GenderEnum doctorGender;//性别(0：男，1：女，2：保密)

    @ApiModelProperty(value = "医生职称")
    private String doctorTitle;//职称

    @ApiModelProperty(value = "医生每日预约名额")
    private Integer doctorQuota;//每日预约名额

}

