package de.hdm.gruppe2.client;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;

public class NewPostOverview extends VerticalPanel {
	
	public void onLoad() {
		
		final Grid mainGrid = new Grid(2,1);
		final TextArea taPost = new TextArea();
		final Button btnPost = new Button("Posten!");
		
		mainGrid.setWidget(0, 0, taPost);
		mainGrid.setWidget(1, 0, btnPost);
		
		this.add(mainGrid);
	}

}
