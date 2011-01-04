package com.aljamaa.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.TimeZone;

import com.aljamaa.entity.Task;
import com.aljamaa.entity.TaskSeed;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.datepicker.client.CalendarUtil;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Button;

public class NewTaskUI extends Composite {

	private final TaskServiceAsync taskService = GWT.create(TaskService.class);
	SuggestBox nameTextBox;
	RadioButton boolRadioButton;
	RadioButton numericRadioButton;
	TextBox minTextBox;
	ListBox priorityComboBox ;
	DateBox dateBox;
	TaskRepeat taskRepeat;		
	DisclosurePanel disclosurePanel ;
	DialogBox dlg;
	ListBox hourCB;
	ListBox minCB;


	public NewTaskUI(final DialogBox dlg) {
		setDlg(dlg);
		//		TimeZone.setDefault(TimeZone.getTimeZone("GMT"));
		VerticalPanel mainPanel = new VerticalPanel();
		mainPanel.setSize("417px", "250px");
		initWidget(mainPanel);
		Grid grid = new Grid(4, 2);
		mainPanel.add(grid);
		grid.setWidth("427px");

		Label label = new Label("Name");
		grid.setWidget(0, 0, label);

		MultiWordSuggestOracle oracle = new MultiWordSuggestOracle();
		oracle.addAll(Arrays.asList("fajr","dohr","3asr","maghrib","3ichaa","istighfar","salat 3ala rassoul sws","kalima tayiba","witr nabawi","coran","do3a2 rabita"));
		nameTextBox = new SuggestBox(oracle);
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

		HorizontalPanel datePan =  new  HorizontalPanel();
		dateBox = new DateBox();
		datePan.add(dateBox);
		grid.setWidget(3, 1, datePan);

		hourCB = new ListBox();
		for(int i=0;i<24;i++)
			hourCB.addItem(""+i);

		datePan.add(hourCB);

		minCB = new ListBox();
		minCB.addItem("00");
		minCB.addItem("10");
		minCB.addItem("20");
		minCB.addItem("30");
		minCB.addItem("40");
		minCB.addItem("50");
		datePan.add(minCB);

		taskRepeat = new TaskRepeat();

		//		DialogBox dialog = new DialogBox(false,false);
		//		dialog.add(taskRepeat);
		//		dialog.show();

		disclosurePanel = new DisclosurePanel("Repeat");
		//		disclosurePanel.setOpen(false);
		mainPanel.add(disclosurePanel);
		disclosurePanel.add(taskRepeat);		
		disclosurePanel.setAnimationEnabled(true);

		HorizontalPanel horizontalPanel_1 = new HorizontalPanel();
		mainPanel.add(horizontalPanel_1);
		mainPanel.setCellHorizontalAlignment(horizontalPanel_1, HasHorizontalAlignment.ALIGN_CENTER);

		Button btnCreatetask = new Button("CreateTask");
		btnCreatetask.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				createTask();
			}
		});
		horizontalPanel_1.add(btnCreatetask);

		Button btnCancel = new Button("Cancel");
		btnCancel.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				dlg.hide();
			}

		});
		btnCancel.setText("Cancel");
		horizontalPanel_1.add(btnCancel);
	}

	public void createTask()
	{
		Task task = new Task();
		task.setName(nameTextBox.getValue());
		if(numericRadioButton.getValue())
			task.setMin(Integer.parseInt(minTextBox.getValue()));
		else
			task.setMin(-1);
		
		task.setPriority(priorityComboBox.getSelectedIndex());
		Date d = CalendarUtil.copyDate(dateBox.getValue());
		d.setTime(Date.UTC(d.getYear(), d.getMonth(), d.getDate(), hourCB.getSelectedIndex(), minCB.getSelectedIndex()*10, 0));
		//		d.setHours(hourCB.getSelectedIndex());
		//		d.setMinutes(minCB.getSelectedIndex()*10);
		task.setDate(d);
		task.setMominId("mominid");
		
		if(disclosurePanel.isOpen()){
			TaskSeed seed = taskRepeat.getTaskSeed();
			seed.setTaskAttributes(task);
			taskService.createSeed(seed, new AsyncCallback<String>() {
				@Override
				public void onSuccess(String result) {
					
				}
				@Override
				public void onFailure(Throwable caught) {
					
				}

			});

		}
		//diclosure panel closed
		else{


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
		WeekCalendar.weekCalendar.initData();

	}

	public void setDlg(DialogBox dlg) {
		this.dlg=dlg;

	}

}
