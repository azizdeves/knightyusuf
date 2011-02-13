package com.aljamaa.server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import java.util.logging.Logger;

import com.aljamaa.client.TaskService;
import com.aljamaa.dao.TaskDao;
import com.aljamaa.entity.Momin;
import com.aljamaa.entity.Task;
import com.aljamaa.entity.TaskSeed;

import com.google.appengine.api.labs.taskqueue.Queue;
import com.google.appengine.api.labs.taskqueue.QueueFactory;
import com.google.appengine.api.labs.taskqueue.TaskOptions;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import com.google.gwt.user.datepicker.client.CalendarUtil;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class TaskServiceImpl extends RemoteServiceServlet implements
		TaskService {

	private static final Logger log = Logger.getLogger(TaskServiceImpl.class.getName());
	
	
	public Task createTask(Task task) throws IllegalArgumentException {

		TaskDao tdao= new TaskDao();
		UserService usrSrvc = UserServiceFactory.getUserService();
		task.setMominId(usrSrvc.getCurrentUser().getUserId());
		tdao.saveTask(task);
		return task ;
	}

	@Override
	public List<Task> getWeekTasks(Date startWeek, String user, String group) throws IllegalArgumentException,Exception  {
		UserService usrSrvc = UserServiceFactory.getUserService();
		User u=usrSrvc.getCurrentUser();
		if(u==null)
			throw new Exception("out");
 		TaskDao tdao= new TaskDao();
 		if(user==null)
 			user=u.getUserId(); 		
 		return tdao.getWeekTasks(user,group, startWeek);
		
	}

	@Override
	public Task[] save(Task[] tasks) throws IllegalArgumentException {
		UserService usrSrvc = UserServiceFactory.getUserService();
		User u=usrSrvc.getCurrentUser();
		TaskDao tdao= new TaskDao();
		tdao.saveTask(tasks,u.getUserId());
		
		Queue que = QueueFactory.getDefaultQueue();
		que.add(TaskOptions.Builder.url("/task/tskseed?s="));
		
		return tasks;		
	}

	@Override
	public  List <Task> createSeed(TaskSeed seed) throws IllegalArgumentException {
		TaskDao dao = new TaskDao();
		UserService usrSrvc = UserServiceFactory.getUserService();
		User u=usrSrvc.getCurrentUser();
		seed.setMominId(u.getUserId());		
		dao.saveSeed(seed);
		
		List <Task> list = TaskGenerator.generate(seed);
		dao.saveTask(list.toArray(),null);
		seed.setUpdate(new Date());
		dao.saveSeed(seed);
		
		return list;
	}
	
	public String updateSeed(TaskSeed seed){
		TaskDao dao = new TaskDao();
		UserService usrSrvc = UserServiceFactory.getUserService();
		User u=usrSrvc.getCurrentUser();
		seed.setMominId(u.getUserId());
		
		dao.saveSeed(seed);
		List <Task> list = TaskGenerator.generate(seed);
		dao.saveTask(list.toArray(),null);
		seed.setUpdate(new Date());
		dao.saveSeed(seed);
		return null;
	}

	
	public Momin getCurrentMomin() throws Exception
	{
		TaskDao dao = new TaskDao();
		//naitsfot 105208827974973865566
		//test    18580476422013912411
		//test1  11701531798136846518
		UserService usrSrvc = UserServiceFactory.getUserService();
		User u=usrSrvc.getCurrentUser();
		Momin m ;
		
		if(u==null)
		{
			throw new Exception("out");
		}
		String mominId  = u.getUserId();		
		m=dao.getMomin(u.getUserId());
		
		// utilisateur n'existe pas dans la base
		if(m==null){
			m= new Momin();
			m.setEmail(u.getEmail());
			m.setId(u.getUserId());
			m.setName(u.getNickname());
			dao.save(m);
		}
		m=m.clon();
//		m.setFriendsCalendar(Arrays.asList("105208827974973865566&#0&#naitsoft","110949677754069966012&#0&#sharpensoul","18580476422013912411&#0&#test","11701531798136846518&#0&#test1"));
		return m; 
	}

	public String shareGroup(String email, int group) throws Exception 
	{
		TaskDao dao = new TaskDao();
		Momin dest = dao.getMominByEmail(email);
		if(dest==null)
			//Envoyer invitation
			return null;
		Momin src = getCurrentMomin();
		String newGroup = src.getId()+"&#"+group+"&#"+src.getName()+"-"+group;
		dest.getFriendsCalendar().add(newGroup);
		dest.setFriendsCalendar(new ArrayList<String>(dest.getFriendsCalendar()));
		dao.save(dest);
		return null;
	}



	@Override
	public Long deleteTask(Long idTsk) {

		TaskDao dao = new TaskDao();
		UserService usrSrvc = UserServiceFactory.getUserService();
		User u=usrSrvc.getCurrentUser();
		if(u == null )
//			throw new Exception("out")
			;
		Task tsk = dao.getTaskById(idTsk);
		if( tsk.getMominId().equals(u.getUserId()))
			dao.deleteTask(tsk);
		else
//			throw new Exception("droit")
			;
		return idTsk;
	}

	@Override
	public TaskSeed getSeed(long id) throws Exception {
		TaskDao dao = new TaskDao();
		UserService usrSrvc = UserServiceFactory.getUserService();
		User u=usrSrvc.getCurrentUser();
		if(u == null )
//			throw new Exception("out")
			;
		TaskSeed seed = dao.getSeed(id);
		if( seed.getMominId().equals(u.getUserId())){
			seed.getParam();
			return seed;
		}
		else
//			throw new Exception("droit")
			return seed;		
	}

	public  static  Date getStartWeek(Date date) {
		Date startWeek = new Date();
		date=CalendarUtil.copyDate(date);
		int timeZoneOffset = 0;
		if(timeZoneOffset > 0){
			startWeek = Task.moveToDay(date, -7);
			startWeek.setHours(24-timeZoneOffset);
		}
		else{
			startWeek = Task.moveToDay(date, -1);
			startWeek.setHours(timeZoneOffset);
		}
		
		startWeek.setMinutes(0);
		startWeek.setSeconds(0);
		
		return startWeek;
	}
//	@Override
//	public List<Task> friendCalend(Date start, String momin, String group) throws Exception {
//		
//		TaskDao dao = new TaskDao();
//		if(getCurrentMomin().getFriendsCalendar().contains(momin+"&#"+group))
//			return dao.getFriendTask(momin, Integer.parseInt(group), start);
//		throw new Exception("");
//		
//	}	
}
