package com.eplugger.rest;

import com.eplugger.core.query.Pagination;
import com.eplugger.core.query.QueryCriteria;
import com.eplugger.core.result.DataResult;
import com.eplugger.core.result.DateResultListVO;
import com.eplugger.core.result.JsonResult;
import com.eplugger.core.result.RestResult;
import com.eplugger.core.uploadFile.util.UploadFileUtil;
import com.eplugger.module.business.InterviewRegister;
import com.eplugger.service.InterviewRegisterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/api")
public class InterviewRegisterController {

    @Autowired
    private InterviewRegisterService interviewRegisterService;

    @RequestMapping("/ephr")
    public DataResult list(QueryCriteria queryCriteria, Pagination pagination) {
        Page<InterviewRegister> anniversaries = interviewRegisterService.getList(queryCriteria, pagination);
        List<InterviewRegister> vos = new ArrayList<>(anniversaries.getContent());
        DateResultListVO<InterviewRegister> results = new DateResultListVO(anniversaries.getTotalElements(), pagination.getPageNumber(), vos);
        return RestResult.data(results);
    }

    @RequestMapping("/download/{interviewId}")
    public ResponseEntity<byte[]> download(@PathVariable("interviewId") String interviewId) throws IOException {
        File file = interviewRegisterService.getRegistrationFile(interviewId);
        HttpHeaders headers = new HttpHeaders();
        String fileName = new String(file.getName().getBytes("UTF-8"), "iso-8859-1");//为了解决中文名称乱码问题
        headers.setContentDispositionFormData("attachment", fileName);
        headers.setContentType(UploadFileUtil.getMediaType(UploadFileUtil.getExtName(file.getName())));
        return new ResponseEntity<byte[]>(FileCopyUtils.copyToByteArray(file), headers, HttpStatus.CREATED);
    }

    @PostMapping("/interview/save")
    public DataResult save(InterviewRegister interviewRegister) {
        return RestResult.data(interviewRegisterService.saveOrUpdate(interviewRegister));
    }

    @GetMapping(value = "/interview/{id}/get")
    public DataResult get(@PathVariable("id") String id) {
        return RestResult.data(interviewRegisterService.get(id));
    }

	@GetMapping(value = "/interview/{id}/del")
	public JsonResult delete(@PathVariable("id") String id) {
		interviewRegisterService.delete(id);
		return RestResult.success();
	}

	@PostMapping(value = "/interview/{id}/evaluate")
	public JsonResult evaluate(@PathVariable("id") String id, String fileIds, String evaluate, String result) {
		interviewRegisterService.saveEvaluate(id, fileIds, evaluate, result);
		return RestResult.success();
	}
}
