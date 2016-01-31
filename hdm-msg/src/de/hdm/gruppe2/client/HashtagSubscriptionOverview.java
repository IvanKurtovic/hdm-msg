package de.hdm.gruppe2.client;

import java.util.ArrayList;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
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

import de.hdm.gruppe2.shared.MsgServiceAsync;
import de.hdm.gruppe2.shared.bo.Hashtag;
import de.hdm.gruppe2.shared.bo.HashtagSubscription;
import de.hdm.gruppe2.shared.bo.Message;
import de.hdm.gruppe2.shared.bo.User;

/**
 * Diese Klasse repräsentiert die Hashtag Abo Übersicht. Es enthält alle
 * momentan in der Datenbank befindlichen Hashtag Abos des momentan eingeloggten Nutzers. 
 * Der Nutzer kann jeden vorhandenen Hashtag über ein PopUp Dialog abonnieren. Ebenfalls
 * kann er im PopUp Dialog manuell Hashtags anlegen oder löschen.
 * Die notwendigen Methoden zum abonnieren und deabonnieren erhält diese 
 * Klasse über den MsgService.
 * 
 * @author Yilmaz
 * @author Sari
 *
 */
public class HashtagSubscriptionOverview extends VerticalPanel {
	
	private MsgServiceAsync msgSvc = ClientsideSettings.getMsgService();
	private ArrayList<HashtagSubscription> hashtagSubscriptions = null;
	private ArrayList<Message> allMessagesOfSubscription = null;
	private ArrayList<Hashtag> allHashtags = null;
	private HashtagSubscription selectedSubscription = null;
	private User loggedInUser = null;
	
	private final ListBox subscriptionsList = new ListBox();
	private final ListBox allHashtagsList = new ListBox();
	private final FlexTable ftPosts = new FlexTable();
	
	public HashtagSubscriptionOverview(User currentUser) {
		this.loggedInUser = currentUser;
	}
	
