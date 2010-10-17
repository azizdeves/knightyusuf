package com.aljamaa.client;

import com.aljamaa.entity.Task;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ClickEvent;

public class NewTaskUI extends Composite {

	private final TaskServiceAsync taskService = GWT
	.create(TaskService.class);
	TextBox nameTextBox;
	RadioButton boolRadioButton;
	RadioButton numericRadioButton;
	TextBox minTextBox;
	ListBox priorityComboBox ;
	DateBox dateBox;
	TaskRepeat taskRepeat;		
	DisclosurePanel disclosurePanel ;
	
	public NewTaskUI() {
		
		Grid grid = new Grid(6, 2);
		initWidget(grid);
		
		Label label = new Label("Name");
		grid.setWidget(0, 0, label);
		
		nameTextBox = new TextBox();
		grid.setWidget(0, 1, nameTextBox);
		
		Label label_1 = new Label("Type Eval");
		grid.setWidget(1, 0, label_1);
		
		HorizontalPanel horizontalPanel = new HorizontalPanel();
		horizontalPanel.setSpacing(10);
		grid.setWidget(1, 1, horizontalPanel);
		
		boolRadioButton = new RadioButton("new name", "boolean");
		horizontalPanel.add(boolRadioButton);
		boolRadioButton.setWidth("120");
		
		numericRadioButton = new RadioButton("new name", "numeric");
		horizontalPanel.add(numericRadioButton);
		numericRadioButton.setWidth("120");
		
		Label label_2 = new Label("Min:");
		horizontalPanel.add(label_2);
		
		minTextBox = new TextBox();
		horizontalPanel.add(minTextBox);
		minTextBox.setWidth("26px");
		
		Label label_3 = new Label("Priority");
		grid.setWidget(2, 0, label_3);
		
		priorityComboBox = new ListBox();
		grid.setWidget(2, 1, priorityComboBox);
		
		Label label_4 = new Label("Date");
		grid.setWidget(3, 0, label_4);
		
		dateBox = new DateBox();
		grid.setWidget(3, 1, dateBox);
		
		taskRepeat = new TaskRepeat();
		
//		DialogBox dialog = new DialogBox(false,false);
//		dialog.add(taskRepeat);
//		dialog.show();
		
		disclosurePanel = new DisclosurePanel("New panel");
		disclosurePanel.add(taskRepeat);		
		grid.setWidget(4, 1, disclosurePanel);
		
		PushButton pushButton = new PushButton("Create Task");
		pushButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				createTask();
			}
		});
		grid.setWidget(5, 1, pushButton);
		pushButton.setWidth("100");
		grid.getCellFormatter().setWidth(5, 1, "");
	}
	
	public void createTask()
	{
		Task task = new Task();
		task.setName(nameTextBox.getValue());
		task.setMin(Integer.parseInt(minTextBox.getValue()));
		task.setPriority(priorityComboBox.getSelectedIndex());
		task.setDate(dateBox.getValue());
		taskService.createTask(task, new AsyncCallback<String>() {
			
			@Override
			public void onSuccess(String result) {
				nameTextBox.setValue(result);
				
			}
			
			@Override
			public void onFailure(Throwable caught) {
				
			}
		});
		
	}
	
	public void createSeed(){
		
	}

}
