package com.aljamaa.client;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.InlineHTML;

public class ChartBar extends Composite {
	
	int oScale,nScale,width;
	
	InlineHTML inlineHTML ;
	
	public ChartBar() {
		this(0,0,100);
		
	}
	
	public ChartBar(int oScale, int nScale,int width) {
		super();
		inlineHTML = new InlineHTML();
		initWidget(inlineHTML);
		this.oScale = oScale;
		this.nScale = nScale;
		this.width = width;
		init();
	}

	public void init(){
		int o = oScale*width/100;
		int n = nScale*width/100;
		int u = 100-oScale-nScale*width/100;
		inlineHTML.setHTML("<div style='display: inline-block; background-color:green; height : 10px; width:"+o+"px;'></div>" +
				"<div style='display: inline-block; background-color:#FFDD00; height : 10px; width:"+u+"px;'></div>" +
				"<div style='display:inline-block; background-color:red; height : 10px; width:"+n+"px;'></div>");
	}

}
