package com.obc.csrg.bean;

import java.util.List;
import java.util.ArrayList;
import java.io.Serializable;

import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;


import org.jboss.seam.annotations.Begin;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.Destroy;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.web.RequestParameter;

import com.obc.csrg.constants.NotificationMediaEnum;
import com.obc.csrg.model.ComposedNotification;
import com.obc.csrg.model.Department;
import com.obc.csrg.model.DepartmentNotification;
import com.obc.csrg.model.Model;
import com.obc.csrg.model.Notification;
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
import com.obc.csrg.util.TargetsManager;

@Name("composedNotificationHome")
public class ComposedNotificationHome<E extends Model> extends
		ObcEntityHome<ComposedNotification> {

	@RequestParameter
	Long composedNotificationId;
	
	private List<User> userList	= new ArrayList<User>();
	
	private List<Notification> notificationList = new ArrayList<Notification>();
	
	private TargetsManager targetsManager;
	
	protected NotificationUtil notificationUtil;

	// Business functions
	@Override
	public Object getId() {
		if (composedNotificationId == null)
			return super.getId();
		else
			return composedNotificationId;

	}

	@Override
	@Create
	@Begin(join=true)
	public void create(){
		super.create();
	}
	
	@Override
	@Destroy
	public void destroy(){
	}
	
	@Override
	protected void loadAfterCreate() {
		super.loadAfterCreate();
		// log.info("[loadAfterCreate]");
		this.initUserList();
		
		if(this.instance.getId()==null)
			//I'm creating the notification
			instance.setSender(stateBean.getUser());
		this.initNotificationList();
		this.targetsManager = new TargetsManager(entityManager, log, messages);
		this.getTargetsManager().initAvUsers();
		this.getTargetsManager().initAvDepartments();
		this.getTargetsManager().initAvProfCategories();
		this.getTargetsManager().initAvServiceClassifications();
		this.getTargetsManager().initAvServiceAreas();
		
		this.loadNotificationUsers();
		this.loadNotificationDepartments();
		this.loadNotificationProfCategories();
		this.loadNotificationServiceAreas();
		this.loadNotificationServiceClassifications();
		this.getTargetsManager().initCollapsiblePanels();
		
	}

	protected void loadNotificationUsers() {
		this.getTargetsManager().getSelUsers().clear();
		for (UserNotification u : instance.getUsers()) {
			if (!u.isCalculated())
				this.getTargetsManager().getSelUsers().add(
						new SelectModelItem<User>(u.getUser(), false));
			this.getTargetsManager().getCalculatedUsers().add(
					new SelectModelItem<User>(u.getUser(), u.isCalculated()));
		}
		this.getTargetsManager().getAvUsers().removeAll(
				this.getTargetsManager().getSelUsers());
	}

	protected void loadNotificationDepartments() {
		this.getTargetsManager().getSelDepartments().clear();
		for (DepartmentNotification mn : instance.getDepartments()) {
			this.getTargetsManager().getSelDepartments().add(
					new SelectModelItem<Department>(mn.getDepartment(), true));
		}
		this.getTargetsManager().getAvDepartments().removeAll(
				this.getTargetsManager().getSelDepartments());
	}

	protected void loadNotificationProfCategories() {
		this.getTargetsManager().getSelProfCategories().clear();
		for (ProfCategoryNotification mn : instance.getProfCategories()) {
			this.getTargetsManager().getSelProfCategories().add(
					new SelectModelItem<ProfCategory>(mn.getProfCategory(),
							true));
		}
		this.getTargetsManager().getAvProfCategories().removeAll(
				this.getTargetsManager().getSelProfCategories());
	}

	protected void loadNotificationServiceAreas() {
		this.getTargetsManager().getSelServiceAreas().clear();
		for (ServiceAreaNotification mn : instance.getServiceAreas()) {
			this.getTargetsManager().getSelServiceAreas()
					.add(
							new SelectModelItem<ServiceArea>(mn
									.getServiceArea(), true));
		}
		this.getTargetsManager().getAvServiceAreas().removeAll(
				this.getTargetsManager().getSelServiceAreas());
	}

	protected void loadNotificationServiceClassifications() {
		this.getTargetsManager().getSelServiceClassifications().clear();
		for (ServiceClassificationNotification mn : instance
				.getServiceClassifications()) {
			this.getTargetsManager().getSelServiceClassifications().add(
					new SelectModelItem<ServiceClassification>(mn
							.getServiceClassification(), true));
		}
		this.getTargetsManager().getAvServiceClassifications().removeAll(
				this.getTargetsManager().getSelServiceClassifications());
	}
	
	private void initUserList(){
		userList = entityManager.createQuery("select m from User m order by m.person.name")
							.getResultList();
	}
	
	private void initNotificationList(){
		this.notificationList.clear();
		List<Notification> notifications = entityManager.createQuery("" +
				"select m from Notification m where m.composedNotification.id is not null and " +
				"m.composedNotification.id=?")
				.setParameter(1, instance.getId()==null? 0L : instance.getId())
				.getResultList();
		for(Notification n:notifications){
			this.notificationList.add(n);
		}
		if(this.notificationList.size()==0){
			//add a new notification
			Notification n = new Notification();
			n.setUsersNotificationMedia(NotificationMediaEnum.sms);
			this.notificationList.add(n);
			n = new Notification();
			n.setUsersNotificationMedia(NotificationMediaEnum.email);
			this.notificationList.add(n);
		}
	}
	
	public List<SelectItem> getNotificationsMedia(){
		List<SelectItem> result = new ArrayList<SelectItem>();
		for(NotificationMediaEnum m:NotificationMediaEnum.values()){
			if(m.ordinal()!=NotificationMediaEnum.none.ordinal())
				result.add(new SelectItem(m,messages.get(m.getName())));
		}
		return result;
	}

	public void saveNotification(ActionEvent event) {
		Notification notification = (Notification)event.getComponent().getAttributes().get("notification");
		notification.setSubject(this.instance.getSubject());
	}
	
	public void removeNotification(ActionEvent event) {
		Notification notification = (Notification)event.getComponent().getAttributes().get("notification");
		this.notificationList.remove(notification);
	}
	public void addNotification(ActionEvent event) {
		Notification n = new Notification();
		n.setUsersNotificationMedia(NotificationMediaEnum.fax);
		this.notificationList.add(n);
	}
	
	public void calculateUsers(){
		this.notificationUtil = new NotificationUtil(entityManager,log,messages);
		this.notificationUtil.addUsers(this.getTargetsManager().getSelUsers());
		this.notificationUtil.addDepartmentUsers(this.getTargetsManager().getSelDepartments());
		this.notificationUtil.addProfCategoryUsers(this.getTargetsManager().getSelProfCategories());
		this.notificationUtil.addServiceAreaUsers(this.getTargetsManager().getSelServiceAreas());
		this.notificationUtil.addServiceClassificationUsers(this.getTargetsManager().getSelServiceClassifications());
		this.notificationUtil.calculateUsers();
		this.getTargetsManager().getCalculatedUsers().clear();
		for(SelectModelItem<User> item:this.notificationUtil.getSelectedUsers()){
			log.info("[calculateUsers] user:#0", item.getInstance());
			this.getTargetsManager().getCalculatedUsers().add(item);
		}
	}
	
	// getters and setters
	public List<Notification> getNotificationList() {
		return notificationList;
	}

	public void setNotificationList(List<Notification> notificationList) {
		this.notificationList = notificationList;
	}

	public List<User> getUserList() {
		return userList;
	}

	public void setUserList(List<User> userList) {
		this.userList = userList;
	}
	
	public TargetsManager getTargetsManager() {
		return targetsManager;
	}

	public void setTargetsManager(TargetsManager targetsManager) {
		this.targetsManager = targetsManager;
	}
}
