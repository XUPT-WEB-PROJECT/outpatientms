package com.xupt.outpatientms.service;

import com.github.pagehelper.PageHelper;
import com.xupt.outpatientms.bean.Department;
import com.xupt.outpatientms.bean.Record;
import com.xupt.outpatientms.dto.RecordCreateDTO;
import com.xupt.outpatientms.mapper.UserRecordMapper;
import com.xupt.outpatientms.vo.UserChoseDoctorVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserRecordServiceImpl implements UserRecordService {

    @Autowired
    private UserRecordMapper userRecordMapper;

    @Override
    public List<Department> choseDepartment() {
        return userRecordMapper.choseDepartment();
    }

    @Override
    public List<UserChoseDoctorVO> choseDoctor(String departmentName, Integer workday) {
        return userRecordMapper.choseDoctor(departmentName, workday);
    }

    @Override
    public Record setRecord(RecordCreateDTO record) {
        return userRecordMapper.setRecord(record);
    }

    @Override
    public int createRecord(Record record) {
        return userRecordMapper.createRecord(record);
    }

    @Override
    public Record getRecord(String recordId) {
        return userRecordMapper.getRecord(Integer.valueOf(recordId));
    }

    @Override
    public int payRecord(String recordId, Integer order) {
        return userRecordMapper.payRecord(Integer.valueOf(recordId), order);
    }

    @Override
    public int checkExpireRecord(Integer userId){
        return userRecordMapper.checkExpireRecord(userId);
    }

    @Override
    public List<Record> listRecord(Integer userId, int p, int size) {
        PageHelper.startPage(p,size);
        return userRecordMapper.listRecord(userId);
    }

}