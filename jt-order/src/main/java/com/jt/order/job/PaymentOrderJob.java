package com.jt.order.job;

import java.util.Date;

import org.joda.time.DateTime;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.jt.order.mapper.OrderMapper;

public class PaymentOrderJob extends QuartzJobBean{
	/**
	 * 需求:
	 * 	 将超时的订单 修改状态为6
	 */
	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		//删除2天天的恶意订单
		ApplicationContext applicationContext = (ApplicationContext) context.getJobDetail().getJobDataMap().get("applicationContext");
		
		OrderMapper orderMapper = applicationContext.getBean(OrderMapper.class);
		Date timeOut = new DateTime().minusDays(2).toDate();
		//实现超时处理
		orderMapper.updateStatus(timeOut);
		System.out.println("定时任务执行完成!!!!");
		
	}
}
