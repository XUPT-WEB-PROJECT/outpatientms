package com.xupt.outpatientms.mapper;

import com.xupt.outpatientms.bean.Doctor;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * Created by BorisLiu on 2020/5/31
 */
@Mapper
public interface DoctorMapper {
   public Doctor login(@Param("doctorTel")String doctorTel, @Param("doctorPwd")String doctorPwd);

   public Integer add(Doctor doctor);

}
