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

import com.atguigu.atcrowdfunding.bean.Permission;
import com.atguigu.atcrowdfunding.bean.Role;
import com.atguigu.atcrowdfunding.bean.User;
import com.atguigu.atcrowdfunding.manager.service.PermissionService;
import com.atguigu.atcrowdfunding.manager.service.RoleService;
import com.atguigu.atcrowdfunding.util.AjaxResult;
import com.atguigu.atcrowdfunding.util.Page;
import com.atguigu.atcrowdfunding.util.StringUtil;
import com.atguigu.atcrowdfunding.vo.Data;

@Controller
@RequestMapping("/role")
public class RoleController {

	@Autowired
	private RoleService roleService;
	@Autowired
	private PermissionService permissionService;

	@RequestMapping("/index")
	public String index() {
		return "role/index";
	}

	
	@RequestMapping("/add")
	public String add() {
		return "role/add";
	}

	
	
	
	@RequestMapping("/assign")
	public String assign() {
		return "role/assignPermission";
	}
	
	
	
	@ResponseBody
	@RequestMapping("/doAssignPermission")
	public Object doAssignPermission(Integer roleid, Data datas){	//传进来要分配权限的id，和要要分配的权限对应的数组
		AjaxResult result = new AjaxResult();
		try {
			int count = roleService.saveRolePermissionRelationship(roleid,datas);
			
			result.setSuccess(count==datas.getIds().size());
			
		} catch (Exception e) {
			e.printStackTrace();
			result.setSuccess(false);
		}
		
		return result;
	}
	
	
	@ResponseBody
	@RequestMapping("/loadDataAsync")		//异步加载树结构，要求返回一个数组
	public Object loadDataAsync(HttpSession session,int roleid) {	
				
		AjaxResult result = new AjaxResult();
		List<Permission> root=new ArrayList<Permission>();
		
		try {
			Map<Integer, Permission> map=new HashMap<Integer, Permission>();
			List<Permission> permissions=permissionService.getAllPermission();
			List<Integer> permissionIds=permissionService.getPermissionIdByRoleId(roleid);
			
			for (Permission permission : permissions) {
				map.put(permission.getId(), permission);
				if(permissionIds.contains(permission.getId())) {
					permission.setChecked(true);
				}
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
		
		
		return root;
	}
	
	
	//传递多个对象方式
	@ResponseBody
	@RequestMapping("/batchDelete")
	public Object batchDelete(Data datas){
		AjaxResult result = new AjaxResult();
		try {
			int count = roleService.batchDeleteRole(datas);
			if(count==datas.getDatas().size()){
				result.setSuccess(true);
			}else{
				result.setSuccess(false);
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.setSuccess(false);
		}
		
		return result;
	}
	/* 传递多个id值方式
	@ResponseBody
	@RequestMapping("/batchDelete")
	public Object batchDelete(Integer[] ids){
		AjaxResult result = new AjaxResult();
		try {
			int count = roleService.batchDeleteRole(ids);
			if(count==ids.length){
				result.setSuccess(true);
			}else{
				result.setSuccess(false);
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.setSuccess(false);
		}
		
		return result;
	}*/
	
	@ResponseBody
	@RequestMapping("/delete")
	public Object delete(Integer uid){
		AjaxResult result = new AjaxResult();
		try {
			int count = roleService.deleteRole(uid);
			if(count==1){
				result.setSuccess(true);
			}else{
				result.setSuccess(false);
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.setSuccess(false);
		}
		
		return result;
	}
	
	
	@ResponseBody
	@RequestMapping("/doEdit")
	public Object doEdit(Role role) {
		AjaxResult result = new AjaxResult();
		try {
			int count = roleService.updateRole(role);// id没传的情况下,更新并不会报错,但是并没有更新成功,所以,需要加以判断
			if(count==1){
				result.setSuccess(true);
			}else{
				result.setSuccess(false);
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.setSuccess(false);
		}
		
		return result;
	}
	
	@RequestMapping("/edit")
	public String edit(Integer id,Map<String,Object> map) {
		Role role = roleService.getRole(id);
		map.put("role", role);
		return "role/edit";
	}
	
	
	@ResponseBody
	@RequestMapping("/doAdd")
	public Object doAdd(Role role) {
		
		AjaxResult result = new AjaxResult();
		
		try {
			
			roleService.saveRole(role);
			
			result.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
			result.setSuccess(false);
		}
		
		return result;
	}
	
	/**
	 * 异步分页查询
	 * @param pageno
	 * @param pagesize
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/pageQuery")
	public Object pageQuery(String queryText,@RequestParam(required = false, defaultValue = "1") Integer pageno,
			@RequestParam(required = false, defaultValue = "2") Integer pagesize){
		AjaxResult result = new AjaxResult();
		try {
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("pageno", pageno); // 空指针异常
			paramMap.put("pagesize", pagesize);
			
			if(StringUtil.isNotEmpty(queryText)){
				queryText = queryText.replaceAll("%", "\\\\%"); //斜线本身需要转译
				System.out.println("--------------"+queryText);
			}
			
			paramMap.put("queryText", queryText);
			
			// 分页查询数据
			Page<Role> rolePage = roleService.pageQuery(paramMap);

			result.setPage(rolePage);
			result.setSuccess(true);
		} catch (Exception e) {			
			e.printStackTrace();
			result.setSuccess(false);
		}
		return  result;
	}
}
