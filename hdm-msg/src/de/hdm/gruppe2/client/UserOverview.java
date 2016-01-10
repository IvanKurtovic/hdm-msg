package de.hdm.gruppe2.client;

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
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

import de.hdm.gruppe2.shared.MsgService;
import de.hdm.gruppe2.shared.MsgServiceAsync;
import de.hdm.gruppe2.shared.bo.User;

public class UserOverview extends VerticalPanel{
	
	private String[] names = {"Kerim", "Serkan", "Cem", "Ivan", "Cemile", "Hatun",
							  "Feridun", "Ibne", "Semsettin", "Bahtiyar", "Osman"};
	
	private MsgServiceAsync msgSvc = GWT.create(MsgService.class);
	private Label lblNotification = new Label();
		
	@Override
	public void onLoad() {
		
		// User Details
		final Grid mainGrid = new Grid(5,3);
		final Grid detailsGrid = new Grid(4,2);
		
		final Label lblFirstName = new Label("Vorname: ");
		final TextBox tbFirstName = new TextBox();
		final Label lblLastName = new Label("Nachname: ");
		final TextBox tbLastName = new TextBox();
		final Label lblEmail = new Label("Email: ");
		final TextBox tbEmail = new TextBox();		

		// User List links
		final ListBox userNames = new ListBox();
		userNames.setStyleName("listbox");
		userNames.setVisibleItemCount(11);
		userNames.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				tbFirstName.setText(userNames.getValue(userNames.getSelectedIndex()));
			}
		});
		
		for (String s : names) {
			userNames.addItem(s);
		}
		
		//final HorizontalPanel pnlFunctions = new HorizontalPanel();		
		final Button btnCreateUser = new Button("Neuer User");
		btnCreateUser.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				DialogBox dialogBox = createUserDialog();
				dialogBox.show();
				dialogBox.center();
			}
		});
		
		final Button btnSaveUser = new Button("Speichern");
		btnSaveUser.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				
				if(userNames.getSelectedIndex() == -1) {
					lblNotification.setText("Kein User ausgewaehlt.");
					return;
				}
				
				if(userNames.getValue(userNames.getSelectedIndex()) == tbFirstName.getText()) {
					lblNotification.setText("Keine Aenderung vorgenommen.");
					return;	
				} else if (userNames.getValue(userNames.getSelectedIndex()) != tbFirstName.getText()) {
					int index = userNames.getSelectedIndex();
					userNames.removeItem(index);
					userNames.insertItem(tbFirstName.getText(), index);
				}
			}
			
		});
		
		final Button btnDeleteUser = new Button("Entfernen");
		btnDeleteUser.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				
				if(userNames.getSelectedIndex() == -1) {
					lblNotification.setText("Kein User ausgewaehlt.");
					return;
				}
				
				userNames.removeItem(userNames.getSelectedIndex());
			}
		});
		
		// Damit die Buttons zusammen an einer Stelle des Grids angezeigt werden,
		// legen wir sie in einem Horizontal Panel zusammen.
		HorizontalPanel pnlSaveAndDeleteButton = new HorizontalPanel();
		pnlSaveAndDeleteButton.add(btnCreateUser);
		pnlSaveAndDeleteButton.add(btnDeleteUser);

		detailsGrid.setWidget(0, 0, lblFirstName);
		detailsGrid.setWidget(0, 1, tbFirstName);
		detailsGrid.setWidget(1, 0, lblLastName);
		detailsGrid.setWidget(1, 1, tbLastName);
		detailsGrid.setWidget(2, 0, lblEmail);
		detailsGrid.setWidget(2, 1, tbEmail);
		detailsGrid.setWidget(3, 0, btnSaveUser);
		
		mainGrid.setWidget(0, 0, userNames);
		mainGrid.setWidget(0, 1, detailsGrid);
		mainGrid.setWidget(1, 0, pnlSaveAndDeleteButton);
		mainGrid.setWidget(1, 1, lblNotification);

		this.add(mainGrid);
	}
	
	private DialogBox createUserDialog() {
		
		final DialogBox dialogBox = new DialogBox();
		dialogBox.setGlassEnabled(true);
		dialogBox.setAnimationEnabled(true);
		
		final Grid mainGrid = new Grid(6,2);
		
		final Label lblTitle = new Label("Neuer User");
		lblTitle.setStyleName("popup-title");
		final Label lblId = new Label("ID: ");
		final Label lblFirstName = new Label("Vorname: ");
		final Label lblLastName = new Label("Nachname: ");
		final Label lblEmail = new Label("Email: ");
		
		final TextBox tbId = new TextBox();
		tbId.setEnabled(false);
		final TextBox tbFirstNameDialog = new TextBox();
		final TextBox tbLastNameDialog = new TextBox();
		final TextBox tbEmailDialog = new TextBox();
		
		final Button btnCreate = new Button("Anlegen");
		btnCreate.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				// Der folgende reguläre Ausdruck entspricht einer standard Email Adresse.
				String regEx = "^\\w+([-+.']\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
				String email = tbEmailDialog.getText();
				String firstName = tbFirstNameDialog.getText();
				String lastName = tbLastNameDialog.getText();
				
				// Nur wenn die Eingaben des Nutzers sinnvoll sind soll ein Eintrag in
				// die Datenbank erfolgen.
				if(firstName == null || firstName.isEmpty()) {
					lblNotification.setText("Keinen Vornamen eingetragen.");
					return;
				} else if (lastName == null || lastName.isEmpty()){
					lblNotification.setText("Keinen Nachnamen eingetragen.");
					return;
				} else if(email == null || email.isEmpty()) {
					lblNotification.setText("Keine Email eingetragen.");
					return;
				} else if(!(email.matches(regEx))) {
					lblNotification.setText("Eingabe entspricht keiner Email-Adresse.");
					return;
				}
				
				msgSvc.createUser(tbEmailDialog.getText(), tbFirstNameDialog.getText(), tbLastNameDialog.getText(), new CreateUserCallback(lblNotification));
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
		mainGrid.setWidget(1, 0, lblId);
		mainGrid.setWidget(2, 0, lblFirstName);
		mainGrid.setWidget(3, 0, lblLastName);
		mainGrid.setWidget(4, 0, lblEmail);
		mainGrid.setWidget(5, 0, btnCreate);
		mainGrid.setWidget(1, 1, tbId);
		mainGrid.setWidget(2, 1, tbFirstNameDialog);
		mainGrid.setWidget(3, 1, tbLastNameDialog);
		mainGrid.setWidget(4, 1, tbEmailDialog);
		mainGrid.setWidget(5, 1, btnCancel);
		
		dialogBox.add(mainGrid);
		
		return dialogBox;
	}
	
	private class CreateUserCallback implements AsyncCallback<User> {

		private Label notification = null;
		
		public CreateUserCallback(Label notificationLabel) {
			this.notification = notificationLabel;
		}
		
		@Override
		public void onFailure(Throwable caught) {
			this.notification.setText("Der User wurde nicht angelegt!");			
		}

		@Override
		public void onSuccess(User result) {
			this.notification.setText("Der User wurde angelegt!");
		}
		
	}

}
