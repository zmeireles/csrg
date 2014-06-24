package com.obc.csrg.bean;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;


import com.icesoft.faces.context.effects.JavascriptContext;
import com.obc.csrg.constants.NotificationMediaEnum;
import com.obc.csrg.local.DBCronAppLocal;
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
import com.obc.csrg.model.VisualItem;
import com.obc.csrg.util.NotificationUtil;
import com.obc.csrg.util.SelectModelItem;

@Name("visualItemEditHome")
public class VisualItemEditHome extends VisualItemHome<VisualItem> {

	@In
    private DBCronAppLocal dbCronApp;
	
	@Override
	protected void loadAfterPersist() {
		super.loadAfterPersist();
		this.setNewParent(instance.getParent());
		this.initParentList();
		
		this.loadNotificationUsers();
		this.loadNotificationDepartments();
		this.loadNotificationProfCategories();
		this.loadNotificationServiceAreas();
		this.loadNotificationServiceClassifications();
		this.getTargetsManager().initCollapsiblePanels();
		notificationUtil = new NotificationUtil(entityManager,log,messages);
		this.calculateUsers();
		if(instance.getNotification()!=null){
			if(instance.getNotification().getUsersNotificationMedia().ordinal()==
					NotificationMediaEnum.news.ordinal())
				notificationUtil.createNewsFromNotification(instance.getNotification(), entityManager);
			//also send to DBCron
			notificationUtil.sendtoDBCron(dbCronApp, entityManager, instance.getNotification());
		}
	}

	@Override
	protected void loadAfterUpdate() {
		super.loadAfterUpdate();
		this.setNewParent(instance.getParent());
		this.initParentList();
		
		this.loadNotificationUsers();
		this.loadNotificationDepartments();
		this.loadNotificationProfCategories();
		this.loadNotificationServiceAreas();
		this.loadNotificationServiceClassifications();
		this.getTargetsManager().initCollapsiblePanels();
		notificationUtil = new NotificationUtil(entityManager,log,messages);
		this.calculateUsers();
		if(instance.getNotification()!=null){
			if(instance.getNotification().getUsersNotificationMedia().ordinal()==
					NotificationMediaEnum.news.ordinal())
				notificationUtil.createNewsFromNotification(instance.getNotification(), entityManager);
			//also send to DBCron 
			notificationUtil.sendtoDBCron(dbCronApp, entityManager, instance.getNotification());
		}
	}

	@Override
	protected boolean verifyData() {
		boolean result = false;
		if (super.verifyData()) {
			boolean valid = true;
			if(this.instance.getLocale()==null || this.instance.getLocale().equals(""))
				this.instance.setLocale(locale.getLanguage());
			if(this.instance.getWebpage().getLocale()==null || this.instance.getWebpage().getLocale().equals(""))
				this.instance.getWebpage().setLocale(locale.getLanguage());
			//TODO: perform more intensive tests
			if (instance.getId() == null) {// new model
				instance.setParent(getNewParent());
				if (this.getNewParent() != null) {
					this.getNewParent().getChildren().add(this.instance);
				}
			} else if (!(instance.getParent() == null && this.getNewParent() == null)
					|| this.getNewParent()==null || !instance.getId().equals(this.getNewParent().getId())) {
				// parent changed
				if (this.getNewParent() != null) {
					// if new parent is one of the descendant
					VisualItem directChild = instance.imParentOf(this
							.getNewParent());
					if (directChild != null) {// new parent is a descendant
						if (this.instance.getParent() != null) {
							this.instance.getParent().getChildren().add(
									directChild);
							this.instance.getParent().getChildren().remove(
									this.instance);
						}
						directChild.setParent(this.instance.getParent());
						this.instance.setParent(this.getNewParent());
						this.getNewParent().getChildren().add(this.instance);
					} else {// new parent it's not a descendant
						if (this.instance.getParent() != null) {
							this.instance.getParent().getChildren().remove(
									this.instance);
						}
						this.instance.setParent(this.getNewParent());
						this.getNewParent().getChildren().add(this.instance);
					}
				} else {// new parent is null
					if (this.instance.getParent() != null) {
						this.instance.getParent().getChildren().remove(
								this.instance);
					}
					this.instance.setParent(this.getNewParent());
				}
			}
			
			//NS integration
			if(this.integrateWithNS){
				if(this.instance.getNotification()==null){
					//not yet integrated
					this.notification.setVisualItem(instance);
					instance.setNotification(this.getNotification());
					entityManager.persist(notification);
					instance.setNotification(notification);
				}else{
					//check if notification media was news and changed
					if(instance.getNotification().getUsersNotificationMedia().ordinal()!=NotificationMediaEnum.news.ordinal() &&
							instance.getNotification().getNews()!=null){
						//old notification media was news. Not anymore, news needs to be removed
						instance.getNotification().getNews().remove(entityManager);
					}
				}
				if(instance.isPublishAfterNotification()){
					log.info("[verifyData] set publish to false");
					instance.setPublish(false);
				}
			}else{
				if(this.instance.getNotification()!=null){
					//there was a notification link that needs to be deleted
					notification.remove(entityManager);
					instance.setNotification(null);
				}//else there is nothing to do
			}
			//finally update access/notification rules
			this.calculateUsers();
			this.updateNotificationUsers();
			this.updateNotificationDepartments();
			this.updateNotificationProfCategories();
			this.updateNotificationServiceAreas();
			this.updateNotificationServiceClassifications();
			if (valid)
				result = true;
		}
		return result;
	}


	
	
	private void updateNotificationUsers(){
		//update/create notification manyToMany objects
		//users - since there are info about delivered properties and read this method must preserve already valid rows
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
			un.setVisualItem(instance);
			un.setRead(false);
			un.setSent(false);
			un.setShowned(false);
			un.setUser(u.getInstance());
			un.setNotification(instance.getNotification());
			entityManager.persist(un);
			u.getInstance().getNotifications().add(un);
			instance.getUsers().add(un);
			if(instance.getNotification()!=null)
				instance.getNotification().getUsers().add(un);
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
			mn.setVisualItem(instance);
			mn.setDepartment(item.getInstance());
			mn.setNotification(instance.getNotification());
			entityManager.persist(mn);
			item.getInstance().getNotifications().add(mn);
			instance.getDepartments().add(mn);
			if(instance.getNotification()!=null)
				instance.getNotification().getDepartments().add(mn);
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
			mn.setVisualItem(instance);
			mn.setProfCategory(item.getInstance());
			mn.setNotification(instance.getNotification());
			entityManager.persist(mn);
			item.getInstance().getNotifications().add(mn);
			instance.getProfCategories().add(mn);
			if(instance.getNotification()!=null)
				instance.getNotification().getProfCategories().add(mn);
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
			mn.setVisualItem(instance);
			mn.setServiceArea(item.getInstance());
			mn.setNotification(instance.getNotification());
			entityManager.persist(mn);
			item.getInstance().getNotifications().add(mn);
			instance.getServiceAreas().add(mn);
			if(instance.getNotification()!=null)
				instance.getNotification().getServiceAreas().add(mn);
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
			mn.setVisualItem(instance);
			mn.setServiceClassification(item.getInstance());
			mn.setNotification(instance.getNotification());
			entityManager.persist(mn);
			item.getInstance().getNotifications().add(mn);
			instance.getServiceClassifications().add(mn);
			if(instance.getNotification()!=null)
				instance.getNotification().getServiceClassifications().add(mn);
		}
	}
}
