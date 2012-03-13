package com.aljamaa.shared;

import java.io.Serializable;

public class TaskException extends Exception implements Serializable {

	public TaskException(String msg)
	{
		super(msg);
	}
	public TaskException()
	{
		super();
	}
}
