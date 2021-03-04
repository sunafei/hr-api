package com.eplugger.core.result;

/**
 * 登录返回结果
 */
public class LoginResult extends JsonResult {
    public LoginResult(String type, String msg, Object result) {
        super(RESULT_TYPE_NOTIFY, type, msg, result, 0l);
    }
}
