package com.eplugger.jpa;

import com.eplugger.module.upload.UploadFile;

/**
 * 上传附件的存储信息
 */
public interface UploadFileJPA extends BaseJPA<UploadFile> {

    public UploadFile getFirstByStorageFileName(String id);
}
