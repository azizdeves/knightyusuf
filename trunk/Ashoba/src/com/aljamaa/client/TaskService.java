package com.aljamaa.client;

import java.util.Date;
import java.util.List;
import java.util.TreeSet;

import com.aljamaa.entity.Momin;
import com.aljamaa.entity.Statistic;
import com.aljamaa.entity.Task;
import com.aljamaa.entity.TaskSeed;
import com.aljamaa.shared.TaskException;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("task")
public interface TaskService extends RemoteService {

	com.aljamaa.entity.Task createTask(com.aljamaa.entity.Task task) throws TaskException	;
	
	com.aljamaa.entity.Task[]  save (com.aljamaa.entity.Task[] tasks) throws TaskException;
	
	List<com.aljamaa.entity.Task> createSeed(TaskSeed seed) throws IllegalArgumentException, TaskException;
	
	TaskSeed getSeed(long id)throws TaskException;
	String updateSeed(TaskSeed seed)throws TaskException;

	List<com.aljamaa.entity.Task> getWeekTasks(Date startWeek, String user, String group) throws IllegalArgumentException, TaskException;
	
	Momin getCurrentMomin() throws TaskException;
	
	String shareGroup(String email, int group) throws TaskException;
	
	Long deleteTask(Long id) throws TaskException ;
	
	List<Statistic> getStatistics(Date start, Date end, List<String> names)throws TaskException ;
}
