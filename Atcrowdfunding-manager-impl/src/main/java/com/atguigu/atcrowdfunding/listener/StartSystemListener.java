package com.atguigu.atcrowdfunding.listener;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.atguigu.atcrowdfunding.bean.Permission;
import com.atguigu.atcrowdfunding.manager.service.PermissionService;

public class StartSystemListener implements ServletContextListener {

	//在服务器启动时,创建application对象时需要执行的方法.
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		//1.将项目上下文路径(request.getContextPath())放置到application域中.
		ServletContext application = sce.getServletContext();
		String contextPath = application.getContextPath();
		application.setAttribute("APP_PATH", contextPath);
		System.out.println("APP_PATH...");
		
		//2.将项目上权限访问地址放置到application域中.
		ApplicationContext ioc = WebApplicationContextUtils.getWebApplicationContext(application);		
		PermissionService permissionService = (PermissionService)ioc.getBean(PermissionService.class);
		Set<String> allPermissionUrls = new HashSet<String>();
		List<Permission> permissions=permissionService.getAllPermission();
		for (Permission permission : permissions) {
			allPermissionUrls.add("/"+permission.getUrl());
		}
		
		application.setAttribute("allPermissionUrls", allPermissionUrls);
		
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		
	}

}
