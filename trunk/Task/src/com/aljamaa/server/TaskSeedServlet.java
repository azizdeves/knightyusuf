package com.aljamaa.server;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.aljamaa.dao.TaskDao;
import com.aljamaa.entity.Task;
import com.aljamaa.entity.TaskSeed;

public class TaskSeedServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
	throws ServletException, IOException {

		TaskDao dao = new TaskDao();
		List<TaskSeed> lst = dao.getSeedToUpdate();
		if(lst.size()==0) return;

		TaskSeed seed = lst.get(0);
		long avance=0;
		switch(seed.getType()){
			case Task.EVERY_DAY:  avance = 3;break;
			case Task.EVERY_WEEK : avance = 7;break;
			case Task.EVERY_MONTH : avance = 30;break;
			case Task.EVERY_YEAR : avance = 60;break;
		}	
		
		if(new Date().getTime()-seed.getUpdate().getTime() >avance * (long)86400000){
			List <Task> list = TaskGenerator.generate(seed);
			dao.saveTask(list.toArray());
		}
		seed.setUpdate(new Date());
		dao.saveSeed(seed);


	}


}
