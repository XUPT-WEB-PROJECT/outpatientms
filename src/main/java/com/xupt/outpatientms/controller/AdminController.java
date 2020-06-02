package com.xupt.outpatientms.controller;

import com.xupt.outpatientms.bean.Admin;
import com.xupt.outpatientms.bean.Department;
import com.xupt.outpatientms.bean.Schedule;
import com.xupt.outpatientms.common.Token;
import com.xupt.outpatientms.dto.ScheduleDTO;
import com.xupt.outpatientms.enumeration.ErrCodeEnum;
import com.xupt.outpatientms.service.AdminService;
import com.xupt.outpatientms.service.JwtService;
import com.xupt.outpatientms.util.ResponseBuilder;
import com.xupt.outpatientms.vo.AdminChoseDoctorVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/admin")
@Api(tags = "管理员端接口")
public class AdminController {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AdminService adminService;

    @ApiOperation(value="管理员登录",
            notes = "管理员登录接口，接口调用成功errCode=0，管理员信息返回在data字段，token在Response Headers\"authorization\"字段中。否则错误信息返回至errMsg\n")
    @ApiImplicitParam(name = "admin", required = true, dataType = "application/json",
            value = "管理员登录\n"+
                    "eg:\n"+
                    "{\n" +
                    "\t\"adminName\": \"kafm\",\n" +
                    "\t\"adminPwd\": \"123456\"\n" +
                    "}\n")
    @RequestMapping(value = "login", method = RequestMethod.POST)
    public ResponseBuilder<Admin> login(@Validated @RequestBody Admin admin,
                                         BindingResult bindingResult, HttpServletResponse response){
        if(bindingResult.hasErrors()){
            return new ResponseBuilder<>(ErrCodeEnum.ERR_FAILED,"管理员名或密码错误");
        }
        Admin adminUser = adminService.login(admin.getAdminName(), admin.getAdminPwd());
        if(adminUser!=null){
            //把token返回给客户端 客户端请求再header中验证token
            Token token = jwtService.refreshToken(adminUser.getAdminName());
            response.setHeader("Access-Control-Expose-Headers", "Authorization");
            response.setHeader("Authorization","Bearer "+token.getToken());
            return new ResponseBuilder<>(ErrCodeEnum.ERR_SUCCESS, "登录成功", adminUser);
        }else{
            return new ResponseBuilder<>(ErrCodeEnum.ERR_FAILED, "用户名或密码错误");
        }
    }

    @ApiOperation(value="管理员获取科室信息",
            notes = "获取科室信息接口，用于选择科室，接口调用成功errCode=0，科室信息返回在data字段。\n")
    @RequestMapping(value = "choseDepartment", method = RequestMethod.GET)
    public ResponseBuilder<List<Department>> choseDepartment() {
        return new ResponseBuilder<>(ErrCodeEnum.ERR_SUCCESS, "查询成功", adminService.choseDepartment());
    }

    @ApiOperation(value="管理员获取科室全部医生信息，用于排班",
            notes = "获取医生信息接口，用于为医生排班，接口调用成功errCode=0，否则错误信息保存在errMsg中。\n")
    @ApiImplicitParam(name = "departmentName", value = "eg:呼吸科")
    @RequestMapping(value = "choseDoctor/{departmentName}", method = RequestMethod.GET)
    public ResponseBuilder<List<AdminChoseDoctorVO>> choseDoctor(@PathVariable(value = "departmentName") String departmentName) {
        if(!adminService.checkDepartment(departmentName)) return new ResponseBuilder<>(ErrCodeEnum.ERR_ARG, "请检查科室名");
        return new ResponseBuilder<>(ErrCodeEnum.ERR_SUCCESS, "查询成功", adminService.choseDoctor(departmentName));
    }

    @ApiOperation(value="查看科室排班情况",
            notes = "接口调用成功errCode=0，排班信息返回在data字段，否则错误信息返回至errMsg\n")
    @RequestMapping(value = "getSchedule/{departmentName}", method = RequestMethod.GET)
    public ResponseBuilder<List<Schedule>> getSchedule(@PathVariable(value = "departmentName") String departmentName){
        if(!adminService.checkDepartment(departmentName)) return new ResponseBuilder<>(ErrCodeEnum.ERR_ARG, "请检查科室名");
        return new ResponseBuilder<>(ErrCodeEnum.ERR_SUCCESS, "查询成功", adminService.getSchedule(departmentName));
    }

    @ApiOperation(value="为医生添加排班记录",
            notes = "接口调用成功errCode=0，添加的信息返回至data字段，否则错误信息返回至errMsg\n")
    @ApiImplicitParam(name = "schedule", dataType = "application/json", required = true,
            value = "eg:\n"+
                    "{\n" +
                    "\t\"departmentName\": \"内科\",\n" +
                    "\t\"doctorId\": \"0000010041\",\n" +
                    "\t\"workday\": \"1\"\n" +
                    "}\n")
    @RequestMapping(value = "addSchedule", method = RequestMethod.POST)
    public ResponseBuilder<Schedule> addSchedule(@Validated @RequestBody ScheduleDTO schedule, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return new ResponseBuilder<>(ErrCodeEnum.ERR_ARG,bindingResult.getFieldError().getDefaultMessage());
        }
        Schedule s = adminService.checkSchedule(schedule);
        if(s == null) return new ResponseBuilder<>(ErrCodeEnum.ERR_FAILED, "参数有误");
        else {
            int res = adminService.addSchedule(s);
            if(res == 0) return new ResponseBuilder<>(ErrCodeEnum.ERR_FAILED, "该医生已在当日值班");
        }
        return new ResponseBuilder<>(ErrCodeEnum.ERR_SUCCESS, "添加成功", s);
    }

    @ApiOperation(value="删除一条排班记录",
            notes = "接口调用成功errCode=0，删除的信息返回值data字段，否则错误信息返回至errMsg\n")
    @ApiImplicitParam(name = "schedule", dataType = "application/json", required = true,
            value = "eg:\n"+
                    "{\n" +
                    "\t\"departmentName\": \"内科\",\n" +
                    "\t\"doctorId\": \"内科医生1\",\n" +
                    "\t\"weekDay\": \"1\"\n" +
                    "}\n")
    @RequestMapping(value = "delSchedule", method = RequestMethod.POST)
    public ResponseBuilder<ScheduleDTO> delSchedule(@Validated @RequestBody ScheduleDTO schedule, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return new ResponseBuilder<>(ErrCodeEnum.ERR_ARG,bindingResult.getFieldError().getDefaultMessage());
        }
        int re = adminService.delSchedule(schedule);
        if(re == 0) return new ResponseBuilder<>(ErrCodeEnum.ERR_FAILED, "参数有误");
        return new ResponseBuilder<>(ErrCodeEnum.ERR_SUCCESS, "删除成功", schedule);
    }

}
