package com.obc.csrg.bean;

import java.io.File;
import java.io.Serializable;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.ArrayList;

import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;

import org.directwebremoting.ScriptSession;
import org.directwebremoting.proxy.dwr.Util;
import org.jboss.seam.Component;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Synchronized;
import org.jboss.seam.core.Events;
import org.jboss.seam.log.Log;
import org.jboss.seam.security.Identity;

import com.icesoft.faces.async.render.SessionRenderer;

import com.obc.csrg.util.UserIdentityUtil;
import com.obc.csrg.events.SessionEventQueue;
import com.obc.csrg.util.ScriptSessionWrapper;
import com.obc.csrg.action.TreeController;
import com.obc.csrg.constants.Constants;
import com.obc.csrg.model.AccessLogExclusion;
import com.obc.csrg.model.DBCronTask;
import com.obc.csrg.model.Model;
import com.obc.csrg.model.Notification;
import com.obc.csrg.model.SessionLog;
import com.obc.csrg.util.BackLinkUtil;
import com.obc.csrg.util.ParamUtil;
import com.obc.csrg.local.ApplicationLocal;
import com.obc.csrg.local.StateBeanLocal;
import com.obc.csrg.local.SessionChatLocal;

import com.obc.csrg.model.User;
import com.obc.csrg.constants.ProfileConstant;
import com.obc.csrg.events.ChatEventQueue;
import com.obc.csrg.events.ModelEvent;
import com.obc.csrg.events.ModelEventAdapter;
import com.obc.csrg.events.ModelEventListener;
import com.obc.csrg.events.ModelEventQueue;
import com.obc.csrg.model.UserProfile;
import com.obc.csrg.model.Config;
import com.obc.csrg.local.CreateKeywordsActionLocal;


@Stateful
@Name("stateBean")
@Scope(ScopeType.SESSION)
@Synchronized
public class StateBean implements StateBeanLocal {

	@Logger
	private Log log;

	@In
	private EntityManager entityManager;

	@In
	private Map<String, String> messages;

	@In(required = false)
	private ApplicationLocal applicationBean;

	@In(required = false)
	private User user;

	@In(required = false)
	private FacesContext facesContext;

	@In(create = true, required = false)
	private String remoteAddr;
	
	@In
	private Identity identity;
	
	@In(create=true)
	private CreateKeywordsActionLocal createKeywordsAction;
	
	@In(required=false)
	private SessionChatLocal sessionChatAction;

	private Config config;

	// back link management
	private Stack<BackLinkUtil> backLinkList = new Stack<BackLinkUtil>();
	private BackLinkUtil currentLink = null;
	// saves session log
	private SessionLog sessionHistoric = null;
	// permission to save session log in the database
	private boolean allowPersistSessionHistoric = true;
	// if true then the client browser is running in the same machin jboss runs
	private Boolean localhost = null;
	// holds stateBean signature
	private String thisSignature;
	
	private String timezone="";
	private boolean logout = false;
	
	//if set to true then there were one login in this session
	private boolean loginDone = false;
	
	//model CUD listerner
	private ModelEventListener modelEventListener;
	
	//DWR
	private ScriptSession scriptSession;
	private Util myPage;
	
	public static String appName = "csrg";
	public static String dbName = "csrg";
	
	private User myUser=null;
	
	private UserIdentityUtil userIdentity;
	
	// business functions
	@Create
	public void create() {
		//log.info("[create] stateBean created! user:#0", this.user);
		thisSignature = this.toString().substring(this.toString().indexOf("@"));
		//this.log4Debug(this.toString(), "ip: #0", remoteAddr);
		//log.info("[create] remoteAddr:#0", remoteAddr);
		if (remoteAddr != null) {
			for (String requestHeader : this.facesContext.getExternalContext()
					.getRequestHeaderMap().keySet()) {
				this.log4Debug(this.toString(), "#0: #1", requestHeader,
						this.facesContext.getExternalContext()
								.getRequestHeaderMap().get(requestHeader));
			}
		}
		openSessionHistoric();
		this.registerModelListener();
		//SessionRenderer.addCurrentSession("all");
	}

