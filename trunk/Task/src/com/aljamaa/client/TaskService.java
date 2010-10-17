package com.aljamaa.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("task")
public interface TaskService extends RemoteService {

	String createTask(com.aljamaa.entity.Task task);
}
