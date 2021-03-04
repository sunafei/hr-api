package com.eplugger.core.uploadFile;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 上传文件配置信息
 */
@Data
@ConfigurationProperties(prefix = "upload.file")
@Component
public class UploadFileConfig {
    //请求服务地址
    private String domain;
    //存储的根目录
    private String savePath;
    //图片文件路径
    private String imgPath;
}
