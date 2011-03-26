package com.aljamaa.server;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.aljamaa.dao.EMF;
import com.aljamaa.dao.TaskDao;
import com.aljamaa.entity.Statistic;
import com.aljamaa.entity.Task;
import com.aljamaa.entity.TaskSeed;
import com.aljamaa.shared.TaskException;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

public class TaskSeedServlet extends HttpServlet {


	private static final Logger log = Logger.getLogger(TaskSeedServlet.class.getName());
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
	throws ServletException, IOException {
		
		if(req.getParameter("s")!=null)
		{
			String momin = req.getParameter("m");
			String name = req.getParameter("n");
			long time = Long.parseLong(req.getParameter("t"));
			Date week = new Date(Long.parseLong((req.getParameter("w"))));
			stat(name, momin, week,time);
			return;
		}
		if(req.getParameter("u")!=null)
		{
			String id = UserServiceFactory.getUserService().getCurrentUser().getUserId();
			EntityManager em =EMF.get().createEntityManager();
			Query query = em.createQuery("select from "+Task.class.getName());
			List<Task> list = query.getResultList();
			for(Task t : list){
				t.setMominId(id);
				em.persist(t);
			}
			em.close();
		}

		if(req.getParameter("k")!=null){
			TaskDao dao = new TaskDao();
		       Map<String, String> openIdProviders;
		   
		        openIdProviders = new HashMap<String, String>();
		        openIdProviders.put("Google", "google.com/accounts/o8/id");
		        openIdProviders.put("Yahoo", "yahoo.com");
		        openIdProviders.put("MySpace", "myspace.com");
		        openIdProviders.put("AOL", "aol.com");
		        openIdProviders.put("MyOpenId.com", "myopenid.com");
//		        Set<String> attributes = new HashSet<String>();
			UserService usrSrvc = UserServiceFactory.getUserService();
			User u=usrSrvc.getCurrentUser();
			  for (String providerName : openIdProviders.keySet()) {
	                String providerUrl = openIdProviders.get(providerName);
	                String loginUrl = usrSrvc.createLoginURL(req.getRequestURI(), null, providerUrl, null);
	                log.info(providerName+" =  "+loginUrl+"  logout: "+usrSrvc.createLogoutURL(""));
	            }			
			return;
		}
		TaskDao dao = new TaskDao();
		List<TaskSeed> lst = dao.getSeedToUpdate();
		if(lst.size()==0) return;

		TaskSeed seed = lst.get(0);
		long avance=0;
		switch(seed.getType()){
			case Task.EVERY_DAY:  avance = 10;break;
			case Task.EVERY_WEEK : avance = 14;break;
			case Task.EVERY_MONTH : avance = 60;break;
			case Task.EVERY_YEAR : avance = 60;break;
		}	
		
		if( seed.getLast().getTime() - new Date().getTime() < avance * (long)86400000 ){
			List <Task> list = TaskGenerator.generate(seed);
			try {
				dao.saveTask(list.toArray(),null);
			} catch (TaskException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 log.info("new Tasks generated ");
		}
		seed.setUpdate(new Date());
		log.info("saved: "+seed);
		dao.saveSeed(seed);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		if(req.getParameter("s")!=null)
		{
			String momin = req.getParameter("m");
			String name = req.getParameter("n");
			long time = Long.parseLong(req.getParameter("t"));
			Date week = new Date(Long.parseLong((req.getParameter("w"))));
			stat(name, momin, week,time);
			return;
		}
	}

	public static void stat(String name, String momin, Date week, long time){
		TaskDao dao = new TaskDao();
		Statistic stat=null;
		{
			List<Statistic> stats = dao.getStatisitc(name, momin , week,1);
			if(stats.size()>0)
				stat = (Statistic)stats.iterator().next(); 
		}
		if(stat != null)
			if(stat.getUpdate() > time)
				return;
//			week = TaskServiceImpl.getStartWeek(week);
			List<Task> tsks = dao.getWeekTasksStat(name, momin, week);
			stat = StatisticTools.getStatistic( tsks);
			dao.saveStat(stat);
			//stat.init();
			
//		dao.getAllTasks().asQueryResultIterator(FetchOptions.Builder.withLimit(3)).getCursor();
	}


}
