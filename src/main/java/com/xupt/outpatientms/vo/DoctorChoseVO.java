package com.xupt.outpatientms.vo;

import com.xupt.outpatientms.enumeration.GenderEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DoctorChoseVO {

    private String doctorName;//姓名
    private GenderEnum doctorGender;//性别(0：男，1：女，2：保密)
    private String departmentName;//所属科室
    private String doctorTitle;//职称
    private String doctorPhoto;//照片url
    private String doctorTel;//电话号码
    private String doctorInfo;//医生简介
    private Integer curQuota;//当日预约名额

}
