package com.eplugger.core.consts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class AppConst {

	private static AppConst appConst;

	@PostConstruct
	public void initFactory() {
		appConst = this;
	}

	/**
	 * 获得web部署的绝对路径
	 *
	 * @return 项目运行在文件系统中的绝对路径
	 */
	public static String getWebPath() {
//        return System.getProperty("webapp.root");
		return appConst.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
	}
}
