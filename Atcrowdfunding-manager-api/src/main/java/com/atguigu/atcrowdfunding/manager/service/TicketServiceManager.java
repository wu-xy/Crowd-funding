package com.atguigu.atcrowdfunding.manager.service;

import com.atguigu.atcrowdfunding.bean.Member;
import com.atguigu.atcrowdfunding.bean.Ticket;

public interface TicketServiceManager {

	Ticket getByMemberId(Integer id);

	void saveTicket(Ticket ticket);

	void updatePstep(Ticket ticket);

	void updatePAP(Ticket ticket);

	Ticket queryTicketByPiid(String processInstanceId);

	Member queryMemberByTicket(Ticket ticket);	

	

}
