package com.aljamaa.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface TaskServiceAsync {
	void createTask(com.aljamaa.entity.Task task, AsyncCallback<String> callback)
			throws IllegalArgumentException;
}
