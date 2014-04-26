package com.soloway.city.milesharing.api;

public class UserProfile {
	private String firstName;
	private String secondName;
	private String userLogin;
	
	//MD5 of password
	// I know, it's very stupid :)
	private String userPassword;
	private boolean isDriver;
	private boolean isPassenger;
	private int freePlaces;
	private boolean isOccupied;
	private boolean blackmark;
	
	public UserProfile() {
		firstName = "empty";
		secondName = "empty";
		userLogin = "empty";
		userPassword = "12345";
		isDriver = false;
		isPassenger = true;
		freePlaces = 0;
		isOccupied = false;
		blackmark = false;
	}
	
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getSecondName() {
		return secondName;
	}
	public void setSecondName(String secondName) {
		this.secondName = secondName;
	}
	public String getUserLogin() {
		return userLogin;
	}
	public void setUserLogin(String userLogin) {
		this.userLogin = userLogin;
	}
	public String getUserPassword() {
		return userPassword;
	}
	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}
	public boolean isDriver() {
		return isDriver;
	}
	public void setDriver(boolean isDriver) {
		this.isDriver = isDriver;
	}
	public boolean isPassenger() {
		return isPassenger;
	}
	public void setPassenger(boolean isPassenger) {
		this.isPassenger = isPassenger;
	}
	public int getFreePlaces() {
		return freePlaces;
	}
	public void setFreePlaces(int freePlaces) {
		this.freePlaces = freePlaces;
	}
	public boolean isOccupied() {
		return isOccupied;
	}
	public void setOccupied(boolean isOccupied) {
		this.isOccupied = isOccupied;
	}
	public boolean isBlackmark() {
		return blackmark;
	}
	public void setBlackmark(boolean blackmark) {
		this.blackmark = blackmark;
	}
	
	public UserSession pushUser(){
		return UsersHelper.pushUser(this);
	}
	
	public UserSession authUser(){
		return UsersHelper.authUser(this);
	}
	
	public UserSession exitUser(){
		return UsersHelper.exitUser(this);
	}
	
	public UserProfile getUserInfo(){
		UserProfile result = new UserProfile();
		
		return result;
	}
}