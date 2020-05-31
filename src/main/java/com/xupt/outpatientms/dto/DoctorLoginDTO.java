package com.xupt.outpatientms.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * Created by BorisLiu on 2020/5/31
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "医生登录信息")
public class DoctorLoginDTO {

    @ApiModelProperty(value = "手机号码", required = true)
    @NotNull(message = "手机号码不能为空")
    @Pattern(regexp = "^(13[0-9]|14[5|7]|15[0|1|2|3|5|6|7|8|9]|18[0|1|2|3|5|6|7|8|9])\\d{8}$",
            message = "请检查电话号码")
    private String doctorTel;

    @ApiModelProperty(value = "密码", required = true)
    @NotNull(message = "密码不能为空")
    @Pattern(regexp = "\\w{6,18}",
            message = "密码长度在6~18之间，只能包含字母、数字和下划线")
    private String doctorPwd;

}
