package com.obc.csrg.chat;

import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Vector;

import java.io.Serializable;

import com.obc.csrg.model.User;

public class ChatRoom implements Serializable{

	private List<ChatUser> users = new ArrayList<ChatUser>();
	private String name;
	private ChatMessageLog chatMessageLog;
	private boolean onlineSupport = false;
	
	public static final String DEFAULT_COLOR = "#000000";
    private static final String[] ALL_COLORS = {"33CC33", "660033", "FF0033",
                                                "FF6633", "FFCC33", "FFFF33",
                                                "3333CC", "33CCCC", "6600CC",
                                                "FF00CC", "CC00CC", "00FF99",
                                                "99FF33", "996633", "990033",
                                                "999999", "CCCCCC", "000000"};
    private Random generator = new Random(System.currentTimeMillis());
    private Vector colorList = new Vector(Arrays.asList(ALL_COLORS));
    public static Long ROOM_ID = 1L;
    private Long roomId;
    
	//constructors
    
	public ChatRoom(String name){
    	this.name = name;
    	this.setRoomId();
    }
    
	//business logic
	private synchronized void setRoomId(){
    	this.roomId = ROOM_ID ++;
    }
	
    @Override
    public String toString(){
    	return this.name;
    }
	/**
     * Method to randomly select an HTML color code from a preset list
     * eg: #C62FD5
     *
     * @return String hex value (to be used directly in HTML tags)
     */
    public String generateColorCode() {
        return "#" + colorList.remove(generator.nextInt(colorList.size())).toString();
    }
    
    public boolean isUserInChatRoom(User user){
    	for(ChatUser u:users){
    		if(u.getUser().getId().equals(user.getId()))
    			return true;
    	}
    	return false;
    }
    public boolean removeUser(User user){
    	for(ChatUser u:users){
    		if(u.getUser().getId().equals(user.getId()))
    			return users.remove(u);
    	}
    	return false;
    }
    
    public boolean isStarter(User user){
    	for(ChatUser u:users){
    		if(u.getUser().getId().equals(user.getId()) && u.isStarter())
    			return true;
    	}
    	return false;
    }
    
    /**
     * 
     * @param starter
     * @param participant
     * @return the room id if starter is the starter and participant is in the room
     */
    public Long getStarterRoomId(User starter,User participant){
    	if(starter.getId().equals(participant.getId()))
    		return null;
    	if(isStarter(starter) && this.isUserInChatRoom(participant))
    		return this.roomId;
    	return null;
    }
    public String messageLogToHtml(){
    	String result ="";
    	if(this.chatMessageLog==null)
    		return "<br/>";
    	for(int i=0;i<this.chatMessageLog.size();i++){
    		if(i==0)
    			result = this.chatMessageLog.getMessageAt(i);
    		else
    			result += "<br/>" + this.chatMessageLog.getMessageAt(i);
    	}
    	return result;
    }
    public void addMessage(User user,String message){
    	if(message.trim().equals(""))
    		return;
    	if(this.chatMessageLog==null)
    		this.chatMessageLog = new ChatMessageLog(0);
    	for(ChatUser u:this.users){
    		if(u.getUser().getId().equals(user.getId())){
    			//this.chatMessageLog.add(new ChatMessage(u,message,u.getColor()));
    			this.chatMessageLog.addMessage(u, message, u.getColor());
    			return;
    		}
    	}
    }
    public void deactivateUser(User user){
    	for(ChatUser u:this.users){
    		if(u.getUser().getId().equals(user.getId())){
    			u.setActive(false);
    			break;
    		}
    	}
    }
    public void reactivateUser(User user){
    	for(ChatUser u:this.users){
    		if(u.getUser().getId().equals(user.getId())){
    			u.setActive(true);
    			break;
    		}
    	}
    }
    public boolean isUserActive(User user){
    	for(ChatUser u:this.users){
    		if(u.getUser().getId().equals(user.getId())){
    			return u.isActive();
    		}
    	}
    	return false;
    }
    public int activeUsersCount(){
    	int result = 0;
    	for(ChatUser u:this.users){
			if(u.isActive())
				result++;
    	}
    	return result;
    }
    public String getHtmlChattersButMe(User user){
    	String result = "";
    	for(ChatUser cu:this.users){
    		if(!cu.getUser().getId().equals(user.getId())){
    			if(result=="")
    				result = cu.getUser().getUsername();
    			else
    				result += ", " +cu.getUser().getUsername();
    		}
    	}
    	return result;
    }
    /**
     * returns a different user from the root user
     * @param rootUser
     * @return
     */
    public User getTargetUser(User rootUser){
    	for(ChatUser user:this.getUsers()){
			if(!user.getUser().getId().equals(rootUser.getId()))
				return user.getUser();
		}
    	return null;
    }
	//getters and setters
	public List<ChatUser> getUsers() {
		return users;
	}
	public void setUsers(List<ChatUser> users) {
		this.users = users;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public ChatMessageLog getChatMessageLog() {
		return chatMessageLog;
	}
	public void setChatMessageLog(ChatMessageLog chatMessageLog) {
		this.chatMessageLog = chatMessageLog;
	}
	public boolean isOnlineSupport() {
		return onlineSupport;
	}

	public void setOnlineSupport(boolean onlineSupport) {
		this.onlineSupport = onlineSupport;
	}
	
	public Long getRoomId() {
		return roomId;
	}

	public void setRoomId(Long roomId) {
		this.roomId = roomId;
	}
}
