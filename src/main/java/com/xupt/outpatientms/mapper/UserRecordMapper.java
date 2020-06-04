package com.xupt.outpatientms.mapper;

import com.xupt.outpatientms.bean.Department;
import com.xupt.outpatientms.bean.Feedback;
import com.xupt.outpatientms.bean.Record;
import com.xupt.outpatientms.dto.FeedbackDTO;
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

    int payRecord(Integer recordId, Integer order);

    int updateRecordStatus(@Param("recordId")Integer recordId, @Param("userId")Integer userId,
                           @Param("recordStatus")Integer recordStatus,
                           @Param("previousStatus")Integer previousStatus);

    List<Record> listRecord(Integer userId);

    int checkExpireRecord(Integer userId);

    int delRecord(Integer recordId, Integer userId);

    Feedback setFeedback(FeedbackDTO feedback);

    int commentRecord(Feedback feedback);

    Feedback getFeedback(String recordId, String userId);
}
