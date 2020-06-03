package com.xupt.outpatientms.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xupt.outpatientms.bean.Department;
import com.xupt.outpatientms.bean.Record;
import com.xupt.outpatientms.common.CurrentUserData;
import com.xupt.outpatientms.dto.RecordCreateDTO;
import com.xupt.outpatientms.enumeration.ErrCodeEnum;
import com.xupt.outpatientms.enumeration.RecordStatusEnum;
import com.xupt.outpatientms.enumeration.TimeEnum;
import com.xupt.outpatientms.service.UserRecordService;
import com.xupt.outpatientms.util.ResponseBuilder;
import com.xupt.outpatientms.vo.UserChoseDoctorVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/userRecord")
@Api(tags = "用户端挂号相关接口")
public class UserRecordController {

    @Autowired
    private UserRecordService userRecordService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @ApiOperation(value = "用户获取科室信息",
            notes = "用户获取科室信息接口，用于选择科室，接口调用成功errCode=0，科室信息返回在data字段。\n")
    @RequestMapping(value = "choseDepartment", method = RequestMethod.GET)
    public ResponseBuilder<List<Department>> choseDepartment() {
        return new ResponseBuilder<>(ErrCodeEnum.ERR_SUCCESS, "查询成功", userRecordService.choseDepartment());
    }

