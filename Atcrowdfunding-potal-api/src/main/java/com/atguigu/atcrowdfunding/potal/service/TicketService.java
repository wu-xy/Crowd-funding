package com.atguigu.atcrowdfunding.potal.service;

import com.atguigu.atcrowdfunding.bean.Ticket;

public interface TicketService {

	Ticket getByMemberId(Integer id);

	void saveTicket(Ticket ticket);

	void updatePstep(Ticket ticket);

	void updatePAP(Ticket ticket);

	void updateStatus(Ticket ticket);	

	

}
