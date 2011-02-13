package com.aljamaa.client;

import java.util.Date;
import java.util.List;
import java.util.TreeSet;

import com.aljamaa.entity.Momin;
import com.aljamaa.entity.Task;
import com.aljamaa.entity.TaskSeed;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("task")
public interface TaskService extends RemoteService {

	com.aljamaa.entity.Task createTask(com.aljamaa.entity.Task task)	;
	
	com.aljamaa.entity.Task[]  save (com.aljamaa.entity.Task[] tasks);
	
	List<com.aljamaa.entity.Task> createSeed(TaskSeed seed) throws IllegalArgumentException;
	
	TaskSeed getSeed(long id)throws Exception;
	String updateSeed(TaskSeed seed)throws Exception;

	List<com.aljamaa.entity.Task> getWeekTasks(Date startWeek, String user, String group) throws IllegalArgumentException, Exception;
	
	Momin getCurrentMomin() throws Exception;
	
	String shareGroup(String email, int group) throws Exception;
	
	Long deleteTask(Long id) ;
}