	@Remove
	public void destroy() {
		this.unregisterModelListener();
		this.closeSessionHistoric();
		//SessionRenderer.removeCurrentSession("all");
		//leave or end chats
		/*
		if(sessionChatAction!=null){
			sessionChatAction.destroy();
		}*/
	}

	/**
	 * checks into the user profile to see if is admin, content or access
	 * manager
	 * 
	 * @return false if not the case
	 */
	public boolean isManager() {
		//log.info("[isManager] user:#0", user);
		if (this.user != null) {
			for (UserProfile up : user.getProfiles()) {
				ProfileConstant p = ProfileConstant.valueOf(up.getProfile()
						.getType());
				if (p.includes(ProfileConstant.ADMIN)
						|| p.includes(ProfileConstant.ACCESS_MANAGER)
						|| p.includes(ProfileConstant.CONTENT_MANAGER))
					return true;
			}
		}
		return false;
	}

	// Define se a maquina cliente é a mesma k o servidor
	public boolean isLocalhost() {
		if (this.localhost == null) {
			String host = this.facesContext.getExternalContext()
					.getRequestHeaderMap().get("host");
			if (host.lastIndexOf(":") > -1)
				host = host.substring(0, host.lastIndexOf(":"));
			if ((host.equalsIgnoreCase("127.0.0.1")
					|| host.equalsIgnoreCase("0:0:0:0:0:0:0:1") || host
					.equalsIgnoreCase("localhost"))
					&& (remoteAddr.equalsIgnoreCase("127.0.0.1")
							|| remoteAddr.equalsIgnoreCase("0:0:0:0:0:0:0:1") || remoteAddr
							.equalsIgnoreCase("::1"))) {
				this.localhost = false;// TODO: turn this to true
			} else {
				this.localhost = false;
			}
		}
		return this.localhost;
	}

	public String myPath() {
		String result = myPath(facesContext.getExternalContext()
				.getRequestServletPath());
		//log.info("[myPath] servletPath: #0", facesContext.getExternalContext()
		//		.getRequestServletPath());
		//log.info("[myPath] result: #0", result);
		if(result.equals("")){
			
		}
		return result;
	}

	private static String myPath(final String requestServletPath) {
		String result;
		File f = new File(requestServletPath);
		result = requestServletPath.substring(0, requestServletPath.length()
				- f.getName().length() - 1);
		return result;
	}

	/*
	 * get a directory name according to my profile/role
	 */
	public String getDir(String...profiles){
		for(String p : profiles){
			if(this.hasRole(p)){
				return "/" + Constants.PROFILES_DIR[Integer.valueOf(p)];
			}
		}
		return "";
	}
	public String getDir(){
		if(user!=null){
			return getDir(user.profilesArray());
		}
		return "";
	}
	public String getIP(){
		return this.sessionHistoric.getMyIP();
	}
	
