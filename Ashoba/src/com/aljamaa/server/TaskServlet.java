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

import com.aljamaa.dao.TaskDao;
import com.aljamaa.entity.Momin;

public class TaskServlet extends HttpServlet {


	private static final Logger log = Logger.getLogger(TaskServlet.class.getName());
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
	throws ServletException, IOException {
		String key = req.getParameter("k");	
		if(key == null ) return ;
		TaskDao dao = new TaskDao();
		Momin dest = dao.getMomin("&$"+key);
		if(dest == null ) return ;
		Momin momin;
		try {
			momin = dao.getCurrentMomin();
		} catch (Exception e) {		
			req.getRequestURL();
			req.getRequestURI();
			String s = URLEncoder.encode(req.getRequestURL()+"?"+req.getQueryString());
			resp.sendRedirect("https://yawmlayla.appspot.com/login.jsp?go="+s);
			e.printStackTrace();	return;	}
		momin.setFriendsCalendar(new ArrayList<String>(momin.getFriendsCalendar()));
		momin.getFriendsCalendar().add(dest.getFriendsCalendar().get(0));
		
		dao.save(momin);
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
