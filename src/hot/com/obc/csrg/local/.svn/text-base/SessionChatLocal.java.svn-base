package com.obc.csrg.local;

import java.util.List;

import javax.ejb.Local;
import javax.faces.event.ActionEvent;

import com.obc.csrg.action.SessionChatAction.Message;
import com.obc.csrg.chat.ChatRoom;
import com.obc.csrg.model.User;

@Local
public interface SessionChatLocal {

	public void create();
	public void destroy();
	public List<ChatRoom> getChats();
	public void setChats(List<ChatRoom> chats);
	public User getMyUser();
	public void setMyUser(User myUser);
	
	public boolean isInChatWith(User user);
	public Long getChatStarterRoomId(User user);
	public boolean isChatStarter(User user);
	public void sendMessage(ActionEvent event);
	public Message getMessageObject(Long roomId);
	public boolean isInActiveChatWith(User user);
	public List<ChatRoom> getActiveChats();
	public String getHtmlUsersStringButMe(Long roomId);
	public String obtainFirstLastName(String name, int size);
	public User getTargetUser(ChatRoom room);
}
