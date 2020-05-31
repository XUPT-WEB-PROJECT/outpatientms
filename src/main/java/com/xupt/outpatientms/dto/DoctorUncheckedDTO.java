package com.xupt.outpatientms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DoctorUncheckedDTO {

    @NotNull(message = "姓名不能为空")
    @Size(min = 2, max = 12, message = "请检查名字长度")
    private String doctorName;//姓名
    private Integer doctorGender;//性别(0：男，1：女，2：保密)
    private String departmentName;//所属科室，外键
    private String doctorTitle;//职称
    @NotNull(message = "手机号码不能为空")
    @Pattern(regexp = "^(13[0-9]|14[5|7]|15[0|1|2|3|5|6|7|8|9]|18[0|1|2|3|5|6|7|8|9])\\d{8}$",
            message = "请检查电话号码")
    private String doctorTel;//电话号码
    @NotNull(message = "密码不能为空")
    @Pattern(regexp = "\\w{6,18}",
            message = "密码长度在6~18之间，只能包含字母、数字和下划线")
    private String doctorPwd;//登录密码
    private String doctorInfo;//医生简介
}
