package com.xupt.outpatientms.controller;

import com.qiniu.common.QiniuException;
import com.xupt.outpatientms.bean.User;
import com.xupt.outpatientms.common.CurrentUserData;
import com.xupt.outpatientms.common.Token;
import com.xupt.outpatientms.dto.UserLoginDTO;
import com.xupt.outpatientms.dto.UserRegisterDTO;
import com.xupt.outpatientms.enumeration.ErrCodeEnum;
import com.xupt.outpatientms.service.JwtService;
import com.xupt.outpatientms.service.QiniuService;
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

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RestController
@RequestMapping("/user")
@Api(tags = "患者端相关接口")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private QiniuService qiniuService;

    @ApiOperation(value="用户注册",
            notes = "用户注册接口,注册成功errCode=0，否则错误信息返回至errMsg\n"+
                    "")
    @ApiImplicitParam(name = "user",dataType = "application/json", required = true,
            value = "用户注册信息\neg:\n"+
                    "{\n" +
                    "    \"userTel\": \"15955897607\", \n" +
                    "    \"userPwd\": \"123456\", \n" +
                    "    \"userName\": \"kafm\", \n" +
                    "    \"userGender\": \"女\"\n" +
                    "}"
            )
    @RequestMapping(value = "register", method = RequestMethod.POST)
    public ResponseBuilder<Object> register(@Validated @RequestBody UserRegisterDTO user, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return new ResponseBuilder(ErrCodeEnum.ERR_ARG,bindingResult.getFieldError().getDefaultMessage());
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
            return new ResponseBuilder(ErrCodeEnum.ERR_FAILED,"用户名或密码错误");
        }
        User u = userService.login(user.getUserTel(),user.getUserPwd());
        if(u!=null){
            //把token返回给客户端-->客户端保存至cookie-->客户端每次请求附带cookie参数
            Token token = jwtService.refreshToken(u.getUserId());
            response.setHeader("Access-Control-Expose-Headers", "Authorization");
            response.setHeader("Authorization","Bearer "+token.getToken());
            return new ResponseBuilder(ErrCodeEnum.ERR_SUCCESS, "登录成功！", new UserVO(u));
        }else{
            return new ResponseBuilder(ErrCodeEnum.ERR_FAILED, "手机号或密码错误！");
        }
    }

    @ApiOperation(value="检查手机号是否注册",
            notes = "检查手机号是否注册过用户账号，接口调用成功errCode=0，结果保存在data字段，否则错误信息返回至errMsg\n")
    @ApiImplicitParam(name = "userTel", required = true, paramType = "query",
            value = "检查手机号是否注册过账号，\n返回json中data字段为0表示已注册，1表示未注册；请求信息eg:{\n" +
                    "\"userTel\": \"15955897607\"}\n")
    @RequestMapping(value = "checkUserTelUnique", method = RequestMethod.POST)
    public ResponseBuilder<Integer> checkUserTelUnique(String userTel){
        if(!userTel.matches("^(13[0-9]|14[5|7]|15[0|1|2|3|5|6|7|8|9]|18[0|1|2|3|5|6|7|8|9])\\d{8}$")){
            return new ResponseBuilder(ErrCodeEnum.ERR_ARG, "手机号码格式错误");
        }
        if(userService.checkUserTelUnique(userTel)) return new ResponseBuilder(ErrCodeEnum.ERR_SUCCESS, "电话号码已存在", 0);
        else return new ResponseBuilder(ErrCodeEnum.ERR_SUCCESS, "该电话号码未注册", 1);
    }

    @ApiOperation(value = "更换头像", notes = "用户更换头像")
    @ApiImplicitParams(
            @ApiImplicitParam(name = "avatarFile", required = true, paramType = "form",
                    dataType = "file", value = "新头像文件，仅接受png、jpg或jpeg格式")
    )
    @RequestMapping(value = "newAvatar", method = RequestMethod.POST)
    public ResponseBuilder<Object> newAvatar(MultipartFile avatarFile, ServletRequest request){
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

}
