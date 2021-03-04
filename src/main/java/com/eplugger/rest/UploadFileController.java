package com.eplugger.rest;

import com.eplugger.core.consts.ReqConst;
import com.eplugger.core.result.JsonResult;
import com.eplugger.core.result.RestResult;
import com.eplugger.core.uploadFile.util.UploadFileUtil;
import com.eplugger.jpa.UploadFileJPA;
import com.eplugger.module.upload.UploadFile;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 附件上传服务接口,适合于单独上传一个附件信息，而不是接收整个表单
 *
 * @since 2018/2/28
 * @since 2018/3/7 调整接收和返回格式，当前不支持多个表单项的一同上传，因为没有做name分辨
 */
@Api("附件上传服务")
@RestController
@RequestMapping(value = "/api")
public class UploadFileController {
    private static final Logger LOG = LoggerFactory.getLogger(UploadFileController.class);

	@Autowired
	private UploadFileJPA uploadFileJPA;

	@ApiOperation(value = "接收多附件上传，返回Json描述信息字符", httpMethod = ReqConst.POST)
	@RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
	public JsonResult uploadFile(MultipartRequest request) {
		Map<String, MultipartFile> fileMap = request.getFileMap();
		List<String> ufInfos = new ArrayList<>();
		for (String name : fileMap.keySet()) {
			MultipartFile mf = fileMap.get(name);
			LOG.debug("文件信息：长度:{};类型:{};名称:{};原名:{};", new Object[]{mf.getSize(), mf.getContentType(), mf.getName(), mf.getOriginalFilename()});
			UploadFile uf = UploadFileUtil.convert2UploadFile(mf);
			ufInfos.add(UploadFileUtil.getJsonInfo(uf).toJSONString());
		}
		String idsStr = StringUtils.collectionToCommaDelimitedString(ufInfos);
		LOG.info("接收~附件成功，附件id：{}", idsStr);
		return RestResult.data(idsStr);
	}

	@ApiOperation(value = "接收富文本编辑器的图片上传，返回Json描述信息字符，不存附件表可直接访问", httpMethod = ReqConst.POST)
	@RequestMapping(value = "/uploadImg", method = RequestMethod.POST)
	public JsonResult uploadImage(MultipartRequest request) {
		Map<String, MultipartFile> fileMap = request.getFileMap();
		List<String> ufInfos = new ArrayList<>();
		for (String name : fileMap.keySet()) {
			MultipartFile mf = fileMap.get(name);
			LOG.debug("文件信息：长度:{};类型:{};名称:{};原名:{};", new Object[]{mf.getSize(), mf.getContentType(), mf.getName(), mf.getOriginalFilename()});
			String path = UploadFileUtil.saveImage(mf);
			ufInfos.add(path);
		}
		String imgPaths = StringUtils.collectionToCommaDelimitedString(ufInfos);
		LOG.info("接收~附件成功，图片路径：{}", imgPaths);
		return RestResult.data(imgPaths);
	}

	@ApiOperation(value = "下载附件")
	@ApiImplicitParam(paramType = "path", name = "id", dataType = "String", required = true, value = "附件ID", defaultValue = "")
	@RequestMapping("/downFile/{id}")
	public ResponseEntity<byte[]> download(@PathVariable(required = true) String id) throws IOException {
		UploadFile uf = uploadFileJPA.findById(id).get();
		File file = UploadFileUtil.getFile(uf);
		HttpHeaders headers = new HttpHeaders();
		String fileName = new String(uf.getUploadFileName().getBytes("UTF-8"), "iso-8859-1");//为了解决中文名称乱码问题
		headers.setContentDispositionFormData("attachment", fileName);
		headers.setContentType(UploadFileUtil.getMediaType(uf.getExtName()));
		return new ResponseEntity<byte[]>(FileCopyUtils.copyToByteArray(file), headers, HttpStatus.CREATED);
	}

	@ApiOperation(value = "删除已上传的附件", httpMethod = ReqConst.GET)
	@ApiImplicitParam(paramType = "path", name = "id", dataType = "String", required = true, value = "附件ID", defaultValue = "")
	@RequestMapping(value = "/delFile/{id}", method = RequestMethod.GET)
	public JsonResult delfile(@PathVariable(required = true) String id) throws IOException {
		UploadFile uf = uploadFileJPA.getOne(id);
		File file = UploadFileUtil.getFile(uf);
		file.delete();
		uploadFileJPA.deleteById(id);
		return RestResult.success();
	}
}