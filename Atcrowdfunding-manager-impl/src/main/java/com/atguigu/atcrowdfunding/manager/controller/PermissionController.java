package com.atguigu.atcrowdfunding.manager.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.atguigu.atcrowdfunding.bean.Permission;
import com.atguigu.atcrowdfunding.bean.User;
import com.atguigu.atcrowdfunding.manager.service.PermissionService;
import com.atguigu.atcrowdfunding.util.AjaxResult;
import com.atguigu.atcrowdfunding.vo.Data;

@Controller
@RequestMapping("/permission")
public class PermissionController {
	@Autowired
	private PermissionService permissionService;
	
	@RequestMapping("/index")
	public String index(HttpSession session) {
		User user = (User) session.getAttribute("user");
		if(user==null) {
			return "login";
		}
		return "permission/indexDemo";
		
	}
	
	@RequestMapping("/add")
	public String add(HttpSession session) {
		User user = (User) session.getAttribute("user");
		if(user==null) {
			return "login";
		}
		return "permission/add";
		
	}
	@RequestMapping("/edit")
	public String edit(HttpSession session,int id,Map map) {
		User user = (User) session.getAttribute("user");
		if(user==null) {
			return "login";
		}
		Permission permission = permissionService.getPermission(id);
		map.put("permission", permission);
		
		return "permission/update";
		
	}
	
	@ResponseBody
	@RequestMapping("/doUpdate")
	public Object doUpdate(HttpSession session,Permission permission) {
		User user = (User) session.getAttribute("user");
		if(user==null) {
			return "login";
		}
		
		AjaxResult result = new AjaxResult();
		
		try {
			int count=permissionService.doUpdate(permission);			
			result.setSuccess(count==1);
		} catch (Exception e) {			
			e.printStackTrace();
			result.setSuccess(false);
		}
		
		
		return result;
	}
	
	@ResponseBody
	@RequestMapping("/doAdd")
	public Object doAdd(HttpSession session,Permission permission) {
		User user = (User) session.getAttribute("user");
		if(user==null) {
			return "login";
		}
		
		AjaxResult result = new AjaxResult();
		
		try {
			int count=permissionService.doAdd(permission);			
			result.setSuccess(count==1);
		} catch (Exception e) {			
			e.printStackTrace();
			result.setSuccess(false);
		}
		
		
		return result;
	}
	
	@ResponseBody
	@RequestMapping("/doDelete")
	public Object doDelete(HttpSession session,int id) {
		User user = (User) session.getAttribute("user");
		if(user==null) {
			return "login";
		}
		
		AjaxResult result = new AjaxResult();
		
		try {
			int count=permissionService.doDelete(id);			
			result.setSuccess(count==1);
		} catch (Exception e) {			
			e.printStackTrace();
			result.setSuccess(false);
		}
		
		
		return result;
	}
	
	/**
	 * 继续改进算法，只进行一次查询，减少与数据库的交互
	 * 容器改为hashmap，减少循环次数，提高性能
	 * @param session
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/doIndex")
	public Object doIndex(HttpSession session) {
		User user = (User) session.getAttribute("user");
		if(user==null) {
			return "login";
		}
		
		AjaxResult result = new AjaxResult();
		
		try {
			List<Permission> root=new ArrayList<Permission>();
			Map<Integer, Permission> map=new HashMap<Integer, Permission>();
			List<Permission> permissions=permissionService.getAllPermission();
			
			for (Permission permission : permissions) {
				map.put(permission.getId(), permission);
			}
						
			for (Permission permission : permissions) {
				Permission child=permission;
				if(child.getPid()==null) {
					root.add(child);
				}else {
					Permission parent=map.get(child.getPid());
					if(parent!=null) {
						parent.getChildren().add(child);						
					}
				}
			}			
			result.setData(root);
			result.setSuccess(true);
		} catch (Exception e) {			
			e.printStackTrace();
			result.setSuccess(false);
		}
		
		
		return result;
	}
	
	/**
	 * 改进递归算法，只进行一次查询，减少与数据库的交互
	 * @param session
	 * @return
	 */
//	@ResponseBody
//	@RequestMapping("/doIndex")
//	public Object doIndex(HttpSession session) {
//		User user = (User) session.getAttribute("user");
//		if(user==null) {
//			return "login";
//		}
//		
//		AjaxResult result = new AjaxResult();
//		
//		try {
//			List<Permission> root=new ArrayList<Permission>();
//			List<Permission> permissions=permissionService.getAllPermission();;
//			
//			for (Permission permission : permissions) {
//				Permission child=permission;
//				if(child.getPid()==null) {
//					root.add(child);
//				}else {
//					for (Permission permission2 : permissions) {
//						if(child.getPid()==permission2.getId()) {
//							permission2.getChildren().add(child);
//							break;
//						}
//					}
//				}
//			}			
//			result.setData(root);
//			result.setSuccess(true);
//		} catch (Exception e) {			
//			e.printStackTrace();
//			result.setSuccess(false);
//		}
//		
//		
//		return result;
//	}
	
	
	
	/**采用递归方法进行改进
	 * 
	 * @param session
	 * @return
	 */
//	@ResponseBody
//	@RequestMapping("/doIndex")
//	public Object doIndex(HttpSession session) {
//		User user = (User) session.getAttribute("user");
//		if(user==null) {
//			return "login";
//		}
//		
//		AjaxResult result = new AjaxResult();
//		
//		try {
//			
//			List<Permission> root=new ArrayList<Permission>();
//			
//			Permission permission=permissionService.getRoot();
//			queryChildrenPermission(permission); 	//调用递归方法，查找子节点
//			
//			root.add(permission);
//			result.setData(root);
//			result.setSuccess(true);
//		} catch (Exception e) {			
//			e.printStackTrace();
//			result.setSuccess(false);
//		}
//		
//		
//		return result;
//	}
	
	private void queryChildrenPermission(Permission permission) {
		List<Permission> children=permissionService.getChildren(permission.getId());
		permission.setChildren(children);
		permission.setOpen(true);
		
		for (Permission chil : children) {
			queryChildrenPermission(chil);
		}
	}
	
	
//	@ResponseBody
//	@RequestMapping("/doIndex")
//	public Object doIndex(HttpSession session) {
//		User user = (User) session.getAttribute("user");
//		if(user==null) {
//			return "login";
//		}
//		
//		AjaxResult result = new AjaxResult();
//		
//		try {
//			
//			List<Permission> root=new ArrayList<Permission>();
//			
//			Permission permission=permissionService.getRoot();
//			permission.setOpen(true);
//			
//			List<Permission> children=permissionService.getChildren(permission.getId());
//			permission.setChildren(children);
//			
//			for (Permission chil : children) {
//				chil.setChildren(permissionService.getChildren(chil.getId()));
//				chil.setOpen(true);
//			}
//			root.add(permission);
//			result.setData(root);
//			result.setSuccess(true);
//		} catch (Exception e) {			
//			e.printStackTrace();
//			result.setSuccess(false);
//		}
//		
//		
//		return result;
//	}
		
	
}
