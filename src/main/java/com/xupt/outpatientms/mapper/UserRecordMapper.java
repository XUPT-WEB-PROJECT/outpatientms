package com.xupt.outpatientms.mapper;

import com.xupt.outpatientms.bean.Department;
import com.xupt.outpatientms.bean.Record;
import com.xupt.outpatientms.dto.RecordCreateDTO;
import com.xupt.outpatientms.vo.UserChoseDoctorVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserRecordMapper {

    List<Department> choseDepartment();

    List<UserChoseDoctorVO> choseDoctor(@Param("departmentName")String departmentName, @Param("workday")Integer workday);

    int createRecord(Record record);

    Record setRecord(RecordCreateDTO record);

    Record getRecord(@Param("recordId") Integer recordId);

    int payRecord(@Param("recordId")Integer recordId, @Param("order")Integer order);

    List<Record> listRecord(Integer userId);

    int checkExpireRecord(Integer userId);
}