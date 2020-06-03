package com.xupt.outpatientms.service;

import com.xupt.outpatientms.bean.Doctor;
import com.xupt.outpatientms.bean.DoctorUnchecked;
import org.springframework.stereotype.Service;

/**
 * Created by BorisLiu on 2020/5/31
 */
@Service
public interface DoctorService {
    public Doctor login(String doctorTel,String doctorPwd);

    boolean review(Doctor doctor);

    DoctorUnchecked checkDoctorUncheckedTel(String phone);
}
