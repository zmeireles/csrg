/**
 * 
 */
package com.obc.csrg.util;

/**
 * @author jmeireles
 *
 * provides utility methods related with the Notification system like calculating target users
 */
import java.io.File;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import java.util.Date;

import javax.persistence.EntityManager;

import org.jboss.seam.Component;
import org.jboss.seam.log.Log;

import com.obc.csrg.local.DBCronAppLocal;
import com.obc.csrg.model.DBCronTask;
import com.obc.csrg.model.DepartmentNotification;
import com.obc.csrg.model.ProfCategoryNotification;
import com.obc.csrg.model.ServiceAreaNotification;
import com.obc.csrg.model.ServiceClassificationNotification;
import com.obc.csrg.model.User;
import com.obc.csrg.model.Model;
import com.obc.csrg.model.Department;
import com.obc.csrg.model.ServiceClassification;
import com.obc.csrg.model.ServiceArea;
import com.obc.csrg.model.ProfCategory;
import com.obc.csrg.model.Notification;
import com.obc.csrg.model.UserNotification;
import com.obc.csrg.model.Config;
import com.obc.csrg.model.News;
import com.obc.csrg.model.VisualItem;
import com.obc.csrg.notification.EmailNotification;
import com.obc.csrg.notification.FaxNotification;
import com.obc.csrg.util.SmsUtil;
import com.obc.csrg.constants.DBCronTaskEnum;
import com.obc.csrg.constants.NotificationMediaEnum;
import com.obc.csrg.constants.Constants;
import com.obc.csrg.events.ModelEventQueue;


public class NotificationUtil {

	private Set<SelectModelItem<User>> selectedUsers = new HashSet<SelectModelItem<User>>();
	private Set<SelectModelItem<Department>> selectedDepartments = new HashSet<SelectModelItem<Department>>();

	private Set<SelectModelItem<User>> directUsers = new HashSet<SelectModelItem<User>>();
	private Set<SelectModelItem<User>> usersDepartment = new HashSet<SelectModelItem<User>>();
	private Set<SelectModelItem<User>> usersServiceArea = new HashSet<SelectModelItem<User>>();
	private Set<SelectModelItem<User>> usersServiceClassification = new HashSet<SelectModelItem<User>>();
	private Set<SelectModelItem<User>> usersProfCategory = new HashSet<SelectModelItem<User>>();
	private Set<SelectModelItem<Department>> departmentsServiceArea = new HashSet<SelectModelItem<Department>>();
	private Set<SelectModelItem<Department>> departmentsServiceClassification = new HashSet<SelectModelItem<Department>>();

	private boolean filterDepartments = false;
	private boolean filterServiceAreas = false;
	private boolean filterServiceClassifications = false;
	private boolean filterProfCategories = false;
	
	private Log log;
	private Map<String,String> messages;
	private EntityManager entityManager;

	//constructors
	public NotificationUtil(){
		super();
	}
	
	public NotificationUtil(EntityManager entityManager,Log log,Map<String,String> messages){
		super();
		this.entityManager = entityManager;
		this.log = log;
		this.messages = messages;
	}
	//business logic
	
