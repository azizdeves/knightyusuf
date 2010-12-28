package com.aljamaa.client;

import java.util.Date;
import java.util.List;

import com.aljamaa.entity.TaskSeed;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("task")
public interface TaskService extends RemoteService {

	String createTask(com.aljamaa.entity.Task task);

	String createTaskSeed(TaskSeed taskSeed); 
	
	List<com.aljamaa.entity.Task> getWeekTasks(Date startWeek);
}