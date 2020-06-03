package com.xupt.outpatientms.service;

import com.xupt.outpatientms.bean.DoctorUnchecked;
import com.xupt.outpatientms.dto.DoctorUncheckedDTO;
import com.xupt.outpatientms.vo.DoctorUncheckedVO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by BorisLiu on 2020/5/31
 */
@Service
public interface DoctorUncheckedService {
    public Integer register(DoctorUncheckedDTO doctorUnchecked);
    public List<DoctorUncheckedVO> list();
    public boolean deleteByDoctorTel(String doctorTel);
}
