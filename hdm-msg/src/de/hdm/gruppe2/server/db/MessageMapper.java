package de.hdm.gruppe2.server.db;

import java.util.Vector;
import java.sql.*;
import de.hdm.gruppe2.shared.bo.*;

public class MessageMapper {

	private static MessageMapper messageMapper = null;
	
	protected MessageMapper() {
		
	}
	
	public static MessageMapper messageMapper() {
		if(messageMapper == null) {
			messageMapper = new MessageMapper();
		}
		
		return messageMapper;
	}
	
	public Message insert(Message message) {
		return null;
	}
	
	public Message update(Message message) {
		return null;
	}
	
	public Message delete(Message message) {
		return null;
	}
	
	public Vector<Message> findByUser(User user) {
		return null;
	}
	
	public Vector<Message> findById(int id) {
		return null;
	}
	
	public Vector<Message> findByChat(int chatId) {
		return null;
	}
}
