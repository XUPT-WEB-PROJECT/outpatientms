package com.xupt.outpatientms.service;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import com.xupt.outpatientms.config.QiniuCloudConfig;
import com.xupt.outpatientms.enumeration.ErrCodeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class QiniuService {

    @Autowired
    private UploadManager uploadManager;

    @Autowired
    private Auth auth;

    private static final String bucket = "ylmzglxt";

    private static final String baseUrl = "http://qax0m7o02.bkt.clouddn.com/";

    private StringMap putPolicy;

    public Map<String,Object> uploadFile(File file) throws QiniuException {
        Map<String,Object> map = new HashMap();
        Response response = this.uploadManager.put(file,null,getUploadToken());
        //解析上传的结果
        DefaultPutRet putRet = new Gson().fromJson(response.bodyString(),DefaultPutRet.class);

        String imageName = putRet.hash;
        int retry = 0;
        while(response.needRetry() && retry < 3){
            response = this.uploadManager.put(file,null,getUploadToken());
            retry++;
        }
        if(response.isOK()){
            map.put("errCode", ErrCodeEnum.ERR_SUCCESS.getErrCode());
            map.put("errMsg","上传成功");
        }else{
            map.put("errCode",ErrCodeEnum.ERR_FAILED.getErrCode());
            map.put("errMsg",response.error);
        }
        map.put("imageUrl",baseUrl + imageName);
        return map;
    };

    public Map<String, Object> uploadFile(MultipartFile multipartFile) throws QiniuException {
        File file = new File(QiniuCloudConfig.baseUploadUrl+multipartFile.getName());
        try {
            multipartFile.transferTo(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return uploadFile(file);
    };

    private String getUploadToken(){
        return this.auth.uploadToken(bucket,null,3600,putPolicy);
    }

}
