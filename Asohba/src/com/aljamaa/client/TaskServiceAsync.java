package com.aljamaa.client;

import java.util.Date;
import java.util.List;
import com.aljamaa.entity.Momin;
import com.aljamaa.entity.Statistic;
import com.aljamaa.entity.Task;
import com.aljamaa.entity.TaskSeed;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface TaskServiceAsync {
	void createTask(com.aljamaa.entity.Task task, AsyncCallback<com.aljamaa.entity.Task> callback)
			throws IllegalArgumentException;
	void getWeekTasks(Date startWeek, String user, String group, AsyncCallback<List<com.aljamaa.entity.Task>> callback)throws IllegalArgumentException;
	void save(com.aljamaa.entity.Task[] objects, AsyncCallback<com.aljamaa.entity.Task[]> asyncCallback)throws IllegalArgumentException;
	void createSeed(TaskSeed seed, AsyncCallback<List<com.aljamaa.entity.Task>> callback);
	void getCurrentMomin(AsyncCallback<Momin> callback);
	void shareGroup(String email, int group, AsyncCallback<String> callback);
	void deleteTask(Task long1, char action,  AsyncCallback<Long> callback);
	void getSeed(long id, AsyncCallback<TaskSeed> callback);
	void updateSeed(TaskSeed seed, char action, AsyncCallback<String> callback);

	void getStatistics(Date start, Date end, List<String> names,
			AsyncCallback<List<Statistic>> callback);
	void getDayTasks(Date day, String string, String string2,
			AsyncCallback<List<Task>> asyncCallback);

}
