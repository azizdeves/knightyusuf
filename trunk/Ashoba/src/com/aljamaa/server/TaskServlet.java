package com.aljamaa.server;

import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
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
import javax.servlet.http.HttpSession;

import com.aljamaa.dao.TaskDao;
import com.aljamaa.entity.Momin;
import com.aljamaa.shared.TaskException;

public class TaskServlet extends HttpServlet {


	private static final Logger log = Logger.getLogger(TaskServlet.class.getName());
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
	throws ServletException, IOException {
//		if(req.getParameter("mtn")!=null)
//		{
//			TaskDao dao= new TaskDao();
//			dao.getWeekTasks(momin, group, startWeek)
//		}
		String key = req.getParameter("k");	
		if(key == null ) return ;
		TaskDao dao = new TaskDao();
		Momin dest = dao.getMomin("&$"+key);
		if(dest == null ) return ;
		Momin mmn;
		try {
			mmn = (Momin) req.getSession().getAttribute("mmn");
			if(mmn == null)
				throw new TaskException("out");
			
		} catch (Exception e) {		
			req.getRequestURL();
			req.getRequestURI();
			String s = URLEncoder.encode(req.getRequestURL()+"?"+req.getQueryString());
			resp.sendRedirect("https://yawmlayla.appspot.com/login.jsp?go="+s);
			e.printStackTrace();	return;	}
		mmn.setFriendsCalendar(new ArrayList<String>(mmn.getFriendsCalendar()));
		mmn.getFriendsCalendar().add(dest.getFriendsCalendar().get(0));
		
		dao.save(mmn);
		dao.delete(dest);
		//req.getLocalAddr()
		resp.sendRedirect("https://yawmlayla.appspot.com");
//	resp.encodeRedirectURL("https://yawmlayla.appspot.com");
		
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		

	}



}
