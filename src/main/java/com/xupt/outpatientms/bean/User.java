package com.xupt.outpatientms.bean;

import com.xupt.outpatientms.enumeration.GenderEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseModel{

    private String userId;
    private String userName;
    private GenderEnum userGender;
    private String userTel;
    private String userPwd;
    private Integer userAge;
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
