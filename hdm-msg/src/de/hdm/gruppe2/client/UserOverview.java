package de.hdm.gruppe2.client;

import java.util.ArrayList;

import com.google.gwt.core.shared.GWT;
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

import de.hdm.gruppe2.shared.MsgService;
import de.hdm.gruppe2.shared.MsgServiceAsync;
import de.hdm.gruppe2.shared.bo.User;

/**
 * Diese Klasse repräsentiert die User Übersicht. Es enthält alle
 * momentan in der Datenbank befindlichen Nutzer. Der Nutzer kann
 * alle Nutzer bearbeiten, löschen oder neue Nutzer über ein PopUp-Dialog
 * anlegen. Die notwendigen Methoden zum auslesen, anlegen und editieren
 * erhält diese Klasse über den MsgService.
 * 
 * 
 * @author Yilmaz
 * @author Sari
 *
 */
public class UserOverview extends VerticalPanel{
	
	private ArrayList<User> users = new ArrayList<User>();	
	private MsgServiceAsync msgSvc = GWT.create(MsgService.class);
	private Label lblNotification = new Label();
	private final ListBox userList = new ListBox();
	private User selectedUser = null;
	
	private final TextBox tbNickname = new TextBox();
	private final TextBox tbEmail = new TextBox();
	private final TextBox tbCreationDate = new TextBox();
	private final Button btnDeleteUser = new Button("Entfernen");
	
