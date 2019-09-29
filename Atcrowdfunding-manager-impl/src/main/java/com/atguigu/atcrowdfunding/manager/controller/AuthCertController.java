package com.atguigu.atcrowdfunding.manager.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.atguigu.atcrowdfunding.bean.Member;
import com.atguigu.atcrowdfunding.bean.Ticket;
import com.atguigu.atcrowdfunding.manager.service.CertService;
import com.atguigu.atcrowdfunding.manager.service.TicketServiceManager;
import com.atguigu.atcrowdfunding.util.AjaxResult;
import com.atguigu.atcrowdfunding.util.Page;
import com.atguigu.atcrowdfunding.vo.CertImg;

@Controller
@RequestMapping("/authcert")
public class AuthCertController {
	
	@Autowired
	private ProcessEngine processEngine;
	@Autowired
	private TicketServiceManager ticketService ;
	@Autowired
	private CertService certService ;
	/**
	 * 实名认证首页
	 * @return
	 */
	@RequestMapping("/index")
	public String index() {
		return "authcert/index";
	}
	/**
	 * 认证信息
	 * @return
	 */
	@RequestMapping("/show")
	public String show( Integer memberid, Model model) {
		Member member = certService.queryMemberById(memberid);
		
		// 查询当前会员提交资质文件
		List<CertImg> certImgs = certService.queryCertImgsByMemberid(memberid);
		
		model.addAttribute("member", member);
		model.addAttribute("certImgs", certImgs);

		return "authcert/show";
	}
	

	/**
	 * 分页查询
	 * @param pageno
	 * @param pagesize
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/doIndex")
	public Object pageQuery(String pagetext, Integer pageno, Integer pagesize) {
		AjaxResult result = new AjaxResult();
		
		try {
			Page<Map<String, Object>> page = new Page<Map<String, Object>>(pageno,pagesize);
			
			TaskService taskService = processEngine.getTaskService();
			RepositoryService repositoryService = processEngine.getRepositoryService();
			
			//根据流程定义的关键字和委托组名来查询任务			
			TaskQuery query = taskService.createTaskQuery();
			List<Task> tasks = query.processDefinitionKey("auth")
					.taskCandidateGroup("backuser").listPage(page.getStartIndex(), page.getPagesize());	
			List<Map<String, Object>> taskMapList = new ArrayList<Map<String, Object>>();//避免JSON数据转换出错
			for ( Task task : tasks ) {
				Map<String, Object> taskMap = new HashMap<String, Object>();
				taskMap.put("id", task.getId());
				taskMap.put("name", task.getName());
				//通过任务表的流程定义id查询流程定义
				ProcessDefinition pd = repositoryService.createProcessDefinitionQuery()
					    .processDefinitionId(task.getProcessDefinitionId())
					    .singleResult();
				
				taskMap.put("procDefName", pd.getName());	//流程定义id
				taskMap.put("procDefVersion", pd.getVersion());	//流程定义版本
				
				// 通过流程查找流程审批单，再查询会员信息
				Ticket ticket = ticketService.queryTicketByPiid(task.getProcessInstanceId());
				Member member = ticketService.queryMemberByTicket(ticket);
				taskMap.put("memberName", member.getRealname());
				taskMap.put("memberid", member.getId());
				
				taskMapList.add(taskMap);
			}
			
			// 获取数据的总条数
			int count = (int)query.count(); //同一个query 对象,查询条件是一样的

			page.setData(taskMapList);
			page.setTotalsize(count );
				
			result.setPage(page);				
			result.setSuccess(true);

		} catch ( Exception e ) {
			e.printStackTrace();
			result.setSuccess(false);
		}
		
		return result;
	}
	/**
	 * 审核通过
	 * @param taskId
	 * @param memberid
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/pass")
	public Object pass( String taskId, Integer memberid ) {
		AjaxResult result = new AjaxResult();
		
		try {
			TaskService taskService = processEngine.getTaskService();
			taskService.setVariable(taskId, "flag", true);
			taskService.setVariable(taskId, "memberid", memberid);
			// 传递参数，让流程继续执行
			taskService.complete(taskId);
			result.setSuccess(true);
		} catch ( Exception e ) {
			e.printStackTrace();
			result.setSuccess(false);
		}
		
		return result;
	}
	
	/**
	 * 审核不通过
	 * @param taskId
	 * @param memberid
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/refuse")
	public Object refuse(String taskId, Integer memberid) {
		AjaxResult result = new AjaxResult();
		
		try {
			TaskService taskService = processEngine.getTaskService();
			taskService.setVariable(taskId, "flag", false);
			taskService.setVariable(taskId, "memberid", memberid);
			// 传递参数，让流程继续执行
			taskService.complete(taskId);
			result.setSuccess(true);
		} catch ( Exception e ) {
			e.printStackTrace();
			result.setSuccess(false);
		}		
		
		return result;
	}

	
}
