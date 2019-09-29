package com.atguigu.atcrowdfunding.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.atguigu.atcrowdfunding.bean.Member;
import com.atguigu.atcrowdfunding.bean.Permission;
import com.atguigu.atcrowdfunding.bean.User;
import com.atguigu.atcrowdfunding.potal.service.MemberService;
import com.atguigu.atcrowdfunding.manager.service.PermissionService;
import com.atguigu.atcrowdfunding.manager.service.UserService;
import com.atguigu.atcrowdfunding.util.AjaxResult;
import com.atguigu.atcrowdfunding.util.Const;
import com.atguigu.atcrowdfunding.util.DesUtil;
import com.atguigu.atcrowdfunding.util.MD5Util;

@Controller
public class DispatcherController {

	@Autowired
	private UserService userService ;
	@Autowired
	private MemberService meberService ;
	@Autowired
	private PermissionService permissionService;
	
	
	@RequestMapping("/index")
	public String index(){		
		return "index";
	}
		
	@RequestMapping("/login")
	public String login(HttpServletRequest request , HttpSession session) throws Exception{	
		
		//判断是否需要自动登录
		//如果之前登录过，cookie中存放了用户信息，需要获取cookie中的信息，并进行数据库的验证		
		boolean needLogin = true;
		String logintype = null ;
		User user = null;
		Member member=null;
		Cookie[] cookies = request.getCookies();
		if(cookies != null){ //如果客户端禁用了Cookie，那么无法获取Cookie信息
			
			for (Cookie cookie : cookies) {
				if("logincode".equals(cookie.getName())){
					String logincode = URLDecoder.decode(cookie.getValue(), "UTF-8"); //使用UTF-8的方式解码
					//loginacct=admin&userpwd=21232f297a57a5a743894a0e4a801fc3&logintype=member
					System.out.println("获取到Cookie中的键值对"+cookie.getName()+"===== " + logincode);
					String[] split = logincode.split("&");
					if(split.length == 3){
						String loginacct = split[0].split("=")[1];
						String userpswd = split[1].split("=")[1];
						logintype = split[2].split("=")[1];
						
						Map<String,Object> paramMap = new HashMap<String,Object>();
						paramMap.put("loginacct", loginacct);
						paramMap.put("userpswd",userpswd);	
						if("user".equals(logintype)){							
							 user = userService.queryUserlogin(paramMap);							
							if(user!=null){
								session.setAttribute(Const.LOGIN_USER, user);
								needLogin = false ;
							}
						}else if("member".equals(logintype)){														
							 member = meberService.queryUserlogin(paramMap);							
							if(member!=null){
								session.setAttribute(Const.LOGIN_USER, member);
								needLogin = false ;
							}
						}
						
					}
				}
			}
		}
		
		if(needLogin){
			return "login";
		}else{
			if("user".equals(logintype)){
				return "redirect:/main.htm?id="+user.getId();
			}else if("member".equals(logintype)){
				return "redirect:/member.htm?id="+member.getId();
			}
		}
		return "login";

	}
	
	@RequestMapping("/reg")
	public String reg(){		
		return "reg";
	}
	
	
	@RequestMapping("/main")
	public String main(HttpSession session,int id){
		
		Permission permissionRoot=null;
		Map<Integer, Permission> map=new HashMap<Integer, Permission>();
		List<Permission> permissions=userService.getPermissionByUserId(id);
		
		for (Permission permission : permissions) {
			map.put(permission.getId(), permission);
		}
					
		for (Permission permission : permissions) {
			Permission child=permission;
			if(child.getPid()==null) {
				permissionRoot=child;
			}else {
				Permission parent=map.get(child.getPid());
				if(parent!=null) {
					parent.getChildren().add(child);						
				}
			}
		}			
		
		session.setAttribute("permissionRoot", permissionRoot);
		
		return "main";
	}
	
