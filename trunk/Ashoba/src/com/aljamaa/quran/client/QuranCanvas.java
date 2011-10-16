package com.aljamaa.quran.client;




import sun.java2d.loops.FillSpans;

import com.aljamaa.entity.quran.BMaskStatus;
import com.aljamaa.entity.quran.BoxMask;
import com.aljamaa.entity.quran.Mask;
import com.aljamaa.shared.Base64Utils;
import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.CssColor;
import com.google.gwt.canvas.dom.client.FillStrokeStyle;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;




public class QuranCanvas extends Composite {

	static  Canvas canvas;
	static Context2d context;
	static Mask mask ;
	static ImageElement image;
	 boolean drawing=false;
	 boolean startDrag;
	 BoxMask currentBox;
	
	public QuranCanvas() {
		mask= new Mask();
		Grid grid = new Grid(1, 1);
		
		canvas = Canvas.createIfSupported();
		canvas.setWidth("400px");
		canvas.setHeight("600px");
		if(canvas != null)
			context = canvas.getContext2d();
		canvas.sinkEvents(Event.MOUSEEVENTS|Event.ONCLICK);
		grid.setWidget(0, 0, canvas);

		initWidget(grid);
		canvas.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				int x = getAbsoluteX(event.getClientX());
				int y = getAbsoluteY(event.getClientY());
        		if(drawing){
        			drawing = false;
        			return ;
        		}
        		BoxMask bm = mask.getBoxMask(x, y);
        		drawing=false;
        		if(bm!=null){
        			if(bm.getStatus() == BMaskStatus.active)
        				bm.setStatus(BMaskStatus.deactive);
        			else
        				if(bm.getStatus() == BMaskStatus.deactive)
            				bm.setStatus(BMaskStatus.active);
        			draw();
        		}
			}
		});
		canvas.addMouseDownHandler(new MouseDownHandler() {
			public void onMouseDown(MouseDownEvent event) {
				int x = getAbsoluteX(event.getClientX());
				int y = getAbsoluteY(event.getClientY());
				startDrag = true;
            	startMask(x,y);
			}
		});
		canvas.addMouseMoveHandler(new MouseMoveHandler() {
			public void onMouseMove(MouseMoveEvent event) {
	        	if(startDrag)
            		drawing = true;
            	else return;
            	if(currentBox!=null){
            		int x = getAbsoluteX(event.getClientX());
            		int y = getAbsoluteY(event.getClientY());
            		drawMask(x,y);
            	}
			}
		});
		canvas.addMouseUpHandler(new MouseUpHandler() {
			public void onMouseUp(MouseUpEvent event) {
				startDrag = false;
            	if(drawing)
            		stopMask();
			}
		});
       
            
	}
	private int getAbsoluteX(int x){
		return x - getAbsoluteLeft()+Window.getScrollLeft();
	}
	private int getAbsoluteY(int y){
		return y - getAbsoluteTop()+Window.getScrollTop();
	}
	
	public void setImage(ImageElement ie){
		image = ie;
		draw();
	}
	
	public static void draw(BoxMask bm)
	{
		CssColor color=null;
		switch(bm.getStatus()){
			case active: color =  CssColor.make(255, 255, 255); 
						context.setGlobalAlpha(1);
						break;
			case drawing: color =  CssColor.make(0, 0, 255); 
						context.setGlobalAlpha(0.5);
						break;
			case deactive: color = CssColor.make(255, 255, 0); 
						context.setGlobalAlpha(0.5);
						break;
		}
		context.setFillStyle(color);
//		context.fillRect(bm.getX(), bm.getY(), bm.getW(), bm.getH());
		context.fillRect(10, 10, 100, 100);
	}
	
	static public void draw(){
//		context.drawImage(image, 0, 0);
		for(BoxMask bm : mask.getBoxMasks())
			draw(bm);
	}

	public static Mask getMask() {
		return mask;
	}


	public  void stopMask() {
//		drawing = false;
		currentBox.setStatus(BMaskStatus.active);
		currentBox=null;
		draw();
	}

	public void drawMask(int x, int y) {
		currentBox.setHeightWidth(x, y);
		draw();
	}

	public void startMask(int x, int y) {
		currentBox = new BoxMask(x,y);
		currentBox.setStatus(BMaskStatus.drawing);
		mask.addBoxMask(currentBox);
	}
//	public static void setMask(Mask mask) {
//		QuranCanvas.mask = mask;
//	}
	
}

