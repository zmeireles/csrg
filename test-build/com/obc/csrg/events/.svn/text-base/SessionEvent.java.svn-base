package com.obc.csrg.events;

import org.directwebremoting.ScriptSession;
import org.directwebremoting.proxy.dwr.Util;

import com.obc.csrg.util.ScriptSessionWrapper;

public class SessionEvent extends BasicEventObject{

	private static final long serialVersionUID = 201005131539L;
	
	private ScriptSessionWrapper scriptSessionWrapper;
	
	public SessionEvent(final Object eventSource){
		super(eventSource);
		if(eventSource instanceof ScriptSessionWrapper)
			this.scriptSessionWrapper = (ScriptSessionWrapper)eventSource;
	}

	//getters and setters
	public ScriptSessionWrapper getScriptSessionWrapper() {
		return scriptSessionWrapper;
	}

	public void setScriptSessionWrapper(ScriptSessionWrapper scriptSessionWrapper) {
		this.scriptSessionWrapper = scriptSessionWrapper;
	}
}
