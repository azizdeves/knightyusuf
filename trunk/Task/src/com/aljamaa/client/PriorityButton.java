package com.aljamaa.client;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ClickEvent;

public class PriorityButton extends Composite {

	long time;
	Image image;
	public PriorityButton() {
		
		image = new Image((String) null);
		image.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
//				image.set
			}
		});
		initWidget(image);
	}

}
