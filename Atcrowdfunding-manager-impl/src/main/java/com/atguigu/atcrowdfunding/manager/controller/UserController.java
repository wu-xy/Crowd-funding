package com.atguigu.atcrowdfunding.manager.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.atguigu.atcrowdfunding.bean.Role;
import com.atguigu.atcrowdfunding.bean.User;
import com.atguigu.atcrowdfunding.manager.service.UserService;
import com.atguigu.atcrowdfunding.util.AjaxResult;
import com.atguigu.atcrowdfunding.util.Page;
import com.atguigu.atcrowdfunding.util.StringUtil;
import com.atguigu.atcrowdfunding.vo.Data;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService ;
	
	@RequestMapping("/index")
	public String index(){		
		return "user/index";
	}
	
	@RequestMapping("/toAdd")
	public String toAdd(){		
		return "user/add";
	}
	
	
	@RequestMapping("/assignrole")
	public String assignRole(HttpSession session,int id,Map map){
		User user = (User) session.getAttribute("user");
		if(user==null) {
			return "login";
		}
		List<Role> listRoles=userService.queryRoles();
		List<Integer> listRoleIds=userService.queryRoleIds(id);
		
		List<Role> leftListRole=new ArrayList<>();
		List<Role> rightListRole=new ArrayList<>();
		
		for (Role role : listRoles) {			
			leftListRole.add(role);
			for (Integer integer : listRoleIds) {
				if(integer==role.getId()) {
					rightListRole.add(role);
					leftListRole.remove(role);
				}
				
			}
		}
				
		map.put("leftListRole", leftListRole);
		map.put("rightListRole", rightListRole);
		
		return "user/assignrole";
	}
	
	@ResponseBody
	@RequestMapping("/doUnAssignRole")
	public Object doUnAssignRole(HttpSession session,int userid,Data data){
		User user = (User) session.getAttribute("user");
		if(user==null) {
			return "login";
		}
		AjaxResult result = new AjaxResult();
		
		try {
			int count=userService.doUnAssignRole(userid,data);
			result.setSuccess(true);
		} catch (Exception e) {			
			e.printStackTrace();
			result.setSuccess(false);
		}
						
		
		return result;
	}
	
	@ResponseBody
	@RequestMapping("/doAssignRole")
	public Object doAssignRole(HttpSession session,int userid,Data data){
		User user = (User) session.getAttribute("user");
		if(user==null) {
			return "login";
		}
		AjaxResult result = new AjaxResult();
		
		try {
			int count=userService.doAssignRole(userid,data);
			result.setSuccess(true);
		} catch (Exception e) {			
			e.printStackTrace();
			result.setSuccess(false);
		}
						
		
		return result;
	}
	
	
	@RequestMapping("/toUpdate")
	public String toUpdate(Integer id,Map map){	
		
		User user = userService.getUserById(id);
		map.put("user", user);
		
		return "user/update";
	}
	
	
	//接收多条数据.
	@ResponseBody
	@RequestMapping("/doDeleteBatch")
	public Object doDeleteBatch(Data data){
		AjaxResult result = new AjaxResult();
		try {
			
			int count = userService.deleteBatchUserByVO(data);
			result.setSuccess(count==data.getDatas().size());
		} catch (Exception e) {
			result.setSuccess(false);
			e.printStackTrace();
			result.setMessage("删除数据失败!");
		}
		 
		return result; 
	}
	
	//接收一个参数名带多个值.
	/*@ResponseBody
	@RequestMapping("/doDeleteBatch")
	public Object doDeleteBatch(Integer[] id){
		AjaxResult result = new AjaxResult();
		try {
			
			int count = userService.deleteBatchUser(id);
			result.setSuccess(count==id.length);
		} catch (Exception e) {
			result.setSuccess(false);
			e.printStackTrace();
			result.setMessage("删除数据失败!");
		}
		 
		return result; 
	}*/
	
	@ResponseBody
	@RequestMapping("/doDelete")
	public Object doDelete(Integer id){
		AjaxResult result = new AjaxResult();
		try {
			
			int count = userService.deleteUser(id);
			result.setSuccess(count==1);
		} catch (Exception e) {
			result.setSuccess(false);
			e.printStackTrace();
			result.setMessage("删除数据失败!");
		}
		 
		return result; 
	}
	
	@ResponseBody
	@RequestMapping("/doUpdate")
	public Object doUpdate(User user){
		AjaxResult result = new AjaxResult();
		try {
			
			int count = userService.updateUser(user);
			result.setSuccess(count==1);
		} catch (Exception e) {
			result.setSuccess(false);
			e.printStackTrace();
			result.setMessage("修改数据失败!");
		}
		 
		return result; 
	}
	
	
	@ResponseBody
	@RequestMapping("/doAdd")
	public Object doAdd(User user){
		AjaxResult result = new AjaxResult();
		try {
			
			int count = userService.saveUser(user);
			result.setSuccess(count==1);
		} catch (Exception e) {
			result.setSuccess(false);
			e.printStackTrace();
			result.setMessage("保存数据失败!");
		}
		 
		return result; 
	}
	
	
	
	//条件查询
	@ResponseBody
	@RequestMapping("/doIndex")
	public Object index(@RequestParam(value="pageno",required=false,defaultValue="1") Integer pageno,
				@RequestParam(value="pagesize",required=false,defaultValue="10") Integer pagesize,
				String queryText){
		AjaxResult result = new AjaxResult();
		try {
			
			Map paramMap = new HashMap();
			paramMap.put("pageno", pageno);
			paramMap.put("pagesize", pagesize);			
			
			if(StringUtil.isNotEmpty(queryText)){
				
				if(queryText.contains("%")){
					queryText = queryText.replaceAll("%", "\\\\%");
				}
				
				paramMap.put("queryText", queryText); //   \%
			}
			Page page = userService.queryPage(paramMap);
			result.setSuccess(true);
			result.setPage(page);
		} catch (Exception e) {
			result.setSuccess(false);
			e.printStackTrace();
			result.setMessage("查询数据失败!");
		}
		 
		return result; //将对象序列化为JSON字符串,以流的形式返回.
	}
	
	
	//异步请求
	/*@ResponseBody
	@RequestMapping("/index")
	public Object index(@RequestParam(value="pageno",required=false,defaultValue="1") Integer pageno,
				@RequestParam(value="pagesize",required=false,defaultValue="10") Integer pagesize){
		AjaxResult result = new AjaxResult();
		try {
			Page page = userService.queryPage(pageno,pagesize);
			
			result.setSuccess(true);
			result.setPage(page);
		} catch (Exception e) {
			result.setSuccess(false);
			e.printStackTrace();
			result.setMessage("查询数据失败!");
		}
		 
		return result; //将对象序列化为JSON字符串,以流的形式返回.
	}*/
	
	
	//同步请求
	/*@RequestMapping("/index")
	public String index(@RequestParam(value="pageno",required=false,defaultValue="1") Integer pageno,
				@RequestParam(value="pagesize",required=false,defaultValue="10") Integer pagesize,Map map){
		
		Page page = userService.queryPage(pageno,pagesize);
		 
		map.put("page", page);
		
		return "user/index";
	}*/
	
}
