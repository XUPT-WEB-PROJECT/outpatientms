package com.xupt.outpatientms.mapper;

import com.xupt.outpatientms.bean.Record;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by BorisLiu on 2020/6/3
 */
@Mapper
public interface DoctorRecordMapper {

    public Integer updateRecord(@Param("recordId") String recordId);

    public Integer writeMedicalRecord(@Param("medicalRecord") String medicalRecord,@Param("recordId")String recordId,@Param("userId")String userId, @Param("doctorId")String doctorId);

    public List<Record> getTodayAllRecord(@Param("doctorId")String doctorId,@Param("recordDate") String recordDate);

}
