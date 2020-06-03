package com.xupt.outpatientms.service;

import com.xupt.outpatientms.bean.User;
import com.xupt.outpatientms.dto.UserRegisterDTO;
import com.xupt.outpatientms.dto.UserUpdateDTO;
import com.xupt.outpatientms.mapper.UserMapper;
import com.xupt.outpatientms.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.apache.commons.codec.digest.DigestUtils;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserMapper userMapper;

    @Override
    public int addUser(UserRegisterDTO user) {
        user.setUserPwd(DigestUtils.md5Hex(user.getUserPwd()));
        return userMapper.addUser(user);
    }

    @Override
    public User login(String userTel, String userPwd) {
        return userMapper.login(userTel, DigestUtils.md5Hex(userPwd));
    }

    @Override
    public boolean checkUserTelUnique(String userTel) {
        return userMapper.checkUserTelUnique(userTel) == 1;
    }

    @Override
    public boolean newAvatar(String userId, String avatarUrl) {
        return userMapper.newAvatar(Integer.valueOf(userId), avatarUrl) == 1;
    }

    @Override
    public boolean updateUser(UserUpdateDTO user) {
        if(user.getNewPwd()!=null) user.setNewPwd(DigestUtils.md5Hex(user.getNewPwd()));
        if(user.getOldPwd()!=null) user.setOldPwd(DigestUtils.md5Hex(user.getOldPwd()));
        return userMapper.updateUser(user) == 1;
    }

    @Override
    public UserVO getUserInfo(Integer userId) {
        return userMapper.getUserInfo(userId);
    }


}
