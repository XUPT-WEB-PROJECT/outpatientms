package com.xupt.outpatientms.mapper;

import com.xupt.outpatientms.bean.Admin;
import com.xupt.outpatientms.bean.Department;
import com.xupt.outpatientms.bean.Schedule;
import com.xupt.outpatientms.dto.ScheduleDTO;
import com.xupt.outpatientms.vo.AdminChoseDoctorVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AdminMapper {

    Admin login(@Param("adminName")String adminName, @Param("adminPwd")String adminPwd);

    List<Department> choseDepartment();

    List<AdminChoseDoctorVO> choseDoctor(@Param("departmentName")String departmentName);

    List<Schedule> getSchedule(@Param("departmentName")String departmentName);

    int checkDepartment(@Param("departmentName")String departmentName);

    int addSchedule(Schedule schedule);

    int delSchedule(ScheduleDTO schedule);

    Schedule checkSchedule(ScheduleDTO schedule);
}
