package com.eplugger.core.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

/**
 * 系统异常
 */
public class SysException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public SysException(Object target, Exception e, String msg, Object ... params) {
		super("[系统异常]"+msg,e);
		Assert.notNull(target,"调用系统异常打印时缺少目标对象");
		Assert.notNull(msg,"产生的异常必须描述异常信息");
		Logger LOG = LoggerFactory.getLogger(target.getClass());
		LOG.error(msg, e, params);
	}

	public SysException(Object target, String msg, Object ... params) {
		super("[系统异常]"+msg);
		Assert.notNull(target,"调用系统异常打印时缺少目标对象");
		Assert.notNull(msg,"产生的异常必须描述异常信息");
		Logger LOG = LoggerFactory.getLogger(target.getClass());
		LOG.error(msg, params);
	}
}
