package com.obc.csrg.bean;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;

import com.obc.csrg.constants.NotificationMediaEnum;
import com.obc.csrg.local.DBCronAppLocal;
import com.obc.csrg.model.ComposedNotification;
import com.obc.csrg.model.Department;
import com.obc.csrg.model.DepartmentNotification;
import com.obc.csrg.model.ProfCategory;
import com.obc.csrg.model.ProfCategoryNotification;
import com.obc.csrg.model.ServiceArea;
import com.obc.csrg.model.ServiceAreaNotification;
import com.obc.csrg.model.ServiceClassification;
import com.obc.csrg.model.ServiceClassificationNotification;
import com.obc.csrg.model.User;
import com.obc.csrg.model.UserNotification;
import com.obc.csrg.model.Notification;
import com.obc.csrg.util.NotificationUtil;
import com.obc.csrg.util.SelectModelItem;

@Name("composedNotificationEditHome")
public class ComposedNotificationEditHome extends ComposedNotificationHome<ComposedNotification> {

	@In
    private DBCronAppLocal dbCronApp;
	
	private final long DELAY = 15000L;//delay between notifications
	
	@Override
	protected void loadAfterCreate(){
		super.loadAfterCreate();
	}
	
	protected void loadAfterUpdate(){
		super.loadAfterUpdate();
		
		this.loadNotificationUsers();
		this.loadNotificationDepartments();
		this.loadNotificationProfCategories();
		this.loadNotificationServiceAreas();
		this.loadNotificationServiceClassifications();
		this.calculateUsers();
		this.notifyTargets();
	}
	
	@Override
	protected void loadAfterPersist(){
		super.loadAfterPersist();
		this.loadNotificationUsers();
		this.loadNotificationDepartments();
		this.loadNotificationProfCategories();
		this.loadNotificationServiceAreas();
		this.loadNotificationServiceClassifications();
		this.calculateUsers();
		this.notifyTargets();
	}
	@Override
	protected boolean verifyData(){
		boolean result = false;
		if(super.verifyData()){
			boolean valid = true;
			if(this.instance.getLocale()==null || this.instance.getLocale().equals(""))
				this.instance.setLocale(locale.getLanguage());
			
			this.calculateUsers();
			
			//manage N1 tables
			this.updateNotificationUsers();
			this.updateNotificationDepartments();
			this.updateNotificationProfCategories();
			this.updateNotificationServiceAreas();
			this.updateNotificationServiceClassifications();
			//manage notifications
			this.updateNotifications();
			//update notifications N1 tables using composed notification N1 tables as a reference
			this.updateNotificationsN1Tables();
			if(valid) result = true;
		}
		return result;
	}

	/**
	 * iterates notifications and notify targets using DBCron
	 */
	private void notifyTargets(){
		for(Notification n:instance.getNotifications()){
			NotificationUtil notificationUtil = new NotificationUtil(entityManager,log,messages);
			if(n.getUsersNotificationMedia().ordinal()==NotificationMediaEnum.news.ordinal())
				notificationUtil.createNewsFromNotification(n, entityManager);
			else{
				//log.info("[notifyTargets] notification id:#0, notification type:#1", n.getId(),n.getUsersNotificationMedia().getName());
				notificationUtil.sendtoDBCron(dbCronApp, entityManager, n);
				//log.info("[notifyTargets] going to sleep");
				//this.sleepAtLeast(7000L);
				//log.info("[notifyTargets] going to next");
			}
		}
	}
	
