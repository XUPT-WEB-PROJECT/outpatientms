package com.xupt.outpatientms.bean;

import com.xupt.outpatientms.enumeration.GenderEnum;
import lombok.*;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.*;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {

    private String userId;

    @NotNull(message = "姓名不能为空")
    @Size(min = 2, max = 12, message = "请检查名字长度")
    private String userName;

    private GenderEnum userGender;

    @NotNull(message = "手机号码不能为空")
    @Pattern(regexp = "^(13[0-9]|14[5|7]|15[0|1|2|3|5|6|7|8|9]|18[0|1|2|3|5|6|7|8|9])\\d{8}$",
                message = "请检查电话号码")
    private String userTel;

    @NotNull(message = "密码不能为空")
    @Pattern(regexp = "\\w{6,18}",
            message = "密码长度在6~18之间，只能包含字母、数字和下划线")
    private String userPwd;

    @Min(value = 0, message = "年龄在0~120之间")
    @Max(value = 120, message = "年龄在0~120之间")
    private Integer userAge;

    @URL
    private String userAvatar;

    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + "'" +
                ", userName='" + userName + "'" +
                ", userGender='" + userGender.desc() + "'" +
                ", userTel='" + userTel + "'" +
                ", userPwd='" + userPwd + "'" +
                ", userAge='" + userAge + "'" +
                ", userAvatar='" + userAvatar + "'"+
                "}";
    }
}
