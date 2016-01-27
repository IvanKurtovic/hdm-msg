package de.hdm.gruppe2.shared;

import java.util.ArrayList;

import de.hdm.gruppe2.shared.bo.Hashtag;
import de.hdm.gruppe2.shared.bo.Message;

public class HashtagParser {
	
	public static ArrayList<Hashtag> checkForHashtags(String text) {

		String[] substrings = text.split("#");
		ArrayList<Hashtag> hashtags = new ArrayList<Hashtag>();
		
		for(String s : substrings) {
			Hashtag h = new Hashtag();
			
			if(substrings[0] == s) {
				continue;
			}
			
			if(s.charAt(s.length() - 1) == ' ') {
				h.setKeyword(s.subSequence(0, s.indexOf(" ")).toString());
			} else {
				s += " ";
				h.setKeyword(s.subSequence(0, s.indexOf(" ")).toString());
			}
			
			hashtags.add(h);
		}

		return hashtags;
	}
}
