package com.aljamaa.client;

import com.aljamaa.entity.TaskSeed;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;

public class TaskRepeat extends Composite {

	HorizontalPanel repeatEveryPanel;
	HorizontalPanel repeatWeekPanel;
	HorizontalPanel typeRepeatPanel;
	HorizontalPanel repeatMonthPanel;
	ListBox typeRepeatCombo;
	ListBox repeatEveryCombo;
	CheckBox dayCheckBox[];
	RadioButton dayOfWeekRadioButton;
	DateBox endDateBox;

	
	public TaskRepeat() {
		
		com.google.gwt.user.client.ui.VerticalPanel verticalPanel = new com.google.gwt.user.client.ui.VerticalPanel();
		verticalPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		initWidget(verticalPanel);
		
		typeRepeatPanel = new HorizontalPanel();
		verticalPanel.add(typeRepeatPanel);
		typeRepeatPanel.setWidth("273px");
		
		Label label = new Label("Repeats");
		typeRepeatPanel.add(label);
		
		typeRepeatCombo = new ListBox();
		typeRepeatCombo.addItem("Daily");
		typeRepeatCombo.addItem("Weekly");
		typeRepeatCombo.addItem("Monthly");
		typeRepeatCombo.addItem("Yearly");
		typeRepeatCombo.addChangeHandler(new ChangeHandler() {
			public void onChange(ChangeEvent event) {
				changeType();				
			}
		});
		typeRepeatPanel.add(typeRepeatCombo);
		
		repeatEveryPanel = new HorizontalPanel();
		verticalPanel.add(repeatEveryPanel);
		repeatEveryPanel.setWidth("273px");
		
		Label label_1 = new Label("Repeat every");
		repeatEveryPanel.add(label_1);
		
		repeatEveryCombo = new ListBox();
		for(int i =1; i<31 ; i++)
			repeatEveryCombo.addItem(i+"", i+"");
		
		repeatEveryPanel.add(repeatEveryCombo);
		
		Label label_2 = new Label("day");
		repeatEveryPanel.add(label_2);
		
		repeatWeekPanel = new HorizontalPanel();
		verticalPanel.add(repeatWeekPanel);
		repeatWeekPanel.setVisible(false);
		
		Label label_3 = new Label("Repeat on");
		repeatWeekPanel.add(label_3);
		
		dayCheckBox = new CheckBox[7];
		dayCheckBox[0] = new CheckBox("S");
		repeatWeekPanel.add(dayCheckBox[0]);
		
		dayCheckBox[1] = new CheckBox("M");
		repeatWeekPanel.add(dayCheckBox[1]);
		
		dayCheckBox[2] = new CheckBox("T");
		repeatWeekPanel.add(dayCheckBox[2]);
		
		dayCheckBox[3] = new CheckBox("W");
		repeatWeekPanel.add(dayCheckBox[3]);
		
		dayCheckBox[4] = new CheckBox("T");
		repeatWeekPanel.add(dayCheckBox[4]);
		
		dayCheckBox[5] = new CheckBox("F");
		repeatWeekPanel.add(dayCheckBox[5]);
		
		dayCheckBox[6] = new CheckBox("S");
		repeatWeekPanel.add(dayCheckBox[6]);
		
		repeatMonthPanel = new HorizontalPanel();
		repeatMonthPanel.setVisible(false);
		verticalPanel.add(repeatMonthPanel);
		
		Label label_4 = new Label("Repeat by day of the");
		repeatMonthPanel.add(label_4);
		
		dayOfWeekRadioButton = new RadioButton("repeatByRadio", "week");
		repeatMonthPanel.add(dayOfWeekRadioButton);
		
		RadioButton radioButton_1 = new RadioButton("repeatByRadio", "month");
		repeatMonthPanel.add(radioButton_1);
		
		HorizontalPanel startPanel = new HorizontalPanel();
		verticalPanel.add(startPanel);
		
		Label label_5 = new Label("Starts on");
		startPanel.add(label_5);
		label_5.setWidth("76px");
		
		DateBox dateBox = new DateBox();
		dateBox.setEnabled(false);
		startPanel.add(dateBox);
		
		HorizontalPanel endPanel = new HorizontalPanel();
		verticalPanel.add(endPanel);
		endPanel.setWidth("361px");
		
		Label label_6 = new Label("Ends on");
		endPanel.add(label_6);
		label_6.setWidth("66px");
		
		RadioButton radioButton_2 = new RadioButton("endDateRadio", "Never");
		radioButton_2.setValue(true);
		radioButton_2.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				if(event.getValue().equals(true))
					endDateBox.setEnabled(false);
				else
					endDateBox.setEnabled(true);
			}
		});
		endPanel.add(radioButton_2);
		radioButton_2.setWidth("85px");
		
		RadioButton radioButton_3 = new RadioButton("endDateRadio", "Until");
		radioButton_3.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				if(event.getValue().equals(true))
					endDateBox.setEnabled(true);
				else
					endDateBox.setEnabled(false);
			}
		});
		endPanel.add(radioButton_3);
		radioButton_3.setWidth("72px");
		
		endDateBox = new DateBox();
		endDateBox.setEnabled(false);
		endPanel.add(endDateBox);
		
	}
	
	public void changeType()
	{
		if(typeRepeatCombo.getSelectedIndex()==1){
			repeatWeekPanel.setVisible(true);
			repeatMonthPanel.setVisible(false);
		}
		else
			if(typeRepeatCombo.getSelectedIndex()==2){
				repeatMonthPanel.setVisible(true);
				repeatWeekPanel.setVisible(false);
			}
			else{
				repeatMonthPanel.setVisible(false);
				repeatWeekPanel.setVisible(false);
			}
	}
	
	public int getType()
	{
		return typeRepeatCombo.getSelectedIndex();
	}

	public int getEvery()
	{
		return repeatEveryCombo.getSelectedIndex();
	}
	public boolean[] getDaysSelected()
	{
		boolean[] days = new boolean[7];
		for(int i=0 ; i < 7 ; i++)
			days[i]=dayCheckBox[i].getValue();
		return days;
	}
	
	public boolean[] isDayOfWeek()
	{
		boolean[] param = new boolean[1];		
		param[0]= dayOfWeekRadioButton.getValue();
		return param;
	}
	
	public TaskSeed getTaskSeed(){
		TaskSeed taskSeed = new  TaskSeed();
		taskSeed.setType(getType());
		taskSeed.setEvery(getEvery());
		if(getType()==1)
			taskSeed.setParam(getDaysSelected());
		else
			if(getType() == 2)
				taskSeed.setParam(isDayOfWeek());
		taskSeed.setEnd(endDateBox.getValue());
		return taskSeed;
	}
}
