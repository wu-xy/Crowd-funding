package com.atguigu.atcrowdfunding.manager.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.atguigu.atcrowdfunding.bean.Permission;
import com.atguigu.atcrowdfunding.manager.dao.PermissionMapper;
import com.atguigu.atcrowdfunding.manager.service.PermissionService;


@Service
public class PermissionServiceImpl implements PermissionService {
	
	@Autowired
	private PermissionMapper permissionMapper;

	@Override
	public Permission getRoot() {
		
		return permissionMapper.getRoot();
	}

	@Override
	public List<Permission> getChildren(Integer id) {
		
		return permissionMapper.getChildren(id);
	}

	@Override
	public List<Permission> getAllPermission() {
		
		return permissionMapper.selectAll();
	}

	@Override
	public int doAdd(Permission permission) {
		
		return permissionMapper.insert(permission);
	}

	@Override
	public int doDelete(int id) {
		
		return permissionMapper.deleteByPrimaryKey(id);
	}

	@Override
	public Permission getPermission(int id) {
		
		return permissionMapper.selectByPrimaryKey(id);
	}

	@Override
	public int doUpdate(Permission permission) {
		
		return permissionMapper.doUpdate(permission);
	}

	@Override
	public List<Integer> getPermissionIdByRoleId(int roleid) {
	
		return permissionMapper.getPermissionIdByRoleId(roleid);
	}
	
	
}