	/**
	 * empties all the lists
	 */
	public void init(){
		this.selectedDepartments.clear();
		this.selectedUsers.clear();
		this.directUsers.clear();
		this.usersDepartment.clear();
		this.usersProfCategory.clear();
		this.usersServiceArea.clear();
		this.usersServiceClassification.clear();
	}
	private List<SelectModelItem<User>> calculateUsers(
			List<SelectModelItem<Model>> list) {
		
		List<SelectModelItem<User>> returnList = new ArrayList<SelectModelItem<User>>();
		Set<User> usersSet = new HashSet<User>();
		for (SelectModelItem<Model> m : list) {
			List<User> itemUsers = m.instance.relatedUsers();
			for (User u : itemUsers)
				usersSet.add(u);
		}
		for (User u : usersSet) {
			returnList.add(new SelectModelItem<User>(u, true));
		}
		return returnList;
	}
	public void addDepartmentUsers(List<SelectModelItem<Department>> list){
		usersDepartment.clear();
		if(list.size()==0){
			filterDepartments=false;
			return;
		}
		this.filterDepartments = true;
		for(SelectModelItem<Department> item:list){
			List<User> itemUsers = item.instance.relatedUsers();
			if(entityManager!=null){
				List<User> items = entityManager.createQuery(
						"select m.user from UserDepartment m where m.user.active=true and m.department.id is not null and m.department.id=?")
						.setParameter(1, item.instance.getId())
						.getResultList();
				itemUsers = items;
			}
			for(User u:itemUsers)
				usersDepartment.add(new SelectModelItem<User>(u,true));
		}
	}
	public void addProfCategoryUsers(List<SelectModelItem<ProfCategory>> list){
		this.usersProfCategory.clear();
		if(list.size()==0){
			this.filterProfCategories=false;
			return;
		}
		this.filterProfCategories = true;
		for(SelectModelItem<ProfCategory> item:list){
			List<User> itemUsers = item.instance.relatedUsers();
			if(entityManager!=null){
				List<User> items = entityManager.createQuery(
						"select m from User m where m.active=true and m.profCategory.id is not null and m.profCategory.id=?")
						.setParameter(1, item.instance.getId())
						.getResultList();
				itemUsers = items;
			}
			for(User u:itemUsers)
				this.usersProfCategory.add(new SelectModelItem<User>(u,true));
		}
	}
	public void addServiceAreaUsers(List<SelectModelItem<ServiceArea>> list){
		this.usersServiceArea.clear();
		
		//if(log!=null) log.info("[addServiceAreaUsers] list:#0", list);
		if(list.size()==0){
			this.filterServiceAreas=false;
			return;
		}
		this.filterServiceAreas = true;
		for(SelectModelItem<ServiceArea> item:list){
			List<User> itemUsers = item.instance.relatedUsers();
			//if(log!=null) log.info("[addServiceAreaUsers] serviceArea:#0, users:#1", item,itemUsers);
			if(entityManager!=null){
				List<User> items = entityManager.createQuery(
						"select m from User m where m.active=true and m.serviceArea.id is not null and m.serviceArea.id=?")
						.setParameter(1, item.instance.getId())
						.getResultList();
				itemUsers = items;
				//if(log!=null) log.info("[addServiceAreaUsers] after reading from db serviceArea:#0, users:#1", item,itemUsers);
			}
			for(User u:itemUsers)
				this.usersServiceArea.add(new SelectModelItem<User>(u,true));
		}
	}
	public void addServiceClassificationUsers(List<SelectModelItem<ServiceClassification>> list){
		this.usersServiceClassification.clear();
		if(list.size()==0){
			this.filterServiceClassifications=false;
			return;
		}
		this.filterServiceClassifications = true;
		for(SelectModelItem<ServiceClassification> item:list){
			List<User> itemUsers = item.instance.relatedUsers();
			if(entityManager!=null){
				itemUsers = item.instance.relatedUsers(entityManager);
			}
			for(User u:itemUsers)
				this.usersServiceClassification.add(new SelectModelItem<User>(u,true));
		}
	}
	
	public void addUsers(List<SelectModelItem<User>> list){
		this.directUsers.clear();
		for(SelectModelItem<User> item:list){
			// first remove the user from the set. direct users have
			// precedence
			directUsers.remove(item);
			// add again
			User u = (User) item.instance;
			directUsers.add(new SelectModelItem<User>(u, false));
		}
	}
	
	// add the users related with the list to the selectedUsers list
	public void addToSelectedUsers(List list) {
		if (list.size() == 0)
			return; // nothing to add
		// determine the nature of the list
		Model sample = (Model) ((SelectModelItem) list.get(0)).instance;

		if (sample instanceof User) {
			// add directly to the selected users. It's a non calculated user
			for (SelectModelItem<Model> item : (List<SelectModelItem<Model>>) list) {
				// first remove the user from the set. direct users have
				// precedence
				selectedUsers.remove(item);
				// add again
				User u = (User) item.instance;
				selectedUsers.add(new SelectModelItem<User>(u, false));
			}
		} else {
			Set<SelectModelItem<User>> usersSet = new HashSet<SelectModelItem<User>>();
			List<SelectModelItem<User>> users = this.calculateUsers(list);
			for (SelectModelItem<User> item : users) {
				usersSet.add(item);
			}
		}
	}
	
