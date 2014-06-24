package com.obc.csrg.bean;

import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.io.Serializable;

import org.directwebremoting.ScriptSession;
import org.jboss.seam.Component;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Startup;
import org.jboss.seam.annotations.Synchronized;
import org.jboss.seam.log.Log;

//Ajax push
import com.icesoft.faces.async.render.SessionRenderer;

//import com.obc.csrg.events.SessionEventQueue;
import com.obc.csrg.events.ChatEvent;
import com.obc.csrg.events.ChatEventAdapter;
import com.obc.csrg.events.ChatEventListener;
import com.obc.csrg.events.ChatEventQueue;
import com.obc.csrg.events.SessionEventListener;
import com.obc.csrg.events.SessionEvent;
import com.obc.csrg.events.SessionEventAdapter;
import com.obc.csrg.events.SessionEventQueue;
import com.obc.csrg.util.ScriptSessionWrapper;
import com.obc.csrg.util.UserIdentityUtil;
import com.obc.csrg.lib.NameValueConstant;
import com.obc.csrg.local.ApplicationLocal;
import com.obc.csrg.model.DBCronTask;
import com.obc.csrg.model.Model;
import com.obc.csrg.model.Notification;
import com.obc.csrg.model.Profile;
import com.obc.csrg.model.User;
import com.obc.csrg.model.Person;
import com.obc.csrg.chat.ChatRoom;
import com.obc.csrg.constants.Constants;
import com.obc.csrg.constants.ProfileConstant;
import com.obc.csrg.events.ModelEvent;
import com.obc.csrg.events.ModelEventAdapter;
import com.obc.csrg.events.ModelEventListener;
import com.obc.csrg.events.ModelEventQueue;
import com.obc.csrg.local.DBCronAppLocal;
import com.obc.csrg.util.NotificationUtil;
import com.obc.csrg.util.ParseUtil;
import com.obc.csrg.chat.ChatUser;

@Startup
@Stateful
@Name("applicationBean")
@Scope(ScopeType.APPLICATION)
@Synchronized
public class ApplicationBean implements ApplicationLocal, Serializable {

	@Logger
	private Log log;

	@In
	private EntityManager entityManager;
	
	@In(create=true)
	private DBCronAppLocal dbCronApp;
	
	@In 
	private Map<String,String> messages;
	
	@In
	private Locale locale;

	private ModelEventListener modelEventListener;
	
	//user sessions control
	private List<UserIdentityUtil> userIdentityList = new ArrayList<UserIdentityUtil>();
	private Long usersCount = 0L;
	private static Long sessionId = 0L;
	
	private boolean onlineSupportLogged=false;
	
	//DWR
	private List<ScriptSessionWrapper> scriptSessions =new ArrayList<ScriptSessionWrapper>(); 
	private List<String> exceptionPages = new ArrayList<String>();
	private SessionEventListener sessionEventListener;
	
	//all current chats
	private List<ChatRoom> chats = new ArrayList<ChatRoom>();
	private ChatEventListener chatEventListener;

	@Create
	public void create() {
		this.initProfiles();
		this.initUsers();
		//create geographic areas in case they don't exist
		ParseUtil.initGeographicArea(log, entityManager,locale);
		
		this.initTasks();
		
		this.initExceptionPages();
		
		this.registerModelListener();
		this.registerSessionListener();
		this.registerChatListener();
	}

	@Remove
	public void destroy() {
		this.unregisterModelListener();
		this.unregisterSessionListener();
		this.unregisterChatListener();
	}

	// business functions
	
	/**
	 * Initializes application tasks
	 */
	private void initTasks(){
		log.info("[initTasks]");
		dbCronApp.init();
	}
	
