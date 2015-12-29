package de.hdm.gruppe2.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class HdmMsg extends VerticalPanel implements EntryPoint {
	
	public void onModuleLoad() {
		
		Command test = new Command() {
			public void execute() {
				Window.alert("BANANAAAAA!!!");
			}
		};
		
		MenuBar messageMenu = new MenuBar(true);
		messageMenu.addItem("Neue Nachricht", test);
		
		MenuBar userMenu = new MenuBar(true);
		userMenu.addItem("Alle User anzeigen", test);
		
		MenuBar aboMenu = new MenuBar(true);
		aboMenu.addItem("User Abos", test);
		aboMenu.addItem("Hashtag Abos", test);
		
		MenuBar chatMenu = new MenuBar(true);
		chatMenu.addItem("Chats anzeigen", test);
		
		MenuBar mainMenu = new MenuBar();
		mainMenu.setWidth("100%");
		mainMenu.setAutoOpen(true);
		mainMenu.addItem("Nachrichten", messageMenu);
		mainMenu.addItem("Chats", chatMenu);
		mainMenu.addItem("Users", userMenu);
		mainMenu.addItem("Abos", aboMenu);
		
		//RootPanel.get("content_wrap").add(welcomeImage);
	    RootPanel.get("header_wrap").add(mainMenu);
		RootPanel.get("footer_wrap").add(new Impressum());
		
	}
	
}
