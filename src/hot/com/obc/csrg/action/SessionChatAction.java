package com.obc.csrg.action;

import javax.ejb.Remove;
import javax.ejb.Stateless;
import javax.faces.event.ActionEvent;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

import org.jboss.seam.Component;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.Destroy;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;

import com.icesoft.faces.async.render.SessionRenderer;

import com.obc.csrg.local.ApplicationLocal;
import com.obc.csrg.local.SessionChatLocal;
import com.obc.csrg.local.StateBeanLocal;
import com.obc.csrg.bean.ApplicationBean;
import com.obc.csrg.chat.ChatRoom;
import com.obc.csrg.events.ChatEvent;
import com.obc.csrg.events.ChatEventAdapter;
import com.obc.csrg.events.ChatEventListener;
import com.obc.csrg.events.ChatEventQueue;
import com.obc.csrg.model.User;
import com.obc.csrg.chat.ChatUser;

@Scope(ScopeType.SESSION)
@Name("sessionChatAction")
public class SessionChatAction implements SessionChatLocal, Serializable{

	@Logger
	private Log log;
	
	@In(required=false)
    private StateBeanLocal stateBean;
	
	@In(required = false)
	private ApplicationLocal applicationBean;
	
	private User myUser;
	
	private List<ChatRoom> chats = new ArrayList<ChatRoom>();
	private ChatEventListener chatEventListener;
	
	private String message="";
	private List<Message> myMessages = new ArrayList<Message>();
	
	
	@Create
	public void create() {
		this.registerChatListener();
		log.info("[create] stateBean user:#0", stateBean.getUser());
		if(stateBean.getUser()!=null){
			SessionRenderer.addCurrentSession("chat//" + stateBean.getUser().getUsername());
			myUser = stateBean.getUser();
			log.info("[create] add session:#0", "chat//" + stateBean.getUser().getUsername());
		}
	}
	@Remove
	@Destroy
	public void destroy() {
		this.unregisterChatListener();
		if(myUser!=null){
			//leave all conversations without triggering events (or concurrent problems will arise)
			for(ChatRoom r:this.chats){
				for(ChatUser cu:r.getUsers()){
					if(cu.getUser().getId().equals(myUser.getId())){
						r.deactivateUser(myUser);
						if(r.activeUsersCount()==0){
							ChatEventQueue.fireEndChatEvent(r);
						}
					}
				}
				//ChatEventQueue.fireLeaveChatEvent(r, myUser);
			}
			SessionRenderer.removeCurrentSession("chat//" + myUser.getUsername());
			log.info("[destroy] remove myUser session:#0", "chat//" + myUser.getUsername());
		}
	}
	
	public void reRegisterSessionRenderer(){
		if(myUser!=null){
			//SessionRenderer.removeCurrentSession("chat//" + myUser.getUsername());
			SessionRenderer.addCurrentSession("chat//" + myUser.getUsername());
		}
	}
	public final class Message implements Serializable{
		
		private String message="";
		private Long roomId;
		
		public Message(Long roomId){
			this.roomId = roomId;
		}
		public String getMessage() {
			return message;
		}
		public void setMessage(String message) {
			this.message = message;
		}
		public Long getRoomId() {
			return roomId;
		}
		public void setRoomId(Long roomId) {
			this.roomId = roomId;
		}
		
	}
	private boolean removeMessageObject(Long roomId){
		for(Message m:this.myMessages){
			if(m.roomId.equals(roomId))
				return this.myMessages.remove(m);
		}
		return false;
	}
	private boolean addMessageObject(Long roomId){
		return this.myMessages.add(new Message(roomId));
	}
	public Message getMessageObject(Long roomId){
		for(Message m:this.myMessages){
			if(m.roomId.equals(roomId))
				return m;
		}
		return null;
	}
	