	/**
	 * Initializes the profile table
	 */
	private void initProfiles() {
		log.info("[initProfiles] check for profiles");
		List<Profile> results = entityManager.createQuery(
				"select p from Profile p").setHint("org.hibernate.cacheable",
				true).getResultList();
		Set<ProfileConstant> profiles = (HashSet<ProfileConstant>) NameValueConstant
				.getFields(ProfileConstant.class);
		if (results.size() == 0) {
			// create initial profiles
			log.info("[initProfiles] create initial profiles...");
			for (ProfileConstant pc : profiles) {
				Profile profile = new Profile();
				profile.setType(pc.getType());
				entityManager.persist(profile);
			}
			entityManager.flush();
			log.info("[initProfiles] profiles created");
		} else if (results.size() < profiles.size()) {
			for (ProfileConstant pc : profiles) {
				boolean haveProfile = false;
				for (Profile p : results) {
					if (p.getType() == pc.getType()) {
						haveProfile = true;
						break;
					}
				}
				if (!haveProfile) {
					// create new profile
					Profile profile = new Profile();
					profile.setType(pc.getType());
					entityManager.persist(profile);
				}
			}
			entityManager.flush();
		}
	}

	/**
	 * calls the functions responsible for individual user creation
	 */
	private void initUsers() {
		createAdmin();
		createSysAdmin();
	}

	/**
	 * creates the administrator user
	 */
	private void createAdmin() {
		List<User> results = entityManager.createQuery(
				"select u from User u where u.username=?").setParameter(1,
				Constants.ADMIN_LOGIN_NAME).getResultList();
		if (results.size() == 0) {
			log.info("[createAdmin] Creating Admin...");
			User user = new User();
			// user
			user.setUsername(Constants.ADMIN_LOGIN_NAME);
			user.setPassword(User.encrypt(Constants.ADMIN_PWD));
			user.setPendingLogin(false);
			user.setRegistrationComplete(true);
			// person
			user.setPerson(new Person());
			Person person = user.getPerson();
			person.setName(Constants.ADMIN_NAME);
			person.setEmail(Constants.ADMIN_EMAIL);
			// profile
			List<Profile> profiles = entityManager.createQuery(
					"select p from Profile p where p.type=?").setParameter(1,
					ProfileConstant.ADMIN.getType()).setHint(
					"org.hibernate.cacheable", true).getResultList();
			if (profiles.size() != 0) {
				Profile p = profiles.get(0);
				user.addProfile(p);
			}
			entityManager.persist(user);
			entityManager.flush();
			log.info("[createSysAdmin] Admin created");
		}
	}
	/**
	 * creates the system administrator user
	 */
	private void createSysAdmin() {
		List<User> results = entityManager.createQuery(
				"select u from User u where u.username=?").setParameter(1,
				Constants.SYS_ADMIN_LOGIN_NAME).getResultList();
		if (results.size() == 0) {
			log.info("[createSysAdmin] Creating SysAdmin...");
			User user = new User();
			// user
			user.setUsername(Constants.SYS_ADMIN_LOGIN_NAME);
			user.setPassword(User.encrypt(Constants.SYS_ADMIN_PWD));
			user.setPendingLogin(false);
			user.setRegistrationComplete(true);
			// person
			user.setPerson(new Person());
			Person person = user.getPerson();
			person.setName(Constants.SYS_ADMIN_NAME);
			person.setEmail(Constants.SYS_ADMIN_EMAIL);
			// profile
			List<Profile> profiles = entityManager.createQuery(
					"select p from Profile p where p.type=?").setParameter(1,
					ProfileConstant.SYS_ADMIN.getType()).setHint(
					"org.hibernate.cacheable", true).getResultList();
			if (profiles.size() != 0) {
				Profile p = profiles.get(0);
				user.addProfile(p);
			}
			entityManager.persist(user);
			entityManager.flush();
			log.info("[createSysAdmin] SysAdmin created");
		}
	}
	//Model events handling
	private final class ModelEventHandler extends ModelEventAdapter implements Serializable{
		@Override
		public void newModel(ModelEvent e){
			Model m = (Model) e.getSource();
			if(m instanceof User){
				//check for notifications changes
				log.info("[newModel] User:#0#", (User) m);
				NotificationUtil nu = new NotificationUtil();
				if(entityManager==null)
		        	entityManager = (EntityManager) Component.getInstance("entityManager");
				if(log==null)
					log = (Log) Component.getInstance("log");
				nu.recalculateAccessStatus((User) m, entityManager, log,dbCronApp);
			}
		}
		@Override
		public void modelUpdated(ModelEvent e){
			Model m = (Model) e.getSource();
			if(m instanceof User){
				//check for notifications changes
				log.info("[modelUpdated] User:#0#", (User) m);
				NotificationUtil nu = new NotificationUtil();
				if(entityManager==null)
		        	entityManager = (EntityManager) Component.getInstance("entityManager");
				if(dbCronApp==null)
					dbCronApp = (DBCronAppLocal) Component.getInstance("dbCronnApp");
				if(log==null)
					log = (Log) Component.getInstance("log");
				log.info("[modelUpdated] dbCronApp:#0", dbCronApp);
				nu.recalculateAccessStatus((User) m, entityManager, log,dbCronApp);
			}
		}
		@Override
		public void onBeforeRemove(ModelEvent e){
			Model m = (Model) e.getSource();
			
		}
		@Override
		public void onAfterRemove(ModelEvent e){
			Model m = (Model) e.getSource();
			if(m instanceof Notification){
				//remove DBCronTask
				Notification n = (Notification) m;
				if(entityManager==null)
		        	entityManager = (EntityManager) Component.getInstance("entityManager");
				DBCronTask task = n.returnExistingTask(entityManager);
				if(task!=null)
					entityManager.remove(task);
			}
		}
		
