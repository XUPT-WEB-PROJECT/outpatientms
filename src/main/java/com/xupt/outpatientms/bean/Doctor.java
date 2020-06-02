package com.xupt.outpatientms.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by BorisLiu on 2020/5/31
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Doctor {
    private Integer doctorId ;//医生ID，主键
    private String doctorName;//姓名
    private Integer doctorGender;//性别(0：男，1：女，2：保密)
    private String departmentName;//所属科室，外键
    private String doctorTitle;//职称
    private String doctorPhoto;//头像url
    private String doctorTel;//电话号码
    private String doctorPwd;//登录密码
    private String doctorInfo;//医生简介
    private Integer doctorQuota;//每日预约名额
}
