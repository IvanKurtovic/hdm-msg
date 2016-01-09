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
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class UserOverview extends VerticalPanel{
	
	private String[] names = {"Kerim", "Serkan", "Cem", "Ivan", "Cemile", "Hatun",
							  "Feridun", "Ibne", "Semsettin", "Bahtiyar", "Osman"};
		
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
					Window.alert("Kein User ausgewaehlt, Moruk!");
					return;
				}
				
				if(userNames.getValue(userNames.getSelectedIndex()) == tbFirstName.getText()) {
					Window.alert("Du hast nix gemacht, du Spasst Alder!");
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
					Window.alert("Kein User ausgewaehlt, Moruk!");
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

		this.add(mainGrid);
	}
	
	private DialogBox createUserDialog() {
		
		final DialogBox dialogBox = new DialogBox();
		dialogBox.setGlassEnabled(true);
		dialogBox.setAnimationEnabled(true);
		
		Grid mainGrid = new Grid(6,2);
		
		Label lblTitle = new Label("Neuer User");
		lblTitle.setStyleName("popup-title");
		Label lblId = new Label("ID: ");
		Label lblFirstName = new Label("Vorname: ");
		Label lblLastName = new Label("Nachname: ");
		Label lblEmail = new Label("Email: ");
		
		TextBox tbId = new TextBox();
		tbId.setEnabled(false);
		TextBox tbFirstName = new TextBox();
		TextBox tbLastName = new TextBox();
		TextBox tbEmail = new TextBox();
		
		Button btnCreate = new Button("Anlegen");
		Button btnCancel = new Button("Abbrechen");
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
		mainGrid.setWidget(2, 1, tbFirstName);
		mainGrid.setWidget(3, 1, tbLastName);
		mainGrid.setWidget(4, 1, tbEmail);
		mainGrid.setWidget(5, 1, btnCancel);
		
		dialogBox.add(mainGrid);
		
		return dialogBox;
	}

}
