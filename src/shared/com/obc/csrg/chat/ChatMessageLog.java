package com.obc.csrg.chat;

import java.util.ArrayList;

public class ChatMessageLog extends ArrayList<ChatMessage>{

	private static final String DEFAULT_COLOR = "Black";
	
	//constructors
	public ChatMessageLog(int size){
		super(size);
	}
	
	//business logic
	public String getMessageAt(int index) {
        return (((ChatMessage) get(index)).get());
    }
	
	public boolean addMessage(ChatUser sender, String message,String color){
		if(color==null)
			color= DEFAULT_COLOR;
		
		add(new ChatMessage(sender,message,color));
		return true;
	}
}
