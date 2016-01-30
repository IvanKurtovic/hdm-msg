package de.hdm.gruppe2.client.report;

import java.util.Date;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.datepicker.client.DatePicker;

import de.hdm.gruppe2.client.ClientsideSettings;
import de.hdm.gruppe2.shared.ReportRPCAsync;
import de.hdm.gruppe2.shared.report.AllMessagesOfPeriodReport;
import de.hdm.gruppe2.shared.report.HTMLReportWriter;

public class AllMessagesOfPeriodReportForm extends VerticalPanel {

	private ReportRPCAsync reportGenerator = null;
	
	private DatePicker dpFrom = new DatePicker();
	private String dateFrom = null;
	private DatePicker dpTo = new DatePicker();
	private String dateTo = null;
	private Button btnReport = new Button("Laden");
	
	@Override
	public void onLoad() {
		
		reportGenerator = ClientsideSettings.getReportGenerator();
		
		final Grid mainGrid = new Grid(4, 2);
		
		Label lblTitle = new Label("Alle Nachrichten eines Zeitraums");
		Label lblDateFrom = new Label("Datum von:");
		Label lblDateTo = new Label("Datum bis:");
	
		dpFrom.addValueChangeHandler(new ValueChangeHandler<Date>() {

			@Override
			public void onValueChange(ValueChangeEvent<Date> event) {
				dateFrom = DateTimeFormat.getFormat("yyyy-MM-dd").format(dpFrom.getValue());
				dateFrom += " 00:00:00";
			}
		});
		
		dpTo.addValueChangeHandler(new ValueChangeHandler<Date>() {

			@Override
			public void onValueChange(ValueChangeEvent<Date> event) {
				dateTo = DateTimeFormat.getFormat("yyyy-MM-dd").format(dpTo.getValue());
				dateTo += " 23:59:59";
			}
		});
		
		btnReport.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if((dpFrom.getValue() == null) || (dpTo.getValue() == null)) {
					ClientsideSettings.getLogger().severe("Datum nicht erkannt.");
					return;
				}
				
				loadReport(dateFrom, dateTo);
			}
		});
		
		mainGrid.setWidget(0, 0, lblTitle);
		mainGrid.setWidget(1, 0, lblDateFrom);
		mainGrid.setWidget(2, 0, dpFrom);
		mainGrid.setWidget(1, 1, lblDateTo);
		mainGrid.setWidget(2, 1, dpTo);
		mainGrid.setWidget(3, 0, btnReport);
	
		this.add(mainGrid);
		
	}
	
	private void loadReport(String start, String end) {
		reportGenerator.createAllMessagesOfPeriodReport(start, end, new AsyncCallback<AllMessagesOfPeriodReport>() {

			@Override
			public void onFailure(Throwable caught) {
				ClientsideSettings.getLogger().severe(
						"Erzeugen des Reports fehlgeschlagen!");
			}

			@Override
			public void onSuccess(AllMessagesOfPeriodReport result) {
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
