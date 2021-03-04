package com.eplugger.core.result;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Collection;

/**
 * Json结果
 */
@Data
@AllArgsConstructor
public class JsonResult {
	//反馈处理状态
	public static final String STATE_SUCCESS = "200";//操作成功
	public static final String STATE_FAIL = "400";//操作失败
	public static final String STATE_FAIL_NOLOGIN = "401";//未登录

	//返回结果类型
	public static final String RESULT_TYPE_DATA = "data";//返回数据
	public static final String RESULT_TYPE_NOTIFY = "notify";//返回通知，用于提示成功或下一步等。
	//返回数据类型
	public static final String DATA_ARRAY = "array";//数据类型：集合
	public static final String DATA_OBJECT = "object";//数据类型：对象

	protected String type = RESULT_TYPE_DATA;
	private String state = STATE_SUCCESS;//反馈状态 success / error
	private String msg;//要反馈的提示信息
	private String dataType;//数据类型

	private Object data;
	private Long time;//响应时间

    /**
     * 设置数据，并自动判断数据类型
     */
    public JsonResult(String type, String state, String msg, Object data, Long time) {
    	this.type = type;
        this.state = state;
        this.msg = msg;
        this.data = data;
        this.time = time;
        if(data instanceof Collection){
            dataType = DATA_ARRAY;
        } else {
            dataType = DATA_OBJECT;
        }
    }

}
