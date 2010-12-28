package com.aljamaa.client;

import java.util.Date;

import com.aljamaa.entity.Task;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.SimpleCheckBox;

public class TaskCell extends Composite {

	Task task;
	SimpleCheckBox evalCheckBox;
	TextBox evalTextBox;
	Grid grid;
	
	/**
	 * @wbp.parser.constructor
	 */
	public TaskCell() {
		
		grid = new Grid(1, 4);
		grid.setStyleName("taskCell");
		initWidget(grid);
//		grid.setSize("155px", "23px");
		

	}
	public TaskCell(Task task) {
		this();
		this.task = task;
		initView();
	}
	
	public void initView()
	{
		Label timeLabel = new Label("13:45");
		timeLabel.setStyleName("taskCell");
		grid.setWidget(0, 0, timeLabel);
		
		Label nameLabel = new Label(task.getName());
		nameLabel.setStylePrimaryName("taskCell");
		grid.setWidget(0, 1, nameLabel);
		
//		if( task.getMin() < 2 ){
			evalCheckBox = new SimpleCheckBox();
			grid.setWidget(0, 2, evalCheckBox);
//		}else{
//			evalTextBox = new TextBox();
//			evalTextBox.setText("150");
//			grid.setWidget(0, 3, evalTextBox);
//			evalTextBox.setSize("20px", "10px");
//		}
	}

}