	private void sleepAtLeast(long millis) {
		long t0 = System.currentTimeMillis();
		long millisLeft = millis;
		while (millisLeft > 0) {
			try{
				Thread.sleep(millisLeft);
			}catch(InterruptedException ie){}
			long t1 = System.currentTimeMillis();
			millisLeft = millis - (t1 - t0);
		}
	}
	/**
	 * for each notification it will call methods to check and synchronize
	 * individual notifications (user notifications, etc)
	 */
	private void updateNotificationsN1Tables(){
		for(Notification n:instance.getNotifications()){
			this.updateNotificationUsers(n);
			this.updateNotificationDepartments(n);
			this.updateNotificationProfCategories(n);
			this.updateNotificationServiceAreas(n);
			this.updateNotificationServiceClassifications(n);
		}
	}
	
	private void updateNotificationUsers(Notification n){
		//update/create notification manyToMany objects
		//users - since there are info about delivered properties and read status this method must preserve already valid rows
		List<UserNotification> existingUsers = new ArrayList<UserNotification>();
		Set<UserNotification> deletedUsers = new HashSet<UserNotification>();
		Set<UserNotification> newUsers = new HashSet<UserNotification>();
		
		//initialize newUsers
		newUsers.addAll(instance.getUsers());
		log.info("[updateNotificationUsers] newUsers:#0", newUsers);
		//add all old users to deletedUsers
		deletedUsers.addAll(n.getUsers());
		log.info("[updateNotificationUsers] deletedUsers:#0", deletedUsers);
		for(UserNotification un:instance.getUsers()){
			for(UserNotification u:deletedUsers){
				if(un.getUser().getId().equals(u.getUser().getId())){
					existingUsers.add(u);
					break;
				}
			}
		}
		//log.info("[updateNotificationUsers] existingUsers:#0", existingUsers);
		//remove existing users from deletedUsers
		deletedUsers.removeAll(existingUsers);
		//log.info("[updateNotificationUsers] deletedUsers after cleaning existingUsers:#0", deletedUsers);
		//deletedUsers now has only users to be removed
		if(deletedUsers.size()>0){
			n.getUsers().removeAll(deletedUsers);
			//log.info("[updateNotificationUsers] n.getUsers after cleaning deleted users:#0", n.getUsers());
			for(UserNotification un:deletedUsers){
				un.remove(entityManager);
			}
		}
		Set<UserNotification> usersToAdd = new HashSet<UserNotification>();
		for(UserNotification un:newUsers){
			boolean userExist=false;
			for(UserNotification u:existingUsers){
				if(un.getUser().getId().equals(u.getUser().getId())){
					userExist=true;
					break;
				}
			}
			if(!userExist)
				usersToAdd.add(un);
		}
		//log.info("[updateNotificationUsers] usersToAdd:#0", usersToAdd);
		for(UserNotification u:usersToAdd){
			//log.info("[updateNotificationUsers] add new user:#0", u);
			UserNotification un = new UserNotification();
			un.setCalculated(u.isCalculated());
			un.setNotification(n);
			un.setRead(false);
			un.setSent(false);
			un.setShowned(false);
			un.setUser(u.getUser());
			entityManager.persist(un);
			n.getUsers().add(un);
		}
	}
	private void updateNotificationDepartments(Notification n){
		//update/create notification manyToMany objects
		List<DepartmentNotification> existingModels = new ArrayList<DepartmentNotification>();
		Set<DepartmentNotification> deletedModels = new HashSet<DepartmentNotification>();
		Set<DepartmentNotification> newModels = new HashSet<DepartmentNotification>();
		
		//initialize newModels
		newModels.addAll(instance.getDepartments());
		//add all old models to deletedModels
		deletedModels.addAll(n.getDepartments());
		for(DepartmentNotification un:instance.getDepartments()){
			for(DepartmentNotification u:deletedModels){
				if(un.getDepartment().getId().equals(u.getDepartment().getId())){
					existingModels.add(u);
					break;
				}
			}
		}
		//remove existing models from deletedModels
		deletedModels.removeAll(existingModels);
		//deletedModels now has only users to be removed
		if(deletedModels.size()>0){
			n.getDepartments().removeAll(deletedModels);
			for(DepartmentNotification un:deletedModels){
				un.remove(entityManager);
			}
		}
		Set<DepartmentNotification> modelsToAdd = new HashSet<DepartmentNotification>();
		for(DepartmentNotification un:newModels){
			boolean modelExist=false;
			for(DepartmentNotification u:existingModels){
				if(un.getDepartment().getId().equals(u.getDepartment().getId())){
					modelExist=true;
					break;
				}
			}
			if(!modelExist)
				modelsToAdd.add(un);
		}
		for(DepartmentNotification u:modelsToAdd){
			DepartmentNotification un = new DepartmentNotification();
			un.setNotification(n);
			un.setDepartment(u.getDepartment());
			entityManager.persist(un);
			n.getDepartments().add(un);
		}
	}
	private void updateNotificationProfCategories(Notification n){
		//update/create notification manyToMany objects
		List<ProfCategoryNotification> existingModels = new ArrayList<ProfCategoryNotification>();
		Set<ProfCategoryNotification> deletedModels = new HashSet<ProfCategoryNotification>();
		Set<ProfCategoryNotification> newModels = new HashSet<ProfCategoryNotification>();
		
		//initialize newModels
		newModels.addAll(instance.getProfCategories());
		//add all old models to deletedModels
		deletedModels.addAll(n.getProfCategories());
		for(ProfCategoryNotification un:instance.getProfCategories()){
			for(ProfCategoryNotification u:deletedModels){
				if(un.getProfCategory().getId().equals(u.getProfCategory().getId())){
					existingModels.add(u);
					break;
				}
			}
		}
		//remove existing models from deletedModels
		deletedModels.removeAll(existingModels);
		//deletedModels now has only users to be removed
		if(deletedModels.size()>0){
			n.getProfCategories().removeAll(deletedModels);
			for(ProfCategoryNotification un:deletedModels){
				un.remove(entityManager);
			}
		}
		Set<ProfCategoryNotification> modelsToAdd = new HashSet<ProfCategoryNotification>();
		for(ProfCategoryNotification un:newModels){
			boolean modelExist=false;
			for(ProfCategoryNotification u:existingModels){
				if(un.getProfCategory().getId().equals(u.getProfCategory().getId())){
					modelExist=true;
					break;
				}
			}
			if(!modelExist)
				modelsToAdd.add(un);
		}
		for(ProfCategoryNotification u:modelsToAdd){
			ProfCategoryNotification un = new ProfCategoryNotification();
			un.setNotification(n);
			un.setProfCategory(u.getProfCategory());
			entityManager.persist(un);
			n.getProfCategories().add(un);
		}
	}
	private void updateNotificationServiceAreas(Notification n){
		//update/create notification manyToMany objects
		List<ServiceAreaNotification> existingModels = new ArrayList<ServiceAreaNotification>();
		Set<ServiceAreaNotification> deletedModels = new HashSet<ServiceAreaNotification>();
		Set<ServiceAreaNotification> newModels = new HashSet<ServiceAreaNotification>();
		
		//initialize newModels
		newModels.addAll(instance.getServiceAreas());
		//add all old models to deletedModels
		deletedModels.addAll(n.getServiceAreas());
		for(ServiceAreaNotification un:instance.getServiceAreas()){
			for(ServiceAreaNotification u:deletedModels){
				if(un.getServiceArea().getId().equals(u.getServiceArea().getId())){
					existingModels.add(u);
					break;
				}
			}
		}
		//remove existing models from deletedModels
		deletedModels.removeAll(existingModels);
		//deletedModels now has only users to be removed
		if(deletedModels.size()>0){
			n.getServiceAreas().removeAll(deletedModels);
			for(ServiceAreaNotification un:deletedModels){
				un.remove(entityManager);
			}
		}
		Set<ServiceAreaNotification> modelsToAdd = new HashSet<ServiceAreaNotification>();
		for(ServiceAreaNotification un:newModels){
			boolean modelExist=false;
			for(ServiceAreaNotification u:existingModels){
				if(un.getServiceArea().getId().equals(u.getServiceArea().getId())){
					modelExist=true;
					break;
				}
			}
			if(!modelExist)
				modelsToAdd.add(un);
		}
		for(ServiceAreaNotification u:modelsToAdd){
			ServiceAreaNotification un = new ServiceAreaNotification();
			un.setNotification(n);
			un.setServiceArea(u.getServiceArea());
			entityManager.persist(un);
			n.getServiceAreas().add(un);
		}
	}
	
