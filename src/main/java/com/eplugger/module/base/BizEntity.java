package com.eplugger.module.base;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@Data
@MappedSuperclass
public class BizEntity implements EntityAble {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "sys-uuid")
	@GenericGenerator(name = "sys-uuid",strategy = "uuid")
	private String id;
	@Override
	public String getId() {
		return id;
	}

	@Override
	public void setId(String id) {
		this.id=id;
	}
	
	/** 数据创建人的用户编号 */ 
	@Column(updatable=false,length=64)
	private String createUserID;

	@Column(updatable=false)
	private String createUserName;
	
	/** 数据创建日期 */
	@Column(updatable=false)
	private Long createDate;
	
	/** 数据最后一次编辑的用户编号 */
	@Column(length=64)
	private String lastEditUserID;
	
	private String lastEditUserName;
	
	/** 数据最后一次编辑的编辑日期 */
	private Long lastEditDate;
}
