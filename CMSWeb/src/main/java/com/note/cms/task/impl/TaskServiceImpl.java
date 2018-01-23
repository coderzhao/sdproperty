package com.note.cms.task.impl;

import com.note.cms.task.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;


@Component
public class TaskServiceImpl implements TaskService {
	private static final Logger logger = LoggerFactory.getLogger(TaskServiceImpl.class);


	private static AtomicInteger mLock = new AtomicInteger(0);//0表示启动该任务
	private static AtomicInteger mLockDataConvert = new AtomicInteger(1);//1表示不启动该任务，用这个来区分打点服务还是推荐服务

	//注意， 多个shedule任务， 貌似只执行最后一个

//	@Scheduled(cron="0 1/10 *  * * ? ")   //每10分钟执行一次
//	//@Scheduled(cron="1/20 * *  * * ? ")   //每20s执行一次
////	"0 0 12 * * ?" 每天中午12点触发
//	@Override
//	public void timerRun(){
//		logger.info("timerRun");
//	}

//	@Scheduled(cron="1/59 * *  * * ? ")//每分钟执行一次
//	@Scheduled(cron="0 0 2 * * ?")   //每天凌晨2点触发
	@Override
	public void calRun(){
			logger.info("calRun");

	}

}
