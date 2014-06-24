package com.obc.csrg.model;

import static javax.persistence.GenerationType.SEQUENCE;
import static org.jboss.seam.ScopeType.SESSION;

import java.io.Serializable;
import java.security.MessageDigest;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

import com.obc.csrg.constants.Constants;
import com.obc.csrg.events.ModelEventQueue;

@Entity
@Name("user")
@Scope(SESSION)
@Table(name = "user_csrg")
public class User extends Model implements Serializable {

	@SequenceGenerator(name = "generator")
	@Id
	@GeneratedValue(strategy = SEQUENCE, generator = "generator")
	@Column(name = "user_csrg_id", nullable = false)
	private Long id;
	
	private String username = "";

	@OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE,
			CascadeType.REMOVE }, fetch = FetchType.EAGER, mappedBy = "user")
	//@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
	private Set<UserProfile> profiles = new HashSet<UserProfile>(0);

	@OneToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE,
			CascadeType.REFRESH, CascadeType.REMOVE }, fetch = FetchType.EAGER)
	@JoinColumn(name = "person_id")
	private Person person = new Person(this);
	
	private String password = "";
	
	@OneToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE,
			CascadeType.REMOVE }, fetch = FetchType.LAZY)
	@JoinColumn(name = "chat_profile_id")
	private ChatProfile chatProfile;
	
	@Column(name = "pending_login")
	private boolean pendingLogin = true; // assigned to true after first login
	
	@Column(name = "reg_complete")
	private boolean registrationComplete = false; // assigned to true after
	// completing the post
	// registration wizard (if
	// one exists)

	@OneToMany(cascade = { CascadeType.MERGE,
			CascadeType.REFRESH }, fetch = FetchType.LAZY, mappedBy = "destinationUser")
	@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
	private Set<SmsLog> smsReceived = new HashSet<SmsLog>(0);
	
	@OneToMany(cascade = { CascadeType.MERGE,
			CascadeType.REFRESH }, fetch = FetchType.LAZY, mappedBy = "sourceUser")
	@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
	private Set<SmsLog> smsSent = new HashSet<SmsLog>(0);
	
	@OneToMany(cascade = { CascadeType.MERGE,
			CascadeType.REFRESH }, fetch = FetchType.LAZY, mappedBy = "user")
	@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
	private Set<SessionLog> sessionLog = new HashSet<SessionLog>(0);
	
	@OneToMany(cascade = { CascadeType.MERGE,
			CascadeType.REFRESH }, fetch = FetchType.LAZY, mappedBy = "user")
	@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
	private Set<UserDepartment> departments = new HashSet<UserDepartment>(0);
	
	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "service_area_id")
	private ServiceArea serviceArea;
	
	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "prof_category_id")
	private ProfCategory profCategory;

	@OneToMany(cascade = {CascadeType.MERGE,
			CascadeType.REFRESH }, fetch = FetchType.LAZY, mappedBy = "user")
	@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
	private Set<UserNotification> notifications = new HashSet<UserNotification>(0);
	
	@OneToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE,
			CascadeType.REFRESH }, fetch = FetchType.LAZY, mappedBy = "createdBy")
	@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
	private Set<News> createdNews = new HashSet<News>(0);
	
	@OneToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE,
			CascadeType.REFRESH }, fetch = FetchType.LAZY, mappedBy = "createdBy")
	@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
	private Set<News> approvedNews = new HashSet<News>(0);
	
	@OneToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE,
			CascadeType.REFRESH }, fetch = FetchType.LAZY, mappedBy = "sender")
	@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
	private Set<Notification> notificationsSent = new HashSet<Notification>(0);
	
	@OneToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE,
			CascadeType.REFRESH }, fetch = FetchType.LAZY, mappedBy = "sender")
	@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
	private Set<ComposedNotification> composedNotificationsSent = new HashSet<ComposedNotification>(0);
	
	@OneToMany(cascade = { CascadeType.MERGE,
			CascadeType.REFRESH }, fetch = FetchType.LAZY, mappedBy = "user")
	@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
	private Set<DataChangeValue> changedValues = new HashSet<DataChangeValue>(0);
	
	private String locale=Constants.DEFAULT_LOCALE;
	
	private boolean active = true;
	// constructors

	public User() {

	}

	// business functions
	
	public String returnFaxFullNumber(){
		if(this.person!=null)
			return person.returnFaxFullNumber();
		return null;
	}
	
	/**
	 * do all the logic in order to remove the entity from the database
	 */
	@Override
	public boolean remove(EntityManager entityManager){
		ModelEventQueue.fireOnBeforeModelRemoveEvent(this);
		//remove person
		this.person.setUser(null);
		entityManager.remove(this.person);
		this.person=null;
		//remove profiles
		for(UserProfile p:this.profiles){
			p.setProfile(null);
			p.setUser(null);
			entityManager.remove(p);
		}
		this.profiles.clear();
		//remove chat profile
		if(this.chatProfile!=null){
			this.chatProfile.setUser(null);
			entityManager.remove(chatProfile);
			this.chatProfile=null;
		}
		//remove professional category relation
		if(this.profCategory!=null){
			
		}
		//remove user departments
		for(UserDepartment d:this.departments){
			d.getDepartment().getUsers().remove(d);
			d.setDepartment(null);
			d.setUser(null);
			entityManager.remove(d);
		}
		this.departments.clear();
		//remove service area relation
		if(this.serviceArea!=null){
			this.serviceArea.getUsers().remove(this);
			this.serviceArea=null;
		}
		//remove notifications relation
		for(UserNotification n:this.notifications){
			n.setUser(null);
			n.getNotification().getUsers().remove(n);
			n.setNotification(null);
			entityManager.remove(n);
		}
		this.notifications.clear();
		//remove news relation
		for(News n:this.approvedNews){
			n.setApprovedBy(null);
			entityManager.merge(n);
		}
		this.approvedNews.clear();
		for(News n:this.createdNews){
			n.setCreatedBy(null);
			entityManager.merge(n);
		}
		this.createdNews.clear();
		//remove sent notifications relation
		for(Notification n:this.notificationsSent){
			n.setSender(null);
			entityManager.merge(n);
		}
		this.notificationsSent.clear();
		
		//remove composed notifications relation
		for(ComposedNotification cn:this.composedNotificationsSent){
			cn.setSender(null);
			entityManager.merge(cn);
		}
		this.composedNotificationsSent.clear();
		
		//remove sessionLog links
		for(SessionLog session:this.sessionLog){
			session.setUser(null);
			entityManager.merge(session);
			
		}
		this.sessionLog.clear();
		
		//remove changeValues link
		for(DataChangeValue dcv:this.changedValues){
			dcv.setUser(null);
			entityManager.merge(dcv);
		}
		this.changedValues.clear();
		
		entityManager.remove(this);
		ModelEventQueue.fireOnAfterModelRemoveEvent(this);
		return true;
	}
	@Override
	public String toString() {
		return username;
	}
	
	public String returnProfile(){
		for(UserProfile p:this.profiles){
			return p.getProfile().toString();
		}
		return "";
	}

	public boolean addProfile(Profile profile) {
		if (profile == null)
			return false; // perfil invalido
		for (UserProfile p : this.getProfiles()) {
			if (p.getProfile().getType() == profile.getType()) {
				return false; // já tenho um perfil que inclui o que quero
				// adicionar
			}
		}
		UserProfile newObj = new UserProfile();
		newObj.setProfile(profile);
		newObj.setUser(this);
		this.getProfiles().add(newObj);
		return true;
	}

	public static String encrypt(String s) {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("SHA-1");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		md.reset();
		md.update(s.getBytes());
		String result = null;
		try {
			result = new String(md.digest(), "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return result.replace('\u0000', ' ');
	}

	public int hashCode() {
		return (this.id == null) ? 0 : this.id.hashCode();
	}

	@Override
	public boolean equals(Object object) {
		if (object instanceof User) {
			final User obj = (User) object;
			return (this.id != null) ? this.id.equals(obj.id)
					: (obj.id == null);
		}
		return false;
	}

	public boolean userIsOnlineHelper() {
		if (this.chatProfile != null)
			return this.chatProfile.isOnlineHelper();
		return false;
	}
	
	public String[] profilesArray(){
		String[] result = new String[profiles.size()];
		int i=0;
		for(UserProfile p:profiles){
			result[i++] = Integer.toString(p.getProfile().getType());
		}
		return result;
	}
	// getters and setters

	@Override
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Set<UserProfile> getProfiles() {
		return profiles;
	}

	public void setProfiles(Set<UserProfile> profiles) {
		this.profiles = profiles;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public ChatProfile getChatProfile() {
		return chatProfile;
	}

	public void setChatProfile(ChatProfile chatProfile) {
		this.chatProfile = chatProfile;
	}

	public boolean isPendingLogin() {
		return pendingLogin;
	}

	public void setPendingLogin(boolean pendingLogin) {
		this.pendingLogin = pendingLogin;
	}

	public boolean isRegistrationComplete() {
		return registrationComplete;
	}

	public void setRegistrationComplete(boolean registrationComplete) {
		this.registrationComplete = registrationComplete;
	}

	public Set<SmsLog> getSmsReceived() {
		return smsReceived;
	}

	public void setSmsReceived(Set<SmsLog> smsReceived) {
		this.smsReceived = smsReceived;
	}

	public Set<SmsLog> getSmsSent() {
		return smsSent;
	}

	public void setSmsSent(Set<SmsLog> smsSent) {
		this.smsSent = smsSent;
	}
	
	public Set<SessionLog> getSessionLog() {
		return sessionLog;
	}

	public void setSessionLog(Set<SessionLog> sessionLog) {
		this.sessionLog = sessionLog;
	}
	
	public Set<UserDepartment> getDepartments() {
		return departments;
	}

	public void setDepartments(Set<UserDepartment> departments) {
		this.departments = departments;
	}
	
	public ServiceArea getServiceArea() {
		return serviceArea;
	}

	public void setServiceArea(ServiceArea serviceArea) {
		this.serviceArea = serviceArea;
	}
	public Set<UserNotification> getNotifications() {
		return notifications;
	}

	public void setNotifications(Set<UserNotification> notifications) {
		this.notifications = notifications;
	}
	
	public ProfCategory getProfCategory() {
		return profCategory;
	}

	public void setProfCategory(ProfCategory profCategory) {
		this.profCategory = profCategory;
	}
	
	public Set<News> getCreatedNews() {
		return createdNews;
	}

	public void setCreatedNews(Set<News> createdNews) {
		this.createdNews = createdNews;
	}

	public Set<News> getApprovedNews() {
		return approvedNews;
	}

	public void setApprovedNews(Set<News> approvedNews) {
		this.approvedNews = approvedNews;
	}
	
	public Set<Notification> getNotificationsSent() {
		return notificationsSent;
	}

	public void setNotificationsSent(Set<Notification> notificationsSent) {
		this.notificationsSent = notificationsSent;
	}
	
	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}
	
	public Set<ComposedNotification> getComposedNotificationsSent() {
		return composedNotificationsSent;
	}

	public void setComposedNotificationsSent(
			Set<ComposedNotification> composedNotificationsSent) {
		this.composedNotificationsSent = composedNotificationsSent;
	}

	public Set<DataChangeValue> getChangedValues() {
		return changedValues;
	}

	public void setChangedValues(Set<DataChangeValue> changedValues) {
		this.changedValues = changedValues;
	}
	
	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
}
