package com.xupt.outpatientms.service;

import com.xupt.outpatientms.bean.Department;
import com.xupt.outpatientms.mapper.UserRecordMapper;
import com.xupt.outpatientms.vo.UserChoseDoctorVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class UserRecordServiceImpl implements UserRecordService {

    @Autowired
    private UserRecordMapper userMapper;

    @Override
    public List<Department> choseDepartment() {
        return userMapper.choseDepartment();
    }

    @Override
    public List<UserChoseDoctorVO> choseDoctor(String departmentName, Integer workday) {
        return userMapper.choseDoctor(departmentName, workday);
    }


}
