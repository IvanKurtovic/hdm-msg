package de.hdm.gruppe2.client;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class HashtagSubscriptionOverview extends VerticalPanel {
	
	private String[] hashtags = {"thies", "itprojekt", "ibne2015", "banane", "up0rn"};
	
	@Override
	public void onLoad() {
		
		final Grid mainGrid = new Grid(2, 2);
		
		final ListBox hashtagList = new ListBox();
		hashtagList.setVisibleItemCount(11);
		
		for(String s : hashtags) {
			hashtagList.addItem("#" + s);
		}
		
		final Button btnNewSubscription = new Button("Neues Abonnement");
		final Button btnUnsubscribe = new Button("Deabonnieren");
		
		final HorizontalPanel pnlSubscribeAndUnsubscribe = new HorizontalPanel();
		pnlSubscribeAndUnsubscribe.add(btnNewSubscription);
		pnlSubscribeAndUnsubscribe.add(btnUnsubscribe);
		
		mainGrid.setWidget(0, 0, hashtagList);
		mainGrid.setWidget(1, 0, pnlSubscribeAndUnsubscribe);
		
		this.add(mainGrid);		
	}
}
