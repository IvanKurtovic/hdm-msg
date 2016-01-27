package de.hdm.gruppe2.client;

import java.util.ArrayList;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
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
import de.hdm.gruppe2.shared.bo.Chat;
import de.hdm.gruppe2.shared.bo.User;

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
		
		this.getAllChatsOfUser();
		this.getAllContacts(loggedInUser);
		
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
		
		final Button btnRefreshMyChats = new Button("Meine Chats laden");
		btnRefreshMyChats.addStyleName("refresh-user");
		btnRefreshMyChats.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				getAllChatsOfUser();
			}
		});
		
		final Button btnLoadAllChats = new Button("Alle Chats laden");
		btnRefreshMyChats.addStyleName("refresh-user");
		btnLoadAllChats.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				getAllChats();
			}
		});
		
		final Button btnLeaveChat = new Button("Austreten");
		btnLeaveChat.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				
				if(chatNames.getSelectedIndex() == -1) {
					ClientsideSettings.getLogger().severe("Kein Chat ausgewählt.");
					return;
				}
				
				// Jetzt erst wird der User aus der chatparticipants Liste entfernt
				leaveChat(chats.get(chatNames.getSelectedIndex()), loggedInUser);
				
			}
			
		});
		
		final Button btnDeleteChat = new Button("Entfernen");	
		btnDeleteChat.setStyleName("delete-chat");
		btnDeleteChat.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				
				if(chatNames.getSelectedIndex() == -1) {
					ClientsideSettings.getLogger().severe("Kein Chat ausgewählt.");
					return;
				}
				
				// Jetzt erst wird der User aus der Datenbank entfernt
				deleteChat(chats.get(chatNames.getSelectedIndex()));
			}
			
		});
		
		final HorizontalPanel pnlChatControls = new HorizontalPanel();
		pnlChatControls.add(btnCreateChat);
		pnlChatControls.add(btnRefreshMyChats);
		pnlChatControls.add(btnLoadAllChats);
		pnlChatControls.add(btnLeaveChat);
		pnlChatControls.add(btnDeleteChat);
		
		final TextBox tbMessage = new TextBox();
		tbMessage.setStyleName("textbox-chat");
		final Button btnSendMessage = new Button("Senden");	
		btnSendMessage.setStyleName("send-chat");
		
		final HorizontalPanel pnlSendMessage = new HorizontalPanel();
		pnlSendMessage.add(tbMessage);
		pnlSendMessage.add(btnSendMessage);
		
		mainGrid.setWidget(0, 0, chatNames);
		mainGrid.setWidget(1, 0, pnlChatControls);
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
			recipientList.addItem(u.getNickname());
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
	
	private void getAllContacts(User loggedInUser) {
		msgSvc.findAllUserWithoutLoggedInUser(loggedInUser, new AsyncCallback<ArrayList<User>>() {

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
					chatNames.addItem(c.getId() + " " + c.getName());
				}
				
				ClientsideSettings.getLogger().finest("Chats wurden geladen.");
			}

		});
	}
	
	private void getAllChatsOfUser() {
		msgSvc.findAllChatsOfUser(loggedInUser, new AsyncCallback<ArrayList<Chat>>() {

			@Override
			public void onFailure(Throwable caught) {
				ClientsideSettings.getLogger().severe("Chats konnten nicht geladen werden.");
			}

			@Override
			public void onSuccess(ArrayList<Chat> result) {
				chats = result;
				
				chatNames.clear();
				
				for(Chat c : chats) {
					chatNames.addItem(c.getId() + " " + c.getName());
				}
				
				ClientsideSettings.getLogger().finest("Chats wurden geladen.");
			}

		});
	}
	
	private void leaveChat(Chat chat, User currentUser) {
		msgSvc.removeChatParticipant(chat, currentUser,new AsyncCallback<Void>() {

			@Override
			public void onFailure(Throwable caught) {
				ClientsideSettings.getLogger().severe("Es konnte nicht aus dem Chat ausgetreten werden.");
			}

			@Override
			public void onSuccess(Void result) {
				
				// Entfernen des Users aus der Liste
				chatNames.removeItem(chatNames.getSelectedIndex());
				// Entfernen des User-Objekts aus der users Liste
				chats.remove(chatNames.getSelectedIndex());
				
				ClientsideSettings.getLogger().finest("Es wurde aus dem Chat ausgetreten.");				
			}
			
		});
	}
	
	private void deleteChat(Chat chat) {
		msgSvc.deleteChat(chat, new AsyncCallback<Void> () {

			@Override
			public void onFailure(Throwable caught) {
				ClientsideSettings.getLogger().severe("Der Chat konnte nicht gelöscht werden.");
			}

			@Override
			public void onSuccess(Void result) {
				
				// Entfernen des Users aus der Liste
				chatNames.removeItem(chatNames.getSelectedIndex());
				// Entfernen des User-Objekts aus der users Liste
				chats.remove(chatNames.getSelectedIndex());
				
				ClientsideSettings.getLogger().finest("Der Chat wurde gelöscht.");
			}
			
		});
	}
}