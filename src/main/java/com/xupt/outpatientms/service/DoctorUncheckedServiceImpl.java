package com.xupt.outpatientms.service;

import com.xupt.outpatientms.bean.DoctorUnchecked;
import com.xupt.outpatientms.controller.DoctorUncheckedController;
import com.xupt.outpatientms.dto.DoctorUncheckedDTO;
import com.xupt.outpatientms.mapper.DoctorUncheckedMapper;
import com.xupt.outpatientms.vo.DoctorUncheckedVO;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by BorisLiu on 2020/5/31
 */
@Service
public class DoctorUncheckedServiceImpl implements DoctorUncheckedService {

    @Autowired
    private DoctorUncheckedMapper doctorUncheckedMapper;

    private static final Logger logger = LoggerFactory.getLogger(DoctorUncheckedServiceImpl.class);


    @Override
    public Integer register(DoctorUncheckedDTO doctorUncheckedDTO) {
        doctorUncheckedDTO.setDoctorPwd(DigestUtils.md5Hex(doctorUncheckedDTO.getDoctorPwd()));
        DoctorUnchecked doctorUnchecked = new DoctorUnchecked();
        BeanUtils.copyProperties(doctorUncheckedDTO,doctorUnchecked);
        return doctorUncheckedMapper.add(doctorUnchecked);
    }

    @Override
    public List<DoctorUncheckedVO> list() {
        try{
            return doctorUncheckedMapper.selectAll();
        }catch (Exception e){
            logger.error(e.getLocalizedMessage());
        }
       return null;
    }

    @Override
    public boolean deleteByDoctorTel(String doctorTel) {
        int res = doctorUncheckedMapper.deleteByDoctorTel(doctorTel);
        if (res > 0){
            return true;
        }
        logger.error("无此手机号，删除失败！");
        return  false;
    }
}