		@Override
		public void  personIdDataChanged(ModelEvent e,String property, Object oldValue, Object newValue){
			Model m = (Model) e.getSource();
			if(m instanceof Person){
				//send email
				
			}
		}
		
	}
	
	private void registerModelListener(){
		log.info("[registerModelListener]");
		this.modelEventListener = new ModelEventHandler();
		ModelEventQueue.addMsgListener(this.modelEventListener);
	}
	
	private void unregisterModelListener(){
		if(this.modelEventListener!=null){
			log.info("[unregisterModelListener]");
			ModelEventQueue.removeMsgListener(this.modelEventListener);
		}
	}
	
	//users sessions control
	public synchronized void userLoggedIn(User user, StateBean stateBean) {
		if(user != null) userLoggedIn(new UserIdentityUtil(user, stateBean));
	}
	public void userLoggedIn(UserIdentityUtil userIdentity){
		if(userIdentity != null){
			userIdentity.setSessionId(ApplicationBean.sessionId++);
			this.userIdentityList.add(userIdentity);
			usersCount++;
			log.info("[userLoggedIn] usersCount:#0", this.usersCount);
			SessionRenderer.render("all");
			SessionRenderer.render(SessionRenderer.ALL_SESSIONS);
			boolean oldOnlineSupport = this.isOnlineSupportLogged();
			this.checkIsOnlineSupport(userIdentity);
			if(oldOnlineSupport!=this.isOnlineSupportLogged()){
				log.info("[userLoggedIn] alterei estado de online support.");
				this.updateOnlineSupportImage();
			}
		}
	}

	private void checkIsOnlineSupport(UserIdentityUtil userIdentity){
    	if(userIdentity.getUser()!=null){
    		User user = userIdentity.getUser();
    		if(user.getChatProfile()!=null && user.getChatProfile().isOnlineHelper())
    			this.setOnlineSupportLogged(true);
    	}
    }
	
