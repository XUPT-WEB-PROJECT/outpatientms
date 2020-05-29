package com.xupt.outpatientms.mapper;

import com.xupt.outpatientms.bean.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {

    public int addUser(User user);

    public int checkUserTelUnique(String userTel);

}
