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
		
		Command newPostOverview = new Command() {
			public void execute() {
				RootPanel.get("content_wrap").clear();
				RootPanel.get("content_wrap").add(new NewPostOverview());
			}
		};
		
		Command userOverview = new Command() {
			public void execute() {
				RootPanel.get("content_wrap").clear();
				RootPanel.get("content_wrap").add(new UserOverview());
			}
		};
		
		Command chatOverview = new Command() {

			@Override
			public void execute() {
				RootPanel.get("content_wrap").clear();
				RootPanel.get("content_wrap").add(new ChatOverview());			
			}	
		};
		
		Command hashtagSubscription = new Command() {

			@Override
			public void execute() {
				RootPanel.get("content_wrap").clear();
				RootPanel.get("content_wrap").add(new HashtagSubscriptionOverview());	
			}
		};
		
		Command userSubscription = new Command() {

			@Override
			public void execute() {
				RootPanel.get("content_wrap").clear();
				RootPanel.get("content_wrap").add(new UserSubscriptionOverview());
			}
		};
		
		Command about = new Command() {

			@Override
			public void execute() {
				RootPanel.get("content_wrap").clear();
				RootPanel.get("content_wrap").add(new Impressum());	
			}
		};

		MenuBar messageMenu = new MenuBar(true);
		messageMenu.addItem("Neuer Post", newPostOverview);
		
		MenuBar userMenu = new MenuBar(true);
		userMenu.addItem("Alle User anzeigen", userOverview);
		
		MenuBar aboMenu = new MenuBar(true);
		aboMenu.addItem("User Abos", userSubscription);
		aboMenu.addItem("Hashtag Abos", hashtagSubscription);
		
		MenuBar chatMenu = new MenuBar(true);
		chatMenu.addItem("Chats anzeigen", chatOverview);
		
		MenuBar mainMenu = new MenuBar();
		mainMenu.setAutoOpen(true);
		mainMenu.addItem("Home", test);
		mainMenu.addItem("Neuer Post", messageMenu);
		mainMenu.addItem("Chats", chatMenu);
		mainMenu.addItem("Users", userMenu);
		mainMenu.addItem("Abos", aboMenu);
		mainMenu.addItem("About", about);
		
		//RootPanel.get("content_wrap").add(welcomeImage);
	    RootPanel.get("header_wrap").add(mainMenu);
		RootPanel.get("footer_wrap").add(new Impressum());
		
	}
	
}