	private void updateOnlineSupportImage(){
		if(this.isOnlineSupportLogged()){
			log.info("[updateOnlineSupportImage] onlineSupport:#0, image:#1", this.onlineSupportLogged,messages.get("onlineSupportOffImg"));
			//SessionEventQueue.fireUpdateOnlineSupport("updateOnlineSupportImg", messages.get("onlineSupportOnImg"));
		}else{
			log.info("[updateOnlineSupportImage] onlineSupport:#0, image:#1", this.onlineSupportLogged,messages.get("onlineSupportOffImg"));
			//SessionEventQueue.fireUpdateOnlineSupport("updateOnlineSupportImg", messages.get("onlineSupportOffImg"));
		}
	}
	private void initExceptionPages(){
		this.exceptionPages.add("/ochs/iframe.seam");
	}
	public boolean notInExceptionPages(String page){
		
		//log.info("[notInExceptionPage] page:#0", page);
		for(String s:this.exceptionPages){
			if(s.equalsIgnoreCase(page))
				return false;
		}
		return true;
	}
	
	public void unloadScriptSession(ScriptSession session){
		for(ScriptSessionWrapper ssw:this.getScriptSessions()){
			if(ssw.getScriptSession().toString().equals(session.toString())){
				ssw.unregisterSssionListener();
				this.getScriptSessions().remove(ssw);
				log.info("[unloadScriptSession] removi sessao:#0", session);
				break;
			}
		}
	}
	private final class SessionEventListenerHandler extends SessionEventAdapter implements Serializable{
		//acknowledge that a new page has been opened
		public void newSession(SessionEvent e){
			ScriptSessionWrapper session = e.getScriptSessionWrapper();
			scriptSessions.add(session);
			log.info("[newSession] Session:#0, sessions:#1",session.getScriptSession(), scriptSessions.size());
		}
	}
	private void registerSessionListener(){
		this.sessionEventListener = new SessionEventListenerHandler();
		SessionEventQueue.addMsgListener(this.sessionEventListener);
	}
	private void unregisterSessionListener(){
		if(this.sessionEventListener!=null){
			SessionEventQueue.removeMsgListener(this.sessionEventListener);
		}
	}
	private void checkOnlineSupportExists(){
    	for(UserIdentityUtil u:this.userIdentityList){
    		User user = u.getUser();
    		if(user!=null && user.getChatProfile()!=null && user.getChatProfile().isOnlineHelper())
    			return;
    	}
    	this.setOnlineSupportLogged(false);
    }
	public synchronized boolean userLoggedOut(User user, StateBean stateBean){
		if(user == null) return false;
		return userLoggedOut(stateBean.getUserIdentity());
		//return userLoggedOut(new UserIdentityUtil(user, stateBean));
	}
	public synchronized boolean userLoggedOut(UserIdentityUtil userIdentity){
		if(userIdentity == null) return false;
		boolean removed = true;//should be false, but there is a problem with stateBean after a while
		log.info("[userLoggedOut] userIdentity: #0", userIdentity);
		/*UserIdentityUtil aux = getUserIdentity(userIdentity);
		log.info("[userLoggedOut] aux:#0", aux);
		if(aux==null) aux = userIdentity;
		if(aux!=null){*/
		UserIdentityUtil aux = userIdentity;
		if(aux!=null){
			removed = this.userIdentityList.remove(aux);
			if(removed && aux.getUser()!=null && aux.getUser().getChatProfile()!=null &&
					aux.getUser().getChatProfile().isOnlineHelper()){
				this.checkOnlineSupportExists();
				if(!this.isOnlineSupportLogged()){
					//update online support image
					this.updateOnlineSupportImage();
				}
			}
		}
		log.info("[userLoggedOut] removed: #0", removed);
		if(removed){
			usersCount--;
			log.info("[userLoggedOut] usersCount:#0", this.usersCount);
			SessionRenderer.render("all");
			SessionRenderer.render(SessionRenderer.ALL_SESSIONS);
		}
		return removed;
	}
	private UserIdentityUtil getUserIdentity(UserIdentityUtil userIdentity){
		if(userIdentity == null) return null;
		Iterator<UserIdentityUtil> uisIt = this.userIdentityList.iterator();
		UserIdentityUtil result = null;
		UserIdentityUtil aux;
		while(uisIt.hasNext() && result==null){
			aux = uisIt.next();
			if(aux.equals(userIdentity)){
				result = aux;
			}
		}
		return result;
	}
	private UserIdentityUtil getUserIdentity(User user){
		if(user == null) return null;
		for(UserIdentityUtil ui:this.getUserIdentityList()){
			if(ui.getUser().equals(user))
				return ui;
		}
		return null;
		//return obterUserIdentity(new UserIdentityUtil(user, null));
	}
	public synchronized List<UserIdentityUtil> getUserIdentityList() {
		//log.info("[getUserIdentityList] logged users:#0", this.userIdentityList);
		List<UserIdentityUtil> userList = new ArrayList<UserIdentityUtil>();
		Iterator<UserIdentityUtil> uisIt = this.userIdentityList.iterator();
		while(uisIt.hasNext()){
			UserIdentityUtil aux = uisIt.next();
			//if(!aux.getUser().getUsername().equals(Constants.ADMIN_LOGIN_NAME))
				userList.add(aux);
		}
		return userList;
	}
	/*
	 * force user logout
	 */
	public void forcedLogout(User user){
		log.info("[forcedLogout] forcedUserLogout: #0", user.getUsername());
		UserIdentityUtil ui = getUserIdentity(user);
		if (ui!=null){
			log.info("[forcedLogout] ui.identity:#0", ui.getIdentity());
			//ui.getIdentity().logout();
			ui.getStateBean().logout();
			userLoggedOut(ui);
		}
	}
	//chat events
	private final class ChatEventHandler extends ChatEventAdapter implements Serializable{
		@Override
		public synchronized void newChat(ChatEvent e){
			chats.add((ChatRoom) e.getSource());
		}
		@Override
		public synchronized void endChat(ChatEvent e){
			ChatRoom room = (ChatRoom)e.getSource();
			for(ChatRoom c:chats){
				if(c.getRoomId().equals(room.getRoomId())){
					chats.remove(c);
					return;
				}
			}
		}
	}
	public void registerChatListener(){
		this.unregisterChatListener();
		this.chatEventListener = new ChatEventHandler();
		ChatEventQueue.addMsgListener(this.chatEventListener);
	}
	public void unregisterChatListener(){
		if(this.chatEventListener!=null){
			ChatEventQueue.removeMsgListener(this.chatEventListener);
		}
	}
	
