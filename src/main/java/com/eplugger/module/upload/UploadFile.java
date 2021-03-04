package com.eplugger.module.upload;

import com.eplugger.module.base.EntityAble;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * 上传文件
 *
 */
@Data
@Entity
@Table(name = "sys_upload_file")
public class UploadFile implements EntityAble {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(generator = "paymentableGenerator")
	@GenericGenerator(name = "paymentableGenerator", strategy = "assigned")
	private String id;//自定义主键策略

	//存储文件名
	@Column(name = "STORAGE_FILE_NAME")
	private String storageFileName;
	//上传文件名
	@Column(name = "UPLOAD_FILE_NAME")
	private String uploadFileName;
	//上传文件大小（单位字节）
	@Column(name = "FILE_SIZE")
	private Long fileSize;
	//文件类型
	@Column(name = "CONTENT_TYPE")
	private String contentType;
	//文件扩展名
	@Column(name = "EXT_NAME")
	private String extName;
	//创建日期
	@Column(name = "CREATE_DATE",insertable = false)
	private Long createDate;
	//业务数据id
	@Column(name = "BIZ_ENTITY_ID")
	private String bizEntityId;
	//业务对象的属性字段
	@Column(name = "ENTITY_PROPERTY")
	private String entityProperty;
	//所属模块id
	@Column(name = "MODULE_ID")
	private String moduleId;

}

