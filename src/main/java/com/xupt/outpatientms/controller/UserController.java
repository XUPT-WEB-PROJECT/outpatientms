package com.xupt.outpatientms.controller;

import com.qiniu.common.QiniuException;
import com.xupt.outpatientms.bean.User;
import com.xupt.outpatientms.common.CurrentUserData;
import com.xupt.outpatientms.common.Token;
import com.xupt.outpatientms.dto.UserLoginDTO;
import com.xupt.outpatientms.dto.UserRegisterDTO;
import com.xupt.outpatientms.dto.UserUpdateDTO;
import com.xupt.outpatientms.enumeration.ErrCodeEnum;
import com.xupt.outpatientms.service.JwtService;
import com.xupt.outpatientms.service.QiniuService;
import com.xupt.outpatientms.service.SmsMsgsService;
import com.xupt.outpatientms.service.UserService;
import com.xupt.outpatientms.util.ResponseBuilder;
import com.xupt.outpatientms.vo.UserVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RestController
@RequestMapping("/user")
@Api(tags = "患者端相关接口")
public class UserController {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserService userService;

    @Autowired
    private QiniuService qiniuService;

    @Autowired
    private SmsMsgsService smsMsgsService;


    @ApiOperation(value="用户注册",
            notes = "用户注册接口，需要先请求/smsmsgs/sendCode发送验证码并提交验证码，注册成功errCode=0，否则错误信息返回至errMsg\n")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "user",dataType = "application/json", required = true,
                    value = "用户注册信息\neg:\n"+
                            "{\n" +
                            "    \"userTel\": \"15955897607\", \n" +
                            "    \"userPwd\": \"123456\", \n" +
                            "    \"userName\": \"kafm\", \n" +
                            "    \"userGender\": \"女\", \n" +
                            "    \"code\": \"xxx\"\n" +
                            "}"
            )
    })
    @RequestMapping(value = "register", method = RequestMethod.POST)
    public ResponseBuilder<Object> register(@Validated @RequestBody UserRegisterDTO user, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return new ResponseBuilder<>(ErrCodeEnum.ERR_ARG,bindingResult.getFieldError().getDefaultMessage());
        }
        if(!smsMsgsService.check(user.getUserTel(), user.getCode())){
            return new ResponseBuilder<>(ErrCodeEnum.ERR_FAILED,"验证码错误");
        }
        int re = -1;
        ResponseBuilder<Object> rb = new ResponseBuilder<>(ErrCodeEnum.ERR_SUCCESS,"注册成功");
        try {
            re = userService.addUser(user);
            if(re != 1){
                rb = new ResponseBuilder<>(ErrCodeEnum.ERR_FAILED);
            }
        }catch (DataAccessException e){
            if(e instanceof org.springframework.dao.DuplicateKeyException){
                rb = new ResponseBuilder<>(ErrCodeEnum.ERR_FAILED,"该电话号码已被注册");
            }
            else {
                e.printStackTrace();
                rb = new ResponseBuilder<>(ErrCodeEnum.ERR_FAILED,"未知错误：" + e.getClass().getName());
            }
        }
        return rb;
    }

    @ApiOperation(value="用户登录",
            notes = "用户登录接口，接口调用成功errCode=0，用户信息返回在data字段，token在Response Headers\"authorization\"字段中。否则错误信息返回至errMsg\n")
    @ApiImplicitParam(name = "user", required = true, dataType = "application/json",
            value = "用户登录\n"+
                    "eg:\n"+
                    "{\n" +
                    "\t\"userTel\": \"15955897607\",\n" +
                    "\t\"userPwd\": \"123456\"\n" +
                    "}\n")
    @RequestMapping(value = "login", method = RequestMethod.POST)
    public ResponseBuilder<UserVO> login(@Validated @RequestBody UserLoginDTO user,
                        BindingResult bindingResult, HttpServletResponse response){
        if(bindingResult.hasErrors()){
            return new ResponseBuilder<>(ErrCodeEnum.ERR_FAILED,"用户名或密码错误");
        }
        User u = userService.login(user.getUserTel(),user.getUserPwd());
        if(u!=null){
            //把token返回给客户端 客户端请求再header中验证token
            Token token = jwtService.refreshToken(u.getUserId());
            response.setHeader("Access-Control-Expose-Headers", "Authorization");
            response.setHeader("Authorization","Bearer "+token.getToken());
            return new ResponseBuilder<>(ErrCodeEnum.ERR_SUCCESS, "登录成功", new UserVO(u));
        }else{
            return new ResponseBuilder<>(ErrCodeEnum.ERR_FAILED, "手机号或密码错误");
        }
    }

    @ApiOperation(value="检查手机号是否注册",
            notes = "检查手机号是否注册过用户账号，接口调用成功errCode=0，结果保存在data字段，否则错误信息返回至errMsg\n")
    @ApiImplicitParam(name = "userTel", required = true, paramType = "query",
            value = "检查手机号是否注册过账号，\n返回json中data字段为0表示已注册，1表示未注册；请求信息eg: \n" +
                    "15955897607\n")
    @RequestMapping(value = "checkUserTelUnique", method = RequestMethod.GET)
    public ResponseBuilder<Integer> checkUserTelUnique(String userTel){
        if(!userTel.matches("^(13[0-9]|14[5|7]|15[0|1|2|3|5|6|7|8|9]|18[0|1|2|3|5|6|7|8|9])\\d{8}$")){
            return new ResponseBuilder<>(ErrCodeEnum.ERR_ARG, "手机号码有误");
        }
        if(userService.checkUserTelUnique(userTel)) return new ResponseBuilder<>(ErrCodeEnum.ERR_SUCCESS, "该电话号码已注册", 0);
        else return new ResponseBuilder<>(ErrCodeEnum.ERR_SUCCESS, "该电话号码未注册", 1);
    }

    @ApiOperation(value = "更换头像", notes = "用户更换头像，成功则返回新头像URL")
    @ApiImplicitParams(
            @ApiImplicitParam(name = "avatarFile", required = true, paramType = "form",
                    dataType = "file", value = "新头像文件，仅接受png、jpg或jpeg格式")
    )
    @RequestMapping(value = "newAvatar", method = RequestMethod.POST)
    public ResponseBuilder<String> newAvatar(@RequestParam("avatarFile") MultipartFile avatarFile, ServletRequest request){
        if(avatarFile == null || avatarFile.isEmpty()){
            return new ResponseBuilder<>(ErrCodeEnum.ERR_ARG, "请上传png、jpg或jpeg格式的图片");
        }
        String filename = avatarFile.getOriginalFilename();
        if(filename == null || !filename.endsWith(".png")
                &&!filename.endsWith(".jpg")
                &&!filename.endsWith(".jpeg")){
            return new ResponseBuilder<>(ErrCodeEnum.ERR_ARG,"仅支持png、jpg或jpeg格式的图片");
        }
        Map<String,Object> re = null;
        ResponseBuilder<String> rb = null;
        try {
            re = qiniuService.uploadFile(avatarFile);
        } catch (QiniuException e) {
            e.printStackTrace();
            rb = new ResponseBuilder<>(ErrCodeEnum.ERR_FAILED, "图片上传失败");
        }
        if(rb != null) return rb;
        Integer errCode = (Integer) re.get("errCode");
        String errMsg = (String)re.get("errMsg");
        String newAvatarUrl = (String)re.get("imageUrl");
        if(errCode == ErrCodeEnum.ERR_SUCCESS.getErrCode()){
            CurrentUserData userData = ((CurrentUserData)request.getAttribute("currentUser"));
            if(userData == null) return new ResponseBuilder<>(ErrCodeEnum.ERR_FAILED, "获取登录信息失败");
            String userId = userData.getId();
            if(!userService.newAvatar(userId,newAvatarUrl)){
                return new ResponseBuilder<>(ErrCodeEnum.ERR_FAILED, "头像变更失败");
            }
            return new ResponseBuilder<>(ErrCodeEnum.ERR_SUCCESS, "头像变更成功", newAvatarUrl);
        }
        return new ResponseBuilder<>(ErrCodeEnum.ERR_FAILED, errMsg);
    }

    @ApiOperation(value = "更新信息", notes = "用户更换个人信息，更新成功errCode = 0，所更新的用户信息返回至data字段，否则错误信息保存在errMsg中")
    @ApiImplicitParams(
            @ApiImplicitParam(name = "user", required = true, dataType = "application/json",
                    value = "用户信息修改。\n密码、性别、手机号和年龄均为可选项\n，修改密码需要提供旧密码。\neg:"+
                            "{\n" +
                            "    \"userTel\": \"15955897607\", \n" +
                            "    \"newPwd\": \"654321\", \n" +
                            "    \"oldPwd\": \"123456\", \n" +
                            "    \"userGender\": \"男\"\n" +
                            "}"
                    ))
    @RequestMapping(value = "updateUser", method = RequestMethod.POST)
    public ResponseBuilder<UserVO> updateUser(@Validated @RequestBody UserUpdateDTO user,
                                              BindingResult bindingResult, ServletRequest request){
        if(bindingResult.hasErrors()){
            return new ResponseBuilder<>(ErrCodeEnum.ERR_ARG,bindingResult.getFieldError().getDefaultMessage());
        }
        if(user.getUserAge() == null
                && user.getUserGender()== null
                && user.getUserTel() == null
                && user.getNewPwd() == null
        ){
            return new ResponseBuilder<>(ErrCodeEnum.ERR_ARG, "请上传有效的用户信息");
        }
        if(user.getNewPwd() != null && user.getOldPwd() == null){
            return new ResponseBuilder<>(ErrCodeEnum.ERR_FAILED, "原密码错误");
        }
        CurrentUserData userData = ((CurrentUserData)request.getAttribute("currentUser"));
        if(userData == null) return new ResponseBuilder<>(ErrCodeEnum.ERR_FAILED, "获取登录信息失败");
        user.setUserId(userData.getId());
        try {
            if(userService.updateUser(user)){
                return new ResponseBuilder<>(ErrCodeEnum.ERR_SUCCESS, "更新成功", getUserInfo(request).getData());
            }
        }catch (DataAccessException e){
            if(e instanceof org.springframework.dao.DuplicateKeyException){
                return new ResponseBuilder<>(ErrCodeEnum.ERR_FAILED,"该电话号码已经注册");
            }
            else {
                e.printStackTrace();
                return new ResponseBuilder<>(ErrCodeEnum.ERR_FAILED,"未知错误：" + e.getClass().getName());
            }
        }
        if(user.getNewPwd() != null) return new ResponseBuilder<>(ErrCodeEnum.ERR_FAILED, "原密码错误");
        else return new ResponseBuilder<>(ErrCodeEnum.ERR_FAILED, "更新失败");
    }

    @ApiOperation(value = "获取用户信息", notes = "根据token获取当前用户信息。成功errCode = 0，用户信息返回至data字段，否则错误信息保存在errMsg中")
    @RequestMapping(value = "getUserInfo", method = RequestMethod.POST)
    public ResponseBuilder<UserVO> getUserInfo(ServletRequest request){
        CurrentUserData userData = ((CurrentUserData)request.getAttribute("currentUser"));
        if(userData == null) return new ResponseBuilder<>(ErrCodeEnum.ERR_FAILED, "获取登录信息失败");
        return new ResponseBuilder<>(ErrCodeEnum.ERR_SUCCESS, "查询成功", userService.getUserInfo(Integer.valueOf(userData.getId())));
    }

}
