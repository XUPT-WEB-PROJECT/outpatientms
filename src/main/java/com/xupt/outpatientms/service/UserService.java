package com.xupt.outpatientms.service;

import com.xupt.outpatientms.bean.User;
import com.xupt.outpatientms.dto.UserRegisterDTO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    public int addUser(@Param("user") UserRegisterDTO user);

    public User login(@Param("userTel")String userTel, @Param("userPwd")String userPwd);

    public boolean checkUserTelUnique(@Param("userTel")String userTel);

    public boolean newAvatar(@Param("userId")String userId, @Param("avatarUrl")String avatarUrl);

}
