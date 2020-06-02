package com.xupt.outpatientms.mapper;

import com.xupt.outpatientms.bean.Department;
import com.xupt.outpatientms.vo.UserChoseDoctorVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserRecordMapper {

    public List<Department> choseDepartment();

    public List<UserChoseDoctorVO> choseDoctor(@Param("departmentName")String departmentName, @Param("date")String date);

}
