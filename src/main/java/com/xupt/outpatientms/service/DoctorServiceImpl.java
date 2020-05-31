package com.xupt.outpatientms.service;

import com.xupt.outpatientms.bean.Doctor;
import com.xupt.outpatientms.bean.DoctorUnchecked;
import com.xupt.outpatientms.mapper.DoctorMapper;
import com.xupt.outpatientms.mapper.DoctorUncheckedMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by BorisLiu on 2020/5/31
 */
@Service
public class DoctorServiceImpl implements DoctorService {
   @Autowired
   public DoctorMapper doctorMapper;

   @Autowired
   public DoctorUncheckedMapper doctorUncheckedMapper;

    @Override
    public Doctor login(String doctorTel, String doctorPwd) {
        return doctorMapper.login(doctorTel,doctorPwd);
    }

    @Override
    @Transactional
    public boolean review(String phone) {
        DoctorUnchecked doctorUnchecked = doctorUncheckedMapper.selectByTel(phone);
        Doctor doctor = new Doctor();
        BeanUtils.copyProperties(doctorUnchecked,doctor);
        int res1 = doctorMapper.add(doctor);
        int res2 = doctorUncheckedMapper.deleteByDoctorTel(phone);
        if (res1 > 0 && res2 > 0){
            return true;
        }
        return false;
    }
}
