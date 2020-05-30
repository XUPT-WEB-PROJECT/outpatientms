package com.xupt.outpatientms.service;

import com.xupt.outpatientms.bean.User;
import com.xupt.outpatientms.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.apache.commons.codec.digest.DigestUtils;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserMapper userMapper;

    @Override
    public int addUser(User user) {
        user.setUserPwd(DigestUtils.md5Hex(user.getUserPwd()));
        return userMapper.addUser(user);
    }

    @Override
    public User login(String userTel, String userPwd) {
        return userMapper.login(userTel, DigestUtils.md5Hex(userPwd));
    }

    @Override
    public boolean checkUserTelUnique(String userTel) {
        return userMapper.checkUserTelUnique(userTel) == 0;
    }

    @Override
    public int newAvatar(Integer userId, String avatarUrl) {
        userMapper.newAvatar(userId, avatarUrl);
        return 0;
    }

}
