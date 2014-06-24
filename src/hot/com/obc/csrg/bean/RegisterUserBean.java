package com.obc.csrg.bean;

import java.io.Serializable;
import java.io.File;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.faces.event.ValueChangeEvent;
import javax.persistence.EntityManager;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Begin;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.End;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.web.RequestParameter;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.log.Log;

import com.obc.csrg.constants.ProfileConstant;
import com.obc.csrg.events.ModelEventQueue;
import com.obc.csrg.local.DBCronAppLocal;
import com.obc.csrg.local.RegisterUserLocal;
import com.obc.csrg.local.StateBeanLocal;
import com.obc.csrg.model.Config;
import com.obc.csrg.model.Profile;
import com.obc.csrg.model.User;
import com.obc.csrg.util.ReportFilter;
import com.obc.csrg.notification.EmailNotification;
import com.obc.csrg.model.ChatProfile;
import com.obc.csrg.model.Person;

@Stateful
@Scope(ScopeType.CONVERSATION)
@Name("registerUser")
public class RegisterUserBean implements RegisterUserLocal, Serializable {

	@Logger
	private Log log;

	@In
	private EntityManager entityManager;

	@In
	private FacesMessages facesMessages;

	@In
	private Map<String, String> messages;
	
	@In
	private Locale locale;

	@In
	protected StateBeanLocal stateBean;

	private Config config;

	private User newUser = new User();
	private Boolean usernameValid = null;

	private String username;
	private String password;

	private String passwordConfirmation;

	private Profile profile = null;
	private List<Profile> profiles = new ArrayList<Profile>(0);

	@RequestParameter
	private String ajaxUsername;
	
	@Begin(join = true)
	@Create
	public void create() {
		config = stateBean.getConfig();
		log.debug("#0 [create]", this.toString().substring(
				this.toString().lastIndexOf(".")));
		this.initProfiles();
		log.info("[create] perfisCount:#0", profiles.size());
	}

	@Remove
	public void destroy() {
	}

	@End
	public String register() {
		// stateBean.log4Debug(this.toString(), "[register] init");
		log.info("[register] entered register function");
		String result = null;
		if (registerVerify()) { // verifies if data is valid
			
			log.info("[register] begin register, name:#0, username:#1", 
					newUser.getPerson().getName(),newUser.getUsername());
			config = stateBean.getConfig();
			username = newUser.getUsername().trim().toLowerCase();
			newUser.setRegistrationComplete(true);
			newUser.setPendingLogin(false);
			newUser.setUsername(username);
			newUser.setPassword(User.encrypt(password));
			newUser.getPerson().setName(newUser.getPerson().getName().trim());
			newUser.setLocale(locale.getLanguage());
			newUser.getPerson().setLocale(locale.getLanguage());

			if (profile.getType() == ProfileConstant.ONLINE_SUPPORT.getType()) {
				ChatProfile chatProfile = new ChatProfile();
				chatProfile.setUser(newUser);
				chatProfile.setOnlineHelper(true);
				newUser.setChatProfile(chatProfile);
			}
			//define the profile
			List<Profile> profiles = entityManager.createQuery(
					"select p from Profile p where p.type=?").setParameter(1,
					profile.getType()).setHint(
					"org.hibernate.cacheable", true).getResultList();
			if(profiles.size()>0){
				Profile p = profiles.get(0);
				newUser.addProfile(p);
			}
			entityManager.persist(newUser);
			entityManager.flush();
			ModelEventQueue.fireNewModelEvent(newUser);
			facesMessages.add(newUser.getUsername() + " - "
					+ messages.get("RegisterUserSuccessMessage"));
			// send email
			if (config != null
					&& config.isSendEmailOnRegistration()
					&& newUser.getPerson().getEmail() != null
					&& newUser.getPerson().getEmail().matches(
							"^" + com.obc.csrg.constants.Constants.REGEXP_EMAIL
									+ "$")) {
				Config config = stateBean.getConfig();
				log.info("[register] verificar envio de email, config:#0",
						config);
				if (config != null) {
					this.sendEmail(newUser.getPerson().getEmail());
				}
			}
			result = "successRegister";
		} else {
			// captcha.setResponse("");
		}
		// stateBean.log4Debug(this.toString(), "[register] result: #0",
		// result);
		return result;
	}

