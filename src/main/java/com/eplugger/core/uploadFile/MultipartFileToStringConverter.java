package com.eplugger.core.uploadFile;

import com.eplugger.core.uploadFile.util.UploadFileUtil;
import com.eplugger.module.upload.UploadFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.web.multipart.MultipartFile;

/**
 * 上传附件转字符串 实现将上传附件保存到文件系统，并插入上传附件表一条记录，最后返回记录的id。
 */
public final class MultipartFileToStringConverter implements Converter<MultipartFile, String> {
	private static final Logger LOG = LoggerFactory.getLogger(MultipartFileToStringConverter.class);

	@Override
	public String convert(MultipartFile mf) {
		if (mf.isEmpty()) {
			LOG.debug("文件未上传");
			return null;
		}
		LOG.debug("文件信息：长度:{};类型:{};名称:{};原名:{};", new Object[] { mf.getSize(), mf.getContentType(), mf.getName(), mf.getOriginalFilename() });
		UploadFile uf = UploadFileUtil.convert2UploadFile(mf);
		return UploadFileUtil.getJsonInfo(uf).toJSONString();
	}
}
