package com.aljamaa.client;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.InlineHTML;

public class PriorityButton extends Composite {

	HTML inlineHTML;
	ClickHandler clickHandler;
	int max;
	int value = 0;
	String style;
	public PriorityButton(int max, String style, int value) {
		this.max = max;
		this.style = style;
		this.value = value;
		inlineHTML = new HTML("");
		inlineHTML.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				click();
				clickHandler.onClick(event);				
			}
		});
		update();
	
		initWidget(inlineHTML);
	}

	private void setStyle(String css){
		inlineHTML.setStyleName(css);		
	}
	
	private void click()
	{
		if(++value >= max) 
			value = 0;
		update();
		
	}
	
	private void update()
	{
		setStyle(style+value);		
	}
	
	public void addClickHandler(ClickHandler ch)
	{
		this.clickHandler = ch;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
		update();
	}

	public String getStyle() {
		return style;
	}
	
	
}
