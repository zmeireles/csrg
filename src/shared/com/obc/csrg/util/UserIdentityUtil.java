package com.obc.csrg.util;

import java.io.Serializable;

import org.jboss.seam.security.Identity;

import com.obc.csrg.model.User;
import com.obc.csrg.bean.StateBean;

public class UserIdentityUtil implements Serializable{
	
	private static final long serialVersionUID = 200810061842L;

	private User user;
	private StateBean stateBean;
	private Identity identity;
	private int onlineSupportsRequested = 0;
	private Long sessionId = 0L;
	private String ipAddress="";
	
	//Constructors
	public UserIdentityUtil() {
		this.user = null;
		this.stateBean = null;
	}

	public UserIdentityUtil(User user, StateBean stateBean) {
		this.user = user;
		this.stateBean = stateBean;
		stateBean.setUserIdentity(this);
		if(stateBean!=null){
			this.identity = stateBean.getIdentity();
			this.ipAddress = stateBean.getIP();
		}
	}
	//Business logic
	
	@Override
	public boolean equals(Object o){
		//System.err.println("[UserIdentityUtil] [equals] start");
		if(o instanceof UserIdentityUtil){
			UserIdentityUtil uiObject = ((UserIdentityUtil)o);
			return this.sessionId.equals(uiObject.getSessionId());
			/*
			//System.err.println("[UserIdentityUtil] [equals] uiObject: "+uiObject);
			if(this.getUser()!=null && uiObject.getUser()!=null){
				if(this.stateBean==null || uiObject.getStateBean()==null){
					//System.err.println("[UserIdentityUtil] [equals] 4");
					return this.getUser().getId().equals(uiObject.getUser().getId());
				}else{
					//System.err.println("[UserIdentityUtil] [equals] 5");
					return (this.getUser().getId().equals(uiObject.getUser().getId()) && (this.stateBean.equals(uiObject.getStateBean())));
				}
			}*/
		}
		return false;
	}
	
	@Override
	public String toString(){
		String result = user.toString();
		return result;
	}
	
	//Getters and setters
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public StateBean getStateBean() {
		return stateBean;
	}

	public void setStateBean(StateBean stateBean) {
		this.stateBean = stateBean;
	}
	
	public boolean isLoggedIn(){
		return this.stateBean.isLoginDone();
	}
	
	public Identity getIdentity() {
		return identity;
	}

	public void setIdentity(Identity identity) {
		this.identity = identity;
	}
	public int getOnlineSupportsRequested() {
		return onlineSupportsRequested;
	}

	public void setOnlineSupportsRequested(int onlineSupportsRequested) {
		this.onlineSupportsRequested = onlineSupportsRequested;
	}

	public Long getSessionId() {
		return sessionId;
	}

	public void setSessionId(Long sessionId) {
		this.sessionId = sessionId;
	}
	
	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

}
