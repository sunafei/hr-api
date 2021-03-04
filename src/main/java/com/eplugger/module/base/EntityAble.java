package com.eplugger.module.base;

import java.io.Serializable;


/**
 * 可持久化的实体
 */
public interface EntityAble extends Serializable {
	//主键
	public final static String ID = "id";
	
	/** 主键 */
	public String getId();
	public void setId(String id);

}