	//chat events
	private final class ChatEventHandler extends ChatEventAdapter implements Serializable{
		@Override
		public synchronized void newChat(ChatEvent e){
			//only logged in users allowed to chat
			ChatRoom room = (ChatRoom)e.getSource();
			reRegisterSessionRenderer();
			//user = (User) Component.getInstance("user");
			User user = myUser;
			log.info("[newChat] room:#0, stateBean.user:#1", room,user);
			if(user==null)
				return;
			//verify if my user is in the chat room
			if(room.isUserInChatRoom(user)){
				//register to the chat room sessionRenderer
				for(ChatRoom r:chats){
					if(r.getName().equals(room.getName())){
						chats.remove(r);
						break;
					}
				}
				chats.add(room);
				addMessageObject(room.getRoomId());
				log.info("[newChat] current chats for user:#0, are :#1", user,chats);
				SessionRenderer.render("chat//" + user.getUsername());
				log.info("[newChat] render session:#0", "chat//" + user.getUsername());
			}
		}
		@Override
		public synchronized void leaveChat(ChatEvent e){
			ChatRoom room = (ChatRoom)e.getSource();
			reRegisterSessionRenderer();
			User user = myUser;
			if(user==null)
				return;
			if(user.getId().equals(e.getLeavingChatUser().getId())){
				if(room.isUserInChatRoom(user)){
					room.deactivateUser(user);
					if(room.activeUsersCount()==0){
						//end chat
						log.info("[leaveChat] no active users, end chat");
						ChatEventQueue.fireEndChatEvent(room);
					}else{
						log.info("[leaveChat] still active users, refresh browser");
						SessionRenderer.render("chat//" + user.getUsername());
					}
				}
			}
		}
		@Override
		public synchronized void endChat(ChatEvent e){
			ChatRoom room = (ChatRoom)e.getSource();
			reRegisterSessionRenderer();
			User user = myUser;
			if(user==null)
				return;
			if(room.isUserInChatRoom(user)){
				room.removeUser(user);
				log.info("[endChat] user:#0", user);
				for(ChatRoom c:chats){
					if(c.getRoomId().equals(room.getRoomId())){
						chats.remove(c);
						removeMessageObject(c.getRoomId());
						SessionRenderer.render("chat//" + user.getUsername());
						break;
					}
				}
			}
		}
		@Override
		public synchronized void newMessage(ChatEvent e){
			ChatRoom room = (ChatRoom)e.getSource();
			reRegisterSessionRenderer();
			User user = myUser;
			if(user==null)
				return;
			if(room.isUserInChatRoom(user)){
				if(!room.isUserActive(user))
					room.reactivateUser(user);
				log.info("[newMessage] user:#0, refresh session:#1", user,"chat//" + user.getUsername());
				SessionRenderer.render("chat//" + user.getUsername());
			}
		}
	}
	
	public void registerChatListener(){
		this.unregisterChatListener();
		this.chatEventListener = new ChatEventHandler();
		ChatEventQueue.addMsgListener(this.chatEventListener);
		log.info("[registerChatListener]");
	}
	public void unregisterChatListener(){
		if(this.chatEventListener!=null){
			ChatEventQueue.removeMsgListener(this.chatEventListener);
		}
		log.info("[unregisterChatListener]");
	}
	
	public boolean isInChatWith(User user){
		//log.info("[isInChatWithUser] user:#0, myChats:#1", user,chats);
		if(user!=null && myUser!=null && !user.getId().equals(myUser.getId())){
			for(ChatRoom r:this.chats){
				//log.info("[isInChatWithUser] isUserInChatRoom:#0", r.isUserInChatRoom(user));
				if(r.isUserInChatRoom(user))
					return true;
			}
		}
		return false;
	}
	public boolean isInActiveChatWith(User user){
		if(user!=null && myUser!=null && !user.getId().equals(myUser.getId())){
			for(ChatRoom r:this.chats){
				//log.info("[isInChatWithUser] isUserInChatRoom:#0", r.isUserInChatRoom(user));
				if(r.isUserInChatRoom(user) && r.isUserActive(myUser))
					return true;
			}
		}
		return false;
	}
	/**
	 * 
	 * @param user
	 * @return true if stateBean.user is the starter of chat with user
	 */
	public boolean isChatStarter(User user){
		if(user!=null && myUser!=null && !user.getId().equals(myUser.getId())){
			for(ChatRoom r:this.chats){
				if(r.isUserInChatRoom(user) && r.isStarter(myUser))
					return true;
			}
		}
		return false;
	}
	/**
	 * returns the room id of a chat room where sessionChatAction user is the starter and user a participant
	 * null if no such room exists
	 * @param user
	 * @return
	 */
	public Long getChatStarterRoomId(User user){
		if(user!=null && myUser!=null && !user.getId().equals(myUser.getId())){
			for(ChatRoom r:this.chats){
				Long id = r.getStarterRoomId(myUser, user);
				if(id!=null)
					return id;
			}
		}
		return null;
	}
	/**
	 * returns the room id of a chat room where sessionChatAction user and user in parameter are participants
	 * null if no such room exists
	 * @param user
	 * @return
	 */
	public Long getChatRoomId(User user){
		if(user!=null && myUser!=null && !user.getId().equals(myUser.getId())){
			for(ChatRoom r:this.chats){
				if(r.isUserInChatRoom(user))//don't need to check on myUser.
					return r.getRoomId();
			}
		}
		return null;
	}
	
