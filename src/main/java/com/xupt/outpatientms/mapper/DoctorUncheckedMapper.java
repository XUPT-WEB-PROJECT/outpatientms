package com.xupt.outpatientms.mapper;

import com.xupt.outpatientms.bean.DoctorUnchecked;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by BorisLiu on 2020/5/31
 */
@Mapper
public interface DoctorUncheckedMapper {
    // 添加医生（未审核）
    Integer add(DoctorUnchecked doctorUnchecked);

    List<DoctorUnchecked> selectAll();

    Integer deleteByDoctorTel(@Param("doctorTel")String doctorTel);

    DoctorUnchecked selectByTel(@Param("doctorTel")String doctorTel);

}
