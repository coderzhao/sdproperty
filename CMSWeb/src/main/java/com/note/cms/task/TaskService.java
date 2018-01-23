package com.note.cms.task;

/**
 * 定时任务
 *
 */
public interface TaskService{
//	public void timerRun();//定时任务
	public void calRun();//定时任务，每天凌晨两点，将mongo中的播放数据， 按栏目导出拆分转存
}
