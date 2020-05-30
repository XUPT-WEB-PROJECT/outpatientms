package com.xupt.outpatientms.service;

import com.xupt.outpatientms.bean.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    public int addUser(@Param("user")User user);

    public int checkUserTelUnique(@Param("userTel")String userTel);

    public int newAvatar(@Param("userId")Integer userId, @Param("avatarUrl")String avatarUrl);

}
