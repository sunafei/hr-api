package com.eplugger.core.result;

import java.util.HashMap;
import java.util.Map;

/**
 * 反馈失败（ajax - return json）
 */
public class FailResult extends JsonResult {
	private Map<String,String> nextSteps =new HashMap<String,String>();
	
	public FailResult(String message) {
		super(RESULT_TYPE_NOTIFY,STATE_FAIL,message,null,0l);
	}

}