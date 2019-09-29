package com.atguigu.atcrowdfunding.potal.controller;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.atguigu.atcrowdfunding.activiti.listener.PassListener;
import com.atguigu.atcrowdfunding.activiti.listener.RefuseListener;
import com.atguigu.atcrowdfunding.bean.Cert;
import com.atguigu.atcrowdfunding.bean.Member;
import com.atguigu.atcrowdfunding.bean.Ticket;
import com.atguigu.atcrowdfunding.manager.service.CertService;
import com.atguigu.atcrowdfunding.potal.service.MemberService;
import com.atguigu.atcrowdfunding.potal.service.TicketService;
import com.atguigu.atcrowdfunding.util.AjaxResult;
import com.atguigu.atcrowdfunding.util.Const;
import com.atguigu.atcrowdfunding.vo.CertImg;
import com.atguigu.atcrowdfunding.vo.Data;

@Controller
@RequestMapping("/member")
public class MemberController {
	@Autowired
	private MemberService memberService ;
	@Autowired
	private TicketService ticketService ;
	@Autowired
	private CertService certService;
	@Autowired
	private ProcessEngine processEngine;
	
	/**
	 * 会员首页
	 * @return
	 */
	@RequestMapping("")
	public String member(){	
		return "member/member";
	}
	/**
	 * 控制页面跳转
	 * @param session
	 * @return
	 */
	@RequestMapping("/apply")
	public String apply(HttpSession session){
		Member member = (Member) session.getAttribute(Const.LOGIN_USER);
		Ticket ticket=ticketService.getByMemberId(member.getId());
		if(ticket==null) {
			ticket=new Ticket();
			ticket.setMemberid(member.getId());
			ticket.setStatus("0");		//初始状态为0
			ticket.setPstep("apply");	//初始步骤为apply
			ticketService.saveTicket(ticket);
		}else {
			String pstep = ticket.getPstep();
			if("accttype".equals(pstep)) {
				return "redirect:/member/basicinfo.htm";
			}
			if("basicinfo".equals(pstep)) {
				
				return "redirect:/member/uploadCert.htm";
			}
			if("uploadCert".equals(pstep)) {
				return "redirect:/member/checkEmail.htm";
			}
			if("checkEmail".equals(pstep)) {
				return "redirect:/member/checkAuthcode.htm";
			}
			if("checkAuthcode".equals(pstep)) {
				return "redirect:/member.htm";
			}
		}
		
		
		return "redirect:/member/accttype.htm";
	}
	
	//选择账户类型
	@RequestMapping("/accttype")
	public String accttype(){		
		return "member/accttype";
	}
	//填写基本信息	
	@RequestMapping("/basicinfo")
	public String basicinfo(){		
		return "member/basicinfo";
	}
	//上传资质文件
	@RequestMapping("/uploadCert")
	public String uploadCert(HttpSession session){	
		Member member = (Member) session.getAttribute(Const.LOGIN_USER);
		List<Cert> certByAccttype = certService.queryCertByAccttype(member.getAccttype());	//查询需要上传的文件
		session.setAttribute("certByAccttype", certByAccttype);
		
		return "member/uploadCert";
	}
	//填写邮箱
	@RequestMapping("/checkEmail")
	public String checkMail(){		
		return "member/checkEmail";
	}
	//输入验证码 
	@RequestMapping("/checkAuthcode")
	public String checkAuthcode(){		
		return "member/checkAuthcode";
	}
	
	
	//更新账户类型
	@RequestMapping("/updateAcctType")
	@ResponseBody
	public Object updateAcctType(HttpSession session, String accttype){
		
		AjaxResult result = new AjaxResult();
		try {
			
			// 获取登录会员信息
			Member loginMember = (Member)session.getAttribute(Const.LOGIN_USER);
			loginMember.setAccttype(accttype);		//session中的账户类型
			// 更新账户类型
			int count = memberService.updateAcctType(loginMember);	
			
			Ticket ticket=ticketService.getByMemberId(loginMember.getId());
			ticket.setPstep("accttype");
			ticketService.updatePstep(ticket);		//更新操作步骤
			
			result.setSuccess(count==1);
		} catch( Exception e ) {
			e.printStackTrace();
			result.setSuccess(false);
		}
		
		return result;

	}
	//更新基本信息
	@RequestMapping("/updateBasicinfo")
	@ResponseBody		//String realname,String cardnum,String tel
	public Object updateBasicinfo(HttpSession session, Member member ){
		
		AjaxResult result = new AjaxResult();
		try {
			
			// 获取登录会员信息
			Member loginMember = (Member)session.getAttribute(Const.LOGIN_USER);
			loginMember.setRealname(member.getRealname());		//	更新session中的用户信息
			loginMember.setCardnum(member.getCardnum());
			loginMember.setTel(member.getTel());
			// 更新数据库中的用户信息
			int count = memberService.updateBasicinfo(loginMember);	
			
			Ticket ticket=ticketService.getByMemberId(loginMember.getId());
			ticket.setPstep("basicinfo");
			ticketService.updatePstep(ticket);		//更新操作步骤
			
			result.setSuccess(count==1);
		} catch( Exception e ) {
			e.printStackTrace();
			result.setSuccess(false);
		}
		
		return result;

	}
	/**
	 * 资质文件上传
	 * @param session
	 * @param member
	 * @return
	 */
	@RequestMapping("/doUploadCertFile")
	@ResponseBody		
	public Object doUploadCertFile(HttpSession session, Data data ){
		
		AjaxResult result = new AjaxResult();
		try {
			
			ServletContext application = session.getServletContext();
			String realPath = application.getRealPath("pic");	//获取pic目录的真实路径

			// 获取登录会员信息
			Member loginMember = (Member)session.getAttribute(Const.LOGIN_USER);
			
			List<CertImg> certimgs = data.getCertimgs(); //获取到表单上传的文件集合
			for (CertImg certImg : certimgs) {
				certImg.setMemberid(loginMember.getId()); 
				//将文件保存到硬盘中
				MultipartFile imgfile = certImg.getImgfile();
				String filename = imgfile.getOriginalFilename(); //获取上传文件的名称
				//为文件重命名，防止上传文件名重复覆盖了原来的文件，新文件名=随机字符串+原文件的后缀名
				String newFilename =UUID.randomUUID().toString()+filename.substring(filename.lastIndexOf("."));
				String filePath = realPath + "/certs/" + newFilename;	//文件的路径
				imgfile.transferTo(new File(filePath));		//保存到路径
								
				certImg.setIconpath(newFilename);
			}
			
			//将文件保存到数据库中
			memberService.insertMemberCerts(certimgs);
			
			Ticket ticket=ticketService.getByMemberId(loginMember.getId());
			ticket.setPstep("uploadCert");
			ticketService.updatePstep(ticket);		//更新操作步骤
			
			result.setSuccess(true);
		} catch( Exception e ) {
			e.printStackTrace();
			result.setSuccess(false);
		}
		
		return result;

	}
	
