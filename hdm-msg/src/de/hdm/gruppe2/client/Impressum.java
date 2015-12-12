package de.hdm.gruppe2.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

	/**
	* Die Klasse Impressum ist notwendig, um nach TMG §5 den Betreiber der Website eindeutig identifizieren zu können.
	* Dieses Impressum ist mithilfe eines Buttons von jeder Unterseite der Anwendung aus zu erreichen.
	* <p>Im Footer der html-Seite ist ein eigens angelegter Bereich für den Button vorhanden.
	*
	*/
	public class Impressum extends VerticalPanel {
	    
		public void onLoad(){
	    
	    	Button impressumBtn = new Button("Impressum");
	    	impressumBtn.setStylePrimaryName("impressumBtn");
	    	
	    	
	    	
	        final HTML html = new HTML("");
	        html.setHTML("<h2>Impressum</h2>"+
	        		"<h2>Angaben: Paragraph 5 Telemediengesaetz:</h2>"+
	        		"<p>Kerim Sari, Serkan Yilmaz, Ivan Kurtovic, Cem Korkmaz, Marina <br />"+
	        		"Nobelstrasse 10<br />"+
	        		"70569 Stuttgart"+
	        		"</p>" +
	        		"<h2>Kontakt:</h2>"+
	        		"<table><tr>"+
	        		"<td>Telefon:</td>"+
	        		"<td>0711 8923 10</td></tr>"+
	        		"<td>Mobile Phone:</td>"+
	        		"<td>017255667788</td></tr>"+
	        		"<tr><td>E-Mail:</td>"+
	        		"<td>info@hdm-stuttgart.de</td>"+
	        		"</tr></table><p>"
	        		);
	        
	        impressumBtn.addClickHandler(new ClickHandler() {
	        	public void onClick(ClickEvent event) {     	
	        		RootPanel.get("content_wrap").clear();
	        		RootPanel.get("content_wrap").add(html);
	        	}
	        });
	        
	       
	        
	        RootPanel.get("Impressum").add(impressumBtn);

	        
		}
	    
	}