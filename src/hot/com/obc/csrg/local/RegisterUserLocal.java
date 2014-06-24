package com.obc.csrg.local;

import java.util.List;

import javax.ejb.Local;
import javax.faces.event.ValueChangeEvent;

import com.obc.csrg.model.Profile;
import com.obc.csrg.model.User;

@Local
public interface RegisterUserLocal {
	public void create();
	public void destroy();
	public String register();
	public User getNewUser();
	public void setNewUser(User newUser);
	public Boolean getUsernameValid();
	public boolean verifyUsername();
	public String getUsername();
	public String getPassword();
	public void setPassword(String password);
	public String getPasswordConfirmation();
	public void setPasswordConfirmation(String passwordConfirmation);
	public Profile getProfile();
	public void setProfile(Profile profile);
	public List<Profile> getProfiles();
	public void setProfiles(List<Profile> profiles);
	
	public boolean checkUsername();
	public void validateUsername(ValueChangeEvent event);
}
