package com.obc.csrg.events;

import com.obc.csrg.model.User;

public class ChatEvent extends BasicEventObject{

	private static final long serialVersionUID = 201005141540L;
	
	private User leavingChatUser;
	private User sender;
	private String message;
	
	//constructors
	public ChatEvent(final Object eventSource){
		super(eventSource);
	}
	public ChatEvent(final Object eventSource,User leavingChatUser){
		super(eventSource);
		this.leavingChatUser =	leavingChatUser;
	}
	public ChatEvent(final Object eventSource,User sender,String message){
		super(eventSource);
		this.sender = sender;
		this.message = message;
	}
	
	//getters and setters
	public User getLeavingChatUser() {
		return leavingChatUser;
	}
	public void setLeavingChatUser(User leavingChatUser) {
		this.leavingChatUser = leavingChatUser;
	}
	public User getSender() {
		return sender;
	}
	public void setSender(User sender) {
		this.sender = sender;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
}
