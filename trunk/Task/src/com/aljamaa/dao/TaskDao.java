package com.aljamaa.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;
import java.util.TreeSet;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.aljamaa.entity.Task;
import com.aljamaa.entity.TaskSeed;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.gwt.user.datepicker.client.CalendarUtil;

public class TaskDao {

	PersistenceManager pm;
	public TaskDao() {
		pm=PMF.get().getPersistenceManager();
	}
	
	public List<Task> getWeekTasks(String momin, Date startWeek)
	{		
		TimeZone.setDefault(TimeZone.getTimeZone("GMT"));
		//Query query=pm.newQuery("select from Cmnt");//+Cmnt.class.getSimpleName());//+" where key=="+key+" order by date desc  range "+(p*10)+","+((p+1)*10));
		Query query = pm.newQuery(Task.class);
	    query.setFilter("mominId == momin && date >= start  &&  date  < end");
//	    query.setFilter(" date >= start  &&  date  <= end");
//	    query.setRange(p*10,(p+1)*10+1);	    
	    query.setOrdering("date asc");
	   
	    query.declareParameters("String momin , java.util.Date start, java.util.Date end");
	    Date end = CalendarUtil.copyDate(startWeek);
	    CalendarUtil.addDaysToDate(end,7);
	    List<Task> list =new  ArrayList((List<Task>)query.execute("mominid",startWeek,end ));
	    return list;
		//return (List<Cmnt>) query.execute(key);
	}
	
	public List<TaskSeed> getSeedToUpdate()
	{		
		TimeZone.setDefault(TimeZone.getTimeZone("GMT"));
		//Query query=pm.newQuery("select from Cmnt");//+Cmnt.class.getSimpleName());//+" where key=="+key+" order by date desc  range "+(p*10)+","+((p+1)*10));
		Query query = pm.newQuery(TaskSeed.class);
	    query.setRange(0,1);	    
		query.setOrdering("update asc");
		
		List<TaskSeed> list =new  ArrayList((List<Task>)query.execute( ));
		return list;
		//return (List<Cmnt>) query.execute(key);
	}
	
	public Task getTaskById(Long  id)
	{
			Key key=KeyFactory.createKey(Task.class.getSimpleName(), id);
			Task kh=pm.getObjectById(Task.class,key);
			return kh;
	}
	
	

	public void saveTask(Task task)
	{
		TimeZone.setDefault(TimeZone.getTimeZone("GMT"));
		task.setMominId("mominid");
		pm.makePersistent(task);
//		pm.close();
	}
	
//	public void saveTasks(List<Task> list)
//	{
//		pm.makePersistent(list);
//	}
	public void deleteTask(Task c)
	{
		pm.deletePersistent(c);
		pm.close();
	}

	public void saveTask(Object[] tasks) {
		for (Object o : tasks) {
			saveTask((Task) o);
			
		}
		
	}

	public void saveSeed(TaskSeed seed) {
		pm.makePersistent(seed);
		
	}
}
