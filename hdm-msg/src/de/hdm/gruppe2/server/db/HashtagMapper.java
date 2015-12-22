package de.hdm.gruppe2.server.db;

import java.util.Vector;
import de.hdm.gruppe2.shared.bo.*;

public class HashtagMapper {

	private static HashtagMapper hashtagMapper = null;
	
	protected HashtagMapper() {}
	
	public static HashtagMapper hashtagMapper() {
		if(hashtagMapper == null) {
			hashtagMapper = new HashtagMapper();
		}
		
		return hashtagMapper;
	}
	
	public Hashtag insert(Hashtag hashtag) {
		return null;
	}
	
	public Hashtag update(Hashtag hashtag) {
		return null;
	}
	
	public Hashtag delete(Hashtag hashtag) {
		return null;
	}
	
	public Hashtag findById(int id) {
		return null;
	}
	
	public Vector<Hashtag> getAllHashtags() {
		return null;
	}
}
