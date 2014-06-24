package com.obc.csrg.events;

import javax.swing.event.EventListenerList;

import com.obc.csrg.model.User;

public class ChatEventQueue {

	private static final EventListenerList msgListeners = new EventListenerList();
	
	public static void addMsgListener(final ChatEventListener listener){
		msgListeners.add(ChatEventListener.class, listener);
	}
	public static void removeMsgListener(final ChatEventListener listener){
		msgListeners.remove(ChatEventListener.class, listener);
	}
	
	public static void fireNewChatEvent(final Object source){
		if(source!=null){
			if(msgListeners.getListenerList().length>0){
				final ChatEvent chatEvent = new ChatEvent(source);
				final Object[] listeners = msgListeners.getListenerList();
				for(int i=0;i<listeners.length;i+=2){
					((ChatEventListener) listeners[i+1]).newChat(chatEvent);
				}
			}
		}
	}
	public static void fireLeaveChatEvent(final Object source,User leavingChatUser){
		if(source!=null){
			if(msgListeners.getListenerList().length>0){
				final ChatEvent chatEvent = new ChatEvent(source, leavingChatUser);
				final Object[] listeners = msgListeners.getListenerList();
				for(int i=0;i<listeners.length;i+=2){
					((ChatEventListener) listeners[i+1]).leaveChat(chatEvent);
				}
			}
		}
	}
	public static void fireEndChatEvent(final Object source){
		if(source!=null){
			if(msgListeners.getListenerList().length>0){
				final ChatEvent chatEvent = new ChatEvent(source);
				final Object[] listeners = msgListeners.getListenerList();
				for(int i=0;i<listeners.length;i+=2){
					((ChatEventListener) listeners[i+1]).endChat(chatEvent);
				}
			}
		}
	}
	public static void fireNewMessageEvent(final Object source,User sender,String message){
		if(source!=null){
			if(msgListeners.getListenerList().length>0){
				final ChatEvent chatEvent = new ChatEvent(source,sender,message);
				final Object[] listeners = msgListeners.getListenerList();
				for(int i=0;i<listeners.length;i+=2){
					((ChatEventListener) listeners[i+1]).newMessage(chatEvent);
				}
			}
		}
	}
}