	/**
	 * 给邮箱发送验证码
	 * @param session
	 * @param member
	 * @return
	 */
	@RequestMapping("/doCheckEmail")
	@ResponseBody		
	public Object doCheckEmail(HttpSession session, String email ){
		
		AjaxResult result = new AjaxResult();
		try {
			
			// 获取登录会员信息
			Member loginMember = (Member)session.getAttribute(Const.LOGIN_USER);
			
			StringBuilder sb=new StringBuilder();
			for (int i = 0; i < 4; i++) {
				sb.append(new Random().nextInt(10)); //产生4位0-9的数字验证码
			}
								
			//启动流程实例
			Map<String, Object> variables=new HashMap<String, Object>();
			variables.put("toemail", email);	
			variables.put("authcode", sb.toString());
			variables.put("loginacct", loginMember.getLoginacct());
			variables.put("passListener", new PassListener());
			variables.put("refuseListener", new RefuseListener());
			RuntimeService runtimeService = processEngine.getRuntimeService();
			ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("auth", variables);
			
			// 更新流程步骤表中的信息						
			Ticket ticket=ticketService.getByMemberId(loginMember.getId());
			ticket.setPstep("checkEmail");				//更新操作步骤
			ticket.setAuthcode(sb.toString());			//验证码
			ticket.setPiid(processInstance.getId());	//流程实例id
			ticketService.updatePAP(ticket);		
			
			result.setSuccess(true);
		} catch( Exception e ) {
			e.printStackTrace();
			result.setSuccess(false);
		}
		
		return result;

	}
	
	/***
	 * 验证验证码
	 * @param session
	 * @param authcode
	 * @return
	 */
	@RequestMapping("/doCheckAuthcode")
	@ResponseBody		
	public Object doCheckAuthcode(HttpSession session, String authcode ){
		
		AjaxResult result = new AjaxResult();
		try {
			
			// 获取登录会员信息
			Member loginMember = (Member)session.getAttribute(Const.LOGIN_USER);
			//获取流程步骤信息			
			Ticket ticket=ticketService.getByMemberId(loginMember.getId());
			if(ticket.getAuthcode().equals(authcode)) {		//如果验证码正确，完成验证流程		
				TaskService taskService = processEngine.getTaskService();
				//根据流程实例id和委托人找到任务
				Task task = taskService.createTaskQuery().processInstanceId(ticket.getPiid())
						.taskAssignee(loginMember.getLoginacct()).singleResult();
				taskService.complete(task.getId());
				
				loginMember.setAuthstatus("1"); //将实名认证状态改为审核中
				memberService.updataAuthstatus(loginMember);
													
				ticket.setPstep("checkAuthcode");	//更新操作步骤				
				ticketService.updatePstep(ticket);	
				
				result.setSuccess(true);
			}else {
				result.setSuccess(false);
			}					
			
		} catch( Exception e ) {
			e.printStackTrace();
			result.setSuccess(false);
		}
		
		return result;

	}
	
}
