package de.hdm.gruppe2.client;

import java.util.ArrayList;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

import de.hdm.gruppe2.shared.MsgService;
import de.hdm.gruppe2.shared.MsgServiceAsync;
import de.hdm.gruppe2.shared.bo.Chat;
import de.hdm.gruppe2.shared.bo.User;
import de.hdm.gruppe2.shared.bo.Message;

public class ChatOverview extends VerticalPanel {

	private MsgServiceAsync msgSvc = ClientsideSettings.getMsgService();
	private User loggedInUser = null;
	private ArrayList<Chat> chats = null;
	private ArrayList<User> contacts = null;
	
	private final ListBox chatNames = new ListBox();
	
	public ChatOverview(User loggedInUser) {
		this.loggedInUser = loggedInUser;
	}
	
	@Override
	public void onLoad() {
		
		this.getAllChats();
		this.getAllContacts();
		
		final Grid mainGrid = new Grid(2,2);
		
		chatNames.setStyleName("listbox");
		chatNames.setVisibleItemCount(11);
		
		final Button btnCreateChat = new Button("Neuer Chat");
		btnCreateChat.setStyleName("new-chat");
		btnCreateChat.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				DialogBox dialogBox = createChatDialog();
				dialogBox.show();
				dialogBox.center();
			}
		});
		
		final Button btnRefresh = new Button("Refresh");
		btnRefresh.addStyleName("refresh-user");
		btnRefresh.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				getAllChats();
			}
			
		});
		
		final Button btnDeleteChat = new Button("Entfernen");	
		btnDeleteChat.setStyleName("delete-chat");
		final HorizontalPanel pnlCreateAndDeleteChat = new HorizontalPanel();
		pnlCreateAndDeleteChat.add(btnCreateChat);
		pnlCreateAndDeleteChat.add(btnRefresh);
		pnlCreateAndDeleteChat.add(btnDeleteChat);
		
		final TextBox tbMessage = new TextBox();
		tbMessage.setStyleName("textbox-chat");
		final Button btnSendMessage = new Button("Senden");	
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
		
		// Alle User in die Liste einfügen.
		for(User u : contacts) {
			recipientList.addItem(u.getFirstName() + " " + u.getLastName());
		}	
		
		final Button btnCreateChat = new Button("Chat Anlegen");
		btnCreateChat.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				
				if(recipientList.getSelectedIndex() == -1 ) {
					ClientsideSettings.getLogger().info("No Recipient selected.");
					return;
				}
				
				ArrayList<User> selectedRecipients = new ArrayList<User>();
				
				for(int i = 0; i <= recipientList.getItemCount() - 1; i++) {
					if(recipientList.isItemSelected(i)) {
						if(contacts.get(i).getEmail() != loggedInUser.getEmail()) {
							selectedRecipients.add(contacts.get(i));
						}
					}
				}
				
				// Der User selbst muss auch in der Empfängerliste eingetragen sein.
				selectedRecipients.add(loggedInUser);
				createChat(selectedRecipients);				
				dialogBox.hide();
			}
			
		});
		
		final Button btnCancel = new Button("Abbruch");
		btnCancel.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				dialogBox.hide();
			}
		});
		
		final HorizontalPanel pnlMainControls = new HorizontalPanel();
		pnlMainControls.add(btnCreateChat);
		pnlMainControls.add(btnCancel);
		
		mainGrid.setWidget(0, 0, lblTitle);
		mainGrid.setWidget(1, 0, lblContacts);
		mainGrid.setWidget(2, 0, recipientList);
		mainGrid.setWidget(3, 0, pnlMainControls);
		
		dialogBox.add(mainGrid);
		
		return dialogBox;
	}
	
	private void getAllContacts() {
		msgSvc.findAllUser(new AsyncCallback<ArrayList<User>>() {

			@Override
			public void onFailure(Throwable caught) {
				ClientsideSettings.getLogger().severe("User konnten nicht geladen werden.");
			}

			@Override
			public void onSuccess(ArrayList<User> result) {
				contacts = result;
				
				ClientsideSettings.getLogger().finest("User geladen.");
			}
		});
	}
	
	private void createChat(ArrayList<User> selectedRecipients) {
		msgSvc.createChat(selectedRecipients, new AsyncCallback<Chat>() {

			@Override
			public void onFailure(Throwable caught) {
				ClientsideSettings.getLogger().severe("Chat konnte nicht angelegt werden.");
			}

			@Override
			public void onSuccess(Chat result) {
				ClientsideSettings.getLogger().finest("Chat wurde angelegt.");
			}
		});
	}
	
	private void getAllChats() {
		msgSvc.findAllChats(new AsyncCallback<ArrayList<Chat>>() {

			@Override
			public void onFailure(Throwable caught) {
				ClientsideSettings.getLogger().severe("Chats konnten nicht geladen werden.");
			}

			@Override
			public void onSuccess(ArrayList<Chat> result) {
				chats = result;
				
				chatNames.clear();
				
				for(Chat c : chats) {
					chatNames.addItem(c.getName());
				}
				
				ClientsideSettings.getLogger().finest("Chats wurden geladen.");
			}

		});
	}
}

