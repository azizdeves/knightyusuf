package com.aljamaa.server;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import java.util.logging.Logger;

import com.aljamaa.client.TaskService;
import com.aljamaa.dao.TaskDao;
import com.aljamaa.entity.Momin;
import com.aljamaa.entity.Task;
import com.aljamaa.entity.TaskSeed;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class TaskServiceImpl extends RemoteServiceServlet implements
		TaskService {

	private static final Logger log = Logger.getLogger(TaskServiceImpl.class.getName());
	public String createTask(Task task) throws IllegalArgumentException {

		TaskDao tdao= new TaskDao();
		tdao.saveTask(task);
		UserService usrSrvc = UserServiceFactory.getUserService();
		return usrSrvc.getCurrentUser().getEmail() ;
	}
	


	@Override
	public List<Task> getWeekTasks(Date startWeek) throws IllegalArgumentException  {
//		usrSrvc.createLoginURL("destination");
//		UserService usrSrvc = UserServiceFactory.getUserService();
//		User u=usrSrvc.getCurrentUser();
//		log.info("loggin "+usrSrvc.createLoginURL("")+"  logout: "+usrSrvc.createLogoutURL(""));
//		 if(u!=null)
//			 log.info("loggedwwwwww "+u.getEmail());
//		 else log.info("wwwwwwwww user==null");
		 
 		TaskDao tdao= new TaskDao();
		Momin m= new Momin();
		m.setFriendsCalendar(Arrays.asList("moad","yoyo3","yoyo"));
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("cle","valeur");
		m.setProperties(map);
		m.setId("idde5");
		tdao.save(m);
		tdao.get(); 		
 		
 		return tdao.getWeekTasks("momin", startWeek);
		
	}

	@Override
	public String save(Task[] tasks) throws IllegalArgumentException {
		TaskDao tdao= new TaskDao();
		tdao.saveTask(tasks);
		return "";		
	}

	@Override
	public  String createSeed(TaskSeed seed) throws IllegalArgumentException {
		TaskDao dao = new TaskDao();
		dao.saveSeed(seed);
		List <Task> list = TaskGenerator.generate(seed);
		dao.saveTask(list.toArray());
		return null;
	}

	@Override
	public List<Task> friendCalend(Date start, String momin, String group) throws Exception {
		
		TaskDao dao = new TaskDao();
		if(getCurrentMomin().getFriendsCalendar().contains(momin+"&#"+group))
			return dao.getFriendTask(momin, Integer.parseInt(group), start);
		throw new Exception("");
		
	}
	
	public Momin getCurrentMomin()
	{
		TaskDao dao = new TaskDao();
		
		UserService usrSrvc = UserServiceFactory.getUserService();
		User u=usrSrvc.getCurrentUser();
		return dao.getMomin(u.getUserId());

	}

	
}
