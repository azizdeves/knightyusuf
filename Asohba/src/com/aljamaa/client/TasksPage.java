package com.aljamaa.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TreeSet;

import com.aljamaa.entity.Momin;
import com.aljamaa.entity.Task;
import com.aljamaa.shared.TaskException;
import com.aljamaa.shared.TaskMessages;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.Event.NativePreviewHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.datepicker.client.CalendarUtil;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.user.datepicker.client.DatePicker;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventPreview;
import com.google.gwt.user.client.Window.Location;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Grid;
import com.gwtmobile.ui.client.page.Page;
import com.gwtmobile.ui.client.widgets.ListPanel;

public class TasksPage extends Page implements NativePreviewHandler {

	static TreeSet<Task> modifiedTasks;
	private final TaskServiceAsync taskService = GWT.create(TaskService.class);
	private final TaskMessages taskMessages = GWT.create(TaskMessages.class);
	VerticalPanel mainVerticalPanel;
	ListPanel listPanel;
	ArrayList<Task> tasks;
	ArrayList<TaskCell> taskCells ;
	Date startWeek;
	Date endWeek;
	Label[] headerDays;
	NewTaskUI newTskUi;
	static DialogBox dlg;
	Label stateLabel;
	static TasksPage weekCalendar;
	static int timeZoneOffset = new Date().getTimezoneOffset()/60;
	static Momin momin;
	ListBox groupListLb;
	ListPanel mainGrid;
	DateBox weekPicker ;
	boolean readOnly = false;
	final MainBar bar = MainBar.get();
	private Button addTaskBtn;
	private Button saveBtn;
	private Button shareButton;
	private Label lblNewLabel;

	public TasksPage() {
		listPanel = new ListPanel();
		initWidget(listPanel);
		Cookies.removeCookie("mid");
		taskService.getCurrentMomin(new AsyncCallback<Momin>() {
			public void onSuccess(Momin m) {
				momin = m;
				bar.momin = m;
				Cookies.setCookie("mid", m.getId());
				init();
//				initGroup();
				initData();
			}
			public void onFailure(Throwable caught) {
				handleException(caught);
			}
		});


		//		if(momin!=null)
//		init();
//					initData();
	}

	public void init() {
		Event.addNativePreviewHandler(this);
		//		momin = new Momin();
		//		momin.setId("110949677754069966012");
		weekCalendar = this;
		modifiedTasks = new TreeSet<Task>();
		setStartWeek(new Date());

		taskCells = new ArrayList<TaskCell>();
		headerDays = new Label[7];


	
	}

	private void setStartWeek(Date date) {
		date=CalendarUtil.copyDate(date);
		if(timeZoneOffset > 0){
			startWeek = Task.moveToDay(date, -7);
			startWeek.setHours(24-timeZoneOffset);
		}
		else{
			startWeek = Task.moveToDay(date, -1);
			startWeek.setHours(timeZoneOffset);
		}

		startWeek.setMinutes(0);
		startWeek.setSeconds(0);

		endWeek =CalendarUtil.copyDate(startWeek);
		CalendarUtil.addDaysToDate(endWeek, 7);

		//		return null;
	}

	public void initData()
	{
		tasks = new ArrayList<Task>();
		for(int i = 0; i<10; i++)
			tasks.add(new Task("task",10,1,null,new Date()));
		initView(false);
		if(true)
			return;
		
		weekPicker.setValue(startWeek);
		bar.message(taskMessages.loading(), 0);
		String[]  param=groupListLb.getValue(groupListLb.getSelectedIndex()).split("&#");
		boolean isGroupOwned = momin.getId().equals(param[0]);
		if(param.length<2){
			param=new String[]{null,null};
		}
		if(param[0]==null)
			readOnly = false;
		else 
			readOnly = true;

		saveBtn.setEnabled(isGroupOwned);
		addTaskBtn.setEnabled(isGroupOwned);

		taskService.getWeekTasks(startWeek, param[0], param[1], new AsyncCallback<List<Task>>() {			
			@Override
			public void onSuccess(List<Task> tsks) {		
				tasks = (ArrayList<Task>) tsks;
				initView(false);				
			}			
			@Override
			public void onFailure(Throwable caught) {
				handleException(caught);
			}
		});	
	}

