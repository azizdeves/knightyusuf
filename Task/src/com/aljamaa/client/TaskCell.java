package com.aljamaa.client;

import java.util.Date;

import com.aljamaa.entity.Task;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.PopupPanel;
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
	PriorityButton evalCheckBox;
	TextBox evalTextBox;
	Grid grid;
	static PopupPanel pop;

	/**
	 * @wbp.parser.constructor
	 */
	public TaskCell() {

		grid = new Grid(1, 3);
		grid.setStyleName("tskcell");
		initWidget(grid);

	}
	public TaskCell(Task task) {
		this();
		this.task = task;
		initView();
	}

	public void initView()
	{
		DateTimeFormat format =  DateTimeFormat.getFormat("HH:mm");
		Label timeLabel = new Label(format.format(task.getDate()));
		timeLabel.setStyleName("timeTsk");
		grid.setWidget(0, 0, timeLabel);

		Label nameLabel = new Label();
		if(task.getName().length()>12)
			nameLabel.setText(task.getName().substring(0, 12)+"..");
		else
			nameLabel.setText(task.getName());
		
		pop = new PopupPanel(true);
		nameLabel.addMouseOverHandler(new MouseOverHandler() {			
			@Override
			public void onMouseOver(MouseOverEvent event) {
				pop.clear();
				pop.add(new Label(task.getName()));
				pop.setPopupPosition(event.getClientX()+10, event.getClientY()+10);
				pop.show();
			}
		});
		
		nameLabel.addMouseOutHandler(new MouseOutHandler() {			
			@Override
			public void onMouseOut(MouseOutEvent event) {
				pop.hide();
			}
		});		
		
		nameLabel.setStyleName("nameTsk");
		grid.setWidget(0, 1, nameLabel);

		if( task.getMin() < 2 ){
			evalCheckBox = new PriorityButton(3 , "eval" ,task.getEval()+1);
			evalCheckBox.setValue(task.getEval()+1);
			grid.setWidget(0, 2, evalCheckBox);
			evalCheckBox.addClickHandler(new ClickHandler() {			
				@SuppressWarnings("unchecked")
				@Override
				public void onClick(ClickEvent event) {
					task.setEval(evalCheckBox.getValue()-1);
					WeekCalendar.modifiedTasks.add(task);				
				}
			});
		}else{
			evalTextBox = new TextBox();
			setEvalText(true);
			grid.setWidget(0, 2, evalTextBox);
			
			evalTextBox.addChangeHandler(new ChangeHandler() {				
				@Override
				public void onChange(ChangeEvent event) {
					if(!evalTextBox.getValue().isEmpty())
						task.setEval(Integer.parseInt(evalTextBox.getValue()));
					else
						task.setEval(-1);

					WeekCalendar.modifiedTasks.add(task);									
				}
			});
			evalTextBox.addFocusHandler(new FocusHandler() {				
				@Override
				public void onFocus(FocusEvent event) {		
					setEvalText(false);
					evalTextBox.setStyleName("evalTextFocus");
				}			
			});

			evalTextBox.addBlurHandler(new BlurHandler() {
				@Override
				public void onBlur(BlurEvent event) {					
					setEvalText(true);
				}
			});
		}
		
		nameLabel.addClickHandler(new ClickHandler() {			
			@Override
			public void onClick(ClickEvent event) {
				WeekCalendar.weekCalendar.getNwTskUi().task = task;
				WeekCalendar.weekCalendar.shwNwTskDlg();
			}
		});
		
//		DoubleClickHandler dbClkHndlr = new DoubleClickHandler() {			
//			@Override
//			public void onDoubleClick(DoubleClickEvent event) {
//				
//			}
//		};
		

	}

	@Override
	public void onBrowserEvent(Event event) {
		// TODO Auto-generated method stub
		super.onBrowserEvent(event);
	}
	private String condenseNumber(int num){
		if(num > 999)
		{
			String  sn= String.valueOf((double)(num / 1000.0));
			int i =sn.indexOf('.');
			if(sn.length()>3)
				if(i == 1)
					sn = sn.substring(0,3); 
				else if(i>1)
					sn=sn.substring(0,i);
			return sn+"K";
		}
		return num+"";
	}

	private void setEvalText( boolean isCondense)
	{
		int eval = task.getEval();
		if(isCondense){
			if(eval <0){
				evalTextBox.setStyleName("evalText");
				evalTextBox.setText("?");
				return ;
			}
			if(eval < task.getMin())
			{
				evalTextBox.setStyleName("evalTextKo");
			}else
				evalTextBox.setStyleName("evalTextOk");

			evalTextBox.setText(condenseNumber(eval));
		}
		else{
			evalTextBox.setStyleName("evalText");
			if(eval <0){
				evalTextBox.setText("");
				return ;
			}			
			evalTextBox.setText(eval+"");
		}

	}



}
