package com.aljamaa.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import com.aljamaa.client.TaskServiceAsync;
import com.aljamaa.entity.Task;
import com.aljamaa.entity.TaskSeed;
import com.aljamaa.shared.TaskMessages;
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
	static final char ALL_ACTION = 'l';
	static final char AFTER_ACTION = 'a';
	static final char ONE_ACTION = 'o';
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
	TaskMessages taskMessages;


	public NewTaskUI(final DialogBox dlg) {
		setDlg(dlg);
		taskMessages=GWT.create(TaskMessages.class);
		//		TimeZone.setDefault(TimeZone.getTimeZone("GMT"));
		VerticalPanel mainPanel = new VerticalPanel();
		mainPanel.setSize("417px", "250px");
		initWidget(mainPanel);
		Grid grid = new Grid(4, 2);
		mainPanel.add(grid);
		grid.setWidth("427px");

		Label label = new Label(taskMessages.name());
		grid.setWidget(0, 0, label);

		MultiWordSuggestOracle oracle = new MultiWordSuggestOracle();
		oracle.addAll(Arrays.asList("fajr","dohr","3asr","maghrib","3ichaa","istighfar","salat 3ala rassoul sws","kalima tayiba","witr nabawi","coran","do3a2 rabita"));
		nameTextBox = new SuggestBox(oracle);
		grid.setWidget(0, 1, nameTextBox);

		Label label_1 = new Label(taskMessages.typeEval());
		grid.setWidget(1, 0, label_1);

		HorizontalPanel horizontalPanel = new HorizontalPanel();
		horizontalPanel.setSpacing(10);
		grid.setWidget(1, 1, horizontalPanel);

		boolRadioButton = new RadioButton("new name", taskMessages.bool());
		horizontalPanel.add(boolRadioButton);
		boolRadioButton.setWidth("120");

		numericRadioButton = new RadioButton("new name", taskMessages.numeric());
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

		Label label_2 = new Label(taskMessages.minimum());
		horizontalPanel.add(label_2);

		minTextBox = new TextBox();
		horizontalPanel.add(minTextBox);
		minTextBox.setWidth("26px");

		Label label_3 = new Label("Priority");
//		grid.setWidget(2, 0, label_3);

		priorityComboBox = new ListBox();
		
		InlineLabel groupLabel = new InlineLabel(taskMessages.group());
		grid.setWidget(2, 0, groupLabel);
		
		
		groupCB = new ListBox();
		for(int i=0 ; i<10 ; i++)
			groupCB.addItem(""+(i+1), ""+i);
		grid.setWidget(2, 1, groupCB);
		
//		grid.setWidget(2, 1, priorityComboBox);

		Label label_4 = new Label(taskMessages.date());
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

		disclosurePanel = new DisclosurePanel(taskMessages.repeat());
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

		btnCreatetask = new Button(taskMessages.save());
		btnCreatetask.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if(task != null && task.getSeedId()!=null){
					updateSeedDlg(true);
				}else
					createTask();
				dlg.hide();
			}
			
		});
		
		deleteBtn = new Button(taskMessages.delete());
		deleteBtn.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if(task.getSeedId()==null)
					deleteTask(ONE_ACTION);
				else
					updateSeedDlg(false);
				dlg.hide();
			}
		});
		horizontalPanel_1.add(deleteBtn);
		horizontalPanel_1.add(btnCreatetask);

		Button btnCancel = new Button(taskMessages.cancel());
		btnCancel.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				dlg.hide();
			}
		});
//		btnCancel.setText(taskMessages.cancel());
		horizontalPanel_1.add(btnCancel);
	}
	public boolean verify(){
		if("".equals(nameTextBox.getText())){
			MainBar.get().message(taskMessages.nameTaskEmpty(), 2, 5);
			return false;
		}
		return true;
	}
	/**
	 * Charge le seed apartir de BD et Maj l'UI
	 */
	protected void initTaskRepeat() {
//		loadSeed();
		
		if(task!=null){
			taskService.getSeed(task.getSeedId(), new AsyncCallback<TaskSeed>() {
				public void onSuccess(TaskSeed result) {
					seed = taskRepeat.init(result);
				}
				public void onFailure(Throwable caught) {
					
				}
			});
		}
	}
	
