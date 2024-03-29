package com.aljamaa.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
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
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.event.logical.shared.OpenEvent;

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
	Task task;
	TaskSeed seed;
	Button btnCreatetask ;
	ListBox groupCB;
	Button deleteBtn;


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
		ValueChangeHandler<Boolean> evalTypChangeHndlr =new ValueChangeHandler<Boolean>() {			
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				if(numericRadioButton.getValue())
					minTextBox.setEnabled(true);
				else
					minTextBox.setEnabled(false);
			}
		};
		numericRadioButton.addValueChangeHandler(evalTypChangeHndlr );
		boolRadioButton.addValueChangeHandler(evalTypChangeHndlr);

		Label label_2 = new Label("Min:");
		horizontalPanel.add(label_2);

		minTextBox = new TextBox();
		horizontalPanel.add(minTextBox);
		minTextBox.setWidth("26px");

		Label label_3 = new Label("Priority");
//		grid.setWidget(2, 0, label_3);

		priorityComboBox = new ListBox();
		
		InlineLabel groupLabel = new InlineLabel("Group");
		grid.setWidget(2, 0, groupLabel);
		
		
		groupCB = new ListBox();
		for(int i=0 ; i<10 ; i++)
			groupCB.addItem(""+(i+1), ""+i);
		grid.setWidget(2, 1, groupCB);
		
//		grid.setWidget(2, 1, priorityComboBox);

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

		disclosurePanel = new DisclosurePanel("Repeat");
		disclosurePanel.addOpenHandler(new OpenHandler<DisclosurePanel>() {
			public void onOpen(OpenEvent<DisclosurePanel> event) {
				initTaskRepeat();
			}
		});
		//		disclosurePanel.setOpen(false);
		mainPanel.add(disclosurePanel);
		disclosurePanel.add(taskRepeat);		
		disclosurePanel.setAnimationEnabled(true);

		HorizontalPanel horizontalPanel_1 = new HorizontalPanel();
		mainPanel.add(horizontalPanel_1);
		mainPanel.setCellHorizontalAlignment(horizontalPanel_1, HasHorizontalAlignment.ALIGN_CENTER);

		btnCreatetask = new Button("SaveTask");
		btnCreatetask.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				createTask();
			}
		});
		
		deleteBtn = new Button("Delete");
		deleteBtn.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				deleteTask();
			}
		});
		horizontalPanel_1.add(deleteBtn);
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
	
	protected void initTaskRepeat() {
		if(task!=null){
			taskService.getSeed(task.getSeedId(), new AsyncCallback<TaskSeed>() {
				public void onSuccess(TaskSeed result) {
					taskRepeat.init(result);
				}
				public void onFailure(Throwable caught) {
					
				}
			});
		}
	}

	public void deleteTask() {
		if(task==null)
			return;		
		btnCreatetask.setEnabled(false);
		deleteBtn.setEnabled(false);
		taskService.deleteTask(task.getId(), new AsyncCallback<Long>() {
				public void onSuccess(Long id) {		
					weekCalendar().deleteTask(task);
					weekCalendar().initView(true);
					dlg.hide();
				}
				public void onFailure(Throwable caught) {				
					btnCreatetask.setEnabled(true);
					deleteBtn.setEnabled(true);
				}
		});
	}

	public void init(){
		btnCreatetask.setEnabled(true);
		if(task!=null)
		{
			nameTextBox.setText(task.getName());
			dateBox.setValue(task.getDate(), false);
			hourCB.setItemSelected(task.getDate().getHours(), true);
			minCB.setItemSelected(task.getDate().getMinutes()/10, true);
			groupCB.setItemSelected(task.getGroup(), true);
			if(task.getMin()<3)
			{
				boolRadioButton.setValue(true);
				minTextBox.setValue("");
				minTextBox.setEnabled(false);
			}else{
				numericRadioButton.setValue(true);
				minTextBox.setValue(task.getMin()+"");
				minTextBox.setEnabled(true);
			}	
			deleteBtn.setEnabled(true);
		}
		else{
			nameTextBox.setText("");
			dateBox.setValue(new Date(), false);
			hourCB.setItemSelected(0, true);
			minCB.setItemSelected(0, true);
			boolRadioButton.setValue(true);
			minTextBox.setValue("");
			minTextBox.setEnabled(false);
			groupCB.setItemSelected(0, true);
			deleteBtn.setEnabled(false);
		}
	}

	public void createTask()
	{
		if(task == null)
			task = new Task();
		task.setName(nameTextBox.getValue());
		if(numericRadioButton.getValue())
			task.setMin(Integer.parseInt(minTextBox.getValue()));
		else
			task.setMin(-1);
		
		task.setGroup(groupCB.getSelectedIndex());
		task.setPriority(priorityComboBox.getSelectedIndex());
		Date d = CalendarUtil.copyDate(dateBox.getValue());
		d.setTime(Date.UTC(d.getYear(), d.getMonth(), d.getDate(), hourCB.getSelectedIndex(), minCB.getSelectedIndex()*10, 0));
		task.setDate(d);
		if(disclosurePanel.isOpen()){
			TaskSeed seed = taskRepeat.getTaskSeed();
			seed.setTaskAttributes(task);
			taskService.createSeed(seed, new AsyncCallback<List<Task>>() {
				public void onSuccess(List<Task> tsks) {
					weekCalendar().addTasks(tsks);
					weekCalendar().initView(true);					
				}
				public void onFailure(Throwable caught) {				}
			});
		}
		//diclosure panel closed
		else{
			saveTask();
		}
		//weekCalendar().initData();
	}
	public void saveTask() {
		taskService.createTask(task, new AsyncCallback<Task>() {			
			public void onSuccess(Task tsk) {
				dlg.hide();
				btnCreatetask.setEnabled(true);
				weekCalendar().addTask(tsk);
				weekCalendar().initView(true);
			}			
			public void onFailure(Throwable caught) {			}
		});		
	}

	public void updateSeedDlg(){
		final DialogBox dlg =new DialogBox();
		HorizontalPanel hp = new HorizontalPanel();
		final Button oneBt = new Button("just this task");
		Button afterBt = new Button("all tasks");
		Button cancelBt = new Button("Cancel");
		hp.add(oneBt);		hp.add(afterBt);			hp.add(cancelBt);
		
		dlg.add(hp);
		dlg.center();
		dlg.show();
		
		oneBt.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				oneBt.setEnabled(false);
				saveTask();
				dlg.hide();
			}
		});
		afterBt.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
//				oneBt.setEnabled(false);
				updateSeed();
				dlg.hide();
			}
		});
		cancelBt.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				dlg.hide();				
			}
		});		
	
	}
	
	public void updateSeed(){
		
		TaskSeed seed = taskRepeat.getTaskSeed();
		seed.setTaskAttributes(task);
		seed.setLast(new Date());
//		d.setTime(Date.UTC(d.getYear(), d.getMonth(), d.getDate(), hourCB.getSelectedIndex(), minCB.getSelectedIndex()*10, 0));
		taskService.updateSeed(seed, new AsyncCallback<String>() {
			public void onSuccess(String result) {
				
			}
			public void onFailure(Throwable caught) {
				
			}
		});
	}
	

	public WeekCalendar weekCalendar() {
		return WeekCalendar.weekCalendar;
	}

	public void setDlg(DialogBox dlg) {
		this.dlg=dlg;

	}

}
