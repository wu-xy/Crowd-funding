package com.atguigu.atcrowdfunding.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * 获取IOC容器的工具类
 * @author Administrator
 *
 */
@Component
public class ApplicationContextUtils implements ApplicationContextAware {//接口注入.

	public static ApplicationContext applicationContext;
	
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		System.out.println( "applicationContext = " + applicationContext );
		ApplicationContextUtils.applicationContext = applicationContext;
	}

}
