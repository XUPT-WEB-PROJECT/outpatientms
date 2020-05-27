package com.xupt.outpatientms.service;

import com.xupt.outpatientms.bean.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    public boolean addUser(@Param("user") User user);

}
