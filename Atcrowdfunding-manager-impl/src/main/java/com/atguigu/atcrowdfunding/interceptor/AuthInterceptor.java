package com.atguigu.atcrowdfunding.interceptor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.atguigu.atcrowdfunding.bean.Permission;
import com.atguigu.atcrowdfunding.manager.service.PermissionService;

/**
 * 权限拦截器
 * 判断用户是否有权限访问对应的地址
 * @author Administrator
 *
 */
public class AuthInterceptor extends HandlerInterceptorAdapter {
	@Autowired
	private PermissionService permissionService;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		ServletContext application = request.getSession().getServletContext();
		Set<String> allUrl=(Set<String>)application.getAttribute("allPermissionUrls");	//获取URL集合
				
		String url=request.getRequestURI();	//获取当前请求路径
		
		if(!allUrl.contains(url)) {			//如果请求地址不在需控制的地址中，则放行	
			return true;
		}else {
			HttpSession session = request.getSession();
			Set<String> userUrl = (Set<String>)session.getAttribute("userUrl");
			if(userUrl.contains(url)) {
				return true;
			}else {
				response.sendRedirect(request.getContextPath()+"/login.htm");	//客户端跳转到登录页面
				return false;
			}
			
		}						
		
	}
}
