package com.aljamaa.client;

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

public class TaskRepeat extends Composite {

	HorizontalPanel repeatEveryPanel;
	HorizontalPanel repeatWeekPanel;
	HorizontalPanel typeRepeatPanel;
	HorizontalPanel repeatMonthPanel;
	ListBox typeRepeatCombo;
	
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
		
		ListBox repeatEveryCombo = new ListBox();
		for(int i =1; i<31 ; i++)
			repeatEveryCombo.addItem(i+"", i+"");
		
		repeatEveryPanel.add(repeatEveryCombo);
		
		Label label_2 = new Label("day");
		repeatEveryPanel.add(label_2);
		
		repeatWeekPanel = new HorizontalPanel();
		verticalPanel.add(repeatWeekPanel);
		
		Label label_3 = new Label("Repeat on");
		repeatWeekPanel.add(label_3);
		
		CheckBox checkBox = new CheckBox("S");
		repeatWeekPanel.add(checkBox);
		
		CheckBox checkBox_1 = new CheckBox("M");
		repeatWeekPanel.add(checkBox_1);
		
		CheckBox checkBox_2 = new CheckBox("T");
		repeatWeekPanel.add(checkBox_2);
		
		CheckBox checkBox_3 = new CheckBox("W");
		repeatWeekPanel.add(checkBox_3);
		
		CheckBox checkBox_4 = new CheckBox("T");
		repeatWeekPanel.add(checkBox_4);
		
		CheckBox checkBox_5 = new CheckBox("F");
		repeatWeekPanel.add(checkBox_5);
		
		CheckBox checkBox_6 = new CheckBox("S");
		repeatWeekPanel.add(checkBox_6);
		
		repeatMonthPanel = new HorizontalPanel();
		verticalPanel.add(repeatMonthPanel);
		
		Label label_4 = new Label("Repeat by");
		repeatMonthPanel.add(label_4);
		
		RadioButton radioButton = new RadioButton("new name", "day of the month");
		repeatMonthPanel.add(radioButton);
		
		RadioButton radioButton_1 = new RadioButton("new name", "day od the week");
		repeatMonthPanel.add(radioButton_1);
		
		HorizontalPanel startPanel = new HorizontalPanel();
		verticalPanel.add(startPanel);
		
		Label label_5 = new Label("Starts on");
		startPanel.add(label_5);
		label_5.setWidth("76px");
		
		DateBox dateBox = new DateBox();
		startPanel.add(dateBox);
		
		HorizontalPanel endPanel = new HorizontalPanel();
		verticalPanel.add(endPanel);
		endPanel.setWidth("361px");
		
		Label label_6 = new Label("Ends on");
		endPanel.add(label_6);
		label_6.setWidth("66px");
		
		RadioButton radioButton_2 = new RadioButton("new name", "Never");
		endPanel.add(radioButton_2);
		radioButton_2.setWidth("85px");
		
		RadioButton radioButton_3 = new RadioButton("new name", "Until");
		endPanel.add(radioButton_3);
		radioButton_3.setWidth("72px");
		
		DateBox dateBox_1 = new DateBox();
		endPanel.add(dateBox_1);
		
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

//	public LabelField getLabelField() {
//		return labelField;
//	}
//	public VerticalPanel getVerticalPanel() {
//		return verticalPanel;
//	}
//	
	
}
