package com.aljamaa.client;

import java.util.Date;
import java.util.List;
import java.util.TreeSet;

import com.aljamaa.entity.Task;
import com.aljamaa.entity.TaskSeed;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("task")
public interface TaskService extends RemoteService {

	String createTask(com.aljamaa.entity.Task task);

	
	
	List<com.aljamaa.entity.Task> getWeekTasks(Date startWeek) throws IllegalArgumentException;
	
	String save (com.aljamaa.entity.Task[] tasks);
	
	String createSeed(TaskSeed seed) throws IllegalArgumentException;



	List<Task> friendCalend(Date startWeek, String string, String string2) throws Exception;
}