    @ApiOperation(value = "用户查询预约医生",
            notes = "用户按科室、日期查询医生信息接口，仅返回当日值班医生，接口调用成功errCode=0，医生信息返回在data字段，否则错误信息返回至errMsg\n")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "departmentName", value = "预约科室"),
            @ApiImplicitParam(name = "date", value = "预约日期，yyyy-mm-dd格式，范围为第二天起的一周之内")
    })
    @RequestMapping(value = "choseDoctor", method = RequestMethod.POST)
    public ResponseBuilder<List<UserChoseDoctorVO>> choseDoctor(String departmentName, String date) {
        if (StringUtils.isEmpty(departmentName)
                || StringUtils.isEmpty(date)
        ) {
            return new ResponseBuilder<>(ErrCodeEnum.ERR_ARG, "科室或日期不能为空");
        }
        Calendar calendar = Calendar.getInstance();
        Date now = calendar.getTime();
        Date future = calendar.getTime();
        future.setTime(future.getTime() + 7 * 86400 * 1000);
        Date objDate = null;
        try {
            objDate = simpleDateFormat.parse(date);
        } catch (ParseException e) {
            return new ResponseBuilder<>(ErrCodeEnum.ERR_ARG, "预约日期有误或格式错误");
        }
        date = simpleDateFormat.format(objDate);
        if (now.after(objDate) || future.before(objDate)) {
            return new ResponseBuilder<>(ErrCodeEnum.ERR_ARG, "可预约日期为明日起的一周内");
        }
        calendar.setTime(objDate);
        List<UserChoseDoctorVO> doctors = userRecordService.choseDoctor(departmentName, ((calendar.get(Calendar.DAY_OF_WEEK) + 5) % 7) + 1);
        getOrSetCache(doctors, date);
        return new ResponseBuilder<>(ErrCodeEnum.ERR_SUCCESS, "查询成功", doctors);
    }

    @Transactional
    void getOrSetCache(List<UserChoseDoctorVO> doctors, String date) {
        for (UserChoseDoctorVO doctor : doctors) {
            String am = String.format("%s_%s_am", doctor.getDoctorId(), date);
            String pm = String.format("%s_%s_pm", doctor.getDoctorId(), date);
            String quotKey = String.format("%s_quota", doctor.getDoctorId());
            stringRedisTemplate.opsForValue().set(quotKey, String.valueOf(doctor.getAmQuota()), 3600, TimeUnit.SECONDS);
            if (BooleanUtils.isTrue(stringRedisTemplate.hasKey(am))) {
                String amQuota = stringRedisTemplate.opsForValue().get(am);
                String pmQuota = stringRedisTemplate.opsForValue().get(pm);
                if (amQuota == null || pmQuota == null) return;
                doctor.setPmQuota(Integer.valueOf(amQuota));
                doctor.setAmQuota(Integer.valueOf(pmQuota));
            } else {
                //均分
                doctor.setPmQuota(doctor.getAmQuota() / 2);
                doctor.setAmQuota(doctor.getAmQuota() - doctor.getPmQuota());
                stringRedisTemplate.opsForValue().set(am, doctor.getAmQuota().toString(), 7 * 86400, TimeUnit.SECONDS);
                stringRedisTemplate.opsForValue().set(pm, doctor.getPmQuota().toString(), 7 * 86400, TimeUnit.SECONDS);
            }
        }
    }

    @ApiOperation(value = "预约挂号",
            notes = "用户预约看诊，接口调用成功errCode=0，挂号信息返回在data字段中。否则错误信息返回至errMsg\n" +
                    "此时返回信息中的序号为空，支付后得到实际序号,\n须十五分钟内付款，否则订单过期。")
    @ApiImplicitParam(name = "record", required = true, dataType = "application/json",
            value = "预约挂号所需信息\n" +
                    "eg:\n" +
                    "{\n" +
                    "\t\"recordDate\": \"2020-06-10\",\n" +
                    "\t\"recordTime\": \"0\",\n" +
                    "\t\"doctorId\": \"10041\"\n" +
                    "}\n")
    @RequestMapping(value = "createRecord", method = RequestMethod.POST)
    public ResponseBuilder<Record> createRecord(@Validated @RequestBody RecordCreateDTO record,
                                                BindingResult bindingResult, ServletRequest request) {
        if (bindingResult.hasErrors()) {
            return new ResponseBuilder<>(ErrCodeEnum.ERR_ARG, bindingResult.getFieldError().getDefaultMessage());
        }
        Calendar calendar = Calendar.getInstance();
        Date now = calendar.getTime();
        Date future = calendar.getTime();
        future.setTime(future.getTime() + 7 * 86400 * 1000);
        Date objDate = null;
        try {
            objDate = simpleDateFormat.parse(record.getRecordDate());
        } catch (ParseException e) {
            return new ResponseBuilder<>(ErrCodeEnum.ERR_ARG, "预约日期有误或格式错误");
        }
        record.setRecordDate(simpleDateFormat.format(objDate));
        if (now.after(objDate) || future.before(objDate)) {
            return new ResponseBuilder<>(ErrCodeEnum.ERR_ARG, "可预约日期为明日起的一周内");
        }
        CurrentUserData data = (CurrentUserData) request.getAttribute("currentUser");
        if (data == null) return new ResponseBuilder<>(ErrCodeEnum.ERR_FAILED, "获取登录信息失败");
        record.setUserId(data.getId());
        calendar.setTime(objDate);
        record.setWorkday(((calendar.get(Calendar.DAY_OF_WEEK) + 5) % 7) + 1);
        Record r = userRecordService.setRecord(record);
        if (r == null || r.getDoctorName() == null) {
            return new ResponseBuilder<>(ErrCodeEnum.ERR_ARG, "参数有误");
        }
        BeanUtils.copyProperties(record, r);
        r.setRecordFare(5);
        r.setRecordCreateTime(now);
        r.setRecordStatus(RecordStatusEnum.RECORD_UNPAID);
        int recordId = userRecordService.createRecord(r);
        if (recordId == 0) return new ResponseBuilder<>(ErrCodeEnum.ERR_FAILED, "预约失败", r);
        //useGeneratedKey
//        r.setRecordId(String.valueOf(recordId));
        stringRedisTemplate.opsForValue().set(String.format("%s_unpaid", r.getRecordId()), "", 15*60, TimeUnit.SECONDS);
        return new ResponseBuilder<>(ErrCodeEnum.ERR_SUCCESS, "预约成功", r);
    }

    @ApiOperation(value = "支付挂号费",
            notes = "支付挂号费，接口调用成功errCode=0，挂号信息返回在data字段中。否则错误信息返回至errMsg\n此时返回信息中的序号为空，支付后得到实际序号")
    @ApiImplicitParam(name = "recordId", required = true,
            value = "预约挂号所需信息\n" +
                    "eg:\n" +
                    "{\n" +
                    "\t\"recordId\": \"1\",\n" +
                    "}\n")
    @RequestMapping(value = "payRecord", method = RequestMethod.POST)
    @Transactional
    public ResponseBuilder<Record> payRecord(String recordId, ServletRequest request){
        if(StringUtils.isEmpty(recordId)){
            return new ResponseBuilder<>(ErrCodeEnum.ERR_ARG,"参数不为空");
        }
        CurrentUserData data = (CurrentUserData) request.getAttribute("currentUser");
        if (data == null) return new ResponseBuilder<>(ErrCodeEnum.ERR_FAILED, "获取登录信息失败");
        Record record = userRecordService.getRecord(recordId);
        if(record == null)
            return new ResponseBuilder<>(ErrCodeEnum.ERR_FAILED, "参数有误");
        if(!Integer.valueOf(record.getUserId()).equals(Integer.valueOf(data.getId()))){
            return new ResponseBuilder<>(ErrCodeEnum.ERR_FAILED, "暂不支持代付");
        }
        if(BooleanUtils.isFalse(stringRedisTemplate.hasKey(String.format("%s_unpaid", recordId)))){
            return new ResponseBuilder<>(ErrCodeEnum.ERR_FAILED, "订单已过期或已支付");
        }
        //计算order
        String key = null;
        if(record.getRecordTime() == TimeEnum.TIME_AM)
            key = String.format("%s_%s_am", record.getDoctorId(), record.getRecordDate());
        else
            key = String.format("%s_%s_pm", record.getDoctorId(), record.getRecordDate());
        int amQuota, pmQuota, order;
        if (BooleanUtils.isTrue(stringRedisTemplate.hasKey(key))) {
            String leftString = stringRedisTemplate.opsForValue().get(key);
            String quotKey = String.format("%s_quota", record.getDoctorId());
            String quota = stringRedisTemplate.opsForValue().get(quotKey);
            if(quota == null || leftString == null)
                return new ResponseBuilder<>(ErrCodeEnum.ERR_FAILED, "请在选择医生页面重新预约");
            pmQuota = Integer.parseInt(quota) / 2;
            amQuota = Integer.parseInt(quota) - pmQuota;
            int left = Integer.parseInt(leftString);
            if(left == 0)
                return new ResponseBuilder<>(ErrCodeEnum.ERR_FAILED, "对不起，你来晚了，重新选择一位医生吧QaQ");
            order = (record.getRecordTime() == TimeEnum.TIME_AM? amQuota : pmQuota) - left + 1;
            //update left quota
            stringRedisTemplate.opsForValue().set(key,String.valueOf(left - 1),7 * 86400,TimeUnit.SECONDS);
        } else {
            return new ResponseBuilder<>(ErrCodeEnum.ERR_FAILED, "请在选择医生页面重新预约");
        }
        int re =userRecordService.payRecord(recordId,order);
        if(re == 0) return new ResponseBuilder<>(ErrCodeEnum.ERR_FAILED, "参数有误");
        record.setRecordOrder(order);
        record.setRecordStatus(RecordStatusEnum.RECORD_UNRESOLVED);
        //remove unpaid key
        stringRedisTemplate.delete(String.format("%s_unpaid", recordId));
        return new ResponseBuilder<>(ErrCodeEnum.ERR_SUCCESS,"支付成功", record);
    }

    @ApiOperation(value="用户查询自己的预约记录",
            notes = "用户查询预约记录，接口调用成功errCode=0，数据返回在data字段，否则错误信息返回至errMsg\n")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "p", value = "第几页 >0"),
            @ApiImplicitParam(name = "size", value = "每页的大小")
    })
    @RequestMapping(value = "listRecord", method = RequestMethod.POST)
    public ResponseBuilder<PageInfo<Record>> listRecord(int p, int size, ServletRequest request){
        CurrentUserData data = (CurrentUserData) request.getAttribute("currentUser");
        if (data == null) return new ResponseBuilder<>(ErrCodeEnum.ERR_FAILED, "获取登录信息失败");
        int id = Integer.parseInt(data.getId());
        System.out.println(userRecordService.checkExpireRecord(id));
        List<Record> recordList = userRecordService.listRecord(id,p,size);
        PageInfo<Record> pageInfo = new PageInfo<>(recordList);
        return new ResponseBuilder<>(ErrCodeEnum.ERR_SUCCESS, "查询成功", pageInfo);
    }


}
