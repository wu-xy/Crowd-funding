package com.atguigu.atcrowdfunding.activiti.listener;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;

public class YesListener implements ExecutionListener {

	public void notify(DelegateExecution execution) throws Exception {
		System.out.println( "流程通过" );
	}

}
