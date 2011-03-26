package com.aljamaa.client;

import com.aljamaa.entity.Momin;
import com.google.gwt.animation.client.Animation;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;

public class MainBar extends Composite {
	
	/**
	 * @wbp.parser.constructor
	 */
	private final TaskServiceAsync taskService = GWT.create(TaskService.class);
	final static int wait_code = 0; 
	final static int success_code = 1; 
	final static int error_code = 2; 
	Image image;
	Label lblMessage;
	static MainBar mainBar;
	HorizontalPanel messageHP;
	 Momin momin;
	public MainBar() {
		
		mainBar = this;
		VerticalPanel verticalPanel = new VerticalPanel();
		initWidget(verticalPanel);
		verticalPanel.setSize("800px", "43px");
		
		MenuBar menuBar = new MenuBar(false);
		verticalPanel.add(menuBar);
		menuBar.setHeight("");
		
		MenuItem mntmWirde = new MenuItem("Wirde", false, (Command) null);
		menuBar.addItem(mntmWirde);
		
		MenuItem mntmQuran = new MenuItem("Quran", false, (Command) null);
		
		menuBar.addItem(mntmQuran);
		
		messageHP = new HorizontalPanel();
		messageHP.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		verticalPanel.add(messageHP);
		verticalPanel.setCellHorizontalAlignment(messageHP, HasHorizontalAlignment.ALIGN_CENTER);
		
		lblMessage = new Label();
		lblMessage.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		messageHP.add(lblMessage);
		messageHP.setCellHorizontalAlignment(lblMessage, HasHorizontalAlignment.ALIGN_CENTER);
		
		image = new Image((String) null);
		messageHP.add(image);
		messageHP.setVisible(false);
		
		mainBar.lblMessage.setStyleName("message");
	}
	

	public  void message(String msg, int code)
	{
		message(msg, code,0);
	}
	public  void message(String msg, int code, int delay)
	{
		lblMessage.setText(msg);
		
		switch (code){
			case wait_code: image.setVisible(true); image.setUrl("wait.gif"); break;
			case success_code:  image.setVisible(false);//image.setStyleName("eval1"); 
				break;
			case error_code:  image.setVisible(false);
				break;
		}
		messageHP.setVisible(true);
		if(delay ==0)
			return;
		Timer timer = new Timer() {			
			public void run() {
				clear();
			}
		}; 
		timer.schedule(delay*1000);		
	}
	
	public Momin getCurrentMomin(final boolean mondatory){
	
//		if(momin != null)
//			return momin;
//		taskService.getCurrentMomin(new AsyncCallback<Momin>() {
//			public void onSuccess(Momin m) {
//				momin = m;
//			}
//			public void onFailure(Throwable caught) {
//				//si l'utilisateur n'est pas connecté
//				momin = null;
//				if(mondatory)
//					login();
//			}
//		});
		return null;
		
	}
	public void login(){
		Window.Location.assign("/login.jsp");
		
	}
	
	public static MainBar get()
	{
		if(mainBar == null)
			mainBar = new MainBar();
		return mainBar;
	}


	public void clear() {
		messageHP.setVisible(false);
	}

}
