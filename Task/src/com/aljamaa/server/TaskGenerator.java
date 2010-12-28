package com.aljamaa.server;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.aljamaa.entity.Task;
import com.aljamaa.entity.TaskSeed;

import com.google.gwt.user.datepicker.client.CalendarUtil;

public class TaskGenerator {
	
	static public List<Task> generate(TaskSeed seed)
	{
		List< Task> list = new ArrayList<Task>();
		switch(seed.getType()){
		case Task.EVERY_DAY:
		}		
		return list;
	}
	
	public List<Task> everyDay(TaskSeed seed){
		List< Task> list = new ArrayList<Task>();
		Task task=null;
		Date last = seed.getLast();
		for(int i=0;i<10;i++){
			CalendarUtil.addDaysToDate(last,seed.getEvery());
			task = new Task(seed);
			task.setDate(last);
			list.add(task);
		}		
		//TODO update seed
		//seed.setLast(last);
		return list;
	}


	public List<Task> everyWeek(TaskSeed seed){
		List< Task> list = new ArrayList<Task>();
		Task task;
		Date last = seed.getLast();
		for(int i=0;i<10;i++){
			for(int d=0;d<7;d++,i++){
				if(seed.getParam()[d]){
					CalendarUtil.addDaysToDate(moveToDay(last,d),7*seed.getEvery());
					task = new Task(seed);
					task.setDate(last);
					list.add(task);
					
				}
			}
		}		
		return list;
	}
	
	public List<Task> everyMonth(TaskSeed seed){
		List< Task> list = new ArrayList<Task>();
		Task task;
		Date last = seed.getLast();
		for(int i=0;i<10;i++){
				if(seed.getParam()[0]){
//					CalendarUtil.addDaysToDate(moveToDay(last,d),7);
					task = new Task(seed);
					task.setDate(last);
					list.add(task);				
			}
		}		
		return list;
	}
	/**
	 * obtenir la date du jour i suivant la date d
	 * @param d
	 * @param i numero dur jour dans la semaine(0-6)
	 * @return
	 */
	@SuppressWarnings("deprecation")
	private Date moveToDay(Date d,int i)
	{
		if(i>0)
			CalendarUtil.addDaysToDate(d, (i>=d.getDay()?0:7)+i-d.getDay());
		else 
			CalendarUtil.addDaysToDate(d, (i<=d.getDay()?0:-7)+i-d.getDay());
			
		return d;
			
	}
	

}