//	public void loadSeed(){
//	
//	}

	public void deleteTask(char action) {
		if(task==null)
			return;		

		MainBar.get().message(taskMessages.deleting(),0);
		btnCreatetask.setEnabled(false);
		deleteBtn.setEnabled(false);
		taskService.deleteTask(task, action, new AsyncCallback<Long>() {
				public void onSuccess(Long id) {		
					MainBar.get().message(taskMessages.deleted(),1,5);
					weekCalendar().deleteTask(task);
					weekCalendar().initView(true);
					dlg.hide();
				}
				public void onFailure(Throwable caught) {				
					MainBar.get().message(taskMessages.error(),2,20);
					btnCreatetask.setEnabled(true);
					deleteBtn.setEnabled(true);
				}
		});
	}

	public void init(){
		seed = null;
		
		btnCreatetask.setEnabled(true);
		disclosurePanel.setOpen(false);
		if(task!=null)
		{
			nameTextBox.setText(task.getName());
			dateBox.setValue(task.getDate(), false);
			hourCB.setItemSelected(task.getDate().getHours(), true);
			minCB.setItemSelected(task.getDate().getMinutes()/10, true);
			groupCB.setItemSelected(task.getGroup(), true);
			if(task.getMin()<1)
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
		nameTextBox.setFocus(true);
	}

	public void createTask()
	{
		
		if(!verify())
			return;
		
		if(task == null)
			task = new Task();
		
		initTaskFromUI();
		
		MainBar.get().message(taskMessages.saving(),0);
		
		if(disclosurePanel.isOpen()){
			TaskSeed seed = taskRepeat.getTaskSeed();
			seed.setTaskAttributes(task);
			taskService.createSeed(seed, new AsyncCallback<List<Task>>() {
				public void onSuccess(List<Task> tsks) {
					dlg.hide();
					weekCalendar().addTasks(tsks);
					weekCalendar().initView(true);				
					MainBar.get().message(taskMessages.saved(),1,5);
				}
				public void onFailure(Throwable caught) {	
					try{
						throw caught;
					}catch(Exception e){
						if("out".equals(e.getMessage())){
							MainBar.get().login();
						}
					} catch (Throwable e) {				}
					MainBar.get().message("error",2,20);
				}
			});
		}
		//diclosure panel closed
		else{
			saveTask();
		}
		//weekCalendar().initData();
	}
	private void initTaskFromUI() {
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
		
	}
	public void saveTask() {
		initTaskFromUI();
		MainBar.get().message(taskMessages.saving(),0);
		taskService.createTask(task, new AsyncCallback<Task>() {			
			public void onSuccess(Task tsk) {
				dlg.hide();
				btnCreatetask.setEnabled(true);
				weekCalendar().addTask(tsk);
				weekCalendar().initView(true);
				MainBar.get().message(taskMessages.saved(),1,5);
			}			
			public void onFailure(Throwable caught) {	
				try{
					throw caught;
				}catch(Exception e){
					if("out".equals(e.getMessage())){
						MainBar.get().login();
					}
				} catch (Throwable e) {				}
				MainBar.get().message("error",2,20);
			}
		});		
	}

	public void updateSeedDlg(final boolean update){
		final DialogBox dlgSeed =new DialogBox();
		HorizontalPanel hp = new HorizontalPanel();
		final Button oneBt = new Button("this task");
		Button afterBt = new Button("this task and after");
		Button allBt = new Button("all tasks");
		Button cancelBt = new Button(taskMessages.cancel());
		hp.add(cancelBt);	hp.add(oneBt);		hp.add(afterBt);	
		if(!update)hp.add(allBt);		
		
		dlgSeed.add(hp);
		dlgSeed.center();
		dlgSeed.show();
		
		oneBt.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				oneBt.setEnabled(false);
				if(update)
					saveTask();
				else
					deleteTask(ONE_ACTION);
				dlgSeed.hide();
			}
		});
		afterBt.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
//				oneBt.setEnabled(false);
				if(update){
					updateSeed(AFTER_ACTION);
				}
				else
					deleteTask(AFTER_ACTION);
					
				dlgSeed.hide();
			}
		});
		allBt.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				oneBt.setEnabled(false);
				if(update){
//					updateSeed(ALL_ACTION);
				}
				else
					deleteTask(ALL_ACTION);
				dlgSeed.hide();
			}
		});
		cancelBt.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				dlgSeed.hide();				
			}
		});		
	
	}
	
	public void updateSeed(final char action){
		
		if(seed == null){
			taskService.getSeed(task.getSeedId(), new AsyncCallback<TaskSeed>() {
				public void onSuccess(TaskSeed result) {
					seed = taskRepeat.init(result);
					updateSeed(action);
					
				}
				public void onFailure(Throwable caught) {
					
				}
			});
			return;
		}
		TaskSeed seed = taskRepeat.getTaskSeed();
		initTaskFromUI();
		seed.setTaskAttributes(task);
		seed.setId(this.seed.getId());
		seed.setLast(task.getDate());
//		d.setTime(Date.UTC(d.getYear(), d.getMonth(), d.getDate(), hourCB.getSelectedIndex(), minCB.getSelectedIndex()*10, 0));
		taskService.updateSeed(seed, action, new AsyncCallback<String>() {
			public void onSuccess(String result) {
				weekCalendar().initData();
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
