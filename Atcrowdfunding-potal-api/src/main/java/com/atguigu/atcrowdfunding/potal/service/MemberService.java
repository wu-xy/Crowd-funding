package com.atguigu.atcrowdfunding.potal.service;

import java.util.List;
import java.util.Map;

import com.atguigu.atcrowdfunding.bean.Member;
import com.atguigu.atcrowdfunding.vo.CertImg;

public interface MemberService {	

	int doReg(Member member);

	Member queryUserlogin(Map<String, Object> paramMap);

	int updateAcctType(Member loginMember);

	int updateBasicinfo(Member loginMember);

	void insertMemberCerts(List<CertImg> certimgs);

	void updataAuthstatus(Member loginMember);

}
