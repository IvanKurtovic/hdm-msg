package de.hdm.gruppe2.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import de.hdm.gruppe2.client.gui.Impressum;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class HdmMsg extends VerticalPanel implements EntryPoint {
	
	public void onModuleLoad() {
		
	
		Image welcomeImage = new Image();
		HTML welcomeText = new HTML("<h1>Willkommen im Editor!</h1>");
		
		
		
		//Hautpmenü schließlich dem RootPanel in den Menü-div Container zuordnen
		
		RootPanel.get("content_wrap").add(welcomeImage);
	    RootPanel.get("content_wrap").add(welcomeText);
		RootPanel.get("Impressum").add(new Impressum());
		
}
	
}
