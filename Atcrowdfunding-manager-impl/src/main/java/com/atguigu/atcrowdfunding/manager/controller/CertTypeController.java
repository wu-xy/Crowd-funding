package com.atguigu.atcrowdfunding.manager.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.atguigu.atcrowdfunding.bean.AccountTypeCert;
import com.atguigu.atcrowdfunding.bean.Cert;
import com.atguigu.atcrowdfunding.manager.service.CertService;
import com.atguigu.atcrowdfunding.manager.service.CertTypeService;
import com.atguigu.atcrowdfunding.util.AjaxResult;

@Controller
@RequestMapping("/certtype")
public class CertTypeController {

	@Autowired
	private CertTypeService certTypeService;
	@Autowired
	private CertService certService;
	
	/**
	 * 展示首页
	 * @param model
	 * @return
	 */
	@RequestMapping("/index")
	public String index( Model model) {
		
		List<Cert> certs = certService.queryAllCert();
		model.addAttribute("certs", certs);
		
		List<Map<String, Object>> maps=certTypeService.queryAllCertType();
		model.addAttribute("maps", maps);
		
		return "certType/index";
	}
	
	/**
	 * 添加映射关系
	 * @param session
	 * @param accttype
	 * @return
	 */
	@RequestMapping("/insertAcctTypeCert")
	@ResponseBody
	public Object insertAcctTypeCert(HttpSession session, AccountTypeCert accttype){
		
		AjaxResult result = new AjaxResult();
		try {
			int count = certTypeService.insertAcctTypeCert(accttype);						
			result.setSuccess(count==1);
		} catch( Exception e ) {
			e.printStackTrace();
			result.setSuccess(false);
		}
		
		return result;

	}
	
	@RequestMapping("/deleteAcctTypeCert")
	@ResponseBody
	public Object deleteAcctTypeCert(HttpSession session, AccountTypeCert accttype){
		
		AjaxResult result = new AjaxResult();
		try {
			int count = certTypeService.deleteAcctTypeCert(accttype);						
			result.setSuccess(count==1);
		} catch( Exception e ) {
			e.printStackTrace();
			result.setSuccess(false);
		}
		
		return result;

	}
	
	
}
