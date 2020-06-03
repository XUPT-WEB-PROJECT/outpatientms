package com.xupt.outpatientms.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "管理员信息")
public class Admin {

    @ApiModelProperty(value = "管理员登录名", required = true)
    @NotNull(message = "管理员登录名不可为空")
    @Size(min = 2, max = 12, message = "请检查名字长度")
    private String adminName;

    @ApiModelProperty(value = "管理员密码", required = true)
    @NotNull(message = "登录密码不可为空")
    @Pattern(regexp = "\\w{6,18}",
            message = "密码长度在6~18之间，只能包含字母、数字和下划线")
    private String adminPwd;

    @URL
    @ApiModelProperty(value = "管理员头像url")
    private String adminAvatar;

}
