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
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventPreview;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Grid;

public class WeekCalendar extends Composite implements NativePreviewHandler {

	static TreeSet<Task> modifiedTasks;
	private final TaskServiceAsync taskService = GWT.create(TaskService.class);
	private final TaskMessages taskMessages = GWT.create(TaskMessages.class);
	VerticalPanel mainVerticalPanel;
	HorizontalPanel horizontalPanel;
	ArrayList<Task> tasks;
	ArrayList<TaskCell> taskCells[] ;
	DayPanel dayVerticalPanel[];
	Date startWeek;
	Date endWeek;
	Label[] headerDays;
	NewTaskUI newTskUi;
	static DialogBox dlg;
	Label stateLabel;
	static WeekCalendar weekCalendar;
	static int timeZoneOffset = new Date().getTimezoneOffset()/60;
	static Momin momin;
	ListBox groupListLb;
	Grid mainGrid;
	DatePicker weekPicker ;
	boolean readOnly = false;
	final MainBar bar = MainBar.get();
	private Button addTaskBtn;
	private Button saveBtn;
	private Button shareButton;
	private Label lblNewLabel;

	public WeekCalendar() {
		mainGrid = new Grid(3, 2);
		initWidget(mainGrid);

		taskService.getCurrentMomin(new AsyncCallback<Momin>() {
			public void onSuccess(Momin m) {
				momin = m;
				init();
				initGroup();
				initData();
			}
			public void onFailure(Throwable caught) {
				try {
					bar.message(taskMessages.error(), 2, 20);
					throw caught;
				} catch (TaskException e) {
					if("out".equals(e.getMessage())){
						bar.login();
					}
				}catch(Throwable e){}
			}
		});


		//		if(momin!=null)
		//			initData();
	}

	public void init() {
		Event.addNativePreviewHandler(this);
		//		momin = new Momin();
		//		momin.setId("110949677754069966012");
		weekCalendar = this;
		modifiedTasks = new TreeSet<Task>();
		setStartWeek(new Date());

		dlg=new DialogBox();
		taskCells = new ArrayList[7];
		headerDays = new Label[7];
		mainVerticalPanel = new VerticalPanel();
		mainVerticalPanel.setWidth("947px");

		HorizontalPanel verticalPanel = new HorizontalPanel();
		mainGrid.setWidget(0, 0, verticalPanel);

		weekPicker = new DatePicker();
		weekPicker.addValueChangeHandler(new ValueChangeHandler<Date>() {			
			public void onValueChange(ValueChangeEvent<Date> event) {
				setStartWeek(weekPicker.getValue());
				initData();
			}
		});
		verticalPanel.add(weekPicker);

		lblNewLabel = new Label(taskMessages.group());
		verticalPanel.add(lblNewLabel);

		groupListLb = new ListBox();
		verticalPanel.add(groupListLb);
		groupListLb.setWidth("142px");
		groupListLb.addItem("my", momin.getId()+"&#");
		//		{
		//			int i=1;
		//			for(String f : momin.getFriendsCalendar())
		//				groupListLb.addItem("friend"+i++, f);
		//		}
		groupListLb.addChangeHandler(new ChangeHandler() {			
			public void onChange(ChangeEvent event) {
				initData();
			}
		});
		groupListLb.setVisibleItemCount(5);
		//		mainVerticalPanel.add(grid);
		mainGrid.setWidget(1, 0, mainVerticalPanel);


		mainGrid.setSize("626px", "413px");

		HorizontalPanel horizontalPanel_1 = new HorizontalPanel();
		horizontalPanel_1.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		mainVerticalPanel.add(horizontalPanel_1);

		Button prevWeekBtn = new Button("Previous Boutton");
		prevWeekBtn.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				CalendarUtil.addDaysToDate(startWeek,-7);
				CalendarUtil.addDaysToDate(endWeek, -7);		
				initData();
			}
		});
		prevWeekBtn.setText("<<");
		horizontalPanel_1.add(prevWeekBtn);

		Button nextWeekBtn = new Button("NextWeek");
		nextWeekBtn.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				CalendarUtil.addDaysToDate(startWeek, 7);		
				CalendarUtil.addDaysToDate(endWeek, 7);		
				initData();
			}
		});
		nextWeekBtn.setText(">>");
		horizontalPanel_1.add(nextWeekBtn);


		addTaskBtn = new Button(taskMessages.addTask());
		addTaskBtn.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				getNwTskUi().task=null;
				shwNwTskDlg();
			}
		});
		horizontalPanel_1.add(addTaskBtn);

		saveBtn = new Button(taskMessages.save());
		saveBtn.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if(readOnly)	return;
				bar.message(taskMessages.saving(), 0);
				int i=0;
				Task [] tsks = new Task[modifiedTasks.size()];
				for(Task t : modifiedTasks)
					tsks[i++] = t;
				taskService.save(tsks, new AsyncCallback<Task[]>() {
					@Override
					public void onSuccess(Task[] result) {
						modifiedTasks.clear();
						bar.message(taskMessages.saved(), 1, 5);

					}
					@Override
					public void onFailure(Throwable caught) {
						try {
							bar.message(taskMessages.error(), 2, 20);
							throw caught;
						} catch (TaskException e) {
							if("out".equals(e.getMessage())){
								bar.login();
							}
						}catch(Throwable e){}
					}
				});
			}
		});

		shareButton = new Button(taskMessages.share());
		shareButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				showShareDlg();
			}
		});
		horizontalPanel_1.add(shareButton);
		horizontalPanel_1.add(saveBtn);

		stateLabel = new Label();
		horizontalPanel_1.add(stateLabel);

		horizontalPanel = new HorizontalPanel(){
			@Override
			public void onBrowserEvent(Event event) {
				super.onBrowserEvent(event);
				switch (DOM.eventGetType(event)) {
				case Event.ONMOUSEDOWN:
					int i = 5;	    	  
					break;
				case Event.ONMOUSEUP:

					break;
				case Event.ONMOUSEMOVE:

					break;
				case Event.ONMOUSEOVER:

					break;
				case Event.ONMOUSEOUT:

					break;
				}
			}
		};
		mainVerticalPanel.add(horizontalPanel);
		mainGrid.getCellFormatter().setVerticalAlignment(1, 1, HasVerticalAlignment.ALIGN_TOP);
		mainGrid.getCellFormatter().setVerticalAlignment(1, 0, HasVerticalAlignment.ALIGN_TOP);

