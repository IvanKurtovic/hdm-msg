package de.hdm.gruppe2.client;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ChatOverview extends VerticalPanel {

	private String[] names = {"Chat1", "Chat2", "Chat3", "Chat4", "Chat5"};
	private String[] contacts = {"Serkan", "Ivan", "Cem", "Kerim", "Marina"};
	
	@Override
	public void onLoad() {
		
		final Grid mainGrid = new Grid(2,2);
		
		final ListBox chatNames = new ListBox();
		chatNames.setStyleName("listbox");
		chatNames.setVisibleItemCount(11);
		
		for(String s : names) {
			chatNames.addItem(s);
		}
		
		final Button btnCreateChat = new Button("Neuer Chat");
//TODO setStyleName für Neuer Chat Button 
		btnCreateChat.setStyleName("new-chat");
		btnCreateChat.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				DialogBox dialogBox = createChatDialog();
				dialogBox.show();
				dialogBox.center();
			}
		});
		final Button btnDeleteChat = new Button("Entfernen");
//TODO setStyleName für Entfernen Button		
		btnDeleteChat.setStyleName("delete-chat");
		final HorizontalPanel pnlCreateAndDeleteChat = new HorizontalPanel();
		pnlCreateAndDeleteChat.add(btnCreateChat);
		pnlCreateAndDeleteChat.add(btnDeleteChat);
		
		final TextBox tbMessage = new TextBox();
		
//TODO setStyleName für TextBox	
		tbMessage.setStyleName("textbox-chat");
		final Button btnSendMessage = new Button("Senden");
//TODO setStyleName für Senden Button 		
		btnSendMessage.setStyleName("send-chat");
		
		final HorizontalPanel pnlSendMessage = new HorizontalPanel();
		pnlSendMessage.add(tbMessage);
		pnlSendMessage.add(btnSendMessage);
		
		mainGrid.setWidget(0, 0, chatNames);
		mainGrid.setWidget(1, 0, pnlCreateAndDeleteChat);
		mainGrid.setWidget(1, 1, pnlSendMessage);		
		
		this.add(mainGrid);
	}
	
	private DialogBox createChatDialog() {
		
		final DialogBox dialogBox = new DialogBox();
		dialogBox.setStyleName("popup-dialogbox-chat");
		dialogBox.setGlassEnabled(true);
		dialogBox.setAnimationEnabled(true);
		
		final Grid mainGrid = new Grid(4, 2);
		
		final Label lblTitle = new Label("Neuer Chat");
		lblTitle.setStyleName("popup-title-chat");
		final Label lblContacts = new Label("Kontakte: ");
		lblContacts.setStyleName("popup-contacts-chat");
		final Label lblRecipients = new Label("Empfaenger: ");
		lblRecipients.setStyleName("popup-recipients-chat");
		final TextBox tbRecipients = new TextBox();
		
		final HorizontalPanel pnlRecipients = new HorizontalPanel();
		pnlRecipients.add(lblRecipients);
		pnlRecipients.add(tbRecipients);
		
		final ListBox recipientList = new ListBox();
//TODO setStyleName popup-listbox 		
		recipientList.setStyleName("popup-listbox-chat");
		recipientList.setMultipleSelect(true);
		recipientList.setVisibleItemCount(11);
		recipientList.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				String sRecipients = "Changed!";
				
				tbRecipients.setText(sRecipients);
			}
		});
		
		for(String s : contacts) {
			recipientList.addItem(s);
		}
		
		final TextArea taTextField = new TextArea();
		
		final TextBox tbSearchField = new TextBox();
		final Button btnSearch = new Button("Suchen");
		
		final HorizontalPanel pnlSearchControls = new HorizontalPanel();
		pnlSearchControls.add(tbSearchField);
		pnlSearchControls.add(btnSearch);
		
		final Button btnSendMessage = new Button("Senden");
		final Button btnCancel = new Button("Abbruch");
		btnCancel.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				dialogBox.hide();
			}
		});
		
		final HorizontalPanel pnlMainControls = new HorizontalPanel();
		pnlMainControls.add(btnSendMessage);
		pnlMainControls.add(btnCancel);
		
		mainGrid.setWidget(0, 0, lblTitle);
		mainGrid.setWidget(1, 0, lblContacts);
		mainGrid.setWidget(1, 1, pnlRecipients);
		mainGrid.setWidget(2, 0, recipientList);
		mainGrid.setWidget(2, 1, taTextField);
		mainGrid.setWidget(3, 0, pnlSearchControls);
		mainGrid.setWidget(3, 1, pnlMainControls);
		
		dialogBox.add(mainGrid);
		
		return dialogBox;
	}
}