	public synchronized void sendMessage(ActionEvent event) {
		Long roomId = (Long)event.getComponent().getAttributes().get("roomId");
		String message = (String)event.getComponent().getAttributes().get("message");
		log.info("[sendMessage] roomId:#0, message:#1", roomId,message);
		//this.message = "";
		if(this.getMessageObject(roomId)!=null)
			this.getMessageObject(roomId).setMessage("");
		if(message.trim().equals(""))
			return;
		for(ChatRoom room:this.chats){
			if(room.getRoomId().equals(roomId)){
				room.addMessage(myUser, message);
				ChatEventQueue.fireNewMessageEvent(room, myUser, message);
				break;
			}
		}
	}
	
	public List<ChatRoom> getActiveChats(){
		List<ChatRoom> result = new ArrayList<ChatRoom>();
		for(ChatRoom r:this.chats){
			if(r.isUserActive(myUser))
				result.add(r);
		}
		return result;
	}
	
	public String getHtmlUsersStringButMe(Long roomId){
		for(ChatRoom r:this.chats){
			if(roomId.equals(r.getRoomId()))
				return r.getHtmlChattersButMe(myUser);
		}
		return "";
	}
	
	public String obtainFirstLastName(String name, int size){
		
		name = name.trim();
		size--;
		if(name.contains(" "))
		{
			String firstName = name.substring(0, name.indexOf(" "));
			String lastName = name.substring(name.lastIndexOf(" "),name.length());
			
			String firstNameLastName = firstName+lastName;
			
			if(firstNameLastName.length()> size)
				firstNameLastName = firstNameLastName.substring(0, size);

			return firstNameLastName;
		
		}
		else
		{
			if(name.length()> size)
				return name.substring(0, size);
			else
				return name;
		}
	}
	
	/**
	 * returns the other user in the chat
	 * @param room
	 * @return
	 */
	public User getTargetUser(ChatRoom room){
		for(ChatUser user:room.getUsers()){
			if(!user.getUser().getId().equals(myUser.getId()))
				return user.getUser();
		}
		return null;
	}
	//getters and setters
	public User getMyUser() {
		return myUser;
	}
	public void setMyUser(User myUser) {
		log.info("[setMyUser] myUser:#0", myUser);
		if(this.myUser!=null)//remove old session
			SessionRenderer.removeCurrentSession("chat//" + myUser.getUsername());
		if(myUser!=null){//add new session
			SessionRenderer.addCurrentSession("chat//" + myUser.getUsername());
			log.info("[setMyUser] addCurrentSession:#0", "chat//" + myUser.getUsername());
			//recover still active chats
			if(this.chats.size()==0){
				chats = applicationBean.recoverChats(myUser);
				log.info("[setMyUser] recovered chats:#0",chats);
				//rebuild input messages
				for(ChatRoom r:this.chats){
					addMessageObject(r.getRoomId());
				}
			}
		}
		this.myUser = myUser;
	}
	
	public List<ChatRoom> getChats(){
		return chats;
	}
	
	public void setChats(List<ChatRoom> chats) {
		this.chats = chats;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public List<Message> getMyMessages() {
		return myMessages;
	}
	public void setMyMessages(List<Message> myMessages) {
		this.myMessages = myMessages;
	}
}
