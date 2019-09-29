package com.atguigu.atcrowdfunding.potal.dao;

import com.atguigu.atcrowdfunding.bean.Member;
import com.atguigu.atcrowdfunding.vo.CertImg;

import java.util.List;
import java.util.Map;

public interface MemberMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Member record);

    Member selectByPrimaryKey(Integer id);

    List<Member> selectAll();

    int updateByPrimaryKey(Member record);

	Member queryUserlogin(Map<String, Object> paramMap);

	int updateAcctType(Member loginMember);

	int updateBasicinfo(Member loginMember);

	void insertMemberCert(CertImg certImg);

	void updataAuthstatus(Member loginMember);
}