	@RequestMapping("/logout")
	public String logout(HttpSession session){	
		
		session.invalidate(); //销毁session对象,或清理session域.
		
		return "redirect:/index.htm"; 
	}
	
	
	//异步请求
	//HttpMessageConverter
	//@ResponseBody 结合Jackson组件,将返回结果转换为字符串.将JSON串以流的形式返回给客户端.
	@ResponseBody
	@RequestMapping("/doLogin")
	public Object doLogin(String loginacct,String userpswd,String type,String rememberme
			,HttpSession session,HttpServletResponse response){
		
		AjaxResult result = new AjaxResult();
		
		try {
			Map<String,Object> paramMap = new HashMap<String,Object>();
			paramMap.put("loginacct", loginacct);
			paramMap.put("userpswd", MD5Util.digest(userpswd));
			paramMap.put("type", type);		
			
			if (type.equals("user")) {				
				User user = userService.queryUserlogin(paramMap);				
				session.setAttribute(Const.LOGIN_USER, user);
				result.setUrl("toMain.jsp");
				if("1".equals(rememberme)) {	//如果选择记住我，写Cookie到浏览器
					String logincode = "loginacct="+user.getLoginacct()+"&userpwd="+user.getUserpswd()+"&logintype=user";
					//loginacct=admin&userpwd=21232f297a57a5a743894a0e4a801fc3&logintype=1
					System.out.println("用户-存放到Cookie中的键值对：logincode::::::::::::"+logincode);
					Cookie c = new Cookie("logincode",URLEncoder.encode(logincode, "UTF-8"));
					
					c.setMaxAge(60*60*24*14); //2周时间Cookie过期     单位秒
					c.setPath("/"); //表示任何请求路径都可以访问Cookie
					
					response.addCookie(c);

				}												
				
				Set<String> userUrl=new HashSet<String>();
				List<Permission> permissions=userService.getPermissionByUserId(user.getId());
				for (Permission permission : permissions) {
					userUrl.add("/"+permission.getUrl());	//添加该用户的可访问地址
				}
				session.setAttribute("userUrl", userUrl);
				
			}
			if (type.equals("member")) {				
				Member member = meberService.queryUserlogin(paramMap);				
				session.setAttribute(Const.LOGIN_USER, member);
				result.setUrl("member.htm");
				
				if("1".equals(rememberme)) {	//如果选择记住我，写Cookie到浏览器
					String logincode = "loginacct="+member.getLoginacct()+"&userpwd="+member.getUserpswd()+"&logintype=member";
					System.out.println("用户-存放到Cookie中的键值对：logincode::::::::::::"+logincode);
					Cookie c = new Cookie("logincode",URLEncoder.encode(logincode, "UTF-8")); //解决中文问题
					
					c.setMaxAge(60*60*24*14); //2周时间Cookie过期     单位秒
					c.setPath("/"); 		  //表示任何请求路径都可以访问Cookie
					
					response.addCookie(c);

				}	
			}
			
			result.setSuccess(true);
												
			// {"success":true}
		} catch (Exception e) {
			result.setMessage("登录失败!");
			e.printStackTrace();
			result.setSuccess(false);
			// {"success":false,"message":"登录失败!"}
			//throw e ;
		}
		
		return result ;
	}
	
	
	//同步请求:
	/*@RequestMapping("/doLogin")
	public String doLogin(String loginacct,String userpswd,String type,HttpSession session){
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("loginacct", loginacct);
		paramMap.put("userpswd", userpswd);
		paramMap.put("type", type);		
		
		User user = userService.queryUserlogin(paramMap);
		
		session.setAttribute(Const.LOGIN_USER, user);
		
		return "redirect:/main.htm";
	}*/
	
	@RequestMapping("/doReg")
	@ResponseBody
	public Object doReg(Member member,HttpSession session){
		
		AjaxResult result = new AjaxResult();
		try {
			String digest = MD5Util.digest(member.getUserpswd());	//对密码加密
			member.setUserpswd(digest);
			int doReg = meberService.doReg(member);			//doReg大于0说明注册成功				
			session.setAttribute(Const.LOGIN_USER, member);			
			result.setSuccess(true);
			result.setMessage("注册成功");
		} catch (Exception e) {
			e.printStackTrace();
			result.setSuccess(false);
			result.setMessage("注册失败!");
		}
		
		return result;
	}
	
	@ResponseBody
	@RequestMapping("/test/act")
	public Object act( String p ) throws Exception {
		
		String val = DesUtil.decrypt(p, "abcdefghijklmnopquvwxyz");
		
		return val;
	}

	
}
