package com.atguigu.atcrowdfunding.potal.service.impl;


import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.atguigu.atcrowdfunding.bean.Member;
import com.atguigu.atcrowdfunding.exception.LoginFailException;
import com.atguigu.atcrowdfunding.potal.dao.MemberMapper;
import com.atguigu.atcrowdfunding.potal.service.MemberService;
import com.atguigu.atcrowdfunding.vo.CertImg;

@Service
public  class MemberServiceImpl implements MemberService {
		
	@Autowired
	MemberMapper memberMapper;
		
	@Override
	public int doReg(Member member) {
		int insert = memberMapper.insert(member);
		if(insert<=0){
			throw new LoginFailException("注册失败!");
		}
		return insert;
			
	}

	@Override
	public Member queryUserlogin(Map<String, Object> paramMap) {
		Member member=memberMapper.queryUserlogin(paramMap);
		if(member==null){
			throw new LoginFailException("账号或密码错误");
		}
		return member;
	}

	@Override
	public int updateAcctType(Member loginMember) {
		int count=memberMapper.updateAcctType(loginMember);
		return count;
		
	}

	@Override
	public int updateBasicinfo(Member loginMember) {
		int count=memberMapper.updateBasicinfo(loginMember);
		return count;
	}

	@Override
	public void insertMemberCerts(List<CertImg> certimgs) {
		for (CertImg certImg : certimgs) {
			memberMapper.insertMemberCert(certImg);
		}
	}

	@Override
	public void updataAuthstatus(Member loginMember) {
		
		memberMapper.updataAuthstatus(loginMember);
	}

}
