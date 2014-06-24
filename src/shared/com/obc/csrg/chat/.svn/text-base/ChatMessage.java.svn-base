package com.obc.csrg.chat;

import java.sql.Time;
import java.io.Serializable;

public class ChatMessage implements Serializable{

	private static final String SEPARATOR = "<br/> ";
	private String message;
	private String color;
	private String timestamp="";
	private ChatUser sender;
	
	//constructors
	public ChatMessage(ChatUser sender, String message,String color){
		this.sender = sender;
		this.message = message;
        this.color = color;
        timestamp = (new Time(System.currentTimeMillis()).toString() + " ");
	}
	//business logic
	/**
     * Method to return a formatted version of this message
     *
     * @return String formatted message
     */
    public String get() {
        if (color != null) {
            return ("<strong><span style='float:right;padding-right:5px'>"+timestamp + "</span><font color=\"" + color + "\">" + sender +
                    "</font></strong>" + SEPARATOR + message);
        }

        return ("<strong><span style='float:right;padding-right:5px'>"+timestamp+ "</span>" + sender + "</font></strong>" + SEPARATOR + message);
    }
    public String getSenderString(){
    	if (color != null) {
            return ("<font color=\"" + color + "\">" + sender.getUser().toString() + "</font>");
        }
    	return sender.getUser().toString();
    }
	//getters and setters
    public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getTimestamp() {
		return "<font color=\"#000000\">" + timestamp + "</font>";
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public ChatUser getSender() {
		return sender;
	}

	public void setSender(ChatUser sender) {
		this.sender = sender;
	}
}
