package com.atguigu.atcrowdfunding.potal.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.atguigu.atcrowdfunding.bean.Ticket;
import com.atguigu.atcrowdfunding.potal.dao.TicketMapper;
import com.atguigu.atcrowdfunding.potal.service.TicketService;

@Service 
public class TicketServiceImpl implements TicketService {
	
	@Autowired
	TicketMapper ticketMapper;

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
	public void updateStatus(Ticket ticket) {
		
		ticketMapper.updateStatus(ticket);
	}
	
	
}