	@Override
	public void onLoad() {

		// User Details
		final Grid mainGrid = new Grid(5,3);
		final Grid detailsGrid = new Grid(4,2);
		
		final Label lblNickname = new Label("Nickname: ");
		final Label lblEmail = new Label("Email: ");
		final Label lblCreationDate = new Label("Angelegt am: ");
		tbCreationDate.setEnabled(false);
		tbEmail.setEnabled(false);
		
		// Set Style-Names
		detailsGrid.setStyleName("detailsgrid-user");
		lblNickname.setStyleName("nickname-user");
		lblEmail.setStyleName("email-user");
		lblCreationDate.setStyleName("timestamp-user");
		tbNickname.setStyleName("textbox-nickname-user");
		tbEmail.setStyleName("textbox-email-user");
		tbCreationDate.setStyleName("textbox-timestamp-user");
		userList.setStyleName("listbox");
		btnDeleteUser.setStyleName("delete-user");
		
		// User List links
		userList.setVisibleItemCount(11);
		userList.addChangeHandler(new ChangeHandler() {
			
			@Override
			public void onChange(ChangeEvent event) {
				if(userList.getSelectedIndex() == -1) {
					return;
				}
				
				selectedUser = users.get(userList.getSelectedIndex());
				
				tbNickname.setText(selectedUser.getNickname());
				tbEmail.setText(selectedUser.getEmail());
				tbCreationDate.setText(selectedUser.getCreationDate().toString());
			}
		});
		
		// Alle User aus der Datenbank laden und in die Liste speichern.
		this.getAllUsers();
		
		//final HorizontalPanel pnlFunctions = new HorizontalPanel();		
		final Button btnCreateUser = new Button("Neuer User");
		btnCreateUser.setStyleName("newuser-user");
		btnCreateUser.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				DialogBox dialogBox = createUserDialog();
				dialogBox.show();
				dialogBox.center();
			}
		});
		
		final Button btnSaveUser = new Button("Speichern");
		btnSaveUser.setStyleName("save-user");
		btnSaveUser.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				
				String nickname = tbNickname.getText();
				String email = tbEmail.getText();
				
				if(userList.getSelectedIndex() == -1) {
					lblNotification.setText("Kein User ausgewaehlt.");
					return;
				}
				
				if(selectedUser.getNickname() == nickname && selectedUser.getEmail() == email) {
					
					lblNotification.setText("Keine Aenderung vorgenommen.");
					return;	
					
				} else {
					selectedUser.setNickname(nickname);
					selectedUser.setEmail(email);
					
					msgSvc.saveUser(selectedUser, new SaveUserCallback());
				}
			}
			
		});
		
		btnDeleteUser.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				
				if(userList.getSelectedIndex() == -1) {
					ClientsideSettings.getLogger().severe("Kein User ausgewählt.");
					return;
				}
				
				// Jetzt erst wird der User aus der Datenbank entfernt
				deleteUser(users.get(userList.getSelectedIndex()));
			}
		});
		
		final Button btnRefresh = new Button("Refresh");
		btnRefresh.setStyleName("refresh-user");
		btnRefresh.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				getAllUsers();
			}
			
		});
		
		// Damit die Buttons zusammen an einer Stelle des Grids angezeigt werden,
		// legen wir sie in einem Horizontal Panel zusammen.
		HorizontalPanel pnlSaveDeleteRefresh = new HorizontalPanel();
		pnlSaveDeleteRefresh.add(btnCreateUser);
		pnlSaveDeleteRefresh.add(btnRefresh);
		pnlSaveDeleteRefresh.add(btnDeleteUser);

		detailsGrid.setWidget(0, 0, lblNickname);
		detailsGrid.setWidget(0, 1, tbNickname);
		detailsGrid.setWidget(1, 0, lblEmail);
		detailsGrid.setWidget(1, 1, tbEmail);
		detailsGrid.setWidget(2, 0, lblCreationDate);
		detailsGrid.setWidget(2, 1, tbCreationDate);
		detailsGrid.setWidget(3, 0, btnSaveUser);
				
		mainGrid.setWidget(0, 0, userList);
		mainGrid.setWidget(0, 1, detailsGrid);
		mainGrid.setWidget(1, 0, pnlSaveDeleteRefresh);
		mainGrid.setWidget(1, 1, lblNotification);

		this.add(mainGrid);
	}

	
	private DialogBox createUserDialog() {
		
		final DialogBox dialogBox = new DialogBox();
		dialogBox.setStyleName("dialogbox-user");
		dialogBox.setGlassEnabled(true);
		dialogBox.setAnimationEnabled(true);
		
		final Grid mainGrid = new Grid(5,2);
		
		final Label lblTitle = new Label("Neuer User");
		lblTitle.setStyleName("popup-title-user");
		final Label lblNickname = new Label("Nickname: ");
		lblNickname.setStyleName("popup-nickname-user");
		final Label lblEmail = new Label("Email: ");
		lblEmail.setStyleName("popup-email-user");
		final TextBox tbNicknameDialog = new TextBox();
		final TextBox tbEmailDialog = new TextBox();
		
		final Button btnCreate = new Button("Anlegen");
		btnCreate.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				// Der folgende reguläre Ausdruck entspricht einer standard Email Adresse.
				String regEx = "^\\w+([-+.']\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
				String email = tbEmailDialog.getText();
				String nickname = tbNicknameDialog.getText();
				
				// Nur wenn die Eingaben des Nutzers sinnvoll sind soll ein Eintrag in
				// die Datenbank erfolgen.
				if(nickname == null || nickname.isEmpty()) {
					lblNotification.setText("Keinen Nickname eingetragen.");
					return;
				} else if(!(email.matches(regEx))) {
					lblNotification.setText("Eingabe entspricht keiner Email-Adresse.");
					return;
				}
				
				msgSvc.createUser(email, nickname, new CreateUserCallback());
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
		mainGrid.setWidget(1, 0, lblNickname);
		mainGrid.setWidget(2, 0, lblEmail);
		mainGrid.setWidget(3, 0, btnCreate);
		mainGrid.setWidget(1, 1, tbNicknameDialog);
		mainGrid.setWidget(2, 1, tbEmailDialog);
		mainGrid.setWidget(3, 1, btnCancel);
		
		dialogBox.add(mainGrid);
		
		return dialogBox;
	}
	
	private class CreateUserCallback implements AsyncCallback<User> {
		
		@Override
		public void onFailure(Throwable caught) {
			ClientsideSettings.getLogger().severe("Der User wurde nicht angelegt!");
		}

		@Override
		public void onSuccess(User result) {
			getAllUsers();
		}
		
	}
	
	private class SaveUserCallback implements AsyncCallback<User> {

		@Override
		public void onFailure(Throwable caught) {
			ClientsideSettings.getLogger().severe("Änderungen konnten nicht übernommen werden.");			
		}

		@Override
		public void onSuccess(User result) {
			getAllUsers();			
		}		
	}
	
	private void getAllUsers(){
		msgSvc.findAllUser(new AsyncCallback<ArrayList<User>>() {

			@Override
			public void onFailure(Throwable caught) {
				ClientsideSettings.getLogger().severe("User konnten nicht geladen werden.");
			}

			@Override
			public void onSuccess(ArrayList<User> result) {
				users = result;
				
				// Da wir nur ein Listobjekt weiter reichen, säubern wir zunächst alle vorhandenen
				// Einträge.
				userList.clear();
				
				for(User u : users) {
					userList.addItem(u.getNickname());
				}
				
				ClientsideSettings.getLogger().finest("Alle User wurden geladen!");
			}
			
		});
	}
	
	private void deleteUser(User user) {
		msgSvc.deleteUser(user, new AsyncCallback<Void>() {

			@Override
			public void onFailure(Throwable caught) {
				ClientsideSettings.getLogger().severe("User konnte nicht entfernt werden.");
			}

			@Override
			public void onSuccess(Void result) {
				// Aufräumen der Detailmaske
				tbEmail.setText("");
				tbNickname.setText("");
				tbCreationDate.setText("");
				
				// Entfernen des Users aus der Liste
				userList.removeItem(userList.getSelectedIndex());
				// Entfernen des User-Objekts aus der users Liste
				users.remove(userList.getSelectedIndex());
				
				ClientsideSettings.getLogger().finest("User wurde entfernt.");
			}		
		});
	}
}
