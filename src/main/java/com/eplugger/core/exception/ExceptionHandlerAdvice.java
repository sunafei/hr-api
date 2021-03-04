package com.eplugger.core.exception;

import com.eplugger.core.result.FailResult;
import com.eplugger.core.result.RestResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

/**
 * Controller 的全局异常收集
 */
@ControllerAdvice(annotations = RestController.class)
@ResponseBody
public class ExceptionHandlerAdvice {

    @ExceptionHandler(value = Exception.class)
    @ResponseStatus
    public FailResult exception(Exception exception, WebRequest request) {
        return RestResult.fail(exception.getMessage());
    }
}