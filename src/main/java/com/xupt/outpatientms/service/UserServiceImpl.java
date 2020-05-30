package com.xupt.outpatientms.service;

import com.xupt.outpatientms.bean.User;
import com.xupt.outpatientms.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserMapper userMapper;

    @Override
    public int addUser(User user) {
        return userMapper.addUser(user);
    }

    @Override
    public int checkUserTelUnique(String userTel) {
        return 0;
    }

    @Override
    public int newAvatar(Integer userId, String avatarUrl) {
        userMapper.newAvatar(userId, avatarUrl);
        return 0;
    }
}
