package com.xupt.outpatientms.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "医生注册信息")
public class DoctorUncheckedVO {

    @ApiModelProperty(name = "医生姓名")
    @NotNull(message = "姓名不能为空")
    @Size(min = 2, max = 12, message = "请检查名字长度")
    private String doctorName;//姓名

    @ApiModelProperty(name = "医生性别(0：男，1：女，2：保密)")
    private Integer doctorGender;//性别(0：男，1：女，2：保密)

    @ApiModelProperty(name = "医生所属科室")
    private String departmentName;//所属科室，外键

    @ApiModelProperty(name = "职称")
    private String doctorTitle;//职称

    @ApiModelProperty(name = "照片")
    private String doctorPhoto;//头像url

    @ApiModelProperty(name = "电话号码")
    @NotNull(message = "手机号码不能为空")
    @Pattern(regexp = "^(13[0-9]|14[5|7]|15[0|1|2|3|5|6|7|8|9]|18[0|1|2|3|5|6|7|8|9])\\d{8}$",
            message = "请检查电话号码")
    private String doctorTel;//电话号码

    @ApiModelProperty(name = "医生简介")
    private String doctorInfo;//医生简介
}