	public boolean sendEmail(String emailAddress) {
		EmailNotification email = new EmailNotification(entityManager, log,
				stateBean.getConfig());
		if (config != null) {
			String texto = messages.get("RegisterEmailTextoIntro")
					+ "Username: " + newUser.getUsername() + "<br/>"
					+ "Password: " + password + "<br/>"
					+ messages.get("RegisterEmailTextoIndividuo");
			return email.sendNotification(
					config.getOnRegistrationSenderEmail(), config
							.getOnRegistrationSenderName(), newUser, messages
							.get("RegisterEmailAssunto"), texto, null);
		}

		return false;
	}

	private boolean registerVerify() {
		boolean result = true;
		// USERNAME
		if (!verifyUsername()) {
			facesMessages.addToControl("username", messages
					.get("RegisterUsernameInvalid"));
			result = false;
		}
		// password
		if (!(password != null && password.trim().length() > 0
				&& passwordConfirmation != null
				&& passwordConfirmation.trim().length() > 0 && password
				.equals(passwordConfirmation))){
			facesMessages.addToControl("pwd", messages
					.get("RegisterPasswordInvalid"));
			result = false;
		}
		return result;
	}

	public void validateUsername(ValueChangeEvent event){
		String username = (String)event.getNewValue();
		newUser.setUsername(username);
		log.info("[validateUsername] username:#0", newUser.getUsername());
		
		boolean validUsername = this.verifyUsername();
		if(!validUsername)
			facesMessages.addToControl("username", messages.get("RegisterUsernameNotAvailable"));
		//else
			//facesMessages.addToControl("username", messages.get("RegisterUsernameAvailable"));
	}
	// verifies if there is no repeated username
	public boolean verifyUsername() {
		if(newUser.getUsername()==null || newUser.getUsername().trim().equals(""))
			return false;
		List existing = entityManager.createQuery(
				"select u.username from User u where lower(u.username)=?")
				.setParameter(1, newUser.getUsername().trim().toLowerCase()).getResultList();
		if (existing.size() != 0)
			this.usernameValid = false;
		else
			this.usernameValid = true;
		log.info("[verifyUsername] usernameValid:#0", usernameValid);
		return this.usernameValid.booleanValue();
	}

	public User getNewUser() {
		if (newUser == null) {
			newUser = new User();
			usernameValid = null;
		}
		return newUser;
	}

	private void initProfiles() {
		profiles = entityManager.createQuery(
				"select p from Profile p where p.type in (:typeList)")
				.setParameter("typeList", ProfileConstant.getUserTypes())
				.getResultList();
	}

	
	public boolean checkUsername(){
		
		//this.ajaxUsername = ajaxUsername.trim();
		
		if(ajaxUsername!=null && ajaxUsername.trim().length()>0)
		{
			List existing = entityManager.createQuery(
			"select u.username from User u where lower(u.username)=?")
			.setParameter(1, ajaxUsername.trim().toLowerCase() ).getResultList();
			if (existing.size() != 0)
				return false;
			else
				return true;
		}
		else
			return false;
	}
	
	
	// getters and setters

	public void setNewUser(User newUser) {
		this.newUser = newUser;
	}

	public Boolean getUsernameValid() {
		
		log.info("[getUsernameValid] usernameValid:#0", usernameValid);
		return usernameValid;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPasswordConfirmation() {
		return passwordConfirmation;
	}

	public void setPasswordConfirmation(String passwordConfirmation) {
		this.passwordConfirmation = passwordConfirmation;
	}

	public Profile getProfile() {
		return profile;
	}

	public void setProfile(Profile profile) {
		this.profile = profile;
	}

	public List<Profile> getProfiles() {
		return profiles;
	}

	public void setProfiles(List<Profile> profiles) {
		this.profiles = profiles;
	}

	public void setAjaxUsername(String ajaxUsername) {
		this.ajaxUsername = ajaxUsername;
	}

	public String getAjaxUsername() {
		return ajaxUsername;
	}
}
