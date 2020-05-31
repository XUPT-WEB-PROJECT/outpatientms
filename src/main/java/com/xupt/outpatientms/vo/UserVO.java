package com.xupt.outpatientms.vo;

import com.xupt.outpatientms.bean.User;
import com.xupt.outpatientms.enumeration.GenderEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserVO {

    private String userName;
    private GenderEnum userGender;
    private String userTel;
    private Integer userAge;
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
