package com.aljamaa.dao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.TreeSet;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import net.sf.jsr107cache.Cache;
import net.sf.jsr107cache.CacheException;
import net.sf.jsr107cache.CacheFactory;
import net.sf.jsr107cache.CacheManager;

import com.aljamaa.entity.Momin;
import com.aljamaa.entity.Statistic;
import com.aljamaa.entity.Task;
import com.aljamaa.entity.TaskSeed;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.labs.taskqueue.Queue;
import com.google.appengine.api.labs.taskqueue.QueueFactory;
import com.google.appengine.api.labs.taskqueue.TaskOptions;
import com.google.gwt.user.datepicker.client.CalendarUtil;

public class TaskDao {

	static Cache cache;
	EntityManager em;
	public TaskDao() {
		em=EMF.get().createEntityManager();
	}
	
	@SuppressWarnings("unchecked")
	public List<Task> getWeekTasks(String momin, String group, Date startWeek)
	{
		Query query;
		List<Task> list;
		TimeZone.setDefault(TimeZone.getTimeZone("GMT"));
//		String keyWeek = momin + startWeek.toGMTString().substring(0, 11);
		if(group == null){
			query = em.createQuery("select from "+Task.class.getName()+" t where t.mominId = :momin AND t.date >= :start  AND  t.date  < :end order by date asc");	   
//			list = (List<Task>) getCache().get(keyWeek);
//			if(list!=null)
//				return list;		
		}else{
			query = em.createQuery("select from "+Task.class.getName()+" t where t.mominId = :momin AND  group =:group AND  t.date >= :start  AND  t.date  < :end order by date asc");	   
			query.setParameter("group",Integer.parseInt(group));			
		}		
	    Date end = CalendarUtil.copyDate(startWeek);
	    CalendarUtil.addDaysToDate(end,7);
	    query.setParameter("momin",momin);
	    query.setParameter("start",startWeek);
//	    end.setTime(end.getTime()-2000);
	    query.setParameter("end",end);	    
	    list =new  ArrayList((List<Task>)query.getResultList());
	    
//	    if(group == null)
//	    	getCache().put(keyWeek, list);

	    return list;
	}
	@SuppressWarnings("unchecked")
	public Task[] getWeekTasksStat(String name, String momin, Date startWeek)
	{
		Query query;
		List<Task> list;
		TimeZone.setDefault(TimeZone.getTimeZone("GMT"));

		query = em.createQuery("select from "+Task.class.getName()+" t where t.mominId = :momin AND  name =:name AND  t.date >= :start  AND  t.date  < :end order by date asc");	   
		query.setParameter("name",name);			

		Date end = CalendarUtil.copyDate(startWeek);
		CalendarUtil.addDaysToDate(end,7);
		query.setParameter("momin",momin);
		query.setParameter("start",startWeek);
//	    end.setTime(end.getTime()-2000);
		query.setParameter("end",end);	    
		list =new  ArrayList((List<Task>)query.getResultList());
		
//	    if(group == null)
//	    	getCache().put(keyWeek, list);
		
		return (Task[]) list.toArray();
	}
//	public PreparedQuery getAllTasks(){
//		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
//		return ds.prepare(new com.google.appengine.api.datastore.Query(Task.class.getSimpleName()));
//		
//		
//	}
	
//	public List<Task> getFriendTask(String momin , int group , Date start){
//		Query query = em.createQuery("select from "+Task.class.getName()+" t where t.mominId = :momin AND  group =:group AND  t.date >= :start  AND  t.date  < :end order by date asc");	   
//	    Date end = CalendarUtil.copyDate(start);
//	    CalendarUtil.addDaysToDate(end,7);
//	    query.setParameter("momin","mominid");
//	    query.setParameter("group",group);
//	    query.setParameter("start",start);
//	    query.setParameter("end",end);	    
//	    ArrayList<Task> list= new ArrayList<Task>();
//	    list =new  ArrayList((List<Task>)query.getResultList());
//	    return list;
//	}
	
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
//		Date startWeek = Task.moveToDay(CalendarUtil.copyDate(task.getDate()), -1);		
//		String keyWeek = task.getMominId() + startWeek.toGMTString().substring(0, 11);
		boolean update=true;
		TimeZone.setDefault(TimeZone.getTimeZone("GMT"));
		em.getTransaction().begin();
		if(task.getId()!=null){
			em.merge(task);
		}
		else{
			em.persist(task);
			update = false;
		}
		em.flush();
		em.getTransaction().commit();
		if(!update)
			return;
		
