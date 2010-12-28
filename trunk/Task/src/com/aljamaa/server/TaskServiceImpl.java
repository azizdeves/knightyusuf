package com.aljamaa.server;

import java.util.Date;
import java.util.List;

import com.aljamaa.client.TaskService;
import com.aljamaa.dao.TaskDao;
import com.aljamaa.entity.Task;
import com.aljamaa.entity.TaskSeed;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class TaskServiceImpl extends RemoteServiceServlet implements
		TaskService {

	public String createTask(Task task) throws IllegalArgumentException {

		TaskDao tdao= new TaskDao();
		tdao.saveTask(task);
		return "dao";
	}
	
	public String createTaskSeed(TaskSeed taskSeed) throws IllegalArgumentException  {
		return "gooood";
	}

	@Override
	public List<Task> getWeekTasks(Date startWeek) {
		
 		TaskDao tdao= new TaskDao();
		return tdao.getWeekTasks("momin", startWeek);
		
	}
	
	
}
