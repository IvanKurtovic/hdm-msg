package de.hdm.gruppe2.client;

import java.util.ArrayList;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

import de.hdm.gruppe2.shared.HashtagParser;
import de.hdm.gruppe2.shared.MsgServiceAsync;
import de.hdm.gruppe2.shared.bo.Hashtag;
import de.hdm.gruppe2.shared.bo.Message;
import de.hdm.gruppe2.shared.bo.User;

/**
 * Diese Klasse repräsentiert die Post Übersicht. Es enthält alle
 * momentan in der Datenbank befindlichen Posts des momentan eingeloggten Nutzers. 
 * Der Nutzer kann zusätzliche Posts schreiben und beim schreiben auch Hashtags
 * generieren lassen indem er ein Wort mit dem Symbol '#' beginnt.
 * Die notwendigen Methoden zum anlegen, editieren und löschen der Posts erhält diese 
 * Klasse über den MsgService.
 * 
 * @author Yilmaz
 * @author Sari
 *
 */
public class NewPostOverview extends VerticalPanel {
	
	private MsgServiceAsync msgSvc = ClientsideSettings.getMsgService();
	private ArrayList<Message> userPosts = new ArrayList<Message>();
	private User loggedInUser = null;
	
	private final FlexTable ftPosts = new FlexTable();
	private final TextArea taPost = new TextArea();
	
	public NewPostOverview(User user) {
		this.loggedInUser = user;
	}
	
	public void onLoad() {
		
		this.getAllPosts(ftPosts, loggedInUser.getId());

		final Grid mainGrid = new Grid(4,1);
		
		Label lblTitle = new Label("Posting Uebersicht");
		lblTitle.addStyleName("navigation-title");
		
		taPost.setWidth("100%");
		
		final Button btnPost = new Button("Posten!");
		btnPost.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				createPost(taPost.getText(), loggedInUser, HashtagParser.checkForHashtags(taPost.getText()));
			}
			
		});
		
		mainGrid.setWidget(0, 0, lblTitle);
		mainGrid.setWidget(1, 0, ftPosts);
		mainGrid.setWidget(2, 0, taPost);
		mainGrid.setWidget(3, 0, btnPost);
		
		this.add(mainGrid);
	}
	
	private DialogBox editPostDialog(final Message post) {

		final DialogBox dialogBox = new DialogBox();
		dialogBox.setStyleName("dialogbox-user");
		dialogBox.setGlassEnabled(true);
		dialogBox.setAnimationEnabled(true);
		
		final Grid mainGrid = new Grid(3, 2);
		
		final Label lblTitle = new Label("Post bearbeiten");
		final Label lblText = new Label("Text:");
		final TextBox tbText = new TextBox();
		
		final Button btnSave = new Button("Speichern");
		btnSave.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				post.setText(tbText.getText());
				savePost(post);
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
		
		mainGrid.setWidget(0, 0, lblTitle);
		mainGrid.setWidget(1, 0, lblText);
		mainGrid.setWidget(1, 1, tbText);
		mainGrid.setWidget(2, 0, btnSave);
		mainGrid.setWidget(2, 1, btnCancel);
		
		dialogBox.add(mainGrid);
		
		return dialogBox;
	}
	
	private void createPost(String text, User currentUser, ArrayList<Hashtag> hashtagList) {
		msgSvc.createPost(text, currentUser, hashtagList, new AsyncCallback<Message> () {

			@Override
			public void onFailure(Throwable caught) {
				ClientsideSettings.getLogger().severe("Post konnte nicht angelegt werden.");
			}

			@Override
			public void onSuccess(Message result) {
				taPost.setText("");
				getAllPosts(ftPosts, loggedInUser.getId());
				ClientsideSettings.getLogger().finest("Post wurde angelegt.");				
			}
			
		});		
	}
	
	private void getAllPosts(FlexTable flexTable, int userId) {
		msgSvc.findAllPostsOfUser(userId, new AsyncCallback<ArrayList<Message>> () {

			@Override
			public void onFailure(Throwable caught) {
				ClientsideSettings.getLogger().severe("Posts konnten nicht geladen werden.");
			}

			@Override
			public void onSuccess(ArrayList<Message> result) {
				userPosts = result;
				
				ftPosts.clear(true);
				ftPosts.removeAllRows();
				
				for(Message m : userPosts) {
					
					int numOfRows = ftPosts.getRowCount();
					final Message message = m;
					
					ftPosts.setText(numOfRows + 1, 0, Integer.toString(m.getUserId()));
					ftPosts.setText(numOfRows + 1, 1, m.getText());
					ftPosts.setText(numOfRows + 1, 2, m.getCreationDate().toString());
					
					Button btnEdit = new Button("Edit");
					btnEdit.addClickHandler(new ClickHandler() {

						@Override
						public void onClick(ClickEvent event) {
							DialogBox dialogBox = editPostDialog(message);
							dialogBox.show();
							dialogBox.center();
						}
						
					});
					Button btnRemove = new Button("Remove Post");
					btnRemove.addClickHandler(new ClickHandler() {

						@Override
						public void onClick(ClickEvent event) {
							deletePost(message.getId(), ftPosts.getCellForEvent(event).getRowIndex());
						}
						
					});
					
					ftPosts.setWidget(numOfRows + 1, 3, btnEdit);
					ftPosts.setWidget(numOfRows + 1, 4, btnRemove);
				}
				
				ClientsideSettings.getLogger().finest("Posts erfolgreich geladen.");
			}

		});
	}
	
	private void deletePost(int postId, final int flexTableRow) {

		msgSvc.deleteMessage(postId, new AsyncCallback<Void>() {

			@Override
			public void onFailure(Throwable caught) {
				ClientsideSettings.getLogger().severe("Zeile erfolgreich entfernt.");
			}

			@Override
			public void onSuccess(Void result) {
				ftPosts.removeRow(flexTableRow);
				
				ClientsideSettings.getLogger().finest("Zeile erfolgreich entfernt.");
			}
			
		});
	}

	private void savePost(Message post) {
		
		// Hashtag Liste des neuen Posts aktualisieren.
		post.setHashtagList(HashtagParser.checkForHashtags(post.getText()));
		
		// Änderungen in die Datenbank schreiben.
		msgSvc.saveMessage(post, new AsyncCallback<Message> () {

			@Override
			public void onFailure(Throwable caught) {
				ClientsideSettings.getLogger().severe("Aenderungen konnten nicht gespeichert werden.");
			}

			@Override
			public void onSuccess(Message result) {
				ftPosts.clear(true);
				getAllPosts(ftPosts, loggedInUser.getId());
				ClientsideSettings.getLogger().finest("Aenderungen gespeichert.");
			}
			
		});
	}
}
