package com.xupt.outpatientms.controller;

import com.xupt.outpatientms.bean.User;
import com.xupt.outpatientms.common.Token;
import com.xupt.outpatientms.enumeration.ErrCodeEnum;
import com.xupt.outpatientms.service.JwtService;
import com.xupt.outpatientms.service.QiniuService;
import com.xupt.outpatientms.service.UserService;
import com.xupt.outpatientms.util.ResponseBuilder;
import com.xupt.outpatientms.dto.UserLoginDTO;
import com.xupt.outpatientms.vo.UserVO;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

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
    public String register(@Validated @RequestBody User user, BindingResult bindingResult){
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

    @ApiOperation(value="login",
            notes = "用户登录接口，接口调用成功errCode=0，用户信息返回在data字段，否则错误信息返回至errMsg\n")
    @ApiImplicitParam(name = "user", required = true, dataType = "application/json",
            value = "用户登录\n"+
                    "eg:\n"+
                    "{\n" +
                    "\t\"userTel\": \"15955897607\",\n" +
                    "\t\"userPwd\": \"123456\"\n" +
                    "}\n")
    @RequestMapping(value = "login", method = RequestMethod.POST)
    public String login(@Validated @RequestBody UserLoginDTO user,
                        BindingResult bindingResult, HttpServletResponse response){
        if(bindingResult.hasErrors()){
            return new ResponseBuilder(ErrCodeEnum.ERR_ARG,bindingResult.getFieldError().getDefaultMessage()).build();
        }
        User u = userService.login(user.getUserTel(),user.getUserPwd());
        if(u!=null){
            //把token返回给客户端-->客户端保存至cookie-->客户端每次请求附带cookie参数
            Token token = jwtService.refreshToken(u.getUserId());
            System.out.println("token为："+token.getToken());
            response.setHeader("Access-Control-Expose-Headers", "Authorization");
            response.setHeader("Authorization","Bearer "+token.getToken());
            return new ResponseBuilder(ErrCodeEnum.ERR_SUCCESS, "登录成功！", new UserVO(u)).build();
        }else{
            return new ResponseBuilder(ErrCodeEnum.ERR_FAILED, "手机号或密码错误！").build();
        }
    }

    @ApiOperation(value="checkUserTelUnique",
            notes = "检查手机号是否注册过用户账号，接口调用成功errCode=0，结果保存在data字段，否则错误信息返回至errMsg\n")
    @ApiImplicitParam(name = "userTel", required = true, dataType = "application/json",
            value = "检查手机号是否注册过账号，\n返回json中data字段为0表示已注册，1表示未注册\n")
    @RequestMapping(value = "checkUserTelUnique", method = RequestMethod.POST)
    public String checkUserTelUnique(String userTel){
        if(!userTel.matches("^(13[0-9]|14[5|7]|15[0|1|2|3|5|6|7|8|9]|18[0|1|2|3|5|6|7|8|9])\\d{8}$")){
            return new ResponseBuilder(ErrCodeEnum.ERR_ARG, "手机号码格式错误").build();
        }
        if(userService.checkUserTelUnique(userTel)) return new ResponseBuilder(ErrCodeEnum.ERR_SUCCESS, "电话号码已存在", (Integer)0).build();
        else return new ResponseBuilder(ErrCodeEnum.ERR_SUCCESS, "该电话号码未注册", (Integer)1).build();
    }

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

}
