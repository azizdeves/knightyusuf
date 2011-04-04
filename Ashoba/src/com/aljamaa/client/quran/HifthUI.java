package com.aljamaa.client.quran;

import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.IntegerBox;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.widgetideas.graphics.client.ImageLoader;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.user.client.ui.ListBox;

public class HifthUI extends Composite {
	QuranCanvas canvas;
	IntegerBox integerBox;
	int currentPage;
	public HifthUI() {
		
		Grid grid = new Grid(2, 2);
		initWidget(grid);
		integerBox = new IntegerBox();
		integerBox.setValue(2);
		currentPage = integerBox.getValue();
		HorizontalPanel horizontalPanel = new HorizontalPanel();
		grid.setWidget(0, 0, horizontalPanel);
		
		Button btnNewButton = new Button("<<");
		btnNewButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				setCurrentPage(-2);
			}
		});
		horizontalPanel.add(btnNewButton);
		
		integerBox.addChangeHandler(new ChangeHandler() {
			public void onChange(ChangeEvent event) {
				setCurrentPage(integerBox.getValue());
			}
		});
		horizontalPanel.add(integerBox);
		integerBox.setWidth("32px");
		
		Button button = new Button(">>");
		button.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				setCurrentPage(-1);
			}
		});
		horizontalPanel.add(button);
		grid.getCellFormatter().setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_CENTER);
		grid.getCellFormatter().setHorizontalAlignment(1, 0, HasHorizontalAlignment.ALIGN_CENTER);
		
		canvas = new QuranCanvas();
		grid.setWidget(1, 0, canvas);
		
		ListBox masksListBox = new ListBox();
		grid.setWidget(1, 1, masksListBox);
		masksListBox.setVisibleItemCount(5);
		
		loadImage(integerBox.getValue());
	}
	
	protected void setCurrentPage(int i) {
		if(i==-1)
			integerBox.setValue(integerBox.getValue()+1);
		else if(i==-2)
			integerBox.setValue(integerBox.getValue()-1);
		loadImage(integerBox.getValue());
		
	}

	public void loadImage(int page){
		String snum = page+"";
		if(snum.length()==2)
			snum="0"+snum;
		else 	if(snum.length()==1)
			snum="00"+snum;
		
		String[] urls = new String[] {"http://www.quranflash.com/quran%20modules/Warsh/size%20B/Quran_Page_"+snum+".fbk"};//,"http://spirituel-life.appspot.com/saf7as3s.jpg"};
		ImageLoader.loadImages(urls, new ImageLoader.CallBack() {
			public void onImagesLoaded(ImageElement[] imageElements) {
				ImageElement image = imageElements[0];
				canvas.setImage(image);
			}
		});
	}

}
