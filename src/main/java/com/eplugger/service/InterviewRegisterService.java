package com.eplugger.service;

import com.aspose.words.Document;
import com.aspose.words.SaveFormat;
import com.aspose.words.SaveOutputParameters;
import com.eplugger.core.query.Pagination;
import com.eplugger.core.query.QueryCriteria;
import com.eplugger.core.uploadFile.UploadFileConfig;
import com.eplugger.jpa.InterviewRegisterJPA;
import com.eplugger.module.business.InterviewRegister;
import com.eplugger.utils.WordUtils;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

@Service
public class InterviewRegisterService extends BaseService {
	
	@Value("${message.webhook}")
	private String webhook;

	@Value("${message.urlPrefix}")
	private String urlPrefix;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private UploadFileConfig uploadFileConfig;

    @Autowired
    private InterviewRegisterJPA interviewRegisterJPA;

    public Page<InterviewRegister> getList(QueryCriteria queryCriteria, Pagination pagination) {
        return interviewRegisterJPA.findAll(queryCriteria, pagination);
    }

    public InterviewRegister saveOrUpdate(InterviewRegister interviewRegister) {
        setOperationInfo(interviewRegister);
		InterviewRegister register = interviewRegisterJPA.save(interviewRegister);
		sendMessage(register);
		return register;
    }

	private void sendMessage(InterviewRegister register) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		// 设置restemplate编码为utf-8
		restTemplate.getMessageConverters().set(1,new StringHttpMessageConverter(StandardCharsets.UTF_8));
		String phone = register.getInterviewer().split("-")[1];
		String msg = "面试信息推送\\n面试人：" + register.getName() + "\\n面试岗位：" + register.getPosition() + "\\n详细信息：" + urlPrefix + register.getId();
		String content  = "{ \"msgtype\": \"text\", \"text\": {\"content\": \"" + msg + "\", \"mentioned_list\":[], \"mentioned_mobile_list\":[\"" + phone + "\"]}}";

		HttpEntity<String> request = new HttpEntity<>(content, headers);
		ResponseEntity<String> postForEntity = restTemplate.postForEntity(webhook, request, String.class);
		String body = postForEntity.getBody();
		System.out.println("发送内容：");
		System.out.println(content);
		System.out.println("发送结果：");
		System.out.println(body);
		System.err.println("执行静态定时任务时间: " + LocalDateTime.now());
	}

	public InterviewRegister get(String id) {
        return interviewRegisterJPA.findById(id).get();
    }

    public void saveEvaluate(String id, String fileIds, String evaluate, String result) {
		InterviewRegister interviewRegister = interviewRegisterJPA.findById(id).get();
		interviewRegister.setFileIds(fileIds);
		interviewRegister.setEvaluate(evaluate);
		interviewRegister.setResult(result);
		interviewRegisterJPA.save(interviewRegister);
	}


	/**
	 * 获取面试登记表文件
	 *  通过登记表模板，将面试者登记的信息写入到word中，然后转换为pdf文件返回
	 * @param interviewId 面试登记信息id
	 */
	public File getRegistrationFile(String interviewId){
		InterviewRegister interviewRegister = interviewRegisterJPA.getOne(interviewId);
		Document document = null;
		File tempDir = null;
		File tempFile = null;
		// 转换文件为document对象
		try {
			// 面试登记表
			ClassLoader cl = this.getClass().getClassLoader();
			InputStream inputStream = cl.getResourceAsStream("classpath:doc/registration.doc");
			//如果是Linux环境  这个inputStram就不会为空
			if (null == inputStream) {
				//走这里是因为是开发环境
				File targetFile = ResourceUtils.getFile("classpath:doc/registration.doc");
				document = WordUtils.getDoc(targetFile);
			} else {
				document = new Document(inputStream);
			}
			// 申请时间
			Long createdate = interviewRegister.getCreateDate();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
			WordUtils.replaceStr(document,"\\^\\{createdate\\}",sdf.format(new Date(createdate)));

			WordUtils.replaceStr4Obj(document, interviewRegister);
			tempDir = new File(uploadFileConfig.getSavePath() + "/temp");
			if (!tempDir.exists()) {
				tempDir.mkdir();
			}
			tempFile = File.createTempFile("interviewRegister", ".pdf", tempDir);
			document.save(tempFile.getAbsolutePath(), SaveFormat.PDF);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return tempFile;
	}

	public void delete(String id) {
    	interviewRegisterJPA.deleteById(id);
	}

	public String getWebhook() {
		return webhook;
	}

	public void setWebhook(String webhook) {
		this.webhook = webhook;
	}

	public String getUrlPrefix() {
		return urlPrefix;
	}

	public void setUrlPrefix(String urlPrefix) {
		this.urlPrefix = urlPrefix;
	}
}
