package com.aljamaa.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Widget;

public class StatisticsUI extends Composite  {

	private static StatisticsUIUiBinder uiBinder = GWT
			.create(StatisticsUIUiBinder.class);

	interface StatisticsUIUiBinder extends UiBinder<Widget, StatisticsUI> {
	}

	public StatisticsUI() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public StatisticsUI(String firstName) {
		initWidget(uiBinder.createAndBindUi(this));
	}



}
