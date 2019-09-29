package com.atguigu.atcrowdfunding.manager.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.atguigu.atcrowdfunding.bean.AccountTypeCert;
import com.atguigu.atcrowdfunding.manager.dao.AccountTypeCertMapper;
import com.atguigu.atcrowdfunding.manager.service.CertTypeService;

@Service
public class CertTypeServiceImpl implements CertTypeService {
	@Autowired
	private AccountTypeCertMapper accountTypeCertMapper;

	@Override
	public List<Map<String, Object>> queryAllCertType() {
		
		return accountTypeCertMapper.queryAllCertType();
	}

	@Override
	public int insertAcctTypeCert(AccountTypeCert accttype) {
		
		return accountTypeCertMapper.insert(accttype);
	}

	@Override
	public int deleteAcctTypeCert(AccountTypeCert accttype) {

		return accountTypeCertMapper.deleteAcctTypeCert(accttype);
	}
}
