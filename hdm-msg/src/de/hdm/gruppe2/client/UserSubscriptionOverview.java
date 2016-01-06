package de.hdm.gruppe2.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class UserSubscriptionOverview extends VerticalPanel {
	
private String[] users = {"cem", "ivan", "serkan", "marina", "kerim", "thies"};
	
	@Override
	public void onLoad() {
		
		final Grid mainGrid = new Grid(2, 2);
		
		final ListBox userList = new ListBox();
		userList.setStyleName("listbox");
		userList.setVisibleItemCount(11);
		
		for(String s : users) {
			userList.addItem(s);
		}
		
		final Button btnNewSubscription = new Button("Neues Abonnement");
		btnNewSubscription.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				DialogBox dialogBox = subscriptionDialog();
				dialogBox.show();
				dialogBox.center();
			}
		});
		final Button btnUnsubscribe = new Button("Deabonnieren");
		
		final HorizontalPanel pnlSubscribeAndUnsubscribe = new HorizontalPanel();
		pnlSubscribeAndUnsubscribe.add(btnNewSubscription);
		pnlSubscribeAndUnsubscribe.add(btnUnsubscribe);
		
		mainGrid.setWidget(0, 0, userList);
		mainGrid.setWidget(1, 0, pnlSubscribeAndUnsubscribe);
		
		this.add(mainGrid);		
	}
	
	private DialogBox subscriptionDialog() {
		
		final DialogBox dialogBox = new DialogBox();
		dialogBox.setGlassEnabled(true);
		dialogBox.setAnimationEnabled(true);
		
		final Grid mainGrid = new Grid(4,1);
		
		final Label lblTitle = new Label("Neues User-Abo");
		lblTitle.addStyleName("popup-title");
		final TextBox tbSearch = new TextBox();
		final Button btnSearch = new Button("Suchen");
		
		final HorizontalPanel pnlSearchControls = new HorizontalPanel();
		pnlSearchControls.add(tbSearch);
		pnlSearchControls.add(btnSearch);
		
		final ListBox userList = new ListBox();
		userList.setStyleName("listbox");
		userList.setVisibleItemCount(5);
		
		final Button btnSubscribe = new Button("Abonnieren");
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
		mainGrid.setWidget(1, 0, pnlSearchControls);
		mainGrid.setWidget(2, 0, userList);
		mainGrid.setWidget(3, 0, pnlSubscriptionControls);
		
		dialogBox.add(mainGrid);
		
		return dialogBox;		
	}

}
