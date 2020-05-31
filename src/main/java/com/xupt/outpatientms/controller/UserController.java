package com.xupt.outpatientms.controller;

import com.qiniu.common.QiniuException;
import com.xupt.outpatientms.bean.User;
import com.xupt.outpatientms.enumeration.ErrCodeEnum;
import com.xupt.outpatientms.service.QiniuService;
import com.xupt.outpatientms.service.UserService;
import com.xupt.outpatientms.util.ResponseBuilder;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private QiniuService qiniuService;

    @ApiOperation(value="register",
            notes = "用户注册接口,注册成功errCode=0，否则错误信息返回至errMsg\n"+
                    "")
    @ApiImplicitParam(name = "user",dataType = "application/json", required = true,paramType = "application/json",
            value = "用户注册信息\nuserName, userTel, userPwd为必选项\n"+
                    "userGender,userAge为可选项\n"+
                    "eg:\n"+
                        "{\n" +
                        "\t\"userName\": \"kafm\",\n" +
                        "\t\"userTel\": \"15955897607\",\n" +
                        "\t\"userPwd\": \"123456\"\n" +
                        "}\n" +
                    "userName: 长度4~12\n" +
                    "userPwd: 密码长度在6~18之间，只能包含字母、数字和下划线\n" +
                    "userAge: 取值0~120\n" +
                    "userGender: 0(男)1(女)2(默认保密)数字汉字均可\n")
    @RequestMapping(value = "register", method = RequestMethod.POST)
    public String register(@Validated @RequestBody User user, BindingResult bindingResult, HttpServletResponse response){
        if(bindingResult.hasErrors()){
            return new ResponseBuilder(ErrCodeEnum.ERR_ARG,bindingResult.getFieldError().getDefaultMessage()).build();
        }
        int re = -1;
        ResponseBuilder rb = new ResponseBuilder(ErrCodeEnum.ERR_SUCCESS,"注册成功");
        try {
            re = userService.addUser(user);
            if(re != 1){
                rb = new ResponseBuilder(ErrCodeEnum.ERR_FAILED);
            }
        }catch (DataAccessException e){
            e.printStackTrace();
            rb = new ResponseBuilder(ErrCodeEnum.ERR_FAILED,"该电话号码已被注册");
        }
        return rb.build();
    }

    public String checkUserTelUnique(@Validated String userTel){

    }

<<<<<<< Updated upstream
//    @ApiOperation(value = "newAvatar", notes = "更换头像")
//    @ApiImplicitParam(name = "avatar", dataType = "application/json", required = true,
//                        paramType = "multipart/form-data", value = "新头像文件，仅接受png、jpg或jpeg格式")
//    public String newAvatar(MultipartFile avatarFile){
//        String filename = avatarFile.getName();
//        if(!filename.endsWith(".png")
//                &&!filename.endsWith(".jpg")
//                &&!filename.endsWith(".jpeg")){
//            return new ResponseBuilder(ErrCodeEnum.ERR_ARG,"仅支持png、jpg或jpeg格式的图片").build();
//        }
//        Map re = null;
//        ResponseBuilder rb = null;
//        try {
//            re = qiniuService.uploadFile(avatarFile);
//        } catch (QiniuException e) {
//            e.printStackTrace();
//            rb = new ResponseBuilder(ErrCodeEnum.ERR_FAILED, "图片上传失败");
//        }
//        if(rb != null) return rb.build();
//        Integer errCode = (Integer) re.get("errCode");
//        String errMsg = (String)re.get("errMsg");
//        if(errCode == ErrCodeEnum.ERR_SUCCESS.getErrCode()){
//            userService.newAvatar(,re.get("imageName"));
//        }
//        return new ResponseBuilder(errCode, errMsg).build();
//    }
=======
    @ApiOperation(value = "更换头像", notes = "用户更换头像")
    @ApiImplicitParams(
            @ApiImplicitParam(name = "avatarFile", required = true, paramType = "form",
                    dataType = "file", value = "新头像文件，仅接受png、jpg或jpeg格式")
    )
    @RequestMapping(value = "newAvatar", method = RequestMethod.POST)
    public ResponseBuilder<Object> newAvatar(MultipartFile avatarFile, ServletRequest request){
        if(avatarFile == null || avatarFile.isEmpty()){
            return new ResponseBuilder(ErrCodeEnum.ERR_ARG,"请上传png、jpg或jpeg格式的图片");
        }
        String filename = avatarFile.getOriginalFilename();
        if(filename == null || !filename.endsWith(".png")
                &&!filename.endsWith(".jpg")
                &&!filename.endsWith(".jpeg")){
            return new ResponseBuilder(ErrCodeEnum.ERR_ARG,"仅支持png、jpg或jpeg格式的图片");
        }
        Map re = null;
        ResponseBuilder rb = null;
        try {
            re = qiniuService.uploadFile(avatarFile);
        } catch (QiniuException e) {
            e.printStackTrace();
            rb = new ResponseBuilder(ErrCodeEnum.ERR_FAILED, "图片上传失败");
        }
        if(rb != null) return rb;
        Integer errCode = (Integer) re.get("errCode");
        String errMsg = (String)re.get("errMsg");
        if(errCode == ErrCodeEnum.ERR_SUCCESS.getErrCode()){
            String userId = ((CurrentUserData)request.getAttribute("currentUser")).getUserId();
            userService.newAvatar(userId,(String)re.get("imageName"));
        }
        return new ResponseBuilder(errCode, errMsg);
    }
>>>>>>> Stashed changes

}