//		StatisticUI statisticUI = new StatisticUI();
//		mainGrid.setWidget(2, 1, statisticUI);

		dayVerticalPanel = new DayPanel[7];
		for(int i = 0 ; i< 7 ; i++){
			taskCells[i] = new ArrayList<TaskCell>();
			dayVerticalPanel[i] = new DayPanel();
			dayVerticalPanel[i].sinkEvents(Event.MOUSEEVENTS);
			horizontalPanel.add(dayVerticalPanel[i]);			
			dayVerticalPanel[i].setSize( "100px", "");
			headerDays[i] = new Label();
			headerDays[i].setStyleName("dateDay");
		}
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
				bar.clear();
			}			
			@Override
			public void onFailure(Throwable caught) {
				try {
					bar.message(taskMessages.error(), 2 , 20);
					throw caught;
				} catch (TaskException e) {
					if("out".equals(e.getMessage())){
						bar.login();
					}
				}catch(Throwable e){}
			}
		});	
	}

	public void initView (boolean sort)
	{
		for(ArrayList<TaskCell> tc : taskCells) tc.clear();
		int cur,past=0;
		for(Task t : tasks)
		{
			cur = t.getDate().getDay();
			//			if(cur>=past){
			taskCells[cur].add(new TaskCell(t, readOnly));		
			//				past=cur;
			//			}
		} 
		ArrayList<TaskCell> taskCellsDay;
		DateTimeFormat format =  DateTimeFormat.getFormat("EEE dd  MMMM");
		Date d = CalendarUtil.copyDate(startWeek);
		
		int i =0, j=7;
//		boolean isRTL=LocaleInfo.getCurrentLocale().isRTL();
//		if(isRTL=false){
//			i=6;j=-1;
//		}
			
		for( ; i<j ;i++  )
		{
			if(sort)
				Collections.sort( taskCells[i]);
			dayVerticalPanel[i].clear();
			headerDays[i].setText(format.format(d));
			dayVerticalPanel[i].add(headerDays[i]);
			taskCellsDay = taskCells[i];
			for(TaskCell tc : taskCellsDay)
			{
				dayVerticalPanel[i].add(tc);
			}
			CalendarUtil.addDaysToDate(d,1);		
//			if(isRTL) i--;
//			else i++;
		}
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
				try{
					throw caught;
				}catch(TaskException e){
					if("out".equals(e.getMessage())){
						bar.login();
					}
				} catch (Throwable e) {				}
				bar.message(taskMessages.error(),2,20);
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
		if(TaskCell.dragging)
			DOM.eventPreventDefault(Event.as(event.getNativeEvent()));

	}
}

class DayPanel extends VerticalPanel{

	@Override
	public void onBrowserEvent(Event event) {
		super.onBrowserEvent(event);
		switch (DOM.eventGetType(event)) {
		case Event.ONMOUSEDOWN:
			int i = 5;	    	  
			break;
		case Event.ONMOUSEUP:

			break;
		case Event.ONMOUSEMOVE:

			break;
		case Event.ONMOUSEOVER:

			break;
		case Event.ONMOUSEOUT:

			break;
		}
	}
	public DayPanel(){
		super();
		//		this.sinkEvents(Event.MOUSEEVENTS);
	}
}

