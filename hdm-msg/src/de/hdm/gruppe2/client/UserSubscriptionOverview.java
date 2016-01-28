package de.hdm.gruppe2.client;

import java.util.ArrayList;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

import de.hdm.gruppe2.shared.HashtagParser;
import de.hdm.gruppe2.shared.MsgServiceAsync;
import de.hdm.gruppe2.shared.bo.Chat;
import de.hdm.gruppe2.shared.bo.Hashtag;
import de.hdm.gruppe2.shared.bo.HashtagSubscription;
import de.hdm.gruppe2.shared.bo.Message;
import de.hdm.gruppe2.shared.bo.User;
import de.hdm.gruppe2.shared.bo.UserSubscription;

public class UserSubscriptionOverview extends VerticalPanel {
	
	private MsgServiceAsync msgSvc = ClientsideSettings.getMsgService();
	private ArrayList<User> allUsers = null;
	private ArrayList<UserSubscription> userSubscriptions = null;
	private ArrayList<Message> allMessagesOfSubscription = null;
	private UserSubscription selectedSubscription = null;
	private User loggedInUser = null;
	
	private final ListBox userList = new ListBox();
	private final ListBox subscriptionsList = new ListBox();
	private final FlexTable ftPosts = new FlexTable();
	
	public UserSubscriptionOverview(User currentUser) {
		this.loggedInUser = currentUser;
	}
	
