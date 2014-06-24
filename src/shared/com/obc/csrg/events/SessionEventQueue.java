package com.obc.csrg.events;

import javax.swing.event.EventListenerList;

public class SessionEventQueue {

	private static final EventListenerList msgListeners = new EventListenerList();
	
	public static void addMsgListener(final SessionEventListener listener){
		msgListeners.add(SessionEventListener.class, listener);
	}
	public static void removeMsgListener(final SessionEventListener listener){
		msgListeners.remove(SessionEventListener.class, listener);
	}
	
	public static void fireNewSessionEvent(final Object source){
		if(source!=null){
			if(msgListeners.getListenerList().length>0){
				final SessionEvent sessionEvent = new SessionEvent(source);
				final Object[] listeners = msgListeners.getListenerList();
				for(int i=0;i<listeners.length;i+=2){
					((SessionEventListener) listeners[i+1]).newSession(sessionEvent);
				}
			}
		}
	}
	public static void fireUpdateOnlineSupport(String jsFunction,String image){
		if(msgListeners.getListenerList().length>0){
			final Object[] listeners = msgListeners.getListenerList();
			for(int i=0;i<listeners.length;i+=2){
				((SessionEventListener) listeners[i+1]).updateOnlineSupportImage(jsFunction, image);
			}
		}
	}
}
