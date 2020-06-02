package com.xupt.outpatientms.service;

import com.xupt.outpatientms.bean.Department;
import com.xupt.outpatientms.vo.UserChoseDoctorVO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserRecordService {

    public List<Department> choseDepartment();

    List<UserChoseDoctorVO> choseDoctor(String departmentName, String date);
}