		Queue que= QueueFactory.getDefaultQueue();
		que.add(TaskOptions.Builder.url("task/tskseed?s=&n="+task.getName()+"&m="+task.getMominId()+"&w="+task.getDate().getTime()+"&t="+new Date().getTime()));
//		getCache().put(keyWeek, null);
	}
	
	public void deleteTask(Task c)
	{
		em.getTransaction().begin();
		em.remove(c);
		em.getTransaction().commit();
	}

	public void saveTask(Object[] tasks, String mominId) {
		if(mominId==null){
			for (Object o : tasks) {
				saveTask((Task) o);			
			}
			return;
		}
		Task t;
		for (Object o : tasks) {
			t=(Task) o;
			t.setMominId(mominId);
			saveTask(t);			
		}
	}

	public void saveSeed(TaskSeed seed) {
		em.getTransaction().begin();
		if(seed.getId()==null)
			em.persist(seed);
		else 
			em.merge(seed);
		em.getTransaction().commit();
	}
	
	public void save(Momin m){
		em.getTransaction().begin();
		if(m.getId()==null)
			em.persist(m);
		else 
			em.merge(m);
		em.getTransaction().commit();
	}
	
//	public List<Momin> get(){	
//
//		Query query = em.createQuery("select from "+Momin.class.getName()+" t where t.friendsCalendar > :momin ");	   
//		query.setParameter("momin","yoyo2");
//		ArrayList<Momin> list =new  ArrayList((List<Task>)query.getResultList());		    
//		return list;
//	}
//	
	public Momin getMomin(String id)
	{
		return em.find(Momin.class, id);
	}

	public Momin getMominByEmail(String email) {
		Query query = em.createQuery("select from "+Momin.class.getName()+" t where t.email =:mail ");	   
		query.setParameter("mail",email);
		ArrayList<Momin> list =new  ArrayList((List<Task>)query.getResultList());		    
		if(list.size()==0)
			return null;
		return list.get(0);
	}

	public TaskSeed getSeed(long id) {
		Key key=KeyFactory.createKey(TaskSeed.class.getSimpleName(), id);
		TaskSeed s=em.find(TaskSeed.class,key);
		return s;

	}
	
	public void deleteTasksSeed(long seedId, Date afterDate){
		Query query = em.createQuery("select Key from "+Task.class.getName()+" t where t.seedId = :seed AND  t.date >= :after");	   
		query.setParameter("seed",seedId);
		query.setParameter("after",afterDate);
		List<Entity> list =(List<Entity>)query.getResultList();		    
		for(Entity e : list){
			em.remove(e);
		}
	}

	public void saveStat(Statistic stat) {

		stat.setUpdate(new Date().getTime());

		em.getTransaction().begin();
		if(stat.getKey()==null){
			Calendar cal = Calendar.getInstance();
			cal.setTime(stat.getDate());
			String key = stat.getMomin()+"$"+stat.getName()+"$"+cal.get(Calendar.YEAR)+"$"+cal.get(Calendar.WEEK_OF_YEAR);
			em.persist(stat);
		}
		else 
			em.merge(stat);
		em.getTransaction().commit();
	}

	public Statistic[] getStatisitc(String name, String momin, Date start, int nbrWeek) {
		
		List<Key> keys = new ArrayList<Key>();
		String st=null;
		Calendar cal = Calendar.getInstance();
		cal.setTime(start);
		for(int i=0 ; i < nbrWeek ; i++){			
			st = momin+"$"+name+"$"+cal.get(Calendar.YEAR)+"$"+cal.get(Calendar.WEEK_OF_YEAR);
			keys.add(KeyFactory.createKey(Statistic.class.getSimpleName(), st));
			cal.add(Calendar.DAY_OF_MONTH, 7);			
		}
		
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();	
		Map<Key, Entity> map = ds.get(keys);
		return (Statistic[]) map.values().toArray();
		
	}
}