	@Override
	public void onLoad() {
		
		this.getAllUserSubscriptions();
		this.getAllUsers();
		
		final Grid mainGrid = new Grid(2, 2);
		
		subscriptionsList.setStyleName("listbox");
		subscriptionsList.setVisibleItemCount(11);
		subscriptionsList.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				if(subscriptionsList.getSelectedIndex() == -1) {
					ClientsideSettings.getLogger().severe("Kein Abo ausgewählt.");
					return;
				}
				
				selectedSubscription = userSubscriptions.get(subscriptionsList.getSelectedIndex());
				getAllSubscriptionPosts(selectedSubscription);	
			}
		});
		
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
		
		final Button btnRefresh = new Button("Refresh");
		btnRefresh.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				getAllUsers();
				getAllUserSubscriptions();
			}
		});
		
		final Button btnUnsubscribe = new Button("Deabonnieren");
		btnUnsubscribe.setStyleName("unsubs-usersubs");
		btnUnsubscribe.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if(subscriptionsList.getSelectedIndex() == -1) {
					ClientsideSettings.getLogger().severe("Kein Abo ausgewählt.");
					return;
				}
				
				unsubscribe(selectedSubscription);
			}
			
		});
		
		final HorizontalPanel pnlSubscribeAndUnsubscribe = new HorizontalPanel();
		pnlSubscribeAndUnsubscribe.add(btnNewSubscription);
		pnlSubscribeAndUnsubscribe.add(btnRefresh);
		pnlSubscribeAndUnsubscribe.add(btnUnsubscribe);
		
		mainGrid.setWidget(0, 0, subscriptionsList);
		mainGrid.setWidget(0, 1, ftPosts);
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
				dialogBox.hide();
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
	
	private DialogBox replyDialog(Message post, final User replyUser) {
		
		final DialogBox dialogBox = new DialogBox();
		dialogBox.setGlassEnabled(true);
		dialogBox.setAnimationEnabled(true);
		
		final Grid mainGrid = new Grid(3,1);
		
		final Label lblTitle = new Label("Anworten");
		lblTitle.addStyleName("popup-title");
		
		final Grid messageInfo = new Grid(1, 3);
		messageInfo.setText(0, 0, replyUser.getNickname());
		messageInfo.setText(0, 1, post.getText());
		messageInfo.setText(0, 2, post.getCreationDate().toString());
		
		final TextBox tbReplyText = new TextBox();
		final Button btnSendReply = new Button("Antworten");
		btnSendReply.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				String replyText = tbReplyText.getText();
				
				if(replyText.isEmpty()) {
					ClientsideSettings.getLogger().severe("Keine Nachricht eingegeben.");
					return;
				}
				
				createRecipientChatAndSendReply(replyUser, replyText);
				dialogBox.hide();
			}
		});
		
		final Button btnCancel = new Button("Abbrechen");
		btnCancel.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				dialogBox.hide();
			}
		});
		
		final HorizontalPanel pnlReply = new HorizontalPanel();
		pnlReply.add(tbReplyText);
		pnlReply.add(btnSendReply);
		pnlReply.add(btnCancel);
		
		mainGrid.setWidget(0, 0, lblTitle);
		mainGrid.setWidget(1, 0, messageInfo);
		mainGrid.setWidget(2, 0, pnlReply);
		
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
				
				userList.clear();
				
				for(User u : allUsers) {
					userList.addItem(u.getNickname());
				}
				
				ClientsideSettings.getLogger().finest("User wurden geladen.");
			}
		});
	}
	
	private void getAllUserSubscriptions() {
		msgSvc.findAllUserSubscriptionsOfUser(loggedInUser, new AsyncCallback<ArrayList<UserSubscription>> () {

			@Override
			public void onFailure(Throwable caught) {
				ClientsideSettings.getLogger().severe("UserSubscriptions konnten nicht geladen werden.");
			}

			@Override
			public void onSuccess(ArrayList<UserSubscription> result) {
				userSubscriptions = result;
				
				subscriptionsList.clear();
				
				for(UserSubscription us : userSubscriptions) {
					for(User u : allUsers) {
						if(u.getId() == us.getSenderId()) {
							subscriptionsList.addItem(u.getNickname());
						}
					}
				}
				
				ClientsideSettings.getLogger().finest("UserSubscriptions wurden geladen.");	
			}
			
		});
	}
	
	private void subscribe(User user) {
		msgSvc.createUserSubscription(user, loggedInUser, new AsyncCallback<Void>() {

			@Override
			public void onFailure(Throwable caught) {
				ClientsideSettings.getLogger().severe("UserSubscription konnte nicht angelegt werden.");
			}

			@Override
			public void onSuccess(Void result) {
				getAllUserSubscriptions();
				ClientsideSettings.getLogger().finest("UserSubscription wurde angelegt.");	
			}
		});
	}
	
	private void unsubscribe(UserSubscription us) {
		msgSvc.deleteUserSubscription(selectedSubscription, new AsyncCallback<Void>() {

			@Override
			public void onFailure(Throwable caught) {
				ClientsideSettings.getLogger().severe("Abonnement konnte nicht gekündigt werden.");
			}

			@Override
			public void onSuccess(Void result) {
				getAllUserSubscriptions();
				ClientsideSettings.getLogger().finest("Abonnement wurde erfolgreich gekündigt.");
			}
			
		});
	}
	
	private void getAllSubscriptionPosts(UserSubscription us) {
		msgSvc.findAllPostsOfUser(us.getSenderId(), new AsyncCallback<ArrayList<Message>> () {

			@Override
			public void onFailure(Throwable caught) {
				ClientsideSettings.getLogger().severe("Posts konnten nicht geladen werden.");
			}

			@Override
			public void onSuccess(ArrayList<Message> result) {
				allMessagesOfSubscription = result;
				
				ftPosts.clear(true);
				ftPosts.removeAllRows();
				
				int numOfRows = ftPosts.getRowCount();
				
				for(Message m : allMessagesOfSubscription) {
					
					numOfRows++;
					
					final Button btnReply = new Button("Antworten");
					btnReply.addClickHandler(new ClickHandler() {

						@Override
						public void onClick(ClickEvent event) {
							int rowIndex = ftPosts.getCellForEvent(event).getRowIndex();
							int messageIndex = rowIndex - 1;
							Message replyPost = allMessagesOfSubscription.get(messageIndex);
							User replyUser = null;
							
							for(User u : allUsers) {
								if(u.getId() == replyPost.getUserId()) {
									replyUser = u;
								}
							}

							DialogBox dialogBox = replyDialog(replyPost, replyUser);
							dialogBox.setStyleName("dialogbox-usersubs");
							dialogBox.show();
							dialogBox.center();
						}
					});
					
					ftPosts.setText(numOfRows, 0, Integer.toString(m.getUserId()));
					ftPosts.setText(numOfRows, 1, m.getText());
					ftPosts.setText(numOfRows, 2, m.getCreationDate().toString());
					ftPosts.setWidget(numOfRows, 3, btnReply);

				}
				
				ClientsideSettings.getLogger().finest("Posts wurden geladen.");
			}	
		});
	}
	
	private void createRecipientChatAndSendReply(User recipient, final String text) {
		// Da es nur zwischen dem Poster und dem User laufen soll brauchen
		// wir nur die beiden als Empfänger.
		ArrayList<User> chatParticipants = new ArrayList<User>();
		chatParticipants.add(recipient);
		chatParticipants.add(loggedInUser);
		
		msgSvc.createChat(chatParticipants, new AsyncCallback<Chat>() {

			@Override
			public void onFailure(Throwable caught) {
				ClientsideSettings.getLogger().severe("Chat konnte nicht gefunden oder angelegt werden.");
			}

			@Override
			public void onSuccess(Chat result) {
				if(result != null) {
					sendReplyToUser(result, text);		
				}
				ClientsideSettings.getLogger().finest("Chat gefunden, Antwort wird vorbereitet...");
			}
			
		});
	}

	private void sendReplyToUser(Chat recipient, String text) {
		msgSvc.sendMessage(text, loggedInUser, recipient, HashtagParser.checkForHashtags(text), new AsyncCallback<Message>() {

			@Override
			public void onFailure(Throwable caught) {
				ClientsideSettings.getLogger().severe("Antwort konnte nicht versendet werden.");
			}

			@Override
			public void onSuccess(Message result) {
				ClientsideSettings.getLogger().finest("Antwort erfolgreich versendet.");
			}
			
		});
	}
}
