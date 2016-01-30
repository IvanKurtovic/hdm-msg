package de.hdm.gruppe2.shared;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;

import de.hdm.gruppe2.shared.bo.Hashtag;
import de.hdm.gruppe2.shared.bo.User;
import de.hdm.gruppe2.shared.report.AllFollowersOfHashtagReport;
import de.hdm.gruppe2.shared.report.AllFollowersOfUserReport;
import de.hdm.gruppe2.shared.report.AllMessagesOfAllUsersReport;
import de.hdm.gruppe2.shared.report.AllMessagesOfPeriodReport;
import de.hdm.gruppe2.shared.report.AllMessagesOfUserReport;

public interface ReportRPCAsync {

	void init(AsyncCallback<Void> callback);

	void createAllMessagesOfPeriodReport(String start, String end, AsyncCallback<AllMessagesOfPeriodReport> callback);

	void createAllMessagesOfUserReport(String userMail, AsyncCallback<AllMessagesOfUserReport> callback);

	void createAllMessagesOfAllUsersReport(AsyncCallback<AllMessagesOfAllUsersReport> callback);

	void createAllFollowersOfHashtagReport(Hashtag h, AsyncCallback<AllFollowersOfHashtagReport> callback);

	void createAllFollowersOfUserReport(User u, AsyncCallback<AllFollowersOfUserReport> callback);

	void findAllHashtags(AsyncCallback<ArrayList<Hashtag>> callback);

}
