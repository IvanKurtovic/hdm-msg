package de.hdm.gruppe2.shared.bo;

public class HashtagSubscription extends Subscription {
	
	private Hashtag hashtag;

	
	public Hashtag getHashtag(){
		return hashtag;
		}
	
	public void setHashtag(Hashtag aboHashtag){
		this.hashtag=aboHashtag;
		
	}
}
