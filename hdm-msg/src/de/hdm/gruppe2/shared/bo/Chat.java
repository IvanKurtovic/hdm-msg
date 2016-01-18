package de.hdm.gruppe2.shared.bo;

import java.util.Vector;

public class Chat extends BusinessObject {

	private static final long serialVersionUID = 1L;
	
	private Vector<User> memberList;
	
	private Vector<Message> messageList;
	
	private String name;

	public Vector<User> getMemberList() {
		return memberList;
	}

	public void setMemberList(Vector<User> memberList) {
		this.memberList = memberList;
	}

	public Vector<Message> getMessageList() {
		return messageList;
	}

	public void setMessageList(Vector<Message> messageList) {
		this.messageList = messageList;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
