package com.aljamaa.client;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

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

public class WeekCalendar extends Composite {

	private final TaskServiceAsync taskService = GWT.create(TaskService.class);
	VerticalPanel mainVerticalPanel;
	HorizontalPanel horizontalPanel;
	ArrayList<TaskCell> taskCells[] ;
	VerticalPanel dayVerticalPanel[];
	Date startWeek;
	Label[] headerDays;
	NewTaskUI newTskUi;
	final DialogBox dlg;

	public WeekCalendar() {

		startWeek = Task.moveToDay(new Date(), 0);
		startWeek.setHours(0);
		
		dlg=new DialogBox();
		taskCells = new ArrayList[7];
		headerDays = new Label[7];
		mainVerticalPanel = new VerticalPanel();
		initWidget(mainVerticalPanel);

		HorizontalPanel horizontalPanel_1 = new HorizontalPanel();
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
//				else
//					newTskUi.setDlg(dlg);
//				dlg.setAnimationEnabled(true);
				dlg.show();
				dlg.center();
			}
		});
		horizontalPanel_1.add(addTaskBtn);

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

//		Task[] tasks = new Task[3];
//		tasks[1]= new Task("task1", 0, 1, 0, new Date());
//		tasks[2]= new Task("tasksd1", 100, 1, 0, new Date(new Long("1287860878096")));
//		//		tasks[3]= new Task("tezeask1", 5, 1, 0, new Date());
//		tasks[0]= new Task("ta55sk1", 0, 1, 0, new Date());

		initData();
		//initView();
		//		TaskCell taskCell = new TaskCell();
		//		verticalPanel_1.add(taskCell);
		//		
		//		TaskCell taskCell_1 = new TaskCell();
		//		verticalPanel_1.add(taskCell_1);

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
					int d = (int) ((t.getDate().getTime() - startWeek.getTime())/86400000);
					if(d>-1 && d<7)
						taskCells[d].add(new TaskCell(t));
					
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
		//format.format(startWeek);
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
