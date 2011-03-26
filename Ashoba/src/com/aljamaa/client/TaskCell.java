package com.aljamaa.client;

import java.util.Comparator;

import com.aljamaa.entity.Task;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
//
//class Grid2 extends Grid{
//	public Grid2(int i, int j) {
//		super(i,j);
//	}
//
//	public  <H extends EventHandler> HandlerRegistration addHandler2(final H handler, GwtEvent.Type<H> type){
//		return addHandler(handler, type);
//	}
//}
public class TaskCell extends Composite implements Comparable{

	Task task;
	PriorityButton evalCheckBox;
	TextBox evalTextBox;
	Grid grid;
	boolean readOnly;
	static PopupPanel pop;
	static boolean dragging = false;
	TextBox nameLabel;


	static Comparator<Task> dateTaskComp= new Comparator<Task>() {
		public int compare(Task o1, Task o2) {
			return o1.compareTo(o2);
		}
	};
	static Comparator<Task> idTaskComp= new Comparator<Task>() {
		public int compare(Task o1, Task o2) {
			return o1.compareTo(o2);
		}
	};

	/**
	 * @wbp.parser.constructor
	 */
	public TaskCell() {
		grid = new Grid(1, 3);
		initWidget(grid);
	}
	public TaskCell(Task task , boolean readOnly) {
		this();
		this.readOnly = readOnly;
		this.task = task;
		initView();
	}

	public void initView()
	{
//		if(LocaleInfo.getCurrentLocale().isRTL()){
//			setStyleName("rtl tskcell");
//		}else
			setStyleName("tskcell");

		DateTimeFormat format =  DateTimeFormat.getFormat("HH:mm");
		Label timeLabel = new Label(format.format(task.getDate()));
		timeLabel.setStyleName("timeTsk");
		grid.setWidget(0, 0, timeLabel);

		nameLabel = new TextBox();
		nameLabel.setReadOnly(true);
		//		if(task.getName().length()>12)
		//			nameLabel.setText(task.getName().substring(0, 12)+"..");
		//		else
		nameLabel.setText(task.getName());

		pop = new PopupPanel(true);

		nameLabel.sinkEvents(Event.MOUSEEVENTS | Event.ONDBLCLICK);
		grid.sinkEvents(Event.MOUSEEVENTS | Event.ONDBLCLICK);
		nameLabel.setStyleName("nameTsk");
		grid.setWidget(0, 1, nameLabel);

		if( task.getMin() < 1 ){
			evalCheckBox = new PriorityButton(3 , "eval" ,task.getEval()+1, readOnly);
			evalCheckBox.setValue(task.getEval()+1);
			grid.setWidget(0, 2, evalCheckBox);
			if(!readOnly)
				evalCheckBox.addClickHandler(new ClickHandler() {			
					public void onClick(ClickEvent event) {
						task.setEval(evalCheckBox.getValue()-1);
						WeekCalendar.modifiedTasks.add(task);				
					}
				});
		}else{
			evalTextBox = new TextBox();
			setEvalText(true);

			evalTextBox.setReadOnly(readOnly);
			grid.setWidget(0, 2, evalTextBox);

			if(!readOnly)
				evalTextBox.addChangeHandler(new ChangeHandler() {				
					public void onChange(ChangeEvent event) {
						if(!evalTextBox.getValue().isEmpty())
							task.setEval(Integer.parseInt(evalTextBox.getValue()));
						else
							task.setEval(-1);

						WeekCalendar.modifiedTasks.add(task);									
					}
				});
			if(!readOnly)
				evalTextBox.addFocusHandler(new FocusHandler() {				
					public void onFocus(FocusEvent event) {		
						setEvalText(false);
						evalTextBox.setStyleName("evalTextFocus");
					}			
				});

			if(!readOnly)
				evalTextBox.addBlurHandler(new BlurHandler() {
					public void onBlur(BlurEvent event) {					
						setEvalText(true);
					}
				});
		}
		if(!readOnly)  
			nameLabel.addClickHandler(new ClickHandler() {			
				public void onClick(ClickEvent event) {
					WeekCalendar.weekCalendar.getNwTskUi().task = task;
					WeekCalendar.weekCalendar.shwNwTskDlg();
				}
			});

		//		DOM.addEventPreview(this);
	}


