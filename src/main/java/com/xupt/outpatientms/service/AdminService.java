package com.xupt.outpatientms.service;

import com.xupt.outpatientms.bean.*;
import com.xupt.outpatientms.dto.ScheduleDTO;
import com.xupt.outpatientms.util.ResponseBuilder;
import com.xupt.outpatientms.vo.AdminChoseDoctorVO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AdminService {

    Admin login(String adminName, String adminPwd);

    List<Department> choseDepartment();

    List<AdminChoseDoctorVO> choseDoctor(String departmentName);

    List<Schedule> getSchedule(String departmentName);

    boolean checkDepartment(String departmentName);

    int addSchedule(Schedule schedule);

    int delSchedule(ScheduleDTO schedule);

    Schedule checkSchedule(ScheduleDTO schedule);

    List<User> getAllUser();

    List<Schedule> getAllSchedule();

    List<Record> getAllRecord();
}
