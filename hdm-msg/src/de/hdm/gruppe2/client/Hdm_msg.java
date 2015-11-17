package de.hdm.gruppe2.client;

import de.hdm.gruppe2.client.gui.UserSettingsView;
import de.hdm.gruppe2.shared.FieldVerifier;
import de.hdm.gruppe2.shared.LoginInfo;
import de.hdm.gruppe2.shared.MsgServiceAsync;
import de.hdm.gruppe2.shared.bo.User;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Hdm_msg extends VerticalPanel implements EntryPoint {

	HTML welcomeText = new HTML("<h1>Wilkommen im HDM-Messanger Editor!</h1>");
	private LoginInfo loginInfo = null;
	private VerticalPanel loginPanel = new VerticalPanel();
	private Label loginLabel = new Label(
			"Please sign in to your Google Account to access the Hdm_SmS application.");
	private Anchor signInLink = new Anchor("Sign In");

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {

		
		RootPanel.get("header_wrap").add(welcomeText);
		MsgServiceAsync msgEditorVerwaltung = ClientsideSettings.getMsgEditorVerwaltung();
		
		
		String googleId = "testGID1";
		User TestRpcUser = new User();
		msgEditorVerwaltung.getUserByGoogleId(googleId, new GetUserByGoogleIdCallback());
				
		
	}
	
	class GetUserByGoogleIdCallback implements AsyncCallback<User> {

		@Override
		public void onFailure(Throwable caught) {
			Window.alert("Der RPC hat nicht funktoniert");
		}

		@Override
		public void onSuccess(User user) {
			Window.alert("alles hat geklappt! Der einzige User bisher ist"+user.getEmail());

		}
	}

}

// LOGIN PART - NOCH NICHT IMPLEMENTIERT

//// Loginstatus prüfen
//MsgServiceAsync msgEditorVerwaltung = ClientsideSettings
//		.getMsgEditorVerwaltung();
//msgEditorVerwaltung.getUserInfo(GWT.getHostPageBaseURL(),
//		new AsyncCallback<LoginInfo>() {
//
//			public void onFailure(Throwable error) {
//			}
//
//			public void onSuccess(LoginInfo result) {
//				loginInfo = result;
//				if (loginInfo.isLoggedIn()) {
//					ClientsideSettings.getMsgEditorVerwaltung()
//							.setLoginInfo(loginInfo,
//									new LoginInfoCallback());
//					loadMenu();
//				} else {
//					loadLogin();
//				}
//			}
//		});
//}
//
///**
//* Die Login-Methode setzt unter anderem das GUI-Element in den
//* div-Container "head_wrap_right".
//*/
//private void loadLogin() {
//// Assemble login panel.
//signInLink.setHref(loginInfo.getLoginUrl());
//loginPanel.add(loginLabel);
//loginPanel.add(signInLink);
//RootPanel.get("head_wrap_right").add(loginPanel);
//}
//
//private void loadMenu() {
//
//// Commands für die eonzelnen Menü Punkte und Buttons
//Command showUserSettings = new Command() {
//	public void execute() {
//		RootPanel.get("content_wrap").clear();
//		RootPanel.get("content_wrap").add(new UserSettingsView());
//	}
//};
//
//// Menübar anlegen
//MenuBar UserMenu = new MenuBar(true);
//UserMenu.addItem("Bauteil anlegen", showUserSettings);
//
//// Assemble signOutLink (Autor: Google Dokumentation)
//Anchor signOutLink = new Anchor("signout");
//signOutLink.setHref(loginInfo.getLogoutUrl());
//
//// Menü Rootpanel und dann Div Contanier zuordnen
//RootPanel.get("header_wrap").add(UserMenu);
//RootPanel.get("header_wrap").add(signOutLink);
//RootPanel.get("content_wrap").add(welcomeText);
//
//}
//
///**
//* Callbacks für den RPC Mechanismus
//*
//*/
//class LoginInfoCallback implements AsyncCallback<Void> {
//
//@Override
//public void onFailure(Throwable caught) {
//	loginInfo.setLoggedIn(false);
//}
//
//@Override
//public void onSuccess(Void result) {
//	// TODO Auto-generated method stub
//
//}
