package com.eplugger.core.result;

/**
 * 反馈成功（ajax - return json）
 */
public class SuccessResult extends JsonResult {

	public SuccessResult() {
		super(RESULT_TYPE_NOTIFY, STATE_SUCCESS, "操作成功", null, 0l);
	}

}
