package com.eplugger.core.result;

/**
 * 反馈数据
 */
public class DataResult extends JsonResult {

	public DataResult(Object data) {
		super(RESULT_TYPE_DATA,STATE_SUCCESS,"操作成功", data,0l);
	}

}
