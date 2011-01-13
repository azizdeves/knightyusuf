package com.aljamaa.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;
import java.util.TreeSet;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import net.sf.jsr107cache.Cache;
import net.sf.jsr107cache.CacheException;
import net.sf.jsr107cache.CacheFactory;
import net.sf.jsr107cache.CacheManager;

import com.aljamaa.entity.Momin;
import com.aljamaa.entity.Task;
import com.aljamaa.entity.TaskSeed;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.gwt.user.datepicker.client.CalendarUtil;

public class TaskDao {

	static Cache cache;
	EntityManager em;
	public TaskDao() {
		em=EMF.get().createEntityManager();
	}
	
	@SuppressWarnings("unchecked")
	public List<Task> getWeekTasks(String momin, Date startWeek)
	{		
		TimeZone.setDefault(TimeZone.getTimeZone("GMT"));
		String keyWeek = momin + startWeek.toGMTString().substring(0, 11);
		List<Task> list = (List<Task>) getCache().get(keyWeek);
		if(list!=null)
			return list;
		
		Query query = em.createQuery("select from "+Task.class.getName()+" t where t.mominId = :momin AND t.date >= :start  AND  t.date  < :end order by date asc");	   
	    Date end = CalendarUtil.copyDate(startWeek);
	    CalendarUtil.addDaysToDate(end,7);
	    query.setParameter("momin","mominid");
	    query.setParameter("start",startWeek);
	    query.setParameter("end",end);	    
	    list =new  ArrayList((List<Task>)query.getResultList());
	    
	    getCache().put(keyWeek, list);
	    
	    return list;
	}
	
	public List<Task> getFriendTask(String momin , int group , Date start){
		Query query = em.createQuery("select from "+Task.class.getName()+" t where t.mominId = :momin AND  group =:group AND  t.date >= :start  AND  t.date  < :end order by date asc");	   
	    Date end = CalendarUtil.copyDate(start);
	    CalendarUtil.addDaysToDate(end,7);
	    query.setParameter("momin","mominid");
	    query.setParameter("group",group);
	    query.setParameter("start",start);
	    query.setParameter("end",end);	    
	    ArrayList<Task> list= new ArrayList<Task>();
	    list =new  ArrayList((List<Task>)query.getResultList());
	    return list;
	}
	
	public List<TaskSeed> getSeedToUpdate()
	{		//TODO use of cursor
		TimeZone.setDefault(TimeZone.getTimeZone("GMT"));
		Query query = em.createQuery("select from "+TaskSeed.class.getName()+" s order by s.update asc ");
		
		List<TaskSeed> list =new  ArrayList((List<Task>)query.getResultList());
		return list;
	}
	
	public Cache getCache()
	{
		if(cache !=null)
			return cache;
		  try {
	            CacheFactory cacheFactory = CacheManager.getInstance().getCacheFactory();
	            cache = cacheFactory.createCache(Collections.emptyMap());
	        } catch (CacheException e) {
	            // ...
	        }
	     return cache;
	}
	
	public Task getTaskById(Long  id)
	{
			Key key=KeyFactory.createKey(Task.class.getSimpleName(), id);
			Task kh=em.find(Task.class,key);
			return kh;
	}
	
	

	public void saveTask(Task task)
	{
		Date startWeek = Task.moveToDay(CalendarUtil.copyDate(task.getDate()), -1);		
		String keyWeek = "momin" + startWeek.toGMTString().substring(0, 11);
		TimeZone.setDefault(TimeZone.getTimeZone("GMT"));
		task.setMominId("mominid");
		em.persist(task);
		em.getTransaction().commit();
		getCache().put(keyWeek, null);
//		pm.close();
	}
	
//	public void saveTasks(List<Task> list)
//	{
//		pm.makePersistent(list);
//	}
	public void deleteTask(Task c)
	{
		em.remove(c);
		em.close();
	}

	public void saveTask(Object[] tasks) {
		for (Object o : tasks) {
			saveTask((Task) o);
			
		}
		
	}

	public void saveSeed(TaskSeed seed) {
		em.persist(seed);
		
	}
	
	public void save(Momin m){
		em.getTransaction().begin();
		em.persist(m);
		em.flush();
		em.getTransaction().commit();
//		em.close();
	}
	public List<Momin> get(){	
			
			Query query = em.createQuery("select from "+Momin.class.getName()+" t where t.friendsCalendar = :momin ");	   
		    query.setParameter("momin","yoyo3");
		    ArrayList<Momin> list =new  ArrayList((List<Task>)query.getResultList());		    
		    return list;
		
	}
	
	public Momin getMomin(String id)
	{
		return em.find(Momin.class, id);
	}
}
