package com.obc.csrg.local;

import java.util.List;

import javax.ejb.Local;

import org.directwebremoting.ScriptSession;

import com.obc.csrg.util.ScriptSessionWrapper;
import com.obc.csrg.util.UserIdentityUtil;
import com.obc.csrg.bean.StateBean;
import com.obc.csrg.chat.ChatRoom;
import com.obc.csrg.model.User;

@Local
public interface ApplicationLocal {

	public void create();
	public void destroy();
	
	public void userLoggedIn(User user, StateBean stateBean);
	public boolean notInExceptionPages(String page);
	public void unloadScriptSession(ScriptSession session);
	public List<ScriptSessionWrapper> getScriptSessions();
	public void setScriptSessions(List<ScriptSessionWrapper> scriptSessions);
	public Long getUsersCount();
	public boolean userLoggedOut(User user, StateBean stateBean);
	public List<UserIdentityUtil> getUserIdentityList();
	public List<ChatRoom> recoverChats(User user);
}
