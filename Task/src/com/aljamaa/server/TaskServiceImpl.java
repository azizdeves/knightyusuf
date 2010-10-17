package com.aljamaa.server;

import com.aljamaa.client.TaskService;
import com.aljamaa.entity.Task;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class TaskServiceImpl extends RemoteServiceServlet implements
		TaskService {

	public String createTask(Task task) throws IllegalArgumentException {

		String serverInfo = getServletContext().getServerInfo();
		String userAgent = getThreadLocalRequest().getHeader("User-Agent");
		return task.getName()+"woooooow";
	}
}
