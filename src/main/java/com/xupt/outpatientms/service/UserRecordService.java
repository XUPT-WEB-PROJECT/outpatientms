package com.xupt.outpatientms.service;

import com.xupt.outpatientms.bean.Department;
import com.xupt.outpatientms.vo.DoctorChoseVO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserRecordService {

    public List<Department> choseDepartment();

    public List<DoctorChoseVO> choseDoctor(String date);

}