	private void updateNotificationServiceClassifications(Notification n){
		//update/create notification manyToMany objects
		List<ServiceClassificationNotification> existingModels = new ArrayList<ServiceClassificationNotification>();
		Set<ServiceClassificationNotification> deletedModels = new HashSet<ServiceClassificationNotification>();
		Set<ServiceClassificationNotification> newModels = new HashSet<ServiceClassificationNotification>();
		
		//initialize newModels
		newModels.addAll(instance.getServiceClassifications());
		//add all old models to deletedModels
		deletedModels.addAll(n.getServiceClassifications());
		for(ServiceClassificationNotification un:instance.getServiceClassifications()){
			for(ServiceClassificationNotification u:deletedModels){
				if(un.getServiceClassification().getId().equals(u.getServiceClassification().getId())){
					existingModels.add(u);
					break;
				}
			}
		}
		//remove existing models from deletedModels
		deletedModels.removeAll(existingModels);
		//deletedModels now has only users to be removed
		if(deletedModels.size()>0){
			n.getServiceClassifications().removeAll(deletedModels);
			for(ServiceClassificationNotification un:deletedModels){
				un.remove(entityManager);
			}
		}
		Set<ServiceClassificationNotification> modelsToAdd = new HashSet<ServiceClassificationNotification>();
		for(ServiceClassificationNotification un:newModels){
			boolean modelExist=false;
			for(ServiceClassificationNotification u:existingModels){
				if(un.getServiceClassification().getId().equals(u.getServiceClassification().getId())){
					modelExist=true;
					break;
				}
			}
			if(!modelExist)
				modelsToAdd.add(un);
		}
		for(ServiceClassificationNotification u:modelsToAdd){
			ServiceClassificationNotification un = new ServiceClassificationNotification();
			un.setNotification(n);
			un.setServiceClassification(u.getServiceClassification());
			entityManager.persist(un);
			n.getServiceClassifications().add(un);
		}
	}
	private void updateNotifications(){
		//update/create notification objects
		long delay = 0L;
		List<Notification> existingNotifications = new ArrayList<Notification>();
		Set<Notification> deletedNotifications = new HashSet<Notification>();
		//first insert new notifications
		for(Notification existingNotification:this.getNotificationList()){
			if(existingNotification.getId()==null || existingNotification.getId().equals(0L))
				existingNotifications.add(existingNotification);
		}
		for(Notification selectedNotification:instance.getNotifications()){
			boolean notificationExist=false;
			for(Notification existingNotification:this.getNotificationList()){
				if(existingNotification.getId()!=null &&
						selectedNotification.getId().equals(existingNotification.getId())){
					existingNotifications.add(existingNotification);
					notificationExist=true;
					break;
				}
			}
			if(!notificationExist){
				deletedNotifications.add(selectedNotification);
			}
		}
		if(deletedNotifications.size()>0){
			instance.getNotifications().removeAll(deletedNotifications);
			for(Notification n:deletedNotifications){
				n.remove(entityManager);
			}
		}
		for(Notification n:existingNotifications){
			//copy composedNotification info to each notification object
			n.setCreationDate(instance.getCreationDate());
			n.setEndAt(instance.getEndAt());
			n.setKeepAlive(instance.isKeepAlive());
			n.setLocale(instance.getLocale());
			n.setReadyToSend(instance.isReadyToSend());
			n.setSendAt(instance.getSendAt());
			n.setSender(instance.getSender());
			n.setSubject(instance.getSubject());
			Calendar c = Calendar.getInstance();
			
			//deal with user delay when filling form
			if(n.getSendAt().getTime()<c.getTimeInMillis())
				n.setSendAt(c.getTime());
			//add delay
			delay += this.DELAY;
			if(delay!=0L){
				long time = n.getSendAt().getTime() + delay;
				c.setTimeInMillis(time);
				n.setSendAt(c.getTime());
			}
			
			//save notification
			if(n.getId()==null){
				n.setComposedNotification(instance);
				entityManager.persist(n);
				instance.getNotifications().add(n);
			}else{
				entityManager.merge(n);
			}
		}
	}
	private void updateNotificationUsers(){
		//update/create notification manyToMany objects
		//users - since there are info about delivered properties and read status this method must preserve already valid rows
		List<SelectModelItem<User>> existingUsers = new ArrayList<SelectModelItem<User>>();
		Set<UserNotification> deletedUsers = new HashSet<UserNotification>();
		for(UserNotification un:instance.getUsers()){
			boolean userExist=false;
			for(SelectModelItem<User> u:this.getTargetsManager().getCalculatedUsers()){
				if(un.getUser().getId().equals(u.getInstance().getId())){
					existingUsers.add(u);
					userExist=true;
					break;
				}
			}
			if(!userExist){
				deletedUsers.add(un);
			}
		}
		if(deletedUsers.size()>0){
			instance.getUsers().removeAll(deletedUsers);
			for(UserNotification un:deletedUsers){
				un.remove(entityManager);
			}
		}
		this.getTargetsManager().getCalculatedUsers().removeAll(existingUsers);
		for(SelectModelItem<User> u:this.getTargetsManager().getCalculatedUsers()){
			UserNotification un = new UserNotification();
			un.setCalculated(u.isCalculated());
			un.setComposedNotification(instance);
			un.setRead(false);
			un.setSent(false);
			un.setShowned(false);
			un.setUser(u.getInstance());
			entityManager.persist(un);
			u.getInstance().getNotifications().add(un);
			instance.getUsers().add(un);
		}
	}

