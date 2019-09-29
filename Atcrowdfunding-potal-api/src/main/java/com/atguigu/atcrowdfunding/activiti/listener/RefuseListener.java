package com.atguigu.atcrowdfunding.activiti.listener;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.springframework.context.ApplicationContext;

import com.atguigu.atcrowdfunding.bean.Member;
import com.atguigu.atcrowdfunding.bean.Ticket;
import com.atguigu.atcrowdfunding.potal.service.MemberService;
import com.atguigu.atcrowdfunding.potal.service.TicketService;
import com.atguigu.atcrowdfunding.util.ApplicationContextUtils;

public class RefuseListener implements ExecutionListener {

	public void notify(DelegateExecution execution) throws Exception {
		// 审批拒绝
		Integer memberid = (Integer)execution.getVariable("memberid");
		
		// 获取Spring容器
		ApplicationContext context = ApplicationContextUtils.applicationContext;
		
		// 获取service
		MemberService memberService = context.getBean(MemberService.class);
		TicketService ticketService = context.getBean(TicketService.class);
		// 改变会员的状态 
		Member member = new Member();
		member.setId(memberid);
		member.setAuthstatus("0");		//将会员状态改为未实名认证
		memberService.updataAuthstatus(member);
		// Ticket
		Ticket ticket = ticketService.getByMemberId(memberid);
		// 改变流程审批单的状态
		ticket.setStatus("1");		//审批流程已结束
		ticketService.updateStatus(ticket);
	}

}
