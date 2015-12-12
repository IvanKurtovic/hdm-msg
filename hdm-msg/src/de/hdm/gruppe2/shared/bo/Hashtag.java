package de.hdm.gruppe2.shared.bo;


public class Hashtag extends BusinessObject {
	
	private static final long serialVersionUID = 1L;

	private String keyword;  
	
	public String getKeyword(){
		return keyword;
	}
	
	public void setKeyword(String keyword){
		this.keyword=keyword;
	}
	

}

