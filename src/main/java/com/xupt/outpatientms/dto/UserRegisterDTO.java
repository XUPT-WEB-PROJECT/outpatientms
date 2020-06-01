package com.xupt.outpatientms.dto;

import com.xupt.outpatientms.enumeration.GenderEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "用户注册数据")
public class UserRegisterDTO {

    @ApiModelProperty(value = "用户姓名，长度2~12", required = true)
    @NotNull(message = "姓名不能为空")
    @Size(min = 2, max = 12, message = "请检查名字长度")
    private String userName;

    @ApiModelProperty(value = "用户性别， 0(男)1(女)2(保密)，数字汉字均可")
    private GenderEnum userGender;

    @ApiModelProperty(value = "用户手机号码", required = true)
    @NotNull(message = "手机号码不能为空")
    @Pattern(regexp = "^(13[0-9]|14[5|7]|15[0|1|2|3|5|6|7|8|9]|18[0|1|2|3|5|6|7|8|9])\\d{8}$",
            message = "请检查电话号码")
    private String userTel;

    @ApiModelProperty(value = "用户密码，长度6~18，只能包含字母数字下划线", required = true)
    @NotNull(message = "密码不能为空")
    @Pattern(regexp = "\\w{6,18}",
            message = "密码长度在6~18之间，只能包含字母、数字和下划线")
    private String userPwd;

    @ApiModelProperty(value = "用户年龄 0~120")
    @Min(value = 0, message = "年龄在0~120之间")
    @Max(value = 120, message = "年龄在0~120之间")
    private Integer userAge;

    @ApiModelProperty(value = "注册验证码")
    @NotNull
    @Size(min = 6, max = 6, message = "验证码错误")
    private String code;


    @Override
    public String toString() {
        return "User{" +
                ", userName='" + userName + "'" +
                ", userGender='" + userGender.desc() + "'" +
                ", userTel='" + userTel + "'" +
//                ", userPwd='" + userPwd + "'" +
                ", userPwd='" + "userPwd" + "'" +
                ", userAge='" + userAge + "'" +
                "}";
    }
}
