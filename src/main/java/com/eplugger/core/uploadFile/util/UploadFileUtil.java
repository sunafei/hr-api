package com.eplugger.core.uploadFile.util;

import com.alibaba.fastjson.JSONObject;

import com.eplugger.core.consts.AppConst;
import com.eplugger.core.exception.SysException;
import com.eplugger.core.uploadFile.UploadFileConfig;
import com.eplugger.jpa.UploadFileJPA;
import com.eplugger.module.upload.UploadFile;
import com.eplugger.utils.PassMd5;
import com.eplugger.utils.ResourceUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

/**
 * 上传文件工具类
 */
@Component
public class UploadFileUtil {
    private static final Logger LOG = LoggerFactory.getLogger(UploadFileUtil.class);

    @Autowired
    private UploadFileConfig uploadFileConfig;

    @Autowired
    private UploadFileJPA uploadFileJPA;


    private static UploadFileUtil uploadFileUtil;

    @PostConstruct
    public void init() {
        uploadFileUtil = this;
    }

    /**
     * 获取已存储的附件
     * @param fileId 附件表对象id
     * @return 对应的上传文件
     */
    public static File getFile(String fileId){
        UploadFile uploadFile = uploadFileUtil.uploadFileJPA.getOne(fileId);
        return getFile(uploadFile);
    }

    /**
     * 获取已存储的附件
     * @param uploadFile 附件表对象
     * @return 对应的上传文件
     */
    public static File getFile(UploadFile uploadFile){
        File saveFile = getFilePosition(uploadFile.getId(), uploadFile.getStorageFileName());
        if(!saveFile.exists()){
            throw new SysException(uploadFileUtil,"无法在文件系统中找到指定的附件，file.id={},path={}",uploadFile.getId(),saveFile.getAbsolutePath());
        }
        return saveFile;
    }

    /**
     * 存储附件到文件系统及数据库附件表SYS_FILE_UPLOAD
     *
     * @param file 上传的附件
     * @return 存储入库后的附件对象
     */
    public static UploadFile convert2UploadFile(MultipartFile file){
        String fileDBId = UUID.randomUUID().toString();
        String storageFileName = UUID.randomUUID().toString();
        File saveFile = getFilePosition(fileDBId, storageFileName);
        if(!saveFile.getParentFile().exists()){
            saveFile.getParentFile().mkdirs();
        }
        try{
            file.transferTo(saveFile);
            UploadFile uf = new UploadFile();
            uf.setId(fileDBId);
            uf.setUploadFileName(file.getOriginalFilename());
            uf.setStorageFileName(storageFileName);
            uf.setFileSize(file.getSize());
            uf.setContentType(file.getContentType());
            uf.setBizEntityId(null);
            uf.setModuleId(null);
            uf.setEntityProperty(null);
            uf.setExtName(getExtName(file.getOriginalFilename()));
            uf.setCreateDate(System.currentTimeMillis());
            uf = uploadFileUtil.uploadFileJPA.save(uf);
            LOG.debug("[附件]已将文件 {} 存储到 {} 目录中,存储文件名为:{},数据库中的id为：{}",
                    new Object[] { uf.getStorageFileName(), saveFile.getAbsolutePath(), storageFileName, uf.getId() });
            return uf;
        }catch (IOException e){
            throw new SysException(uploadFileUtil, e, "无法存储附件，请联系管理员！");
        }
    }

    /**
     * 存储图片到文件系统，返回存储路径
     * @param file
     * @return 存储路径：/<saveRootPath>/img/...
     */
    public static String saveImage(MultipartFile file){
        File rootDir = getSavePath(uploadFileUtil.uploadFileConfig.getImgPath());
        if(!rootDir.exists()){
            rootDir.mkdirs();
        }
        String storageFileName = UUID.randomUUID().toString() + getExtName(file.getOriginalFilename());
        File saveFile = new File(rootDir,storageFileName);
        try {
            file.transferTo(saveFile);
            LOG.debug("[附件]已将图片 {} 存储到 {} 目录中,存储文件名为:{}", new Object[] { file.getOriginalFilename(), saveFile.getAbsolutePath(), storageFileName });
        } catch (IOException e) {
            throw new SysException(uploadFileUtil, e, "无法存储附件，请联系管理员！");
        }
        return ResourceUtils.getSubPath(saveFile, new File(AppConst.getWebPath()));
    }

    /**
     * 获得Json格式内容
     * 存储在业务字段中的内容。多个附件用","号分隔
     * @return json String
     * {
     *   name: 'img1.jpg', //文件名
     *   url: 'xxx-xxx' //uuid
     * }
     */
    public static JSONObject getJsonInfo(UploadFile uploadFile){
        JSONObject info = new JSONObject();
        info.put("name",uploadFile.getUploadFileName());
        info.put("extName",uploadFile.getExtName());
        info.put("id",uploadFile.getId());
        info.put("url", "/api/downFile/" + uploadFile.getId());
        return info;
    }

