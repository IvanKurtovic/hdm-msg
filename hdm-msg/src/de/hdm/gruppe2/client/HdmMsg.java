package de.hdm.gruppe2.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import de.hdm.gruppe2.shared.LoginInfo;
import de.hdm.gruppe2.shared.LoginServiceAsync;
import de.hdm.gruppe2.shared.MsgServiceAsync;
import de.hdm.gruppe2.shared.bo.User;

/**
 * Die EntryPoint Klasse des Messengers. Er stellt den Startpunkt des Nutzers dar
 * und bietet ihm eine Menüleiste zur Navigation durch den Messenger. Ebenfalls enthalten
 * ist der Login für den User. Nachdem der User sich einloggt wird sein User Objekt an
 * alle geladenen Views übergeben damit sie mit den enthaltenen Informationen weiter arbeiten
 * können.
 * 
 */
public class HdmMsg implements EntryPoint {
	
	private MsgServiceAsync msgSvc = ClientsideSettings.getMsgService();
	
	// Ein Objekt der momentan eingeloggten nutzers wird als private Variable
	// abgelegt und den einzelnen Panelen mitgesendet.
	private User currentUser = null;
	
	private LoginInfo loginInfo = null;
	private VerticalPanel loginPanel = new VerticalPanel();
	private Label loginLabel = new Label(
			"Please sign in to your Google Account to access the Messenger application.");
	private Anchor signInLink = new Anchor("Sign In");
	
	public void onModuleLoad() {
		
		LoginServiceAsync loginSvc = ClientsideSettings.getLoginService();
		loginSvc.login(GWT.getHostPageBaseURL(), new AsyncCallback<LoginInfo> () {

			@Override
			public void onFailure(Throwable caught) {
				ClientsideSettings.getLogger().severe("Login fehlgeschlagen!");
			}

			@Override
			public void onSuccess(LoginInfo result) {
				loginInfo = result;
				if(loginInfo.isLoggedIn() == true) {
					registerUser();	
				} else {
					loadLogin();
				}
			}
		});		
	}
	
	private void registerUser() {
		msgSvc.createUser(loginInfo.getEmailAddress(), loginInfo.getNickname(), new AsyncCallback<User>() {

			@Override
			public void onFailure(Throwable caught) {
				loadLogin();
				
				ClientsideSettings.getLogger().severe("User konnte nicht registriert werden.");
			}

			@Override
			public void onSuccess(User result) {
				if(result != null) {
					currentUser = result;
				}

				loadMessenger();
				
				ClientsideSettings.getLogger().finest("User in Datenbank registriert.");
			}
			
		});
	}
	
	private User getCurrentUser() {
		return this.currentUser;
	}
	
	private void loadLogin() {
		// Assemble login panel
		signInLink.setHref(loginInfo.getLoginUrl());
		loginPanel.add(loginLabel);
		loginPanel.add(signInLink);
		RootPanel.get("content_wrap").add(loginPanel);
	}
	
	private void loadMessenger() {
		
		Command newPostOverview = new Command() {
			public void execute() {
				RootPanel.get("content_wrap").clear();
				RootPanel.get("content_wrap").add(new NewPostOverview(getCurrentUser()));
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
				RootPanel.get("content_wrap").add(new ChatOverview(getCurrentUser()));			
			}	
		};
		
		Command hashtagSubscription = new Command() {

			@Override
			public void execute() {
				RootPanel.get("content_wrap").clear();
				RootPanel.get("content_wrap").add(new HashtagSubscriptionOverview(getCurrentUser()));	
			}
		};
		
		Command userSubscription = new Command() {

			@Override
			public void execute() {
				RootPanel.get("content_wrap").clear();
				RootPanel.get("content_wrap").add(new UserSubscriptionOverview(getCurrentUser()));
			}
		};
		
		Command about = new Command() {

			@Override
			public void execute() {
				RootPanel.get("content_wrap").clear();
				RootPanel.get("content_wrap").add(new Impressum());	
			}
		};
		
		Command callReportGenerator = new Command() {

			@Override
			public void execute() {
				RootPanel.get("content_wrap").clear();
				Window.Location.assign(GWT.getHostPageBaseURL() + "ReportGenerator.html");
			}
		};
		
		Command logout = new Command() {

			@Override
			public void execute() {
				RootPanel.get("content_wrap").clear();
				Window.Location.assign(loginInfo.getLogoutUrl());
			}
		};

		MenuBar homeMenu = new MenuBar(true);
		homeMenu.addItem("Startseite", newPostOverview);
		
		MenuBar userMenu = new MenuBar(true);
		userMenu.addItem("Alle User anzeigen", userOverview);
		
		MenuBar aboMenu = new MenuBar(true);
		aboMenu.addItem("User Abos", userSubscription);
		aboMenu.addItem("Hashtag Abos", hashtagSubscription);
		
		MenuBar chatMenu = new MenuBar(true);
		chatMenu.addItem("Chats anzeigen", chatOverview);
		
		MenuBar mainMenu = new MenuBar();
		mainMenu.setAutoOpen(true);
		mainMenu.addItem("Startseite", homeMenu);
		mainMenu.addItem("Chats", chatMenu);
		mainMenu.addItem("Users", userMenu);
		mainMenu.addItem("Abos", aboMenu);
		mainMenu.addItem("About", about);
		mainMenu.addItem("Report Generator", callReportGenerator);
		mainMenu.addItem("Logout", logout);
		
	    RootPanel.get("header_wrap").add(mainMenu);		
	}
}
