package de.hdm.gruppe2.client;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ChatOverview extends VerticalPanel {

	private String[] names = {"Chat1", "Chat2", "Chat3", "Chat4", "Chat5"};
	
	@Override
	public void onLoad() {
		
		final Grid mainGrid = new Grid(2,2);
		
		final ListBox chatNames = new ListBox();
		chatNames.setVisibleItemCount(11);
		
		for(String s : names) {
			chatNames.addItem(s);
		}
		
		final Button btnCreateChat = new Button("Neuer Chat");
		final Button btnDeleteChat = new Button("Entfernen");
		
		final HorizontalPanel pnlCreateAndDeleteChat = new HorizontalPanel();
		pnlCreateAndDeleteChat.add(btnCreateChat);
		pnlCreateAndDeleteChat.add(btnDeleteChat);
		
		final TextBox tbMessage = new TextBox();
		final Button btnSendMessage = new Button("Senden");
		
		final HorizontalPanel pnlSendMessage = new HorizontalPanel();
		pnlSendMessage.add(tbMessage);
		pnlSendMessage.add(btnSendMessage);
		
		mainGrid.setWidget(0, 0, chatNames);
		mainGrid.setWidget(1, 0, pnlCreateAndDeleteChat);
		mainGrid.setWidget(1, 1, pnlSendMessage);		
		
		this.add(mainGrid);
	}
}
