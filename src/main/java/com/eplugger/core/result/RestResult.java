package com.eplugger.core.result;

/**
 * Rest返回Json格式对象创建器
 */
public class RestResult {

    /**
     * 创建成功返回对象
     * @return
     */
    public static SuccessResult success(){
        return new SuccessResult();
    }

    /**
     * 创建失败返回对象
     * @return
     */
    public static FailResult fail(String msg){
        return new FailResult(msg);
    }

    /**
     * 创建数据返回对象
     * @return
     */
    public static DataResult data(Object data){
        return new DataResult(data);
    }

}