	public void initView (boolean sort)
	{
		taskCells.clear();
		int cur,past=0;
		for(Task t : tasks)
		{
			cur = t.getDate().getDay();
			listPanel.add(new TaskCell(t, readOnly));		
		} 
		listPanel.add(new Button("lkl"));
		ArrayList<TaskCell> taskCellsDay;
		DateTimeFormat format =  DateTimeFormat.getFormat("EEE dd  MMM");
		Date d = CalendarUtil.copyDate(startWeek);
		
		if(sort)
			Collections.sort( taskCells);

//		listPanel.clear();
		

	}

	public void shwNwTskDlg() {
		getNwTskUi().init();
		dlg.show();
		dlg.center();
		getNwTskUi().nameTextBox.setFocus(true);
	}
	public NewTaskUI getNwTskUi()
	{
		if(newTskUi==null){
			newTskUi=new NewTaskUI(dlg);
			dlg.setText("Add a new Task");				
			dlg.add(newTskUi);
		}
		return newTskUi;
	}

	public void initGroup()
	{
		if(momin==null || momin.getFriendsCalendar()==null){
			//TODO clear grouplist
			return ;
		}
		for(String g: momin.getFriendsCalendar()){
			String[] s = g.split("&#");
			groupListLb.addItem( s[2], s[0]+"&#"+s[1]);
		}
	}

	public void showShareDlg()
	{
		final DialogBox dlg =new DialogBox();
		Grid grid = new  Grid(3, 2);
		grid.setWidget(0, 0, new Label(taskMessages.email()));
		final TextBox emailTb = new TextBox();
		grid.setWidget(0, 1, emailTb);
		grid.setWidget(1, 0, new Label(taskMessages.group()));
		final ListBox groupCB = new ListBox();
		for(int i=0 ; i<10 ; i++)
			groupCB.addItem(""+(i+1), ""+i);
		grid.setWidget(1, 1, groupCB);
		HTML div = new HTML("");
		//		div.getElement().appendChild(emailTb.getElement());
		HorizontalPanel hp = new HorizontalPanel();
		final Button okBt = new Button(taskMessages.share());
		Button cancelBt = new Button(taskMessages.cancel());
		hp.add(okBt);
		hp.add(cancelBt);
		grid.setWidget(2, 1, hp);
		dlg.add(grid);
		dlg.center();
		dlg.show();

		okBt.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				okBt.setEnabled(false);
				share(emailTb.getText(), groupCB.getSelectedIndex());
				dlg.hide();
			}
		});
		cancelBt.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				dlg.hide();				
			}
		});		
	}

	public void share(String email , int group)
	{
		bar.message(taskMessages.sharing(),0);
		taskService.shareGroup(email, group, new AsyncCallback<String>() {
			public void onSuccess(String result) {				
				bar.message(taskMessages.shared(),1, 5 );
			}
			public void onFailure(Throwable caught) {	
				handleException(caught);
			}
		});
	}

	public void addTasks(List<Task> tsks)
	{
		for(Task t : tsks){
			addTask(t);
		}
	}
	public void addTask(Task tsk)
	{
		if(tsk.getDate().getTime() < endWeek.getTime() && tsk.getDate().getTime() >= startWeek.getTime()){
			//			int d=tsk.getDate().getDay();
			deleteTask(tsk);
			tasks.add(tsk);
		}
	}

	public void deleteTask(Task task)
	{
		//		Task t= new Task();
		//		t.setId(id);
		tasks.remove(task);
	}
	//	@Override
	//	public boolean onEventPreview(Event event) {
	////		if (DOM.eventGetType(event) == Event.ONMOUSEDOWN &&
	////		        DOM.isOrHasChild(getElement(), DOM.eventGetTarget(event))) {
	////			if(dragging){
	////			return false;
	////		    }
	//		return true;
	//	}

	@Override
	public void onPreviewNativeEvent(NativePreviewEvent event) {
		//		stateLabel.setText(TaskCell.dragging+"");
//		if(TaskCell.dragging)
//			DOM.eventPreventDefault(Event.as(event.getNativeEvent()));

	}
	public void handleException(Throwable t){
		bar.message(taskMessages.error(), 2, 20);
		try{
			throw t;
		}catch(TaskException e){
			if("out".equals(e.getMessage())){
				if(Location.getParameter("fb")==null)
					bar.login();
				else
					Location.reload();
			}
			if("cross".equals(e.getMessage())){
				if(Location.getParameter("fb")==null)
					bar.login();
				else
					Location.replace("https://yawmlayla.appspot.com/login?code="+Location.getParameter("code"));
			}
		}
		 catch (Throwable e) {				}
	}
}


