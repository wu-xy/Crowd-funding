package com.atguigu.atcrowdfunding.manager.controller;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.atguigu.atcrowdfunding.bean.Advert;
import com.atguigu.atcrowdfunding.util.AjaxResult;
import com.atguigu.atcrowdfunding.util.Page;

@Controller
@RequestMapping("/process")
public class ProcessController {

	@Autowired
	private RepositoryService repositoryService;
	

	@RequestMapping("/index")
	public String index() {
		return "process/index";
	}
	
	@RequestMapping("/showimg")
	public String showimg() {
		return "process/showimg";
	}
			
	@ResponseBody
	@RequestMapping("/doIndex")
	public Object index(@RequestParam(value="pageno",required=false,defaultValue="1") Integer pageno,
			@RequestParam(value="pagesize",required=false,defaultValue="10") Integer pagesize,
			String queryText){
		AjaxResult result = new AjaxResult();
		
		try {
			ProcessDefinitionQuery query = repositoryService.createProcessDefinitionQuery(); //repositoryService对象可以采用自动装配
			
			List<ProcessDefinition> pds = query.listPage((pageno-1)*pagesize, pagesize);
			// 使用Map来代替流程定义对象
			List<Map<String, Object>> pdMaps = new ArrayList<Map<String, Object>>(); //圈圈
			
			for ( ProcessDefinition pd : pds ) {
				Map<String, Object> pdMap = new HashMap<String, Object>();
				pdMap.put("id", pd.getId());
				pdMap.put("name", pd.getName());
				pdMap.put("key", pd.getKey());
				pdMap.put("version", pd.getVersion());
				pdMaps.add(pdMap);
			}
			
			int count = (int)query.count();
			
			Page<Map<String, Object>> page = new Page<Map<String, Object>>(pageno,pagesize);
			page.setData(pdMaps);
			page.setTotalsize(count );
			result.setPage(page);
			
			result.setSuccess(true);
		} catch ( Exception e ) {
			e.printStackTrace();
			result.setSuccess(false);
		}
		
		return result;

	}
	
	@ResponseBody
	@RequestMapping("/deploy")
	public Object deploy(HttpServletRequest request, Advert advert ,HttpSession session) {
		AjaxResult result = new AjaxResult();
		System.out.println("进入部署方法");		
		try {
			MultipartHttpServletRequest mreq = (MultipartHttpServletRequest)request;	//可以用来得到上传的文件
			
			MultipartFile mfile = mreq.getFile("procDefFile");		//根据名称获取上传的文件			
			repositoryService.createDeployment()
		    .addInputStream(mfile.getOriginalFilename(), mfile.getInputStream()).deploy();	//部署流程
		
			result.setSuccess(true);
			
		} catch ( Exception e ) {
			e.printStackTrace();
			result.setSuccess(false);
		}
		
		return result;
	}
	
	/**
	 * 删除流程定义
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/doDelete")
	public Object doDelete(String id) {
		AjaxResult result = new AjaxResult();
		
		try {
					
			ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
				.processDefinitionId(id).singleResult();	//流程定义对象
			
			repositoryService.deleteDeployment(processDefinition.getDeploymentId(),true);	//根据部署id删除已部署的流程
		
			result.setSuccess(true);
			
		} catch ( Exception e ) {
			e.printStackTrace();
			result.setSuccess(false);
		}
		
		return result;
	}
	
	@RequestMapping("/queryProcDefImg")
	public void queryProcDefImg(String id, HttpServletResponse response) throws Exception{		

		//根据主键查询流程定义
		ProcessDefinition pd = repositoryService.createProcessDefinitionQuery().processDefinitionId(id).singleResult();
		
		//读取流程定义图片
		InputStream in = repositoryService.getResourceAsStream(pd.getDeploymentId(), pd.getDiagramResourceName());
		OutputStream out = response.getOutputStream();
		
		//将流程定义图片输出给客户端
//		int i = -1 ;
//		while(( i = in.read() ) != -1){
//			out.write(i);
//		}
		IOUtils.copy(in, out);
	}

	
}
