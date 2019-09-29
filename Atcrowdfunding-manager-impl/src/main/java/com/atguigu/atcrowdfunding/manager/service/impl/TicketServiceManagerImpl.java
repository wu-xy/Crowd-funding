package com.atguigu.atcrowdfunding.manager.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.atguigu.atcrowdfunding.bean.Member;
import com.atguigu.atcrowdfunding.bean.Ticket;
import com.atguigu.atcrowdfunding.manager.dao.TicketMapperManager;
import com.atguigu.atcrowdfunding.manager.service.TicketServiceManager;

@Service 
public class TicketServiceManagerImpl implements TicketServiceManager {
	
	@Autowired
	TicketMapperManager ticketMapper;

	@Override
	public Ticket getByMemberId(Integer id) {
		
		return ticketMapper.getByMemberId(id);
	}

	@Override
	public void saveTicket(Ticket ticket) {
		
		ticketMapper.saveTicket(ticket);
	}

	@Override
	public void updatePstep(Ticket ticket) {
		
		ticketMapper.updatePstep(ticket);
	}

	@Override
	public void updatePAP(Ticket ticket) {
		
		ticketMapper.updatePAP(ticket);
	}

	@Override
	public Ticket queryTicketByPiid(String pid) {
		
		return ticketMapper.queryTicketByPiid(pid);
	}

	@Override
	public Member queryMemberByTicket(Ticket ticket) {
		
		return ticketMapper.queryMemberByTicket(ticket);
	}
	
	
}
