package com.xupt.outpatientms.controller;

import com.qiniu.common.QiniuException;
import com.xupt.outpatientms.common.CurrentUserData;
import com.xupt.outpatientms.enumeration.ErrCodeEnum;
import com.xupt.outpatientms.service.QiniuService;
import com.xupt.outpatientms.util.ResponseBuilder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletRequest;
import java.util.Map;

@Controller
@Api(tags = "文件上传相关接口")
public class FileUploadController {

    @Autowired
    private QiniuService qiniuService;

    @ApiOperation(value = "图片上传", notes = "上传成功则返回图片URL")
    @ApiImplicitParams(
            @ApiImplicitParam(name = "file", required = true, paramType = "form",
                    dataType = "file", value = "图片文件，仅接受png、jpg或jpeg格式")
    )
    @RequestMapping(value = "pictureUpload", method = RequestMethod.POST)
    @ResponseBody
    public ResponseBuilder<String> pictureUpload(@RequestParam("file") MultipartFile file){
        if(file == null || file.isEmpty()){
            return new ResponseBuilder<>(ErrCodeEnum.ERR_ARG, "请上传png、jpg或jpeg格式的图片");
        }
        String filename = file.getOriginalFilename();
        if(filename == null || !filename.endsWith(".png")
                &&!filename.endsWith(".jpg")
                &&!filename.endsWith(".jpeg")){
            return new ResponseBuilder<>(ErrCodeEnum.ERR_ARG,"仅支持png、jpg或jpeg格式的图片");
        }
        Map<String, Object> re = null;
        ResponseBuilder<String> rb = null;
        try {
            re = qiniuService.uploadFile(file);
        } catch (QiniuException e) {
            e.printStackTrace();
            rb = new ResponseBuilder<>(ErrCodeEnum.ERR_FAILED, "图片上传失败");
        }
        if(rb != null) return rb;
        Integer errCode = (Integer) re.get("errCode");
        String errMsg = (String)re.get("errMsg");
        String imageUrl = (String)re.get("imageUrl");
        if(errCode == ErrCodeEnum.ERR_SUCCESS.getErrCode()){
            return new ResponseBuilder<>(ErrCodeEnum.ERR_SUCCESS, "图片上传成功", imageUrl);
        }
        return new ResponseBuilder<>(ErrCodeEnum.ERR_FAILED, errMsg);
    }

}
