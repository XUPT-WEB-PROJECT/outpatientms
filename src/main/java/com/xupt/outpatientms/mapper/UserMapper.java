package com.xupt.outpatientms.mapper;

import com.xupt.outpatientms.bean.User;
import com.xupt.outpatientms.dto.UserRegisterDTO;
import com.xupt.outpatientms.dto.UserUpdateDTO;
import com.xupt.outpatientms.vo.UserVO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {

    public int addUser(UserRegisterDTO user);

    public User login(String userTel, String userPwd);

    public int checkUserTelUnique(String userTel);

    public int newAvatar(Integer userId, String avatarUrl);

    public int updateUser(UserUpdateDTO user);

    public UserVO getUserInfo(Integer userId);

}
