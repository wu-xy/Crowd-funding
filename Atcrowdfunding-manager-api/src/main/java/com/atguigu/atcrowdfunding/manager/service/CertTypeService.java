package com.atguigu.atcrowdfunding.manager.service;

import java.util.List;
import java.util.Map;

import com.atguigu.atcrowdfunding.bean.AccountTypeCert;

public interface CertTypeService {

	List<Map<String, Object>> queryAllCertType();

	int insertAcctTypeCert(AccountTypeCert accttype);

	int deleteAcctTypeCert(AccountTypeCert accttype);
	
}