    public static MediaType getMediaType(String extName){
        Assert.notNull(extName,"扩展名不能为空");
        String eName = extName.toLowerCase();
        if("jpg".equals(eName) || "jpeg".equals(eName)){
            return MediaType.IMAGE_JPEG;
        }else if("png".equals(eName)){
            return MediaType.IMAGE_PNG;
        }else if("xml".equals(eName)){
            return MediaType.APPLICATION_XML;
        }else if("json".equals(eName)){
            return MediaType.APPLICATION_JSON;
        }else if("pdf".equals(eName)){
            return MediaType.APPLICATION_PDF;
        }else{
            return MediaType.APPLICATION_OCTET_STREAM;
        }
    }

    /**
     * 根据附件的数据库表存储id和文件系统存储名获得附件对象
     * @param fileDBId  数据库表存储id
     * @param storageFileName 文件系统存储名
     * @return 返回的File只是定位，不一定存在，需要通过exists进行判断
     */
    private static File getFilePosition(String fileDBId, String storageFileName){
        Assert.notNull(fileDBId, "要查找的附件必须提供附件存储的id");
        Assert.notNull(storageFileName, "要查找的附件必须提供文件系统存储名");
        File rootDir = getSavePath(uploadFileUtil.uploadFileConfig.getSavePath());
        String path = createFilePath(fileDBId,false);
        File saveDir = new File(rootDir, path);
        File saveFile = new File(saveDir, storageFileName);
        return saveFile;
    }


    /**
     * 获得服务器文件存储的根目录
     */
    private static File getSavePath(String savePathStr) {
//        String savePathStr = uploadFileUtil.uploadFileConfig.getSavePath();
        File savePath = null;
        try {
            savePath = ResourceUtils.getFile(savePathStr);
            if (!savePath.isDirectory()) {
                LOG.error("文件存放目录 {} 应为文件夹，请检查。", savePathStr);
            }
        } catch (FileNotFoundException e) {
            if (savePathStr.startsWith(ResourceUtils.CLASSPATH_URL_PREFIX)) {
                LOG.info("文件存放目录 {} 不存在，将进行创建。", savePathStr);
                try {
                    File classPath = ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX + "/");
                    String path = savePathStr.substring(ResourceUtils.CLASSPATH_URL_PREFIX.length());
                    savePath = new File(classPath, path);
                    savePath.mkdir();
                } catch (FileNotFoundException e1) {
                    LOG.error("系统无法自行创建文件存放目录 {} ，请手工创建。", savePathStr);
                }
            } else if (savePathStr.startsWith(ResourceUtils.WEBROOT_URL_PREFIX)) {
                LOG.info("文件存放目录 {} 不存在，将进行创建。", savePathStr);
                try {
                    File webPath = ResourceUtils.getFile(AppConst.getWebPath());
                    String path = savePathStr.substring(ResourceUtils.WEBROOT_URL_PREFIX.length());
                    savePath = new File(webPath, path);
                    savePath.mkdir();
                } catch (FileNotFoundException e1) {
                    LOG.error("系统无法自行创建文件存放目录 {} ，请手工创建。", savePathStr);
                }
            } else {
                LOG.error("文件存放目录 {} 不存在，请创建。", savePathStr);
            }
        }
        return savePath;
    }

    /**
     * 根据file id md5 产生file path
     * @param fileId 文件存储id
     * @param needServPath 是否生成服务访问路径
     * @return string
     *      needServPath = true ： http://xxx/aa/
     *      needServPath = false ： aa/
     */
    private static String createFilePath(String fileId,boolean needServPath){
        Assert.notNull(fileId,"必须提供附件的存储id");
        String pwdMd5 = PassMd5.encodeByMD5(fileId);
        String temp = pwdMd5.toLowerCase();
        String step1= temp.substring(0,2) ;
//        String step2= temp.substring(2,4) ; //降低目录层级，只保留一级目录
//        String step3= temp.substring(4);
        if(needServPath){
            String host = uploadFileUtil.uploadFileConfig.getDomain();
            return host + "/" + step1 ;// + "/" + step2 + "/" + step3 + "/";
        }else{
            return step1 ;// + "/" + step2 + "/" + step3 + "/";
        }
    }


    /**
     * 获取扩展名
     * @param fileName 上传的文件名
     * @return 文件扩展名，不包含"."
     */
    public static String getExtName(String fileName) {
        Assert.notNull(fileName,"上传文件名不能为空");
        return fileName.substring(fileName.lastIndexOf(".")+1);
    }
}
