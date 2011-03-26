package com.aljamaa.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import java.util.logging.Logger;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.math.RandomUtils;

import com.aljamaa.client.TaskService;
import com.aljamaa.dao.TaskDao;
import com.aljamaa.entity.Momin;
import com.aljamaa.entity.Statistic;
import com.aljamaa.entity.Task;
import com.aljamaa.entity.TaskSeed;
import com.aljamaa.shared.TaskException;

import com.google.appengine.api.labs.taskqueue.Queue;
import com.google.appengine.api.labs.taskqueue.QueueFactory;
import com.google.appengine.api.labs.taskqueue.TaskOptions;
import com.google.appengine.api.mail.MailService;
import com.google.appengine.api.mail.MailService.Message;
import com.google.appengine.api.mail.MailServiceFactory;
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
	public List<Task> getWeekTasks(Date startWeek, String user, String group) throws TaskException  {
		UserService usrSrvc = UserServiceFactory.getUserService();
		User u=usrSrvc.getCurrentUser();
		if(u==null)
			throw new TaskException("out");
 		TaskDao tdao= new TaskDao();
 		if(user==null)
 			user=u.getUserId(); 		
 		return tdao.getWeekTasks(user,group, startWeek);
		
	}

	@Override
	public Task[] save(Task[] tasks) throws IllegalArgumentException, TaskException {
		UserService usrSrvc = UserServiceFactory.getUserService();
		User u=usrSrvc.getCurrentUser();
		TaskDao tdao= new TaskDao();
		tdao.saveTask(tasks,u.getUserId());
		
//		Queue que = QueueFactory.getDefaultQueue();
//		que.add(TaskOptions.Builder.url("/task/tskseed?s="));
		
		return tasks;		
	}

	@Override
	public  List <Task> createSeed(TaskSeed seed) throws IllegalArgumentException, TaskException {
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
	
	public String updateSeed(TaskSeed seed) throws TaskException{
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

	


	public String shareGroup(String email, int group) throws TaskException 
	{
		TaskDao dao = new TaskDao();
//		Momin dest = dao.getMominByEmail(email);
//		if(dest==null){
			//Envoyer invitation
			Momin src = getCurrentMomin();
//			RandomStringUtils r = new RandomStringUtils();
//			String randomString  = r.randomAscii(15)+new Date().getTime();
			String randomString  = src.getId()+new Date().getTime();
			String newGroup = src.getId()+"&#"+group+"&#"+src.getName()+"-"+group;
			Momin dest = new Momin();
			dest.setId("&$"+randomString);
			dest.setFriendsCalendar(new ArrayList<String>());
			dest.getFriendsCalendar().add(newGroup);
			dest.setEmail(email);
			dao.save(dest);
			MailService mailService = MailServiceFactory.getMailService();
			Message message = new Message("naitsoft@gmail.com", email, "the subject", "");
			message.setHtmlBody( "the body of the message" +
					"<br> <a href='https://yawmlayla.appspot.com/task/share?k="+randomString+"'>https://yawmlayla.appspot.com/task/share?k="+randomString+"</a>");
			try {
				mailService.send(message );
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
//		}
//		Momin src = getCurrentMomin();
//		String newGroup = src.getId()+"&#"+group+"&#"+src.getName()+"-"+group;
//		dest.getFriendsCalendar().add(newGroup);
//		dest.setFriendsCalendar(new ArrayList<String>(dest.getFriendsCalendar()));
//		dao.save(dest);
//		return null;
	}



	@Override
	public Long deleteTask(Long idTsk) throws TaskException {

		TaskDao dao = new TaskDao();
		UserService usrSrvc = UserServiceFactory.getUserService();
		User u=usrSrvc.getCurrentUser();
		if(u == null )
			throw new TaskException("out");
		Task tsk = dao.getTaskById(idTsk);
		if( tsk.getMominId().equals(u.getUserId()))
			dao.deleteTask(tsk);
		else
			throw new TaskException("droit")	;
		return idTsk;
	}

	@Override
	public TaskSeed getSeed(long id) throws TaskException {
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
	
	@Override
	public List<Statistic> getStatistics(Date start, Date end, List<String> names) throws TaskException  {

		Calendar cal = Calendar.getInstance();
		cal.setTime(end);
		int endWeek = cal.get(Calendar.WEEK_OF_YEAR);
		int endYear = cal.get(Calendar.YEAR);
		cal.setTime(start);
		int startWeek = cal.get(Calendar.WEEK_OF_YEAR);
		int startYear = cal.get(Calendar.YEAR);
		int nbrWeek  ;
		if(startYear==endYear){
			nbrWeek = endWeek-startWeek+1;
		}else{
			nbrWeek = cal.getMaximum(Calendar.WEEK_OF_YEAR)-startWeek+endWeek+1;
		}
		
		UserService usrSrvc = UserServiceFactory.getUserService();
		User u=usrSrvc.getCurrentUser();
		TaskDao dao = new TaskDao();
		return dao.getStatisitc(names.get(0), u.getUserId(), start, nbrWeek);
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

	@Override
	public Momin getCurrentMomin() throws TaskException {
		return TaskDao.getCurrentMomin();
	}

}
