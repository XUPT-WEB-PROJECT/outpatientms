package com.xupt.outpatientms.vo;

import com.xupt.outpatientms.bean.User;
import com.xupt.outpatientms.enumeration.GenderEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "成功登录时返回的用户信息")
public class UserVO {

    @ApiModelProperty(value = "用户姓名")
    private String userName;
    @ApiModelProperty(value = "用户性别 0(男)1(女)2(保密)")
    private GenderEnum userGender;
    @ApiModelProperty(value = "用户电话号码")
    private String userTel;
    @ApiModelProperty(value = "用户年龄")
    private Integer userAge;
    @ApiModelProperty(value = "用户头像url")
    private String userAvatar;

    public UserVO(User u) {
        this.userTel = u.getUserTel();
        this.userAge = u.getUserAge();
        this.userName = u.getUserName();
        this.userAvatar = u.getUserAvatar();
        this.userGender = u.getUserGender();
    }

    @Override
    public String toString() {
        return "User{" +
                ", userName='" + userName + "'" +
                ", userGender='" + userGender.desc() + "'" +
                ", userTel='" + userTel + "'" +
                ", userAge='" + userAge + "'" +
                ", userAvatar='" + userAvatar + "'"+
                "}";
    }
}
