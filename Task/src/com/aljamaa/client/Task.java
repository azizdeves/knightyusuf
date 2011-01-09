package com.aljamaa.client;

import com.aljamaa.shared.FieldVerifier;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Task implements EntryPoint {


	/**
	 * Create a remote service proxy to talk to the server-side Greeting service.
	 */
	private final TaskServiceAsync taskService = GWT.create(TaskService.class);

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		//		final Button sendButton = new Button("Send");
		//		final TextBox nameField = new TextBox();
		//		nameField.setText("GWT User");
		//		final Label errorLabel = new Label();
		//
		//		// We can add style names to widgets
		//		sendButton.addStyleName("sendButton");

		TaskRepeat task=new TaskRepeat();
		//		NewTaskUI newTask = new NewTaskUI(); 
		WeekCalendar week = new WeekCalendar();
		RootPanel.get("main").add(week);
		
		

	}
}
