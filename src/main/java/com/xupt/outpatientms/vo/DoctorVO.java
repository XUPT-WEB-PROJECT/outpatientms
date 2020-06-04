package com.xupt.outpatientms.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by BorisLiu on 2020/5/31
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DoctorVO {

    private Integer doctorId;
    private String doctorName;//姓名
    private String departmentName;//所属科室，外键
    private String doctorTitle;//职称
    private String doctorPhoto;//头像url
    private String doctorTel;//电话号码
    private String doctorInfo;//医生简介

}
