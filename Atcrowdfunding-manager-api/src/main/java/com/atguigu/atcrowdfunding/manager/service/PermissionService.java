package com.atguigu.atcrowdfunding.manager.service;

import java.util.List;

import com.atguigu.atcrowdfunding.bean.Permission;

public interface PermissionService {

	Permission getRoot();

	List<Permission> getChildren(Integer id);

	List<Permission> getAllPermission();

	int doAdd(Permission permission);

	int doDelete(int id);

	Permission getPermission(int id);

	int doUpdate(Permission permission);

	List<Integer> getPermissionIdByRoleId(int roleid);
	
}
