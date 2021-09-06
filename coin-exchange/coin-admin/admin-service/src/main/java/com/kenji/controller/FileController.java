package com.kenji.controller;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.oss.OSS;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.MatchMode;
import com.aliyun.oss.model.PolicyConditions;
import com.kenji.model.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;


/**
 * @Author Kenji
 * @Date 2021/8/18 22:04
 * @Description
 */
@RestController
@Api(tags = "文件上传")
public class FileController {

    @Autowired
    private OSS ossClient;

    @Value("${oss.bucket.name:coin-exchanage}")
    private String bucketName;

    @Value("${spring.cloud.alicloud.oss.endpoint}")
    private String endPoint;

    @Value("${spring.cloud.alicloud.access-key}")
    private String accessId;

    @Value("${oss.callback.url:http://kenji.gz2vip.idcfengye.com}")
    private String ossCallbackUrl;

    /**
     * 文件上传
     *
     * @param file
     * @return
     */
    @PostMapping("/image/AliYunImgUpload")
    @ApiOperation(value = "图片上传")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "file", value = "上传的文件"),
    })
    public R<String> uploadImages(@RequestParam("file") MultipartFile file) throws IOException {
        /**
         * 三个参数
         * 1、bucketName
         * 2、FileName
         * 3、文件的路径
         */
        String originalFileName = file.getOriginalFilename();
        String fileName = DateUtil.today().replace("-", "/") + "/" + UUID.randomUUID() + originalFileName.substring(originalFileName.indexOf("."));
        ossClient.putObject(bucketName, fileName, file.getInputStream());
        //上传成功的地址：https://coin-exchanage.oss-cn-guangzhou.aliyuncs.com/2021/08/18/0053abf4-9801-474e-ac99-2a928bae78ac.jpg
        return R.ok("https://" + bucketName + "." + endPoint + "/" + fileName);
    }

    /**
     * 身份证图片上传  web直传
     * 1、前台访问后台获取票据
     * 2、前台使用该票据直接去上传图片
     */
    @ApiOperation("身份证图片上传")
    @GetMapping("/image/pre/upload")
    public R uploadIdCard() {
        String dir = DateUtil.today().replace("-", "/");
        Map<String, String> uploadPolicy = getUploadPolicy(30L, 3 * 1024L * 1024L, dir);
        return R.ok(uploadPolicy);
    }

    /**
     * web直传
     *
     * @param expireTime  票据的过期时间
     * @param maxFileSize 最大文件大小
     * @param dir         文件上传的文件夹
     * @return
     */
    private Map<String, String> getUploadPolicy(long expireTime, long maxFileSize, String dir) {
        long expireEndTime = System.currentTimeMillis() + expireTime * 1000;
        Date expiration = new Date(expireEndTime); // 过期时间
        // PostObject 请求最大可支持的文件大小为 5 GB，即 CONTENT_LENGTH_RANGE 为5 * 1024 * 1024 * 1024。
        PolicyConditions policyConds = new PolicyConditions();
        // 设置上传的大小 ,最大为 3M
        policyConds.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, maxFileSize);
        // 上传的到那个文件夹
        policyConds.addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY, dir);
        try {
            // 生成一个票据
            String postPolicy = ossClient.generatePostPolicy(expiration, policyConds);
            // 对票据加密
            byte[] binaryData = postPolicy.getBytes("utf-8");
            String encodedPolicy = BinaryUtil.toBase64String(binaryData);
            String postSignature = ossClient.calculatePostSignature(postPolicy);
            // 返回给前端的值
            Map<String, String> respMap = new LinkedHashMap<String, String>();
            respMap.put("accessid", accessId);
            respMap.put("policy", encodedPolicy);
            respMap.put("signature", postSignature);
            respMap.put("dir", dir);
            respMap.put("host", "https://" + bucketName + "." + endPoint); // host 的格式为 https://bucketname.endpoint
            respMap.put("expire", String.valueOf(expireEndTime / 1000));
            JSONObject jasonCallback = new JSONObject();
            jasonCallback.put("callbackUrl", ossCallbackUrl); // 前端上传成功了,可以使用该地址来通知后端.
            jasonCallback.put("callbackBody",
                    "filename=${object}&size=${size}&mimeType=${mimeType}&height=${imageInfo.height}&width=${imageInfo.width}");
            jasonCallback.put("callbackBodyType", "application/x-www-form-urlencoded");
            String base64CallbackBody = BinaryUtil.toBase64String(jasonCallback.toString().getBytes());
            respMap.put("callback", base64CallbackBody);
            return respMap;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
