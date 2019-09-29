package junit.activiti;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricProcessInstanceQuery;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.atguigu.atcrowdfunding.activiti.listener.NoListener;
import com.atguigu.atcrowdfunding.activiti.listener.YesListener;

public class TestActiviti {
	ApplicationContext ioc=new ClassPathXmlApplicationContext("spring/spring-*.xml");//获取springIOC容器
	ProcessEngine processEngine = (ProcessEngine)ioc.getBean("processEngine");	//使用IOC获取获取流程框架程序引擎对象

	@Test
	public void Test01() {
		System.out.println(processEngine); 	//创建引擎对象时会自动在数据库创建23张表
	}
	/**
	 * 部署流程
	 */
	@Test
	public void Test02() {
		RepositoryService repositoryService = processEngine.getRepositoryService();
		Deployment deploy = repositoryService.createDeployment().addClasspathResource("MyProcesstest07.bpmn").deploy();
		
	}
	
	/**
	 * 查询流程
	 */
	@Test
	public void Test03() {
		RepositoryService repositoryService = processEngine.getRepositoryService();
		ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery();
		List<ProcessDefinition> list = processDefinitionQuery.list();
		for (ProcessDefinition processDefinition : list) {
			System.out.println(""+processDefinition.getId());
			System.out.println(""+processDefinition.getName());
			System.out.println(""+processDefinition.getVersion());
			System.out.println(""+processDefinition.getKey());
		}
	}
	
	/**
	 * 启动流程
	 * 	 act_hi_procinst :   历史流程实例表			保存了流程实例的信息
	 	 act_hi_taskinst :   历史任务实例表			保存了流程任务的相关信息
		 act_hi_actinst:     历史节点表				保存了流程执行的节点顺序
	 	 act_ru_execution :  运行时流程执行实例表		保存了当前流程执行的节点数据,流程开始会自动完成,直接执行第一个任务
	 	 act_ru_task : 		  运行时任务节点表		保存当前流程执行的任务数据

	 */
	
	@Test
	public void Test04() {
		RepositoryService repositoryService = processEngine.getRepositoryService();
		//查询流程定义
		ProcessDefinition processDefinition = repositoryService
			.createProcessDefinitionQuery()
			.processDefinitionKey("myProcess")
			.latestVersion()
			.singleResult();

		RuntimeService runtimeService = processEngine.getRuntimeService();	//获取运行服务
		//根据已定义的流程id来启动流程
		ProcessInstance processInstance = runtimeService.startProcessInstanceById(processDefinition.getId());
		System.out.println(processInstance);

		
		
	}
	
	/**
	 * 根据委托人来查询任务
	 */
	@Test
	public void Test05() {
//		RepositoryService repositoryService = processEngine.getRepositoryService();
//		//查询流程定义
//		ProcessDefinition processDefinition = repositoryService
//			.createProcessDefinitionQuery()
//			.processDefinitionKey("myProcess")
//			.latestVersion()
//			.singleResult();

		TaskService taskService = processEngine.getTaskService();		//获取任务服务		
		TaskQuery taskQuery = taskService.createTaskQuery();			//使用任务服务创建任务查询实例		
		List<Task> list = taskQuery.taskAssignee("zhangsan").list();	//获取张三的任务列表
		List<Task> list2 = taskQuery.taskAssignee("lisi").list();		//获取李四的任务列表
		for (Task task : list) {
			System.out.println("id="+task.getId());
			System.out.println("name="+task.getName());
			taskService.complete(task.getId()); 	//根据任务id，完成任务
		}
		for (Task task : list2) {
			System.out.println("id="+task.getId());
			System.out.println("name="+task.getName());
			taskService.complete(task.getId()); 	//根据任务id，完成任务
		}
		
		System.out.println("------------------------------");
		List<Task> list3 = taskQuery.taskAssignee("zhangsan").list();	//获取张三完成任务后的任务列表
		List<Task> list4 = taskQuery.taskAssignee("lisi").list();		//获取李四的任务列表
		System.out.println("张三的任务：");
		for (Task task : list3) {
			System.out.println("id="+task.getId());
			System.out.println("name="+task.getName());
		}
		System.out.println("李四的任务：");
		for (Task task : list4) {
			System.out.println("id="+task.getId());
			System.out.println("name="+task.getName());			
		}
		
	}
	
	/**
	 * 查询历史任务
	 */
	@Test
	public void Test06() {
		
		 HistoryService historyService = processEngine.getHistoryService();	//获取历史服务
		 HistoricProcessInstanceQuery query = historyService.createHistoricProcessInstanceQuery();
		 HistoricProcessInstance result = query.processInstanceId("101").finished().singleResult();
		System.out.println(result);
		
	}
	
	/**
	 * 根据组名来查询任务
	 * 领取任务
	 */
	@Test
	public void Test07() {

		TaskService taskService = processEngine.getTaskService();		//获取任务服务		
		TaskQuery taskQuery = taskService.createTaskQuery();			//使用任务服务创建任务查询实例		
		List<Task> list = taskQuery.taskCandidateGroup("tl").list();	//获取tl组的任务列表
		long count = taskQuery.taskAssignee("zhangsan").count(); 		//获取张三领取任务前的任务数量
		System.out.println("张三领取任务前的任务数量"+count);
		for (Task task : list) {
			System.out.println("id="+task.getId());
			System.out.println("name="+task.getName());
			taskService.claim(task.getId(), "zhangsan");; 	//领取任务
		}		
		taskQuery = taskService.createTaskQuery();
		count = taskQuery.taskAssignee("zhangsan").count(); 		//获取张三领取任务后的任务数量
		System.out.println("张三领取任务后的任务数量"+count);
		
	}
	
	/**
	 * 测试流程监听
	 */
	@Test
	public void Test08() {
		Map<String, Object> varMap = new HashMap<String, Object>();	//设置流程变量
		varMap.put("yeslistener", new YesListener());
		varMap.put("nolistener", new NoListener());
		
		ProcessInstance pi = processEngine.getRuntimeService().startProcessInstanceByKey("myProcess", varMap);
		
		TaskService taskService = processEngine.getTaskService();
		
		List<Task> tasks = taskService.createTaskQuery().taskAssignee("zhangsan").list();
		
		System.out.println( "zhangsan的任务数量 = " + tasks.size() );
		
		for ( Task task : tasks ) {
			taskService.setVariable(task.getId(), "flag", false);
			taskService.complete(task.getId());
		}
		
		System.out.println( "流程结束" );
	}
	
	
	
}
