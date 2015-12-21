package de.hdm.gruppe2.shared.bo;

public class HashtagSubscription extends Subscription {
	
	private static final long serialVersionUID = 1L;
	
	private Hashtag hashtag;

	
	public Hashtag getHashtag(){
		return hashtag;
		}
	
	public void setHashtag(Hashtag aboHashtag){
		this.hashtag=aboHashtag;
		
	}
}
