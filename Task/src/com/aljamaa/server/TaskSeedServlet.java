package com.aljamaa.server;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.aljamaa.entity.Task;
import com.aljamaa.entity.TaskSeed;

public class TaskSeedServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
//		TaskSeed seed = new TaskSeed("namessed", Task.EVERY_DAY, 200, 1, 0, null, new Date(), null, 0);
		TaskSeed seed = new TaskSeed("namessed", Task.EVERY_DAY, 200, 1, 0, new boolean[]{false,true,false,false,true,false,false}, new Date(), null, 0);
		TaskGenerator.everyWeek(seed);
	}
	

}
