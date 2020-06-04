package com.xupt.outpatientms.service;

import com.xupt.outpatientms.bean.*;
import com.xupt.outpatientms.dto.ScheduleDTO;
import com.xupt.outpatientms.mapper.AdminMapper;
import com.xupt.outpatientms.vo.AdminChoseDoctorVO;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminMapper adminMapper;

    @Override
    public Admin login(String adminName, String adminPwd) {
        return adminMapper.login(adminName, DigestUtils.md5Hex(adminPwd));
    }

    @Override
    public List<Department> choseDepartment() {
        return adminMapper.choseDepartment();
    }

    @Override
    public List<AdminChoseDoctorVO> choseDoctor(String departmentName) {
        return adminMapper.choseDoctor(departmentName);
    }

    @Override
    public List<Schedule> getSchedule(String departmentName) {
        return adminMapper.getSchedule(departmentName);
    }

    @Override
    public boolean checkDepartment(String departmentName) {
        return adminMapper.checkDepartment(departmentName) == 1;
    }

    @Override
    public int addSchedule(Schedule schedule) {
        return adminMapper.addSchedule(schedule);
    }

    @Override
    public int delSchedule(ScheduleDTO schedule) {
        return adminMapper.delSchedule(schedule);
    }

    @Override
    public Schedule checkSchedule(ScheduleDTO schedule) {
        return adminMapper.checkSchedule(schedule);
    }

    @Override
    public List<User> getAllUser() {
        return adminMapper.getAllUser();
    }

    @Override
    public List<Schedule> getAllSchedule() {
        return adminMapper.getAllSchedule();
    }

    @Override
    public List<Record> getAllRecord() {
        return adminMapper.getAllRecord();
    }


}
