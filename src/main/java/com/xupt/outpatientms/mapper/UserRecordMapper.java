package com.xupt.outpatientms.mapper;

import com.xupt.outpatientms.bean.Department;
import com.xupt.outpatientms.vo.DoctorChoseVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserRecordMapper {

    public List<Department> choseDepartment();

    public List<DoctorChoseVO> choseDoctor();

}
