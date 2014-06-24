package com.obc.csrg.chat;

import java.io.Serializable;

import com.obc.csrg.model.User;
public class ChatUser implements Serializable{

	private User user;
	private String color;
	private String nick;
	private boolean starter=false;
	private boolean onlineSupporter=false;
	private boolean active = true;//by default a user will be online
	
	
	//constructors
	public ChatUser(User user, String nick,String color,boolean starter,boolean onlineSupporter){
		this.user = user;
		this.nick = nick;
		if(color==null)
			color = ChatRoom.DEFAULT_COLOR;
		this.color = color;
		this.starter = starter;
		this.onlineSupporter = onlineSupporter;
	}
	//Business logic
	@Override
    public String toString(){
    	return nick;
    }
	
	//Getter and setters
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public String getNick() {
		return nick;
	}
	public void setNick(String nick) {
		this.nick = nick;
	}
	public boolean isStarter() {
		return starter;
	}
	public void setStarter(boolean starter) {
		this.starter = starter;
	}
	public boolean isOnlineSupporter() {
		return onlineSupporter;
	}
	public void setOnlineSupporter(boolean onlineSupporter) {
		this.onlineSupporter = onlineSupporter;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
}
