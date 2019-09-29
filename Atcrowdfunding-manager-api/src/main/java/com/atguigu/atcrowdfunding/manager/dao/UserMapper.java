package com.atguigu.atcrowdfunding.manager.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.atguigu.atcrowdfunding.bean.Permission;
import com.atguigu.atcrowdfunding.bean.Role;
import com.atguigu.atcrowdfunding.bean.User;
import com.atguigu.atcrowdfunding.vo.Data;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    User selectByPrimaryKey(Integer id);

    List<User> selectAll();

    int updateByPrimaryKey(User record);

	User queryUserlogin(Map<String, Object> paramMap);

	//List<User> queryList(@Param("startIndex") Integer startIndex, @Param("pagesize")  Integer pagesize);

	//Integer queryCount();

	List<User> queryList(Map<String, Object> paramMap);

	Integer queryCount(Map<String, Object> paramMap);

	//int deleteBatchUserByVO(Data data);
	
	//int deleteBatchUserByVO(List<User> userList);
	
	//int deleteBatchUserByVO(User[] userList);1
	
	int deleteBatchUserByVO(@Param("userList") List<User> userList);

	List<Role> queryRoles();

	List<Integer> queryRoleIds(int id);

	int doUnAssignRole(@Param("userid") int userid, @Param("data") Data data);

	int doAssignRole(@Param("userid") int userid, @Param("data") Data data);

	List<Permission> getPermissionByUserId(int id);
	
}