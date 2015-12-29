package de.hdm.gruppe2.client.gui;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class HashtagView extends VerticalPanel {
	
	HTML welcomeText = new HTML("<h1>Wilkommen in der HashtagView!</h1>");

	
public HashtagView() {
		
		
		
		this.add(welcomeText);

		/**
		 * Abschlieﬂend wird die Klasse dem RootPanel zugeordnet.
		 */
		RootPanel.get("content_wrap").add(this);
		
		
	}

}
