package com.aljamaa.client;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TreeSet;

import com.aljamaa.entity.Task;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.datepicker.client.CalendarUtil;
import com.google.gwt.user.datepicker.client.DatePicker;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.HasVerticalAlignment;

public class WeekCalendar extends Composite {

	static TreeSet<Task> modifiedTasks;
	private final TaskServiceAsync taskService = GWT.create(TaskService.class);
	VerticalPanel mainVerticalPanel;
	HorizontalPanel horizontalPanel;
	ArrayList<TaskCell> taskCells[] ;
	VerticalPanel dayVerticalPanel[];
	Date startWeek;
	Label[] headerDays;
	NewTaskUI newTskUi;
	final DialogBox dlg;
	Label stateLabel;
	static WeekCalendar weekCalendar;

	public WeekCalendar() {

		weekCalendar = this;
		modifiedTasks = new TreeSet<Task>();
		startWeek = Task.moveToDay(new Date(), 0);
		startWeek.setHours(0);
		startWeek.setMinutes(0);
		startWeek.setSeconds(0);
		
		dlg=new DialogBox();
		taskCells = new ArrayList[7];
		headerDays = new Label[7];
		mainVerticalPanel = new VerticalPanel();
		initWidget(mainVerticalPanel);

		HorizontalPanel horizontalPanel_1 = new HorizontalPanel();
		horizontalPanel_1.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		mainVerticalPanel.add(horizontalPanel_1);

		Button prevWeekBtn = new Button("Previous Boutton");
		prevWeekBtn.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				CalendarUtil.addDaysToDate(startWeek,-7);
				initData();
			}
		});
		prevWeekBtn.setText("<<");
		horizontalPanel_1.add(prevWeekBtn);

		Button nextWeekBtn = new Button("NextWeek");
		nextWeekBtn.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				CalendarUtil.addDaysToDate(startWeek, 7);		
				initData();
			}
		});
		nextWeekBtn.setText(">>");
		horizontalPanel_1.add(nextWeekBtn);
		
		
		Button addTaskBtn = new Button("Add Task");
		addTaskBtn.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if(newTskUi==null){
					newTskUi=new NewTaskUI(dlg);
					dlg.setText("Add a new Task");				
					dlg.add(newTskUi);
				}
				dlg.show();
				dlg.center();
			}
		});
		horizontalPanel_1.add(addTaskBtn);
		
		Button saveBtn = new Button("Save");
		saveBtn.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				stateLabel.setText("Saving...");
				int i=0;
				Task [] tsks = new Task[modifiedTasks.size()];
				for(Task t : modifiedTasks)
					tsks[i++] = t;
				taskService.save(tsks, new AsyncCallback<String>() {
					@Override
					public void onSuccess(String result) {
						modifiedTasks.clear();
						stateLabel.setText("");
						
					}
					@Override
					public void onFailure(Throwable caught) {
						stateLabel.setText("Error Saving");
						
					}
				});
			}
		});
		horizontalPanel_1.add(saveBtn);
		
		stateLabel = new Label();
		horizontalPanel_1.add(stateLabel);

		MenuBar menuBar = new MenuBar(false);
		mainVerticalPanel.add(menuBar);

		MenuItem menuItem = new MenuItem("New item", false, (Command) null);
		menuBar.addItem(menuItem);

		horizontalPanel = new HorizontalPanel();
		mainVerticalPanel.add(horizontalPanel);

		dayVerticalPanel = new VerticalPanel[7];
		for(int i = 0 ; i< 7 ; i++){

			taskCells[i] = new ArrayList<TaskCell>();
			dayVerticalPanel[i] = new VerticalPanel();
			horizontalPanel.add(dayVerticalPanel[i]);			
			dayVerticalPanel[i].setSize( "100px", "");
			headerDays[i] = new Label();
		}
		initData();
	}

	public void initData()
	{
		//startWeek = new Date();
		taskService.getWeekTasks(startWeek, new AsyncCallback<List<Task>>() {
			
			@Override
			public void onSuccess(List<Task> tasks) {				
				for(ArrayList<TaskCell> tc : taskCells) tc.clear();
				for(Task t : tasks)
				{
						taskCells[t.getDate().getDay()].add(new TaskCell(t));					
				}
				initView();				
			}
			
			@Override
			public void onFailure(Throwable caught) {
				
			}
		});
	}

	public void initView ()
	{
		ArrayList<TaskCell> taskCellsDay;
		DateTimeFormat format =  DateTimeFormat.getFormat("EEE dd/MM");
		Date d = CalendarUtil.copyDate(startWeek);
		for(int i = 0 ; i<7 ; i++ )
		{
			dayVerticalPanel[i].clear();
			headerDays[i].setText(format.format(d));
			dayVerticalPanel[i].add(headerDays[i]);
			taskCellsDay = taskCells[i];
			for(TaskCell tc : taskCellsDay)
			{
				dayVerticalPanel[i].add(tc);
			}
			CalendarUtil.addDaysToDate(d,1);			
		}
	}
}
