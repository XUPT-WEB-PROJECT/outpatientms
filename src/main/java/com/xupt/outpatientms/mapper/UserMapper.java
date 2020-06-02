package com.xupt.outpatientms.mapper;

import com.xupt.outpatientms.bean.User;
import com.xupt.outpatientms.dto.UserRegisterDTO;
import com.xupt.outpatientms.dto.UserUpdateDTO;
import com.xupt.outpatientms.vo.UserVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {

    public int addUser(UserRegisterDTO user);

    public User login(@Param("userTel")String userTel, @Param("userPwd")String userPwd);

    public int checkUserTelUnique(@Param("userTel")String userTel);

    public int newAvatar(@Param("userId")Integer userId, @Param("avatarUrl")String avatarUrl);

    public int updateUser(UserUpdateDTO user);

    public UserVO getUserInfo(@Param("userId")Integer userId);

}
