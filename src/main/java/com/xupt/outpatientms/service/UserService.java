package com.xupt.outpatientms.service;

import com.xupt.outpatientms.bean.User;
import com.xupt.outpatientms.dto.UserRegisterDTO;
import com.xupt.outpatientms.dto.UserUpdateDTO;
import com.xupt.outpatientms.vo.UserVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    public int addUser(UserRegisterDTO user);

    public User login(String userTel, String userPwd);

    public boolean checkUserTelUnique(String userTel);

    public boolean newAvatar(String userId, String avatarUrl);

    public boolean updateUser(UserUpdateDTO user);

    public UserVO getUserInfo(Integer userId);

}
