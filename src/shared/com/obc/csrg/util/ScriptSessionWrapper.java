package com.obc.csrg.util;

import java.io.Serializable;

import org.directwebremoting.ScriptSession;
import org.directwebremoting.proxy.dwr.Util;

import com.obc.csrg.events.SessionEventAdapter;
import com.obc.csrg.events.SessionEventListener;
import com.obc.csrg.events.SessionEventQueue;

public class ScriptSessionWrapper {
	
	private ScriptSession scriptSession;
	private Util page;
	private SessionEventListener sessionEventListener;

	public ScriptSessionWrapper(){
		super();
		this.registerSessionListener();
	}
	public ScriptSessionWrapper(ScriptSession scriptSession,Util page){
		super();
		this.scriptSession = scriptSession;
		this.page = page;
		this.registerSessionListener();
	}
	
	//business logic
	private final class SessionEventListenerHandler extends SessionEventAdapter implements Serializable{
		public void updateOnlineSupportImage(String jsFunction,String image){
			page.addFunctionCall(jsFunction, image);
		}
	}
	private void registerSessionListener(){
		this.sessionEventListener = new SessionEventListenerHandler();
		SessionEventQueue.addMsgListener(this.sessionEventListener);
	}
	public void unregisterSssionListener(){
		if(this.sessionEventListener!=null){
			SessionEventQueue.removeMsgListener(this.sessionEventListener);
		}
	}
	//getters and setters
	public ScriptSession getScriptSession() {
		return scriptSession;
	}
	public void setScriptSession(ScriptSession scriptSession) {
		this.scriptSession = scriptSession;
	}
	
	public Util getPage() {
		return page;
	}
	public void setPage(Util page) {
		this.page = page;
	}
}
