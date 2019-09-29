package com.atguigu.atcrowdfunding.interceptor;

import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.atguigu.atcrowdfunding.bean.User;
import com.atguigu.atcrowdfunding.util.Const;

/**
 * 登录拦截器
 * @author Administrator
 *
 */
public class LoginInterceptor extends HandlerInterceptorAdapter {
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		Set<String> urls=new HashSet<String>();	//创建一个无序的URL集合（白名单）
		urls.add("/index.htm");
		urls.add("/login.htm");
		urls.add("/reg.htm");
		urls.add("/logout.do");		
		urls.add("/doLogin.do");
		urls.add("/doReg.do");
		
		String url=request.getRequestURI();	//获取当前请求路径
		
		if(urls.contains(url)) {
			return true;
		}
		
		HttpSession session = request.getSession();
		Object user = session.getAttribute(Const.LOGIN_USER);	//用户类型可能为会员或者管理员，所以使用object来接收
		if(user!=null) {
			return true;
		}else {
			response.sendRedirect(request.getContextPath()+"/login.htm");	//客户端跳转到登录页面
			return false;
		}
				
		
	}
}
