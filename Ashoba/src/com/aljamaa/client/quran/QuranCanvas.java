package com.aljamaa.client.quran;

import java.util.ArrayList;
import java.util.Date;
import java.util.StringTokenizer;


import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.widgetideas.graphics.client.Color;
import com.google.gwt.widgetideas.graphics.client.GWTCanvas;
import com.google.gwt.widgetideas.graphics.client.ImageLoader;

import entity.Mask;


public class QuranCanvas extends Composite {

	static  Canvas canvas;
	static Mask mask ;
	static ImageElement image;
	
	public QuranCanvas() {
		mask= new Mask();
		Grid grid = new Grid(1, 1);
		
		canvas = new Canvas(1000,1000);
		canvas.sinkEvents(Event.MOUSEEVENTS|Event.ONCLICK);
		Base64Utils b = new Base64Utils();
		b.toBase64(5);
		grid.setWidget(0, 0, canvas);
		
		String[] urls = new String[] {"http://www.quranflash.com/quran%20modules/Warsh/size%20B/Quran_Page_011.fbk"};//,"http://spirituel-life.appspot.com/saf7as3s.jpg"};
		ImageLoader.loadImages(urls, new ImageLoader.CallBack() {
			public void onImagesLoaded(ImageElement[] imageElements) {
				image = imageElements[0];
				canvas.drawImage(image, 600, 0);
			}
		});
		initWidget(grid);
	}
	public void draw(BoxMask bm)
	{
		Color color=null;
		switch(bm.getStatus()){
			case BMaskStatus.active: color = new Color(255, 255, 255); break;
			case drawing: color = new Color(0, 0, 255, 0.5f); break;
			case deactive: color = new Color(255, 255, 0, 0.3f); break;
		}
		QuranCanvas.canvas.setFillStyle(color);
		QuranCanvas.canvas.fillRect(bm.x, y,w,h	);
	}
	static public void draw(){
		canvas.drawImage(image, 0, 0);
		for(BoxMask bm : mask.boxMasks)
			bm.draw();
	}
}

class Canvas extends GWTCanvas {
	
	 boolean drawing;
	 boolean startDrag;
	 BoxMask currentBox;

    public Canvas() {
        super(620, 620);
    }

    public Canvas(int i, int j) {
    	super(i,j);
    }

	public void draw()
	{
		Color color=null;
		switch(status){
			case active: color = new Color(255, 255, 255); break;
			case drawing: color = new Color(0, 0, 255, 0.5f); break;
			case deactive: color = new Color(255, 255, 0, 0.3f); break;
		}
		QuranCanvas.canvas.setFillStyle(color);
		QuranCanvas.canvas.fillRect(x, y,w,h	);
	}
	
	@Override
    public void onBrowserEvent(Event event) {
        super.onBrowserEvent(event);
            int x = event.getClientX() - getAbsoluteLeft()+Window.getScrollLeft();
            int y = event.getClientY() - getAbsoluteTop()+Window.getScrollTop();
            switch (event.getTypeInt()) {
            	case Event.ONCLICK:          
            		if(drawing){
            			drawing = false;
            			return ;
            		}
            		BoxMask bm = QuranCanvas.mask.getBoxMask(x, y);
            		drawing=false;
            		if(bm!=null){
            			if(bm.status == Status.active)
            				bm.status=Status.deactive;
            			else
            				if(bm.status == Status.deactive)
                				bm.status=Status.active;
            			QuranCanvas.draw();
            		}
            		break;
                case Event.ONMOUSEDOWN:
                	startDrag = true;
                	startMask(x,y);
                    break;
                case Event.ONMOUSEMOVE:
                	if(startDrag)
                		drawing = true;
                	else return;
                	if(currentBox!=null){
                		drawMask(x,y);
                	}
                    break;
                case Event.ONMOUSEUP:
                	startDrag = false;
                	if(drawing)
                		stopMask();
                    break;
        }
    }
	

	public  void stopMask() {
//		drawing = false;
		currentBox.status=Status.active;
		currentBox=null;
		QuranCanvas.draw();
	}

	public void drawMask(int x, int y) {
		currentBox.setHeightWidth(x, y);
		QuranCanvas.draw();
	}

	public void startMask(int x, int y) {
		currentBox = new BoxMask(x,y);
		currentBox.status=Status.drawing;
		QuranCanvas.mask.addBoxMask(currentBox);
	}
}

