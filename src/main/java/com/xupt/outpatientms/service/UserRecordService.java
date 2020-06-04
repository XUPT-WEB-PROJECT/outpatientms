package com.xupt.outpatientms.service;

import com.xupt.outpatientms.bean.Department;
import com.xupt.outpatientms.bean.Feedback;
import com.xupt.outpatientms.bean.Record;
import com.xupt.outpatientms.dto.FeedbackDTO;
import com.xupt.outpatientms.dto.RecordCreateDTO;
import com.xupt.outpatientms.vo.UserChoseDoctorVO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserRecordService {

    List<Department> choseDepartment();

    List<UserChoseDoctorVO> choseDoctor(String departmentName, Integer workday);

    Record setRecord(RecordCreateDTO record);

    int createRecord(Record record);

    Record getRecord(String recordId);

    int payRecord(String recordId, Integer order);

    int checkExpireRecord(Integer userId);

    List<Record> listRecord(Integer userId, int p, int size);

    int delRecord(Integer recordId, Integer userId);

    Feedback setFeedback(FeedbackDTO feedback);

    int commentRecord(Feedback feedback);

    Feedback getFeedback(String recordId, String userId);
}