	@Override
	public void onBrowserEvent(Event event) {
		super.onBrowserEvent(event);
		switch (DOM.eventGetType(event)) {
		//		case Event.ONDBLCLICK:
		//			WeekCalendar.weekCalendar.shwNwTskDlg();
		//			break;
		case Event.ONMOUSEDOWN:
			//	    	  startDrag(event);
			break;
		case Event.ONMOUSEUP:
			if(dragging)
				stopDrag(event);
			break;
		case Event.ONMOUSEMOVE:
			if(dragging)
				move(event);
			break;
		case Event.ONMOUSEOVER:
			showPopup( event) ;					
			//	    	  event.stopPropagation();
			//	    	  event.cancelBubble(true);
			//	    	  DOM.eventCancelBubble(event, true);
			break;
		case Event.ONMOUSEOUT:
			pop.hide();
			//	    	  stopDrag(event);
			break;
		}
	}
	public void startDrag(Event event){
		dragging = true;
		DOM.setStyleAttribute(getElement(), "position", "absolute");
		DOM.setCapture(getElement());

	}

	public  void move(Event event) {

		int x = event.getClientX();
		int y = event.getClientY();
		//		int newX = Math.max(0, x + getAbsoluteLeft() - dragStartX);
		//		int newY = Math.max(0, y + getAbsoluteTop() - dragStartY);


		DOM.setStyleAttribute(getElement(), "left", x+"px");
		DOM.setStyleAttribute(getElement(), "top",y+"px");
	}
	public void stopDrag(Event event) {
		dragging = false;
		DOM.releaseCapture(getElement());
	}
	private String condenseNumber(int num){
		char unit='.';
		String  sn="";
		if(num > 999999)
		{
			unit='m';
			sn= String.valueOf((double)(num / 1000000.0));
		}else 
			if(num > 999)
			{
				unit='k';
				sn= String.valueOf((double)(num / 1000.0));
			}
			else 
				sn = num+"";
		if(sn.length()>4)
			sn = sn.substring(0,4); 
		//					else
		//						sn='0'+sn;
		sn=sn.replace('.',unit);
		return sn;		

		//		if(num > 999)
		//		{
		//			String  sn= String.valueOf((double)(num / 1000.0));
		//			int i =sn.indexOf('.');
		//			if(sn.length()>3)
		//				if(i == 1)
		//					sn = sn.substring(0,3); 
		//				else if(i>1)
		//					sn=sn.substring(0,i);
		//			return sn+"K";
		//		}
		//		return num+"";
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

	public void showPopup(Event event) {
		if(dragging)
			return;
		pop.clear();
		String innerHtmlPanel =task.getName();
		if(task.getMin()>0)
			innerHtmlPanel += "<br>val: "+task.getEval()+
			"<br>minimum: "+task.getMin();

		pop.add(new HTMLPanel(innerHtmlPanel));
		pop.setPopupPosition(event.getClientX()+10, event.getClientY()+10);
		pop.show();				
	}
	//	public Element getInnerPop(){
	//		HTML pop = new HTML("<div>"+task.getName()+"</div>");
	//		
	//	}
	//@Override
	//public boolean onEventPreview(Event event) {
	////	if (DOM.eventGetType(event) == Event.ONMOUSEDOWN &&
	////	        DOM.isOrHasChild(getElement(), DOM.eventGetTarget(event))) {
	////		if(dragging){
	//	      DOM.eventPreventDefault(event);
	////		return false;
	////	    }
	//	return true;
	//}
	@Override
	public int compareTo(Object o) {
		return (int) (task.getDate().getTime()-((TaskCell)o).task.getDate().getTime());
		//	return task.compareTo(o);

	}




}
