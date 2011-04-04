package com.aljamaa.client.quran;




import com.aljamaa.entity.quran.BMaskStatus;
import com.aljamaa.entity.quran.BoxMask;
import com.aljamaa.entity.quran.Mask;
import com.aljamaa.shared.Base64Utils;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.widgetideas.graphics.client.Color;
import com.google.gwt.widgetideas.graphics.client.GWTCanvas;
import com.google.gwt.widgetideas.graphics.client.ImageLoader;





public class QuranCanvas extends Composite {

	static  Canvas canvas;
	static Mask mask ;
	static ImageElement image;
	
	public QuranCanvas() {
		mask= new Mask();
		Grid grid = new Grid(1, 1);
		
		canvas = new Canvas(470,1000);
		canvas.sinkEvents(Event.MOUSEEVENTS|Event.ONCLICK);
		Base64Utils b = new Base64Utils();
		b.toBase64(5);
		grid.setWidget(0, 0, canvas);
		

		initWidget(grid);
	}
	
	public void setImage(ImageElement ie){
		image = ie;
		draw();
	}
	public static void draw(BoxMask bm)
	{
		Color color=null;
		switch(bm.getStatus()){
			case active: color = new Color(255, 255, 255); break;
			case drawing: color = new Color(0, 0, 255, 0.5f); break;
			case deactive: color = new Color(255, 255, 0, 0.3f); break;
		}
		QuranCanvas.canvas.setFillStyle(color);
		QuranCanvas.canvas.fillRect(bm.getX(), bm.getY(), bm.getW(), bm.getH());
	}
	static public void draw(){
		canvas.drawImage(image, 0, 0);
		for(BoxMask bm : mask.getBoxMasks())
			draw(bm);
	}
}

class Canvas extends GWTCanvas {
	
	 boolean drawing;
	 boolean startDrag;
	 BoxMask currentBox;

    public Canvas() {
        super(300, 620);
    }

    public Canvas(int i, int j) {
    	super(i,j);
    }

//	public void draw()
//	{
//		Color color=null;
//		switch(status){
//			case active: color = new Color(255, 255, 255); break;
//			case drawing: color = new Color(0, 0, 255, 0.5f); break;
//			case deactive: color = new Color(255, 255, 0, 0.3f); break;
//		}
//		QuranCanvas.canvas.setFillStyle(color);
//		QuranCanvas.canvas.fillRect(x, y,w,h	);
//	}
//	
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
            			if(bm.getStatus() == BMaskStatus.active)
            				bm.setStatus(BMaskStatus.deactive);
            			else
            				if(bm.getStatus() == BMaskStatus.deactive)
                				bm.setStatus(BMaskStatus.active);
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
		currentBox.setStatus(BMaskStatus.active);
		currentBox=null;
		QuranCanvas.draw();
	}

	public void drawMask(int x, int y) {
		currentBox.setHeightWidth(x, y);
		QuranCanvas.draw();
	}

	public void startMask(int x, int y) {
		currentBox = new BoxMask(x,y);
		currentBox.setStatus(BMaskStatus.drawing);
		QuranCanvas.mask.addBoxMask(currentBox);
	}
}

