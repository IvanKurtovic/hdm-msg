package de.hdm.gruppe2.client;

import java.util.ArrayList;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

import de.hdm.gruppe2.shared.MsgServiceAsync;
import de.hdm.gruppe2.shared.bo.User;

public class UserSubscriptionOverview extends VerticalPanel {
	
	private MsgServiceAsync msgSvc = ClientsideSettings.getMsgService();
	private ArrayList<User> allUsers = null;
	private User loggedInUser = null;
	
	private final ListBox userList = new ListBox();
	private final ListBox userSubscriptionList = new ListBox();
	
	public UserSubscriptionOverview(User currentUser) {
		this.loggedInUser = currentUser;
	}
	
	@Override
	public void onLoad() {
		
		this.getAllUsers();
		
		final Grid mainGrid = new Grid(2, 2);
		
		userSubscriptionList.setStyleName("listbox");
		userSubscriptionList.setVisibleItemCount(11);
		
		final Button btnNewSubscription = new Button("Neues Abonnement");
		btnNewSubscription.setStyleName("newsubs-usersubs");
		btnNewSubscription.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				DialogBox dialogBox = subscriptionDialog();
				dialogBox.setStyleName("dialogbox-usersubs");
				dialogBox.show();
				dialogBox.center();
			}
		});
		final Button btnUnsubscribe = new Button("Deabonnieren");
		btnUnsubscribe.setStyleName("unsubs-usersubs");
		
		final HorizontalPanel pnlSubscribeAndUnsubscribe = new HorizontalPanel();
		pnlSubscribeAndUnsubscribe.add(btnNewSubscription);
		pnlSubscribeAndUnsubscribe.add(btnUnsubscribe);
		
		mainGrid.setWidget(0, 0, userSubscriptionList);
		mainGrid.setWidget(1, 0, pnlSubscribeAndUnsubscribe);
		
		this.add(mainGrid);		
	}
	
	private DialogBox subscriptionDialog() {
		
		final DialogBox dialogBox = new DialogBox();
		dialogBox.setGlassEnabled(true);
		dialogBox.setAnimationEnabled(true);
		
		final Grid mainGrid = new Grid(3,1);
		
		final Label lblTitle = new Label("Neues User-Abo");
		lblTitle.addStyleName("popup-title");

		userList.setStyleName("listbox-usersubs");
		userList.setVisibleItemCount(5);
		
		final Button btnSubscribe = new Button("Abonnieren");
		btnSubscribe.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if(userList.getSelectedIndex() == -1) {
					ClientsideSettings.getLogger().severe("Kein User ausgewählt.");
					return;
				}
				
				subscribe(allUsers.get(userList.getSelectedIndex()));
			}
		});
		
		final Button btnCancel = new Button("Abbrechen");
		btnCancel.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				dialogBox.hide();
			}
		});
		
		final HorizontalPanel pnlSubscriptionControls = new HorizontalPanel();
		pnlSubscriptionControls.add(btnSubscribe);
		pnlSubscriptionControls.add(btnCancel);
		
		mainGrid.setWidget(0, 0, lblTitle);
		mainGrid.setWidget(1, 0, userList);
		mainGrid.setWidget(2, 0, pnlSubscriptionControls);
		
		dialogBox.add(mainGrid);
		
		return dialogBox;		
	}
	
	private void getAllUsers() {
		msgSvc.findAllUserWithoutLoggedInUser(loggedInUser, new AsyncCallback<ArrayList<User>>() {

			@Override
			public void onFailure(Throwable caught) {
				ClientsideSettings.getLogger().severe("User konnten nicht geladen werden.");
			}

			@Override
			public void onSuccess(ArrayList<User> result) {
				allUsers = result;
				
				for(User u : allUsers) {
					userList.addItem(u.getNickname());
				}
				
				ClientsideSettings.getLogger().finest("User wurden geladen.");
			}
			
		});
	}
	
	private void subscribe(User user) {
		// TODO: Methode zum abonnieren des Nutzers.
	}

}