	private void updateNotificationDepartments(){
		List<SelectModelItem<Department>> existingModels = new ArrayList<SelectModelItem<Department>>();
		Set<DepartmentNotification> deletedModels = new HashSet<DepartmentNotification>();
		for(DepartmentNotification mn:instance.getDepartments()){
			boolean modelExist=false;
			for(SelectModelItem<Department> item:this.getTargetsManager().getSelDepartments()){
				if(mn.getDepartment().getId().equals(item.getInstance().getId())){
					existingModels.add(item);
					modelExist=true;
					break;
				}
			}
			if(!modelExist){
				deletedModels.add(mn);
			}
		}
		if(deletedModels.size()>0){
			instance.getDepartments().removeAll(deletedModels);
			for(DepartmentNotification mn:deletedModels){
				mn.remove(entityManager);
			}
		}
		this.getTargetsManager().getSelDepartments().removeAll(existingModels);
		for(SelectModelItem<Department> item:this.getTargetsManager().getSelDepartments()){
			DepartmentNotification mn = new DepartmentNotification();
			mn.setComposedNotification(instance);
			mn.setDepartment(item.getInstance());
			entityManager.persist(mn);
			item.getInstance().getNotifications().add(mn);
			instance.getDepartments().add(mn);
		}
	}
	private void updateNotificationProfCategories(){
		List<SelectModelItem<ProfCategory>> existingModels = new ArrayList<SelectModelItem<ProfCategory>>();
		Set<ProfCategoryNotification> deletedModels = new HashSet<ProfCategoryNotification>();
		for(ProfCategoryNotification mn:instance.getProfCategories()){
			boolean modelExist=false;
			for(SelectModelItem<ProfCategory> item:this.getTargetsManager().getSelProfCategories()){
				if(mn.getProfCategory().getId().equals(item.getInstance().getId())){
					existingModels.add(item);
					modelExist=true;
					break;
				}
			}
			if(!modelExist){
				deletedModels.add(mn);
			}
		}
		if(deletedModels.size()>0){
			this.instance.getProfCategories().removeAll(deletedModels);
			for(ProfCategoryNotification mn:deletedModels){
				mn.remove(entityManager);
			}
		}
		this.getTargetsManager().getSelProfCategories().removeAll(existingModels);
		for(SelectModelItem<ProfCategory> item:this.getTargetsManager().getSelProfCategories()){
			ProfCategoryNotification mn = new ProfCategoryNotification();
			mn.setComposedNotification(instance);
			mn.setProfCategory(item.getInstance());
			entityManager.persist(mn);
			item.getInstance().getNotifications().add(mn);
			instance.getProfCategories().add(mn);
		}
	}
	private void updateNotificationServiceAreas(){
		List<SelectModelItem<ServiceArea>> existingModels = new ArrayList<SelectModelItem<ServiceArea>>();
		Set<ServiceAreaNotification> deletedModels = new HashSet<ServiceAreaNotification>();
		for(ServiceAreaNotification mn:instance.getServiceAreas()){
			boolean modelExist=false;
			for(SelectModelItem<ServiceArea> item:this.getTargetsManager().getSelServiceAreas()){
				if(mn.getServiceArea().getId().equals(item.getInstance().getId())){
					existingModels.add(item);
					modelExist=true;
					break;
				}
			}
			if(!modelExist){
				deletedModels.add(mn);
			}
		}
		if(deletedModels.size()>0){
			this.instance.getServiceAreas().removeAll(deletedModels);
			for(ServiceAreaNotification mn:deletedModels){
				mn.remove(entityManager);
			}
		}
		this.getTargetsManager().getSelServiceAreas().removeAll(existingModels);
		for(SelectModelItem<ServiceArea> item:this.getTargetsManager().getSelServiceAreas()){
			ServiceAreaNotification mn = new ServiceAreaNotification();
			mn.setComposedNotification(instance);
			mn.setServiceArea(item.getInstance());
			entityManager.persist(mn);
			item.getInstance().getNotifications().add(mn);
			instance.getServiceAreas().add(mn);
		}
	}
	private void updateNotificationServiceClassifications(){
		List<SelectModelItem<ServiceClassification>> existingModels = new ArrayList<SelectModelItem<ServiceClassification>>();
		Set<ServiceClassificationNotification> deletedModels = new HashSet<ServiceClassificationNotification>();
		for(ServiceClassificationNotification mn:instance.getServiceClassifications()){
			boolean modelExist=false;
			for(SelectModelItem<ServiceClassification> item:this.getTargetsManager().getSelServiceClassifications()){
				if(mn.getServiceClassification().getId().equals(item.getInstance().getId())){
					existingModels.add(item);
					modelExist=true;
					break;
				}
			}
			if(!modelExist){
				deletedModels.add(mn);
			}
		}
		if(deletedModels.size()>0){
			this.instance.getServiceClassifications().removeAll(deletedModels);
			for(ServiceClassificationNotification mn:deletedModels){
				mn.remove(entityManager);
			}
		}
		this.getTargetsManager().getSelServiceClassifications().removeAll(existingModels);
		for(SelectModelItem<ServiceClassification> item:this.getTargetsManager().getSelServiceClassifications()){
			ServiceClassificationNotification mn = new ServiceClassificationNotification();
			mn.setComposedNotification(instance);
			mn.setServiceClassification(item.getInstance());
			entityManager.persist(mn);
			item.getInstance().getNotifications().add(mn);
			instance.getServiceClassifications().add(mn);
			
		}
	}
}
