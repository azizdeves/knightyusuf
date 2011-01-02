package com.aljamaa.client;

import java.util.Date;
import java.util.List;
import java.util.TreeSet;

import com.aljamaa.entity.TaskSeed;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface TaskServiceAsync {
	void createTask(com.aljamaa.entity.Task task, AsyncCallback<String> callback)
			throws IllegalArgumentException;
	void createTaskSeed(TaskSeed taskSeed, AsyncCallback<String> callback)
	throws IllegalArgumentException;
	void getWeekTasks(Date startWeek, AsyncCallback<List<com.aljamaa.entity.Task>> callback)throws IllegalArgumentException;
	void save(com.aljamaa.entity.Task[] objects, AsyncCallback<String> asyncCallback)throws IllegalArgumentException;

}