	@Override
	public void onLoad() {
		
		this.getAllSubscriptions();
		this.getAllHashtags();
		
		final Grid mainGrid = new Grid(3, 2);
		
		Label lblTitle = new Label("Hashtag Abonnements");
		lblTitle.addStyleName("navigation-title");
		
		subscriptionsList.setStyleName("listbox");
		subscriptionsList.setVisibleItemCount(11);
		subscriptionsList.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				if(subscriptionsList.getSelectedIndex() == -1) {
					ClientsideSettings.getLogger().severe("Kein Abo ausgewählt.");
					return;
				}
				
				selectedSubscription = hashtagSubscriptions.get(subscriptionsList.getSelectedIndex());
				getAllSubscriptionPosts(selectedSubscription);
			}
		});
		
		final Button btnNewSubscription = new Button("Neues Abonnement");
		btnNewSubscription.setStyleName("newabo-hashtagabo");
		btnNewSubscription.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				DialogBox dialogBox = subscriptionDialog();
				dialogBox.setStyleName("dialogbox-hashtagabo");
				dialogBox.show();
				dialogBox.center();
			}
		});
		
		final Button btnRefresh = new Button("Refresh");
		btnRefresh.addStyleName("refresh-hashtagabo");
		btnRefresh.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				getAllHashtags();
				getAllSubscriptions();
			}
			
		});
		
		final Button btnUnsubscribe = new Button("Deabonnieren");
		btnUnsubscribe.setStyleName("unsubs-hashtagabo");
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
		
		mainGrid.setWidget(0, 0, lblTitle);
		mainGrid.setWidget(1, 0, subscriptionsList);
		mainGrid.setWidget(1, 1, ftPosts);
		mainGrid.setWidget(2, 0, pnlSubscribeAndUnsubscribe);
		
		this.add(mainGrid);		
	}
	
	private DialogBox subscriptionDialog() {
		
		final DialogBox dialogBox = new DialogBox();
		dialogBox.setGlassEnabled(true);
		dialogBox.setAnimationEnabled(true);
		
		final Grid mainGrid = new Grid(4,1);
		
		final Label lblTitle = new Label("Neues Hashtag-Abo");
		lblTitle.addStyleName("popup-title");
		final TextBox tbCreateHashtag = new TextBox();
		final Button btnCreateHashtag = new Button("Hashtag anlegen");
		btnCreateHashtag.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				String keyword = tbCreateHashtag.getText();				
				keyword = keyword.replaceAll("#", " ");
				keyword = keyword.trim();
				
				if(!keyword.isEmpty()) {
					createHashtag(keyword);
				}
			}
		});
		
		final Button btnDeleteHashtag = new Button("Hashtag entfernen");
		btnDeleteHashtag.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if(allHashtagsList.getSelectedIndex() == -1) {
					ClientsideSettings.getLogger().severe("Kein Hashtag ausgewählt.");
					return;
				}
				
				deleteHashtag(allHashtags.get(allHashtagsList.getSelectedIndex()));
			}
		});
		
		final HorizontalPanel pnlSearchControls = new HorizontalPanel();
		pnlSearchControls.add(tbCreateHashtag);
		pnlSearchControls.add(btnCreateHashtag);
		pnlSearchControls.add(btnDeleteHashtag);

		allHashtagsList.setVisibleItemCount(15);
		allHashtagsList.setWidth("100%");
		
		final Button btnSubscribe = new Button("Abonnieren");
		btnSubscribe.setStyleName("popup-abo-hashtagabo");
		btnSubscribe.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if(allHashtagsList.getSelectedIndex() == -1) {
					ClientsideSettings.getLogger().severe("Kein Hashtag ausgewählt.");
					return;
				}
				
				subscribe(allHashtags.get(allHashtagsList.getSelectedIndex()));
				dialogBox.hide();
			}
		});		

		final Button btnCancel = new Button("Abbrechen");
		btnCancel.setStyleName("popup-cancel-hashtagabo");
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
		mainGrid.setWidget(2, 0, allHashtagsList);
		mainGrid.setWidget(3, 0, pnlSubscriptionControls);
		
		dialogBox.add(mainGrid);
		
		return dialogBox;		
	}
	
	private void subscribe(Hashtag hashtag) {
		msgSvc.createHashtagSubscription(hashtag, loggedInUser, new AsyncCallback<Void>() {

			@Override
			public void onFailure(Throwable caught) {
				ClientsideSettings.getLogger().severe("Abonnement konnte nicht eingerichtet werden.");
			}

			@Override
			public void onSuccess(Void result) {
				getAllSubscriptions();
				ClientsideSettings.getLogger().finest("Abonnement wurde erfolgreich eingerichtet.");
			}
		});
	}
	
	
	private void unsubscribe(HashtagSubscription hs) {		
		msgSvc.deleteHashtagSubscription(hs, new AsyncCallback<Void>() {

			@Override
			public void onFailure(Throwable caught) {
				ClientsideSettings.getLogger().severe("Abonnement konnte nicht gekündigt werden.");
			}

			@Override
			public void onSuccess(Void result) {
				getAllSubscriptions();
				ClientsideSettings.getLogger().finest("Abonnement wurde erfolgreich gekündigt.");
			}
			
		});
	}
	
	private void getAllHashtags() {
		msgSvc.findAllHashtags(new AsyncCallback<ArrayList<Hashtag>>() {

			@Override
			public void onFailure(Throwable caught) {
				ClientsideSettings.getLogger().severe("Hashtags konnten nicht geladen werden.");
			}

			@Override
			public void onSuccess(ArrayList<Hashtag> result) {
				allHashtags = result;
				
				allHashtagsList.clear();
				
				for(Hashtag h : allHashtags) {
					allHashtagsList.addItem("#" + h.getKeyword());
				}
				
				ClientsideSettings.getLogger().finest("Alle Hashtags geladen.");
			}
		});
	}
	
	private void getAllSubscriptions() {
		msgSvc.findAllHashtagSubscriptionsOfUser(loggedInUser, new AsyncCallback<ArrayList<HashtagSubscription>> () {

			@Override
			public void onFailure(Throwable caught) {
				ClientsideSettings.getLogger().severe("Abonnements konnten nicht geladen werden.");
			}

			@Override
			public void onSuccess(ArrayList<HashtagSubscription> result) {
				hashtagSubscriptions = result;
				
				subscriptionsList.clear();
				
				for(HashtagSubscription hs : hashtagSubscriptions) {
					for(Hashtag h : allHashtags) {
						if(h.getId() == hs.getHashtagId()) {
							subscriptionsList.addItem("#" + h.getKeyword());	
						}
					}
				}
				
				ClientsideSettings.getLogger().finest("Alle Hashtags geladen.");
			}
		});
	}
	
	private void getAllSubscriptionPosts(HashtagSubscription subscription) {
		msgSvc.findAllHashtagSubscriptionPosts(subscription.getHashtagId(), new AsyncCallback<ArrayList<Message>> () {

			@Override
			public void onFailure(Throwable caught) {
				ClientsideSettings.getLogger().severe("Posts des Abonnements konnten nicht geladen werden.");
			}

			@Override
			public void onSuccess(ArrayList<Message> result) {
				allMessagesOfSubscription = result;
				
				ftPosts.clear(true);
				ftPosts.removeAllRows();
				
				for(Message m : allMessagesOfSubscription) {
					
					int numOfRows = ftPosts.getRowCount();
					
					ftPosts.setText(numOfRows + 1, 0, Integer.toString(m.getUserId()));
					ftPosts.setText(numOfRows + 1, 1, m.getText());
					ftPosts.setText(numOfRows + 1, 2, m.getCreationDate().toString());

				}
				
				ClientsideSettings.getLogger().finest("Alle Posts des Abonnements geladen.");
			}
		});
	}
	
	private void createHashtag(String keyword) {
		msgSvc.createHashtag(keyword, new AsyncCallback<Hashtag>() {

			@Override
			public void onFailure(Throwable caught) {
				ClientsideSettings.getLogger().severe("Hashtag erfolgreich angelegt.");
			}

			@Override
			public void onSuccess(Hashtag result) {
				getAllHashtags();
				ClientsideSettings.getLogger().finest("Hashtag wurde erfolgreich angelegt.");
			}
			
		});
	}
	
	private void deleteHashtag(Hashtag hashtag) {
		msgSvc.deleteHashtag(hashtag, new AsyncCallback<Void> () {

			@Override
			public void onFailure(Throwable caught) {
				ClientsideSettings.getLogger().severe("Hashtag erfolgreich gelöscht.");
			}

			@Override
			public void onSuccess(Void result) {
				getAllHashtags();
				getAllSubscriptions();
				ClientsideSettings.getLogger().finest("Hashtag wurde erfolgreich gelöscht.");
			}
			
		});
	}
}
