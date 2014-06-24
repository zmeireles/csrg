package com.obc.csrg.action;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Observer;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.core.Events;
import org.jboss.seam.log.Log;
import org.jboss.seam.security.Credentials;
import org.jboss.seam.security.Identity;

import com.obc.csrg.local.StateBeanLocal;
import com.obc.csrg.local.Authenticator;

import com.obc.csrg.model.User;
import com.obc.csrg.model.UserProfile;
import com.obc.csrg.constants.ProfileConstant;
import com.obc.csrg.local.StateBeanLocal;

@Stateless
@Name("authenticator")
public class AuthenticatorBean implements Authenticator {
	
	@Logger
	private Log log;

	@In
	Identity identity;
	
	@In
	Credentials credentials;
	
	@In 
    private EntityManager entityManager;
	
	@In(required=false)
    private StateBeanLocal stateBean;
	
	//@In(create=true)
	//private StateBeanLocal stateBean;

	@Out(required = false, scope = ScopeType.SESSION)
	private User user;
	
	public boolean authenticate() {
		if(identity.getCredentials().getPassword()==null)
			identity.getCredentials().setPassword("");
		log.info("authenticating {0}, pwd:#1", credentials.getUsername(),identity.getCredentials().getPassword());
		// write your authentication logic here,
		// return true if the authentication was
		// successful, false otherwise
		String pwd = User.encrypt(identity.getCredentials().getPassword());
		
		log.info("[authenticate] username:#0", identity.getCredentials().getUsername());
		String query = "select u from User u where u.active=true and lower(u.username)=lower(?)";
		//String query = "select u from User u where u.active=true and lower(u.username)=lower(?) and u.password=? ";
		try{
			User user = (User)entityManager.createQuery(query)
					.setParameter(1, identity.getCredentials().getUsername())
					//.setParameter(2, pwd)
					.getSingleResult();
			
			//create permissions
			for(UserProfile p:user.getProfiles()){
				for(String role:ProfileConstant.valueOf(p.getProfile().getType()).getChildrenList()){
					identity.addRole(role);
					//log.info("[authenticate] addRole: #0", role);
				}
			}
			this.user = user;
			stateBean.setUser(user);
			//Events.instance().raiseAsynchronousEvent("newLogin",user);
			return true;
		}catch(Exception e){
			identity.getCredentials().setPassword(null);
			identity.getCredentials().setUsername(null);
			this.user = null;
			log.info("[authenticate] failed: #0", e.getMessage());
			return false;
		}
		/*
		if ("admin".equals(credentials.getUsername())) {
			identity.addRole("admin");
			return true;
		}
		return false;*/
	}
	public String logoutUser(){
		identity.logout();
		log.info("[logoutUser]");
		return "/home.xhtml";
	}

	@Observer("org.jboss.seam.preDestroyContext.SESSION")
    @Transactional
    public void logout() {
    	if(stateBean!=null)
    		user = stateBean.getUser();
    	
    	if (user != null){
    		log.info("[logout] user:#0", user);
	  	    System.out.println("LOGOUT OF " + user.getUsername());
	  	    
	  	    if(stateBean!=null){
	  	    	stateBean.unregisterUser();
	  	    }
    	}//else System.out.println("LOGOUT");
    }
}