	public void calculateUsers(){
		this.selectedUsers.clear();
		boolean usersAdded=false;
		if(this.filterDepartments){
			if(!usersAdded)
				this.selectedUsers = this.usersDepartment;
			else
				this.selectedUsers = this.andOperation(this.selectedUsers, this.usersDepartment);
			usersAdded = true;
		}
		if(this.filterProfCategories){
			if(!usersAdded)
				this.selectedUsers = this.usersProfCategory;
			else
				this.selectedUsers = this.andOperation(this.selectedUsers, this.usersProfCategory);
			usersAdded = true;
		}
		if(this.filterServiceAreas){
			if(!usersAdded)
				this.selectedUsers = this.usersServiceArea;
			else
				this.selectedUsers = this.andOperation(this.selectedUsers, this.usersServiceArea);
			usersAdded = true;
		}
		if(this.filterServiceClassifications){
			if(!usersAdded)
				this.selectedUsers = this.usersServiceClassification;
			else
				this.selectedUsers = this.andOperation(this.selectedUsers, this.usersServiceClassification);
			usersAdded = true;
		}
		//finally add direct users
		this.selectedUsers.addAll(this.directUsers);
	}
	public Set<SelectModelItem<User>> andOperation(Set<SelectModelItem<User>> list1,Set<SelectModelItem<User>> list2){
		Set<SelectModelItem<User>> result = new HashSet<SelectModelItem<User>>();
		for(SelectModelItem<User> item:list2){
			if(list1.remove(item))
				result.add(item);
		}
		return result;
	}
	/**
	 * sends the notification to all targets (departments and users) according to the media chosen.
	 * it also reports for success and failure.
	 * @param notification
	 */
	public void notifyTargets(Notification notification,EntityManager entityManager,Log log,Config config, boolean force){
		//notify users
		log.info("[notifyTargets] entered function");
		
		if(notification.getUsersNotificationMedia().ordinal()!=NotificationMediaEnum.none.ordinal()){
			if(notification.getUsersNotificationMedia().ordinal()!=NotificationMediaEnum.news.ordinal()){
				if(notification.getNews()!=null){
					//delete news associated
					log.info("[notifyTargets] pre delete news");
					notification.getNews().remove(entityManager);
					notification.setNews(null);
					entityManager.merge(notification);
					log.info("[notifyTargets] delete news");
				}
				for(UserNotification un:notification.getUsers()){
					if(force || !un.isSent()){
						//send notification
						un.setSent(this.notifyUser(notification, un,entityManager,log,config));
						entityManager.merge(un);
						log.info("[notifyTargets] messagesSent:#0", un.isSent());
					}
				}
			}else{
				//create a News object and publish
				//don't create from here, event have problem when running from another thread. this has to be done before
				//this.createNewsFromNotification(notification, entityManager);
			}
		}
		//finally check for visual item link and watch for the publishAfterNotification property
		if(notification.getVisualItem()!=null && notification.getVisualItem().isPublishAfterNotification()){
			VisualItem vi = notification.getVisualItem();
			log.info("[notifyTargets] publish Visual Item:#0", vi.getMenuText());
			vi.setPublish(true);
			entityManager.merge(vi);
			//now, if there were news involved then they should be refreshed
			if(notification.getNews()!=null)
				ModelEventQueue.fireModelUpdatedEvent(notification.getNews());
		}
	}
	private boolean notifyUser(Notification notification,UserNotification un,EntityManager entityManager,Log log,Config config){
		this.setLog(log);
		if(notification.getUsersNotificationMedia().ordinal()==NotificationMediaEnum.email.ordinal()){
			String text = MessagesFactory.getEmailText(notification);
			EmailNotification email = new EmailNotification(entityManager,log);
			log.info("[notifyUser] send email");
			boolean response = email.sendNotification(config.getNotificationSourceEmail(),
					config.getNotificationSourceName(), 
					un.getUser().getPerson(), notification.getSubject(), text,null);
			log.info("[notifyUser] email sent, response:#0", response);
			return response;
		}else if(notification.getUsersNotificationMedia().ordinal()==NotificationMediaEnum.sms.ordinal()){
			String mobilePrefix = un.getUser().getPerson().getMobilePhonePrefix();
			String mobilePhone = un.getUser().getPerson().getMobilePhone();
			log.info("[notifyUser] send sms prefix:#0, number:#1", mobilePrefix,mobilePhone);
			if(mobilePrefix!=null && mobilePrefix.matches(Constants.REGEXP_COUNTRY_PHONE_PREFIX) && 
					mobilePhone!=null && mobilePhone.matches(Constants.REGEXP_PHONE_NUMBER)){
				String text = MessagesFactory.getSmsText(notification);
				String fullPhoneNumber = mobilePrefix + mobilePhone;
				log.info("[notifyUser] send sms, invoke SmsUtil, prefix:#0, number:#1", mobilePrefix,mobilePhone);
				SmsUtil smsUtil = new SmsUtil(config,text,fullPhoneNumber,notification.getSender(),
						entityManager,log,messages);
				smsUtil.setToPerson(un.getUser().getPerson());
				return smsUtil.sendSms();
			}
		}else if(notification.getUsersNotificationMedia().ordinal()==NotificationMediaEnum.fax.ordinal()){
			String fullFaxNumber = un.getUser().returnFaxFullNumber();
			log.info("[notifyUser] send fax, user:#0, faxNumber:#1, matchRegex:#2", un.getUser(),
					fullFaxNumber,fullFaxNumber.matches(Constants.REGEXP_FAX));
			if(fullFaxNumber!=null && !fullFaxNumber.equals("") &&
					fullFaxNumber.matches(Constants.REGEXP_FAX)){
				File f = MessagesFactory.getFaxPdfForNotification(notification);
				if(f!=null){
					FaxNotification fax = new FaxNotification(entityManager,log,config);
					log.info("[notifyUser] send fax, invoke FaxNotification send fax method");
					return fax.sendNotification(config.getNotificationSourceName(),un.getUser(),
							fullFaxNumber, f);
				}
			}
		}
		return false;
	}
	public void createNewsFromNotification(Notification notification,EntityManager entityManager){
		News news;
		log.info("[createNewsFromNotification] notification.news.id:#0",notification.getNews().getId());
		if(notification.getNews()!=null){
			news = notification.getNews();
			//notification.getNews().remove(entityManager);
		}else
			news = new News();
		news.setNotification(notification);
		
		//direct data
		news.setActive(true);
		news.setApproved(true);
		news.setApprovedBy(notification.getSender());
		news.setBeginDate(notification.getSendAt());
		if(news.getBeginDate()==null)
			news.setBeginDate(new Date());
		news.setCreatedBy(notification.getSender());
		news.setCreationDate(new Date());
		news.setEndDate(notification.getEndAt());
		news.setEvent(false);
		news.setEventEnd(null);
		news.setEventStart(null);
		news.setHighlight(true);
		news.setHtml(notification.getMessage());
		news.setSynopsis(notification.getMessage());
		news.setTitle(notification.getSubject());
		
		log.info("[createNewsFromNotification] news.id:#0",news.getId());
		if(news.getId()==null){
			entityManager.persist(news);
			notification.setNews(news);
			entityManager.merge(notification);
		}else{
			entityManager.merge(news);
		}
		
		//related tables
		//first clear old connections.
		news.getDepartments().clear();
		news.getProfCategories().clear();
		news.getServiceAreas().clear();
		news.getServiceClassifications().clear();
		news.getUsers().clear();
		
		//reassign old and new relations
		for(DepartmentNotification d:notification.getDepartments()){
			d.setNews(news);
			news.getDepartments().add(d);
			entityManager.merge(d);
		}
		
		for(ProfCategoryNotification p:notification.getProfCategories()){
			p.setNews(news);
			news.getProfCategories().add(p);
			entityManager.merge(p);
		}
		for(ServiceAreaNotification s:notification.getServiceAreas()){
			s.setNews(news);
			news.getServiceAreas().add(s);
			entityManager.merge(s);
		}
		for(ServiceClassificationNotification s:notification.getServiceClassifications()){
			s.setNews(news);
			news.getServiceClassifications().add(s);
			entityManager.merge(s);
		}
		for(UserNotification u:notification.getUsers()){
			u.setNews(news);
			news.getUsers().add(u);
			entityManager.merge(u);
			//log.info("[createNewsFromNotification] u.getNews.id:#0", u.getNews().getId());
		}
		//save news
		
		entityManager.merge(news);
		log.info("[createNewsFromNotification] news created/merged with id:#0",news.getId());
		entityManager.flush();
		ModelEventQueue.fireNewModelEvent(news);
	}
	/**
	 * for all the News, Notifications and Visual items, performs a calculation of targets and checks if user
	 * should be inserted/removed due to a change in it's properties/relation to other objects like department's, etc.
	 * 
	 * @param user
	 */
	public void recalculateAccessStatus(User user,EntityManager entityManager,Log log,DBCronAppLocal dbCronApp){
		this.setLog(log);
		//entityManager = (EntityManager) Component.getInstance("entityManager");
		if(entityManager==null || (entityManager!=null && !entityManager.isOpen()))
			entityManager = (EntityManager) Component.getInstance("entityManager");
		this.setEntityManager(entityManager);
		
		//this.recalculateNotificationAccessStatus(user, entityManager, log, dbCronApp);	
		this.recalculateNewsAccessStatus(user, entityManager, log, dbCronApp);
		this.recalculateVisualItemAccessStatus(user, entityManager, log, dbCronApp);
		
	}
	public void recalculateNotificationAccessStatus(User user,EntityManager entityManager,Log log,DBCronAppLocal dbCronApp){
		List<Notification> notifications = entityManager.createQuery(
					"select m from Notification m " +
					"where m.endAt is null or m.endAt >= current_timestamp()")
					.getResultList();
		for(Notification notification:notifications){
			//first check to see if user exists and is not calculated. If so ignore an go to the next notification
			boolean userIsNative = false;
			UserNotification originalNotification=null;
			for(UserNotification u:notification.getUsers()){
				if(u.getUser().equals(user) && u.getId()!=null){
					originalNotification = u;
					userIsNative=!u.isCalculated();
					//log.info("[recalculateNotificationAccessStatus] user:[#0] is #1 native in notification:[#2]", user,userIsNative,notification);
					break;
				}
			}
			if(originalNotification==null){
				//log.info("[recalculateNotificationAccessStatus] user:[#0] not found in original notification:[#1]", user,notification);
			}
			if(!userIsNative){
				this.init();
				this.addUsers(this.convertUserListToSelectModelItemList(notification.getUsers()));
				this.addDepartmentUsers(this.convertToSelectModelItemList(notification.getDepartments()));
				this.addProfCategoryUsers(this.convertToSelectModelItemList(notification.getProfCategories()));
				this.addServiceAreaUsers(this.convertToSelectModelItemList(notification.getServiceAreas()));
				this.addServiceClassificationUsers(this.convertToSelectModelItemList(notification.getServiceClassifications()));
				this.calculateUsers();
				//log.info("[recalculateNotificationAccessStatus] users calculated:#0",this.selectedUsers);
				//1st. if it's in the list and not in the original list, then add it.
				//2nd. if user is not in the list and was before remove it from the original list
				boolean userFound=false;
				for(SelectModelItem<User> item:this.selectedUsers){
					if(item.getInstance().equals(user)){
						userFound=true;
						//log.info("[recalculateNotificationAccessStatus] user found!");
						if(originalNotification==null){
							// add the user and save
							//log.info("[recalculateNotificationAccessStatus] adding user:[#0] to notification:[#1]", user,notification);
							UserNotification un = new UserNotification();
							un.setNotification(notification);
							un.setUser(user);
							notification.getUsers().add(un);
							if(notification.getNews()!=null){
								un.setNews(notification.getNews());
								notification.getNews().getUsers().add(un);
							}
							if(notification.getVisualItem()!=null){
								un.setVisualItem(notification.getVisualItem());
								notification.getVisualItem().getUsers().add(un);
							}
							entityManager.merge(notification);//now, fire event
							//entityManager.flush();
							if(notification.getUsersNotificationMedia().ordinal()!=NotificationMediaEnum.news.ordinal() &&
									notification.getUsersNotificationMedia().ordinal()!=NotificationMediaEnum.none.ordinal()){
								//this.sendtoDBCron(dbCronApp, entityManager, notification);
							}
							//now, if there are news and visual items involved then those systems should acknowledge that.
							if(notification.getNews()!=null)
								ModelEventQueue.fireModelUpdatedEvent(notification.getNews());
							if(notification.getVisualItem()!=null)
								ModelEventQueue.fireModelUpdatedEvent(notification.getVisualItem());
							//ModelEventQueue.fireModelUpdatedEvent(notification);
							//entityManager.persist(un);
						}
					}
				}
				if(!userFound && originalNotification!=null){
					//delete user notification
					//log.info("[recalculateNotificationAccessStatus] delete user:[#0] from notification:[#1]", user,notification);
					originalNotification.remove(entityManager);
					//entityManager.flush();
				}else if(!userFound){
					//log.info("[recalculateNotificationAccessStatus] user:[#0] not found in calculated notification:[#1]", user,notification);
				}
			}
		}
		
	}
	public void recalculateNewsAccessStatus(User user,EntityManager entityManager,Log log,DBCronAppLocal dbCronApp){
		List<News> newsList = entityManager.createQuery("select m from News m " +
				"where active=true and (endDate is null or endDate>= current_timestamp())")
							.getResultList();
		for(News news:newsList){
			//first check to see if user exists and is not calculated. If so ignore an go to the next notification
			boolean userIsNative = false;
			UserNotification originalNotification=null;
			for(UserNotification u:news.getUsers()){
				if(u.getUser().equals(user) && u.getId()!=null){
					originalNotification = u;
					userIsNative=!u.isCalculated();
					//log.info("[recalculateNewsAccessStatus] user:[#0] is #1 native in news:[#2]", user,userIsNative,news);
					break;
				}
			}
			if(originalNotification==null){
				log.info("[recalculateNewsAccessStatus] user:[#0] not found in original news:[#1]", user,news);
			}
			if(!userIsNative){
				this.init();
				this.addUsers(this.convertUserListToSelectModelItemList(news.getUsers()));
				this.addDepartmentUsers(this.convertToSelectModelItemList(news.getDepartments()));
				this.addProfCategoryUsers(this.convertToSelectModelItemList(news.getProfCategories()));
				this.addServiceAreaUsers(this.convertToSelectModelItemList(news.getServiceAreas()));
				this.addServiceClassificationUsers(this.convertToSelectModelItemList(news.getServiceClassifications()));
				this.calculateUsers();
				//log.info("[recalculateNewsAccessStatus] users calculated:#0",this.selectedUsers);
				//1st. if it's in the list and not in the original list, then add it.
				//2nd. if user is not in the list and was before remove it from the original list
				boolean userFound=false;
				for(SelectModelItem<User> item:this.selectedUsers){
					if(item.getInstance().equals(user)){
						userFound=true;
						//log.info("[recalculateNewsAccessStatus] user found!");
						if(originalNotification==null){
							// add the user and save
							//log.info("[recalculateNewsAccessStatus] adding user:[#0] to news:[#1]", user,news);
							UserNotification un = new UserNotification();
							un.setNews(news);
							un.setUser(user);
							news.getUsers().add(un);
							if(news.getNotification()!=null){
								un.setNotification(news.getNotification());
								news.getNotification().getUsers().add(un);
								Notification notification = news.getNotification();
								if(notification.getUsersNotificationMedia().ordinal()!=NotificationMediaEnum.news.ordinal() &&
										notification.getUsersNotificationMedia().ordinal()!=NotificationMediaEnum.none.ordinal()){
									//this.sendtoDBCron(dbCronApp, entityManager, notification);
								}
							}
							entityManager.merge(news);
							//entityManager.flush();
							
							//now, if there is a notification involved then notification system should acknowledge that.
							if(news.getNotification()!=null)
								ModelEventQueue.fireModelUpdatedEvent(news.getNotification());
						}
					}
				}
				if(!userFound && originalNotification!=null){
					//delete user notification
					//log.info("[recalculateNewsAccessStatus] delete user:[#0] from news:[#1]", user,news);
					originalNotification.remove(entityManager);
					//entityManager.flush();
				}else if(!userFound){
					//log.info("[recalculateNewsAccessStatus] user:[#0] not found in calculated news:[#1]", user,news);
				}
			}
		}
	}
	public void recalculateVisualItemAccessStatus(User user,EntityManager entityManager,Log log,DBCronAppLocal dbCronApp){
		List<VisualItem> vis = entityManager.createQuery("select m from VisualItem m ")
							.getResultList();
		for(VisualItem vi:vis){
			//first check to see if user exists and is not calculated. If so ignore an go to the next notification
			boolean userIsNative = false;
			UserNotification originalNotification=null;
			for(UserNotification u:vi.getUsers()){
				if(u.getUser().equals(user) && u.getId()!=null){
					originalNotification = u;
					userIsNative=!u.isCalculated();
					//log.info("[recalculateVisualItemAccessStatus] user:[#0] is #1 native in VI:[#2]", user,userIsNative,vi);
					break;
				}
			}
			if(originalNotification==null){
				//log.info("[recalculateVisualItemAccessStatus] user:[#0] not found in original VI:[#1]", user,vi);
			}
			if(!userIsNative){
				this.init();
				this.addUsers(this.convertUserListToSelectModelItemList(vi.getUsers()));
				this.addDepartmentUsers(this.convertToSelectModelItemList(vi.getDepartments()));
				this.addProfCategoryUsers(this.convertToSelectModelItemList(vi.getProfCategories()));
				this.addServiceAreaUsers(this.convertToSelectModelItemList(vi.getServiceAreas()));
				this.addServiceClassificationUsers(this.convertToSelectModelItemList(vi.getServiceClassifications()));
				this.calculateUsers();
				//log.info("[recalculateVisualItemAccessStatus] users calculated:#0",this.selectedUsers);
				//1st. if it's in the list and not in the original list, then add it.
				//2nd. if user is not in the list and was before remove it from the original list
				boolean userFound=false;
				for(SelectModelItem<User> item:this.selectedUsers){
					if(item.getInstance().equals(user)){
						userFound=true;
						//log.info("[recalculateVisualItemAccessStatus] user found!");
						if(originalNotification==null){
							// add the user and save
							//log.info("[recalculateVisualItemAccessStatus] adding user:[#0] to VisualItem:[#1]", user,vi);
							UserNotification un = new UserNotification();
							un.setVisualItem(vi);
							un.setUser(user);
							vi.getUsers().add(un);
							if(vi.getNotification()!=null){
								un.setNotification(vi.getNotification());
								vi.getNotification().getUsers().add(un);
								Notification notification = vi.getNotification();
								if(notification.getUsersNotificationMedia().ordinal()!=NotificationMediaEnum.news.ordinal() &&
										notification.getUsersNotificationMedia().ordinal()!=NotificationMediaEnum.none.ordinal()){
									//this.sendtoDBCron(dbCronApp, entityManager, notification);
								}
							}
							entityManager.merge(vi);
							//entityManager.flush();
							
							//now, if there is a notification involved then notification system should acknowledge that.
							if(vi.getNotification()!=null)
								ModelEventQueue.fireModelUpdatedEvent(vi.getNotification());
						}
					}
				}
				if(!userFound && originalNotification!=null){
					//delete user notification
					//log.info("[recalculateVisualItemAccessStatus] delete user:[#0] from VisualItem:[#1]", user,vi);
					originalNotification.remove(entityManager);
					//entityManager.flush();
				}else if(!userFound){
					//log.info("[recalculateNewsAccessStatus] user:[#0] not found in calculated visual items:[#1]", user,vi);
				}
			}
		}
		
	}
	private List<SelectModelItem<User>> convertUserListToSelectModelItemList(Set<UserNotification> list){
		List<SelectModelItem<User>> result = new ArrayList<SelectModelItem<User>>();
		for(UserNotification item:list){
			//only adds native users
			if(!item.isCalculated()){
				result.add(new SelectModelItem<User>(item.getUser(),item.isCalculated()));
			}
		}
		return result;
	}
	private List convertToSelectModelItemList(Set list){
		List<SelectModelItem> result = new ArrayList<SelectModelItem>();
		for(Object item:list){
			if(item instanceof DepartmentNotification)
				result.add(new SelectModelItem(((DepartmentNotification)item).getDepartment()));
			else if(item instanceof ProfCategoryNotification)
				result.add(new SelectModelItem(((ProfCategoryNotification)item).getProfCategory()));
			else if(item instanceof ServiceAreaNotification)
				result.add(new SelectModelItem(((ServiceAreaNotification)item).getServiceArea()));
			else if(item instanceof ServiceClassificationNotification)
				result.add(new SelectModelItem(((ServiceClassificationNotification)item).getServiceClassification()));
		}
		return result;
	}
	public void sendtoDBCron(DBCronAppLocal dbCronApp,EntityManager entityManager,Notification notification){
		DBCronTask task = this.getExistingTask(entityManager,notification);
		if(task!=null){
			//log.info("[sendtoDBCron] send to quartz from existing dbcron");
			dbCronApp.createTask(task);
		}else{
			if(notification.getSendAt()==null){
				//log.info("[sendtoDBCron] send to quartz new dbcron, with null date");
				dbCronApp.createTask(DBCronTask.getInstance(
						DBCronTaskEnum.ADD_NOTIFICATION,notification.getId(), notification.getId().toString()));
			}else{
				//log.info("[sendtoDBCron] send to quartz new dbcron, with begin date at:#0",instance.getSendAt());
				dbCronApp.createTask(DBCronTask.getInstance(notification.getSendAt(),
						DBCronTaskEnum.ADD_NOTIFICATION,notification.getId(), notification.getId().toString()));
			}
		}
	}
	private DBCronTask getExistingTask(EntityManager entityManager,Notification notification){
		List<DBCronTask> tasks = entityManager.createQuery("select m from DBCronTask m where " +
				"m.task=? and m.entityId is not null and m.entityId=?")
				.setParameter(1, DBCronTaskEnum.ADD_NOTIFICATION)
				.setParameter(2, notification.getId())
				.getResultList();
		//log.info("[getExistingTask] taskList size:#0, instanceId:#1",tasks.size(),this.instance.getId());
		if(tasks.size()>0){
			DBCronTask task = tasks.get(0);
			task.setBeginDate(notification.getSendAt());
			//log.info("[getExistingTask] taskId:#0",task.getId());
			return task;
		}
		return null;
	}
	// getters and setters
	public Set<SelectModelItem<User>> getSelectedUsers() {
		return selectedUsers;
	}

	public void setSelectedUsers(Set<SelectModelItem<User>> selectedUsers) {
		this.selectedUsers = selectedUsers;
	}
	
	public Set<SelectModelItem<Department>> getSelectedDepartments() {
		return selectedDepartments;
	}

	public void setSelectedDepartments(
			Set<SelectModelItem<Department>> selectedDepartments) {
		this.selectedDepartments = selectedDepartments;
	}
	public Log getLog() {
		return log;
	}
	public void setLog(Log log) {
		this.log = log;
	}
	public Map<String, String> getMessages() {
		return messages;
	}
	public void setMessages(Map<String, String> messages) {
		this.messages = messages;
	}
	
	public EntityManager getEntityManager() {
		return entityManager;
	}
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
}
