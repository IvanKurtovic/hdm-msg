package de.hdm.gruppe2.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import de.hdm.gruppe2.shared.bo.User;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class HdmMsg extends VerticalPanel implements EntryPoint {
	
	// Ein Objekt der momentan eingeloggten nutzers wird als private Variable
	// abgelegt und den einzelnen Panelen mitgesendet.
	private User currentUser = null;
	
	public void onModuleLoad() {
		// Prüfung ob der User eingeloggt ist wird hier eingefügt
		// und anschließend die Oberfläche geladen
		currentUser = new User();
		currentUser.setEmail("sarikerim@googlemail.com");
		currentUser.setFirstName("Kerim");
		currentUser.setLastName("ks146@hdm-stuttgart.de");
		currentUser.setId(5);
		
		loadMessenger(currentUser);
	}
	
	private User getCurrentUser() {
		return this.currentUser;
	}
	
	private void loadMessenger(User currentUser) {
		
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
				RootPanel.get("content_wrap").add(new UserOverview(getCurrentUser()));
			}
		};
		
		Command chatOverview = new Command() {

			@Override
			public void execute() {
				RootPanel.get("content_wrap").clear();
				RootPanel.get("content_wrap").add(new ChatOverview(getCurrentUser()));			
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
