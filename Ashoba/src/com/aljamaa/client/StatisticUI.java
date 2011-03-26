package com.aljamaa.client;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.aljamaa.entity.Statistic;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.widgetideas.graphics.client.Color;
import com.google.gwt.widgetideas.graphics.client.GWTCanvas;
import com.google.gwt.canvas.dom.client.Context2d;

public class StatisticUI extends Composite {

	DateBox startDB;
	DateBox endDB;
	TextBox nameTB;
	List<Statistic> list ;
	
	private final TaskServiceAsync taskService = GWT.create(TaskService.class);
	private GWTCanvas canvas;
	public StatisticUI() {
		list = new ArrayList<Statistic>();
		Grid grid = new Grid(3, 5);
		initWidget(grid);
		
		canvas =new GWTCanvas();// new Canvas();
		if(canvas == null)			return;
		
		grid.setWidget(0, 3, canvas);
		canvas.setSize("300", "300");
		
		nameTB = new TextBox();
		grid.setWidget(1, 1, nameTB);
		
		startDB = new DateBox();
		grid.setWidget(1, 3, startDB);
		
		endDB = new DateBox();
		grid.setWidget(1, 4, endDB);
		
		Button button = new Button("New button");
		button.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				List<String > names = new ArrayList<String>();
				names.add(nameTB.getValue());
				getStatistics(startDB.getValue(), endDB.getValue(), names);
				
			}
		});
		grid.setWidget(2, 3, button);
	}
	
	public List<Statistic> getStatistics(Date  start, Date end , List<String>names){
		
		taskService.getStatistics(start, end, names, new AsyncCallback<List<Statistic>>() {
			
			public void onSuccess(List<Statistic> l) {
				list.addAll(l) ; 
				l.get(0).init();
				draw();
				return ;
			}
			public void onFailure(Throwable caught) {
				try {
					throw caught;
				} catch (Exception e) {
//					stateLabel.setText(e.getMessage());
				}catch(Throwable e){}
			}
		});
		return list;
	}
	//16 0 554 272 65535 65524 528 0 1502 784 65535 65216 1040 0 48523 1296 0 780 1552 65535 65511
	//16 0 554 272 65535 65524 528 0 1502 784 65535 65216 1040 0 48523 1296 0 780 1552 65535 65511
	//16 0 554 272 65535 65524 528 0 1502 784 65535 65216 1040 0 48523 1296 0 780 1552 65535 65511
	public void draw(){
//		Context2d ctx = canvas.getContext2d();
		int[] val = new int[7];
		int[] done= new int[7];
		int[] count= new int[7];
		int v,d,h;
		for(Statistic stat : list){
//			for(int i=0 ; i<7 ; i++){
			for(int dh : stat.getEvals().keySet()){
				d = dh/100;
				h = dh - d*100;
//				for(int j=0; j<24 ; j++){
					v = stat.getEvals().get(dh);
					if(v == Integer.MAX_VALUE ){						
						count[d] += 1;
						done[d] += 1;
					}else
						if(v == Integer.MIN_VALUE  ){
							count[d] += 1;
						}else
							if(v != Integer.MIN_VALUE+1){
								if(v<0)
									val[d] += -v;
								else{
									done[d] += 1;
									val[d] += v;
								}
								count[d] += 1;
							}
//				}
			}
		}
		int x = 20;
		int y;
		canvas.clear();
		for(int i=0 ; i<7 ; i++){
			if(count[i]==0)
				continue;
			x += i*20;
			y =  done[i] / count[i] * 100;
			canvas.setFillStyle(new Color("#00FF00"));
			canvas.fillRect(x , 300 - y , x+10, y);
			y = val[i];
			canvas.setFillStyle(new Color("#0000FF"));
			canvas.fillRect(x+15 , 300-y , x+25, y);
		}
	}

	
}