	public boolean hasRole(String p){
		if(p==null) return false;
		//log.info("[hasRole] identity:#0, p:#1", identity,p);
		boolean result = false;
		try{
			result = identity.hasRole(p);
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return result;
	}
	private boolean hasRole(int p){
		return hasRole(String.valueOf(p));
	}
	public boolean hasRoleAdmin(){
		return hasRole(ProfileConstant.ADMIN.getType());
	}
	
	public boolean hasRoleContentManager(){
		return hasRole(ProfileConstant.CONTENT_MANAGER.getType());
	}
	public boolean hasRoleSysAdmin(){
		return hasRole(ProfileConstant.SYS_ADMIN.getType());
	}
	public boolean hasRoleAccessManager(){
		return hasRole(ProfileConstant.ACCESS_MANAGER.getType());
	}
	// envia uma mensagem para o log de debug com assinatura do stateBean
	// para facilitar o seguimento de um unico browser
	public void log4Debug(final String beanOrigemToString, String msg,
			Object... arg1) {
		log.debug(thisSignature + " -> [" + beanOrigemToString + "] " + msg,
				arg1);
	}

	/*
	 * back link management
	 */
	public void drawPage() {
		pushBackLinkList();
	}

	public boolean pushBackLinkList() {
		BackLinkUtil newLink = new BackLinkUtil();
		newLink.setPage(facesContext.getExternalContext()
				.getRequestServletPath());
		if (this.currentLink == null || !this.currentLink.equals(newLink)) {
			// guarda o currentLink na stack
			if (this.currentLink != null) {
				// this.log4DebugByStateBean("[pushBackLinkList] push: #0",
				// this.currentLink.toString());
				this.backLinkList.push(this.currentLink);
			} else {
				// this.log4Debug(this.toString(),"[pushBackLinkList] this.backLinkList.clear()");
				this.backLinkList.clear();
			}
			// add params a newLink
			Iterator<String> paramsI = facesContext.getExternalContext()
					.getRequestParameterNames();
			String aux;
			while (paramsI.hasNext()) {
				aux = paramsI.next();
				newLink.getParams().add(
						new ParamUtil(aux, facesContext.getExternalContext()
								.getRequestParameterMap().get(aux)));
			}
			// succ do pageHit
			this.sessionHistoric.pageHit();
			// atribuir o newLink a currentLink
			this.currentLink = newLink;
			// this.log4Debug(this.toString(),"[pushBackLinkList] currentLink: #0",
			// this.currentLink.toString());
			return true;
		}
		return false;
	}

	public String popBackLinkList() {
		String result;
		this.currentLink = (this.backLinkList.empty()) ? null
				: this.backLinkList.pop();
		result = (this.currentLink == null) ? "/home.xhtml" : this.currentLink
				.toString();
		// this.log4Debug(this.toString(),"[popBackLinkList] pop: #0", result);
		return result;
	}

	public boolean showBackLink() {
		if (this.backLinkList.size() > 0)
			return true;
		else if (this.itsHomePage())
			return false;
		else
			return true;
	}

	public void injectId(Long id) {
		this.currentLink.getParams().add(
				new ParamUtil("injectedId", id.toString()));
	}

	public String getCurrentLink() {
		return (this.currentLink == null) ? null : this.currentLink.toString();
	}

	public void clearBackLink() {
		this.currentLink = null;
	}

	// define se esta na pagina home.seam
	public boolean itsHomePage() {
		File f = new File(facesContext.getExternalContext()
				.getRequestServletPath());
		return (f.getName().equals("home.seam"));
	}

	/*
	 * SESSION HISTORIC management
	 */
	// compara remoteIP a IP de exclusao
	// 0 - remoteIP == exclusionIP
	// 1 - remoteIP > exclusionIP
	// -1 - remoteIP < exclusionIP
	public int compareRemoteIPVersosExclusionIP(String remoteIP,
			int exclusionIPClassA, int exclusionIPClassB,
			int exclusionIPClassC, int exclusionIPClassD) throws Exception {
		int[] exclusionIParray = new int[4];
		// parse remoteIP
		if (remoteIP == null)
			throw new Exception("Remote IP null.");
		String[] remoteIPList = remoteIP.split("\\.");
		if (remoteIPList.length != 4)
			throw new Exception("Remote IP Invalid.");
		// parse exclusionIP
		exclusionIParray[0] = exclusionIPClassA;
		exclusionIParray[1] = exclusionIPClassB;
		exclusionIParray[2] = exclusionIPClassC;
		exclusionIParray[3] = exclusionIPClassD;
		// compare IP's
		for (int i = 0; i < 4; i++) {
			if (Integer.parseInt(remoteIPList[i]) != exclusionIParray[i]) {
				if (Integer.parseInt(remoteIPList[i]) < exclusionIParray[i]) {
					return -1;
				} else {
					return 1;
				}
			}
		}
		return 0;
	}

	// usado para definir se uma sessao passa pela exclusao de acessos
	public boolean allowSession(SessionLog sessionHistoric,
			AccessLogExclusion exclusaoLogAcessos) {
		boolean allow = true;
		AccessLogExclusion ela = exclusaoLogAcessos;
		try {
			if (ela.getIpClassAFinal() == 0 && ela.getIpClassBFinal() == 0
					&& ela.getIpClassCFinal() == 0
					&& ela.getIpClassDFinal() == 0) {
				if (ela.getDescription() == null
						|| ela.getDescription().equals("")) {// exclusao por IP
					if (this.compareRemoteIPVersosExclusionIP(sessionHistoric
							.getMyIP(), ela.getIpClassAInitial(), ela
							.getIpClassBInitial(), ela.getIpClassCInitial(),
							ela.getIpClassDInitial()) == 0) {
						allow = false;
					}
				} else {// exclusao por descricao
					if (sessionHistoric.getUserAgent().contains(
							ela.getDescription())) {
						allow = false;
					}
				}
			} else {// exclusao por inertvalo de IP´s
				if (this.compareRemoteIPVersosExclusionIP(sessionHistoric
						.getMyIP(), ela.getIpClassAInitial(), ela
						.getIpClassBInitial(), ela.getIpClassCInitial(), ela
						.getIpClassDInitial()) >= 0
						&& this.compareRemoteIPVersosExclusionIP(
								sessionHistoric.getMyIP(), ela
										.getIpClassAFinal(), ela
										.getIpClassBFinal(), ela
										.getIpClassCFinal(), ela
										.getIpClassDFinal()) <= 0) {
					allow = false;
				}
			}
		} catch (Exception ex) {
			this.log4Debug(this.toString(),
					"[allowSession] exception: #0, sessionHistoric: #1", ex
							.toString(), sessionHistoric);
		}
		return allow;
	}

	// inicializa o booleano para o session historic persist
	private void initAllowPersistSessionHistoric() {
		allowPersistSessionHistoric = true;
		Iterator<AccessLogExclusion> exclusionsIterator = entityManager
				.createQuery("select m from AccessLogExclusion m").setHint(
						"org.hibernate.cacheable", true).getResultList()
				.iterator();
		AccessLogExclusion exclusion;
		while (allowPersistSessionHistoric
				&& exclusionsIterator.hasNext()) {
			exclusion = exclusionsIterator.next();
			
			if (!this.allowSession(this.sessionHistoric, exclusion))
				allowPersistSessionHistoric = false;
		}
	}

	// insert data in session_historic table
	private void openSessionHistoric() {
		// log.trace("[openSessionHistoric]");
		this.sessionHistoric = new SessionLog();
		this.sessionHistoric.setStateBeanSignature(this.thisSignature);
		this.sessionHistoric.setMyIP(remoteAddr);
		this.sessionHistoric.setAcceptLanguage(this.facesContext
				.getExternalContext().getRequestHeaderMap().get(
						"accept-language"));
		log.info("openSessionHistoric]language:#0", this.sessionHistoric.getAcceptLanguage());
		this.sessionHistoric.setUserAgent(this.facesContext
				.getExternalContext().getRequestHeaderMap().get("user-agent"));
		initAllowPersistSessionHistoric();
		// if(allowPersistSessionHistoric)
		// entityManager.persist(this.sessionHistoric);
	}

	private void closeSessionHistoric() {
		if (this.allowPersistSessionHistoric) {
			this.sessionHistoric.setEndDate(new Date());
			
			if(sessionHistoric.getUser()!=null){
				entityManager.persist(this.sessionHistoric);
				entityManager.joinTransaction();
				entityManager.flush();
				this.log4Debug(this.toString(),
						"[closeSessionHistoric] sessionHistoric: #0 (guardado)",
						this.sessionHistoric.getId());
			}
		}
	}
	
	/*
	 * echo - escreve na resposta para o cliente a String s
	 */
	public void echo(String s){
		try{
			if(facesContext!=null)
				facesContext.getResponseWriter().append(s);
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	public void echoContent(){
		this.echo("<p>This is returned from stateBean echo</p> " +
				"<iframe name='internalPage' src='/csrg/forms/testPage.html' " + 
				"width='400' height='200'> " +
				"</iframe>");
	}
	
	//registers the user in the system
	private void registerUser(){
		loginDone = true;
		applicationBean.userLoggedIn(this.user, this);
	}
	public void unregisterUser(){
		loginDone=false;
		applicationBean.userLoggedOut(user, this);
	}
	
	//Model events handling
	private final class ModelEventHandler extends ModelEventAdapter implements Serializable{
		@Override
		public void newModel(ModelEvent e){
			Model m = (Model) e.getSource();
			
			log.info("[newModel] going to create keywords");
			if(createKeywordsAction==null)
				createKeywordsAction = (CreateKeywordsActionLocal) Component.getInstance("createKeywordsAction",true);
			createKeywordsAction.createModelKeywords(m);
		}
		@Override
		public void modelUpdated(ModelEvent e){
			Model m = (Model) e.getSource();
			
			log.info("[modelUpdated] going to update keywords");
			if(createKeywordsAction==null)
				createKeywordsAction = (CreateKeywordsActionLocal) Component.getInstance("createKeywordsAction",true);
			createKeywordsAction.updateModelKeywords(m);
		}
		@Override
		public void onAfterRemove(ModelEvent e){
			Model m = (Model) e.getSource();
			
			if(createKeywordsAction==null)
				createKeywordsAction = (CreateKeywordsActionLocal) Component.getInstance("createKeywordsAction",true);
			createKeywordsAction.removeModelKeywords(m);
		}
	}
	
	private void registerModelListener(){
		//log.info("[registerModelListener]");
		this.unregisterModelListener();//first unregister
		this.modelEventListener = new ModelEventHandler();
		ModelEventQueue.addMsgListener(this.modelEventListener);
	}
	
	private void unregisterModelListener(){
		if(this.modelEventListener!=null){
			//log.info("[unregisterModelListener]");
			ModelEventQueue.removeMsgListener(this.modelEventListener);
		}
	}
	
	public void fireNewSession(){
		ScriptSessionWrapper ss = new ScriptSessionWrapper(this.scriptSession,this.myPage);
		SessionEventQueue.fireNewSessionEvent(ss);
	}
	public String getInstanceString(){
		return this.toString();
	}
	public void logout(){
		//log.info("[logout] stateBean:#0, this.identity:#1", this,this.identity);
		if(identity!=null)
			identity.logout();
		else
			log.info("[logout] null identity");
		logout = true;
	}
	public boolean canSeeOnlineUsers(){
		if(this.user==null)
			return false;
		if(applicationBean!=null && applicationBean.getUserIdentityList().size()>1)
			return true;
		return false;
	}

	// getters and setters
	public Config getConfig() {
		try {
			List<Config> configs = entityManager.createQuery(
					"select c from Config c").setHint(
					"org.hibernate.cacheable", true).getResultList();
			if (configs.size() > 0) {
				config = configs.get(0);
				this.timezone = config.getTimezone();
			} else {
				config = null;
			}
		} catch (Exception ex) {
			// this.log4Debug(this.toString(),
			// "[initConfigOutjection] error: #0", ex);
			config = null;
		}
		return config;
	}

	public void setConfig(Config config) {
		this.config = config;
	}
	
	public User getUser(){
		return this.user;
	}
	//records who logged in
	public void setUser(User user) {
		this.myUser = user;
		sessionChatAction.setMyUser(user);
		//log.info("[setUser] user:#0, actual user:#1", user,this.user);
		if(this.user == null){
			this.user = user;
			//log.info("[setUser] user:#0, actual user:#1", user,this.user);
			this.registerUser();
			this.clearBackLink();
			if(allowPersistSessionHistoric){
				this.sessionHistoric.setUser(this.user);
				//entityManager.merge(this.sessionHistoric);
			}
		}else{
			this.log4Debug(this.toString(),"[setUser] REFUSED ATEMPT TO CHANGE USER");
			this.log4Debug(this.toString(),"[setUser] MY USER : #0", this.user.getUsername());
			this.log4Debug(this.toString(),"[setUser] USER SENT : #0", user.getUsername());
		}
	}
	
	public String getTimezone() {
		//log.info("[getTimezone] timezone:#0", timezone);
		if(config==null)
			this.getConfig();
		//log.info("[getTimezone] timezone:#0", timezone);
		return timezone;
	}

	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}
	
	public boolean isLoginDone() {
		return loginDone;
	}

	public void setLoginDone(boolean loginDone) {
		this.loginDone = loginDone;
	}
	public Identity getIdentity() {
		return identity;
	}
	
	public ScriptSession getScriptSession() {
		return scriptSession;
	}

	public void setScriptSession(ScriptSession scriptSession) {
		this.scriptSession = scriptSession;
	}

	public Util getMyPage() {
		return myPage;
	}

	public void setMyPage(Util myPage) {
		this.myPage = myPage;
	}

	public UserIdentityUtil getUserIdentity() {
		return userIdentity;
	}

	public void setUserIdentity(UserIdentityUtil userIdentity) {
		this.userIdentity = userIdentity;
	}
}
