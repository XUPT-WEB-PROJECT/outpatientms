package com.xupt.outpatientms.service;

import com.xupt.outpatientms.bean.Doctor;
import com.xupt.outpatientms.bean.Record;
import com.xupt.outpatientms.mapper.DoctorMapper;
import com.xupt.outpatientms.mapper.DoctorRecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by BorisLiu on 2020/6/3
 */
@Service
public class DoctorRecordServiceImpl implements DoctorRecordService {

   @Autowired
   public DoctorRecordMapper doctorRecordMapper;

    @Override
    public boolean updateRecord(String recordId) {
        int cnt = doctorRecordMapper.updateRecord(recordId);
        return cnt > 0;
    }

    @Override
    public boolean writeMedicalRecord(String medicalRecord, String recordId, String userId, String doctorId) {
        int cnt = doctorRecordMapper.writeMedicalRecord(medicalRecord,recordId,userId,doctorId);
        return cnt > 0;
    }

    @Override
    public List<Record> getTodayAllRecord(String doctorId, String recordDate) {
        return doctorRecordMapper.getTodayAllRecord(doctorId,recordDate);
    }

    @Override
    public Record getRecord(String recordId) {
        return doctorRecordMapper.getRecord(recordId);
    }

}
