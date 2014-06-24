package com.obc.csrg.local;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Remove;

import org.directwebremoting.ScriptSession;
import org.directwebremoting.proxy.dwr.Util;
import org.jboss.seam.annotations.Create;

import com.obc.csrg.chat.ChatRoom;
import com.obc.csrg.constants.ProfileConstant;
import com.obc.csrg.events.SessionEventQueue;
import com.obc.csrg.model.User;
import com.obc.csrg.model.Config;
import com.obc.csrg.util.ScriptSessionWrapper;
import com.obc.csrg.util.UserIdentityUtil;

@Local
public interface StateBeanLocal {

	@Create
	public void create();
	@Remove
	public void destroy();
	public boolean isManager();
	public String myPath();
	public Config getConfig();
	public String popBackLinkList();
	public boolean showBackLink();
	public void log4Debug(final String beanOrigemToString, String msg,
			Object... arg1);
	public String getDir(String...profiles);
	public String getDir();
	public boolean hasRoleAdmin();
	public boolean hasRoleContentManager();
	public boolean hasRoleSysAdmin();
	public boolean hasRoleAccessManager();
	public void echo(String s);
	public String getTimezone();
	public void setTimezone(String timezone);
	public void echoContent();
	
	public User getUser();
	public void setUser(User user);
	
	public ScriptSession getScriptSession();
	public void setScriptSession(ScriptSession scriptSession);
	public Util getMyPage();
	public void setMyPage(Util myPage);
	
	public void fireNewSession();
	public String getInstanceString();
	public void unregisterUser();

	public String getIP();
	public boolean canSeeOnlineUsers();
	
	public UserIdentityUtil getUserIdentity();
	public void setUserIdentity(UserIdentityUtil userIdentity);
}
