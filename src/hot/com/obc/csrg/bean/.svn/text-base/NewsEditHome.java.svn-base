package com.obc.csrg.bean;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jboss.seam.annotations.Name;

import com.obc.csrg.events.ModelEventQueue;
import com.obc.csrg.model.Department;
import com.obc.csrg.model.DepartmentNotification;
import com.obc.csrg.model.News;
import com.obc.csrg.model.DBFile;
import com.obc.csrg.model.ProfCategory;
import com.obc.csrg.model.ProfCategoryNotification;
import com.obc.csrg.model.ServiceArea;
import com.obc.csrg.model.ServiceAreaNotification;
import com.obc.csrg.model.ServiceClassification;
import com.obc.csrg.model.ServiceClassificationNotification;
import com.obc.csrg.model.User;
import com.obc.csrg.model.UserNotification;
import com.obc.csrg.util.NotificationUtil;
import com.obc.csrg.util.SelectModelItem;

@Name("newsEditHome")
public class NewsEditHome extends NewsHome<News> {

	@Override
	protected void loadAfterPersist(){
		super.loadAfterPersist();
		//log.info("[loadAfterPersist] photo:#0", this.getInstance().getPerson().getPhoto());
		this.setPriFile(new DBFile());
		if(this.getInstance().getThumbnail()!=null){
			this.getPriFile().setData(this.getInstance().getThumbnail());
		}
		this.setSecFile(new DBFile());
		if(this.getInstance().getImage()!=null){
			this.getPriFile().setData(this.getInstance().getImage());
		}
		this.loadNotificationUsers();
		this.loadNotificationDepartments();
		this.loadNotificationProfCategories();
		this.loadNotificationServiceAreas();
		this.loadNotificationServiceClassifications();
		this.getTargetsManager().initCollapsiblePanels();
		notificationUtil = new NotificationUtil(entityManager,log,messages);
		this.calculateUsers();
	}
	protected void loadAfterUpdate(){
		super.loadAfterUpdate();
		//log.info("[loadAfterCreate] photo:#0", this.getInstance().getPerson().getPhoto());
		this.setPriFile(new DBFile());
		if(this.getInstance().getThumbnail()!=null){
			this.getPriFile().setData(this.getInstance().getThumbnail());
		}
		this.setSecFile(new DBFile());
		if(this.getInstance().getImage()!=null){
			this.getPriFile().setData(this.getInstance().getImage());
		}
		this.loadNotificationUsers();
		this.loadNotificationDepartments();
		this.loadNotificationProfCategories();
		this.loadNotificationServiceAreas();
		this.loadNotificationServiceClassifications();
		this.getTargetsManager().initCollapsiblePanels();
		notificationUtil = new NotificationUtil(entityManager,log,messages);
		this.calculateUsers();
	}
	
	@Override
	protected boolean verifyData(){
		boolean result = false;
		if(super.verifyData()){
			boolean valid = true;
			if(this.instance.getLocale()==null || this.instance.getLocale().equals(""))
				this.instance.setLocale(locale.getLanguage());
			//log.info("[verifyData] priFile:#0", this.getPriFile().getData());
			if(this.fileIsImage(this.getPriFile())){
				//log.info("[verifyData] priFile is image!!!");
				this.getInstance().setThumbnail(this.getPriFile().getData());
			}
			if(this.fileIsImage(this.getSecFile())){
				//log.info("[verifyData] secFile is image!!!");
				this.getInstance().setImage(this.getSecFile().getData());
			}
			this.calculateUsers();
			this.updateNotificationUsers();
			this.updateNotificationDepartments();
			this.updateNotificationProfCategories();
			this.updateNotificationServiceAreas();
			this.updateNotificationServiceClassifications();
			if(valid) result = true;
		}
		return result;
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
			un.setNews(instance);
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
			mn.setNews(instance);
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
			mn.setNews(instance);
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
			mn.setNews(instance);
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
			mn.setNews(instance);
			mn.setServiceClassification(item.getInstance());
			entityManager.persist(mn);
			item.getInstance().getNotifications().add(mn);
			instance.getServiceClassifications().add(mn);
			
		}
	}
}
