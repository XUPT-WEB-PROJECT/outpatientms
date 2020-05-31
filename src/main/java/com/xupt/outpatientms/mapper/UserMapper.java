package com.xupt.outpatientms.mapper;

import com.xupt.outpatientms.bean.User;
import com.xupt.outpatientms.dto.UserRegisterDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {

    public int addUser(UserRegisterDTO user);

    public User login(String userTel, String userPwd);

    public int checkUserTelUnique(String userTel);

    public int newAvatar(String userId, String avatarUrl);

}
