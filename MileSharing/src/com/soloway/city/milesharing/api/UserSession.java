package com.soloway.city.milesharing.api;

public class UserSession {
	private String SessionId;
	private boolean isOnline;
	
	public UserSession(){
		SessionId = "nondefined";
		isOnline = false;
	}
	
	public String getSessionId() {
		return SessionId;
	}
	public void setSessionId(String sessionId) {
		SessionId = sessionId;
	}
	public boolean isOnline() {
		return isOnline;
	}
	public void setOnline(boolean isOnline) {
		this.isOnline = isOnline;
	}
	
}
