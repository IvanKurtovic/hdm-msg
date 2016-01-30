package de.hdm.gruppe2.client.report;

import java.util.ArrayList;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import de.hdm.gruppe2.client.ClientsideSettings;
import de.hdm.gruppe2.shared.ReportRPCAsync;
import de.hdm.gruppe2.shared.bo.Hashtag;
import de.hdm.gruppe2.shared.report.AllFollowersOfHashtagReport;
import de.hdm.gruppe2.shared.report.HTMLReportWriter;

/**
 * Diese Klasse bildet die Oberfläche für den Report "Alle Follower eines Hashtags"
 * ab. Es stellt eines ListBox zur Verfügung die alle vorhandenen Hashtags der Datenbank
 * präsentiert. Dazu liest diese Klasse bei Erzeugung via RPC-Call die Datenbank nach
 * allen Hashtags aus und speichert sie in eine ArrayList.
 * Der Anwender kann schließlich über diese Box ein Hashtag wählen und den Report
 * über den Button "btnReport" generieren lassen. Dieser wird als reiner HTML Text
 * in den "footer_wrap" der HTML Seite geschrieben.
 * 
 * @author Kurtovic
 * @author Korkmaz
 *
 */
public class AllFollowersOfHashtagForm extends VerticalPanel {

	private ReportRPCAsync reportGenerator = null;
	private ArrayList<Hashtag> hashtags = null;
	
	private ListBox lbHashtags = new ListBox();
	private Button btnReport = new Button("Laden");
	
	@Override
	public void onLoad() {
		
		reportGenerator = ClientsideSettings.getReportGenerator();
		
		this.getAllHashtags();
		
		Grid mainGrid = new Grid(3, 2);
		
		Label lblTitle = new Label("Alle Follower eines Hashtags");
		
		btnReport.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if(lbHashtags.getSelectedIndex() == -1) {
					ClientsideSettings.getLogger().severe("Kein hashtag ausgewählt.");
					return;
				}
				
				loadReport(hashtags.get(lbHashtags.getSelectedIndex()));
			}
		});
		
		mainGrid.setWidget(0, 0, lblTitle);
		mainGrid.setWidget(1, 0, lbHashtags);
		mainGrid.setWidget(2, 0, btnReport);
		
		this.add(mainGrid);
		
	}
	
	private void getAllHashtags() {
		reportGenerator.findAllHashtags(new AsyncCallback<ArrayList<Hashtag>>() {

			@Override
			public void onFailure(Throwable caught) {
				ClientsideSettings.getLogger().severe(
						"Hashtags konnten nicht geladen werden!");
			}

			@Override
			public void onSuccess(ArrayList<Hashtag> result) {
				hashtags = result;
				
				lbHashtags.clear();
				
				for(Hashtag h : hashtags) {
					lbHashtags.addItem("#" + h.getKeyword());
				}
			}
		});
	}
	
	private void loadReport(Hashtag h) {
		reportGenerator.createAllFollowersOfHashtagReport(h, new AsyncCallback<AllFollowersOfHashtagReport>() {

			@Override
			public void onFailure(Throwable caught) {
				ClientsideSettings.getLogger().severe(
						"Erzeugen des Reports fehlgeschlagen!");
			}

			@Override
			public void onSuccess(AllFollowersOfHashtagReport result) {
				if (result != null) {
					HTMLReportWriter writer = new HTMLReportWriter();
					writer.process(result);
					RootPanel.get("footer_wrap").clear();
					RootPanel.get("footer_wrap").add(new HTML(writer.getReportText()));
				}
			}
		});
	}
}
