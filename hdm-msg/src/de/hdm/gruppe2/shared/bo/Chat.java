package de.hdm.gruppe2.shared.bo;

import java.util.ArrayList;

public class Chat extends BusinessObject {

	private static final long serialVersionUID = 1L;
	
	private ArrayList<User> memberList;
	
	private ArrayList<Message> messageList;
	
	private String name;
	
	// Bei isPrivate = 1 entspricht es einem privaten Chat bzw. der Pinnwand eines Users.
	private boolean isPrivate;

	public boolean isPrivate() {
		return isPrivate;
	}

	public void setPrivate(boolean isPrivate) {
		this.isPrivate = isPrivate;
	}

	public ArrayList<User> getMemberList() {
		return memberList;
	}

	public void setMemberList(ArrayList<User> memberList) {
		this.memberList = memberList;
	}

	public ArrayList<Message> getMessageList() {
		return messageList;
	}

	public void setMessageList(ArrayList<Message> messageList) {
		this.messageList = messageList;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
