package com.obc.csrg.bean;

import java.util.List;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.web.RequestParameter;

import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;

import com.obc.csrg.constants.DBCronTaskEnum;
import com.obc.csrg.constants.NotificationMediaEnum;
import com.obc.csrg.local.DBCronAppLocal;
import com.obc.csrg.model.DBCronTask;
import com.obc.csrg.model.Notification;
import com.obc.csrg.model.User;
import com.obc.csrg.model.Department;
import com.obc.csrg.model.ProfCategory;
import com.obc.csrg.model.ServiceArea;
import com.obc.csrg.model.ServiceClassification;
import com.obc.csrg.model.Model;
import com.obc.csrg.model.UserNotification;
import com.obc.csrg.model.DepartmentNotification;
import com.obc.csrg.model.ServiceAreaNotification;
import com.obc.csrg.model.ServiceClassificationNotification;
import com.obc.csrg.model.ProfCategoryNotification;
import com.obc.csrg.util.SelectModelItem;
import com.obc.csrg.util.NotificationUtil;


@Name("notificationEditHome")
public class NotificationEditHome extends NotificationHome<Notification> {

	@In
    private DBCronAppLocal dbCronApp;
	
	//business functions
	
	@Override
	protected void loadAfterCreate(){
		super.loadAfterCreate();
		log.info("[loadAfterCreate]");
		
	}
	
	@Override
	protected boolean verifyData(){
		boolean result = false;
		if(super.verifyData()){
			boolean valid = true;
			if(this.instance.getLocale()==null || this.instance.getLocale().equals(""))
				this.instance.setLocale(locale.getLanguage());
			this.calculateUsers();
			this.updateNotificationUsers();
			this.updateNotificationDepartments();
			this.updateNotificationProfCategories();
			this.updateNotificationServiceAreas();
			this.updateNotificationServiceClassifications();
			if(instance.getUsersNotificationMedia().ordinal()!=NotificationMediaEnum.news.ordinal() &&
					instance.getNews()!=null){
				//old notification media was news. Not anymore, news needs to be removed
				instance.getNews().remove(entityManager);
			}
			if(valid) result = true;
		}
		return result;
	}
	
	@Override
	protected void loadAfterPersist(){
		this.loadNotificationUsers();
		this.loadNotificationDepartments();
		this.loadNotificationProfCategories();
		this.loadNotificationServiceAreas();
		this.loadNotificationServiceClassifications();
		this.initCollapsiblePanels();
		notificationUtil = new NotificationUtil(entityManager,log,messages);
		this.calculateUsers();
		
		if(instance.getUsersNotificationMedia().ordinal()==NotificationMediaEnum.news.ordinal())
			notificationUtil.createNewsFromNotification(instance, entityManager);
		else
			//send to DBCron
			notificationUtil.sendtoDBCron(dbCronApp, entityManager, instance);
	}
	@Override
	protected void loadAfterUpdate(){
		this.loadNotificationUsers();
		this.loadNotificationDepartments();
		this.loadNotificationProfCategories();
		this.loadNotificationServiceAreas();
		this.loadNotificationServiceClassifications();
		this.initCollapsiblePanels();
		notificationUtil = new NotificationUtil(entityManager,log,messages);
		this.calculateUsers();
		
		if(instance.getUsersNotificationMedia().ordinal()==NotificationMediaEnum.news.ordinal())
			notificationUtil.createNewsFromNotification(instance, entityManager);
		else
			//send to DBCron
			notificationUtil.sendtoDBCron(dbCronApp, entityManager, instance);
	}
	
	private void sendtoDBCron(){
		DBCronTask task = this.getExistingTask();
		if(task!=null){
			//log.info("[sendtoDBCron] send to quartz from existing dbcron");
			dbCronApp.createTask(task);
		}else{
			if(instance.getSendAt()==null){
				//log.info("[sendtoDBCron] send to quartz new dbcron, with null date");
				dbCronApp.createTask(DBCronTask.getInstance(
						DBCronTaskEnum.ADD_NOTIFICATION,this.instance.getId(), this.instance.getId().toString()));
			}else{
				//log.info("[sendtoDBCron] send to quartz new dbcron, with begin date at:#0",instance.getSendAt());
				dbCronApp.createTask(DBCronTask.getInstance(instance.getSendAt(),
						DBCronTaskEnum.ADD_NOTIFICATION,this.instance.getId(), this.instance.getId().toString()));
			}
		}
	}
	
