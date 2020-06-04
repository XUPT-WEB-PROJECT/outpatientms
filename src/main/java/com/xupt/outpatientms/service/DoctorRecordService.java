package com.xupt.outpatientms.service;

import com.xupt.outpatientms.bean.Record;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * Created by BorisLiu on 2020/6/3
 */
@Service
public interface DoctorRecordService {
    public boolean updateRecord(String recordId);

    public boolean writeMedicalRecord(String medicalRecord,String recordId,String userId, String doctorId);

    public List<Record> getTodayAllRecord(String doctorId,String recordDate);

    Record getRecord(String recordId);
}
