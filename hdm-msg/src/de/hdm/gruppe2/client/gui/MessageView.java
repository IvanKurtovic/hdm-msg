package de.hdm.gruppe2.client.gui;

import java.util.Date;
import java.util.Vector;

import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import de.hdm.gruppe2.shared.bo.Hashtag;
import de.hdm.gruppe2.shared.bo.Message;
import de.hdm.gruppe2.shared.bo.User;

public class MessageView extends VerticalPanel {

	
	HTML welcomeText = new HTML("<h1>Wilkommen in der MessageView</h1>");
	
	private VerticalPanel panelLinks = new VerticalPanel();
	private VerticalPanel panelRechts = new VerticalPanel();
	private final FlexTable tableInput = new FlexTable();
	private  Label InputLabel = new Label("Inputbox");
	
	
	public MessageView() {
		
		
		
		//TODO  Diese BOs später löschen, sobald RPC steht
		// User Objekte
		User ich = new User();
		ich.setFirstName("Ivan");
		ich.setLastName("Kurtovic");
		User meinFreund = new User();
		meinFreund.setFirstName("Marina");
		meinFreund.setLastName("Ioannidou");
		
		// Nachrichten Objekte für Meine Nachrichten
		Message message1 = new Message();
		message1.setText("Hallo");
		message1.setSender(ich);
		Date date = new Date();	
		message1.setCreationDate(date);
		Message message2 = new Message();
		message2.setCreationDate(date);
		Hashtag hashtag = new Hashtag();
		hashtag.setKeyword("goodMood");
		Vector<Hashtag> hashtagList = new Vector<Hashtag>();
		hashtagList.add(hashtag);
		message2.setHashtagList(hashtagList);
		message2.setText("Wie gehts ?");
		message2.setSender(ich);
		
		// Nachrichten Objekte für Nachrichten meines Freundes
		Message message3 = new Message();
		message3.setText("hi");
		message3.setSender(meinFreund);
		message3.setCreationDate(date);
		Message message4 = new Message();
		message4.setCreationDate(date);

		Hashtag hashtag2 = new Hashtag();
		hashtag.setKeyword("superMood");
		Vector<Hashtag> hashtagList2 = new Vector<Hashtag>();
		hashtagList2.add(hashtag2);
		message4.setHashtagList(hashtagList2);
		message4.setText("gut und selbst?");
		message4.setSender(meinFreund);
		
		//Chat anlegen
		Vector<User> memberList = new Vector<User>();
		memberList.add(ich);
		memberList.add(meinFreund);
		Vector<Message> messageList = new Vector<Message>();
		messageList.add(message1);
		messageList.add(message2);
		messageList.add(message3);
		messageList.add(message4);
		
		
		
		
		tableInput.setText(0, 0, "Sender");
		tableInput.setText(0, 1, "Nachricht");
		tableInput.setText(0, 2, "Datum");
		
		
		for (int row = 1; row <= messageList.size(); row++) {
			
			/**
			 * Da die flexTable in Reihen-Index 0 bereits mit den
			 * Tabellen-Überschriften belegt ist (Begründung siehe
			 * weiter oben im Code), wird eine "Hilfs-Variable"
			 * benötigt, die den Tabellen-Index für den Vektor-Index
			 * simuliert.
			 */
			final int i = row - 1;
		
		
			tableInput.setText(row, 0, "" + messageList.get(i).getSender().getFirstName());
			tableInput.setText(row, 1, messageList.get(i).getText());
			tableInput.setText(row, 2, messageList.get(i).getCreationDate().toString().substring(0, 19));

			
		}
			
		this.add(welcomeText);
		panelLinks.add(InputLabel);
		panelLinks.add(tableInput);
		this.add(panelLinks);
		this.add(panelRechts);


		/**
		 * Abschließend wird die Klasse dem RootPanel zugeordnet.
		 */
		RootPanel.get("content_wrap").add(this);
		
		
	}
	
}
