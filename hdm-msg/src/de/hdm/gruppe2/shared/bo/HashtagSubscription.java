package de.hdm.gruppe2.shared.bo;

public class HashtagSubscription extends Subscription {
	
	private static final long serialVersionUID = 1L;
	
	private int hashtagId;
	
	public int getHashtagId(){
		return hashtagId;
	}
	
	public void setHashtagId(int hashtagId){
		this.hashtagId = hashtagId;
	}
}