	/**
	 * after a user log in recovers old chat and replaces user object by the new one
	 */
	public List<ChatRoom> recoverChats(User user){
		List<ChatRoom> result = new ArrayList<ChatRoom>();
		for(ChatRoom r:this.chats){
			for(ChatUser cu: r.getUsers()){
				if(cu.getUser().getId().equals(user.getId())){
					cu.setUser(user);
					result.add(r);
					break;
				}
			}
		}
		return result;
	}
	/**
	 * executes the logout procedures regarding chat administration
	 * it ends or leaves user chats according to user status on those conversations
	 * @param user
	 */
	public void logoutChatProcedure(User user){
		if(user==null)
			return;
	}
	//getters and setters
	
	public boolean isOnlineSupportLogged() {
		return onlineSupportLogged;
	}

	public void setOnlineSupportLogged(boolean onlineSupportLogged) {
		this.onlineSupportLogged = onlineSupportLogged;
	}
	public List<ScriptSessionWrapper> getScriptSessions() {
		return scriptSessions;
	}

	public void setScriptSessions(List<ScriptSessionWrapper> scriptSessions) {
		this.scriptSessions = scriptSessions;
	}
	
	public synchronized Long getUsersCount() {
		return usersCount;
	}

	public void setUsersCount(Long usersCount) {
		this.usersCount = usersCount;
	}
	
	public List<ChatRoom> getChats() {
		return chats;
	}

	public void setChats(List<ChatRoom> chats) {
		this.chats = chats;
	}
}