	public void selectAllListener(ValueChangeEvent event) {
		String id = event.getComponent().getId();
		boolean value = (Boolean)event.getNewValue();
		//log.info("[selectAllListener] id:#0, value:#1",id,value);
		if(id.equals("selAvUsers")){
			this.selectAllAvUsers = value;
			for(SelectModelItem<User> sm:this.avUsers){
				sm.setSelected(value);
				//log.info("[selectAllListener] sm name#0, value:#1", sm.getInstance(),sm.isSelected());
			}
		}else if(id.equals("selSelUsers")){
			this.selectAllSelUsers=value;
			for(SelectModelItem<User> sm:this.selUsers){
				sm.setSelected(value);
				//log.info("[selectAllListener] sm name#0, value:#1", sm.getInstance(),sm.isSelected());
			}
		}
		for(SelectModelItem<User> sm:this.avUsers){
			log.info("[selectAllListener] sm name#0, value:#1", sm,sm.isSelected());
		}
	}
	
	
	private void updateNotificationUsers(){
		//update/create notification manyToMany objects
		//users - since there are info about delivered and read this method must preserve already valid rows
		List<SelectModelItem<User>> existingUsers = new ArrayList<SelectModelItem<User>>();
		Set<UserNotification> deletedUsers = new HashSet<UserNotification>();
		//log.info("[updateNotificationUsers] available users:#0", calculatedUsers);
		//log.info("[updateNotificationUsers] already existing users:#0", instance.getUsers());
		for(UserNotification un:instance.getUsers()){
			boolean userExist=false;
			//log.info("[updateNotificationUsers] userNotification:#0", un);
			for(SelectModelItem<User> u:this.calculatedUsers){
				//log.info("[updateNotificationUsers] u:#0, is equal?:#1", u,un.getUser().equals(u.getInstance()));
				//log.info("[updateNotificationUsers] unId:#0, uId:#1", un.getUser().getId(),u.getInstance().getId());
				if(un.getUser().getId().equals(u.getInstance().getId())){
					//log.info("[updateNotificationUsers] existing user:#0", u.getInstance());
					existingUsers.add(u);
					userExist=true;
					break;
				}
			}
			if(!userExist){
				//log.info("[updateNotificationUsers] deleted user:#0", un.getUser());
				deletedUsers.add(un);
			}
		}
		
		//log.info("[updateNotificationUsers] deletedUsers:#0", deletedUsers);
		if(deletedUsers.size()>0){
			instance.getUsers().removeAll(deletedUsers);
			for(UserNotification un:deletedUsers){
				un.remove(entityManager);
			}
		}
		//log.info("[updateNotificationUsers] calculatedUsers before remove:#0", this.calculatedUsers);
		this.calculatedUsers.removeAll(existingUsers);
		//log.info("[updateNotificationUsers] calculatedUsers after remove:#0", this.calculatedUsers);
		for(SelectModelItem<User> u:this.calculatedUsers){
			UserNotification un = new UserNotification();
			un.setCalculated(u.isCalculated());
			un.setNotification(instance);
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
			for(SelectModelItem<Department> item:this.selDepartments){
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
		this.selDepartments.removeAll(existingModels);
		for(SelectModelItem<Department> item:this.selDepartments){
			DepartmentNotification mn = new DepartmentNotification();
			mn.setNotification(instance);
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
			for(SelectModelItem<ProfCategory> item:this.selProfCategories){
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
		this.selProfCategories.removeAll(existingModels);
		for(SelectModelItem<ProfCategory> item:this.selProfCategories){
			ProfCategoryNotification mn = new ProfCategoryNotification();
			mn.setNotification(instance);
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
			for(SelectModelItem<ServiceArea> item:this.selServiceAreas){
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
		this.selServiceAreas.removeAll(existingModels);
		for(SelectModelItem<ServiceArea> item:this.selServiceAreas){
			ServiceAreaNotification mn = new ServiceAreaNotification();
			mn.setNotification(instance);
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
			for(SelectModelItem<ServiceClassification> item:this.selServiceClassifications){
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
		this.selServiceClassifications.removeAll(existingModels);
		for(SelectModelItem<ServiceClassification> item:this.selServiceClassifications){
			ServiceClassificationNotification mn = new ServiceClassificationNotification();
			mn.setNotification(instance);
			mn.setServiceClassification(item.getInstance());
			entityManager.persist(mn);
			item.getInstance().getNotifications().add(mn);
			instance.getServiceClassifications().add(mn);
		}
	}
	
	public void sendNotification(){
		//log.info("[sendNotification]");
		notificationUtil.notifyTargets(instance, entityManager, log, stateBean.getConfig(),false);
	}
	public void forceSendNotification(){
		//log.info("[forceSendNotication] config:#0", stateBean.getConfig());
		notificationUtil.notifyTargets(instance, entityManager, log, stateBean.getConfig(),true);
	}
	//getters and setters
	
}
