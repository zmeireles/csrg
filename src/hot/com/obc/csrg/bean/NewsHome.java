package com.obc.csrg.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.SessionBean;
import javax.faces.model.SelectItem;

import org.jboss.seam.annotations.Begin;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.Destroy;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.web.RequestParameter;

import bsh.This;

import com.obc.csrg.constants.NotificationMediaEnum;
import com.obc.csrg.events.ModelEvent;
import com.obc.csrg.events.ModelEventAdapter;
import com.obc.csrg.events.ModelEventQueue;
import com.obc.csrg.model.Department;
import com.obc.csrg.model.DepartmentNotification;
import com.obc.csrg.model.News;
import com.obc.csrg.model.Model;
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

@Name("newsHome")
public class NewsHome<E extends Model> extends ObcEntityHome<News> {

	@RequestParameter
	Long newsId;

	private List<User> userList = new ArrayList<User>();

	private TargetsManager targetsManager;
	
	protected NotificationUtil notificationUtil;

	@Override
	public Object getId() {
		if (newsId == null)
			return super.getId();
		else
			return newsId;
	}

	@Override
	@Create
	@Begin(join = true)
	public void create() {
		super.create();
		log.info("[create] newsHome created");
		this.registerModelListener();		
	}

	@Override
	@Destroy
	public void destroy() {
		super.destroy();
		this.unregisterModelListener();
	}

	@Override
	protected void loadAfterCreate() {
		super.loadAfterCreate();
		this.initDefaultMessages();
		this.initUserList();
		this.targetsManager = new TargetsManager(entityManager, log, messages);

		this.initUserList();
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
		
		notificationUtil = new NotificationUtil(entityManager,log,messages);
		
		if (this.instance.getId() == null)
			// I'm creating the news
			instance.setCreatedBy(stateBean.getUser());
		// log.info("[loadAfterCreate]");
		// TODO: write the logic to deal with the approvedBy user
		
		updateNewsReadStatus(true);
	}

	private void initUserList() {
		userList = entityManager.createQuery(
				"select m from User m order by m.person.name").getResultList();
	}

	// Model events handling
	private final class ModelEventHandler extends ModelEventAdapter implements
			Serializable {
		@Override
		public void newModel(ModelEvent e) {
			Model m = (Model) e.getSource();

		}

		@Override
		public void onBeforeRemove(ModelEvent e) {
			Model m = (Model) e.getSource();

		}
	}

	private void registerModelListener() {
		log.info("[registerModelListener]");
		this.modelEventListener = new ModelEventHandler();
		ModelEventQueue.addMsgListener(this.modelEventListener);
	}

	private void unregisterModelListener() {
		if (this.modelEventListener != null) {
			log.info("[unregisterModelListener]");
			ModelEventQueue.removeMsgListener(this.modelEventListener);
		}
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

	public List<SelectItem> getNotificationsMedia() {
		List<SelectItem> result = new ArrayList<SelectItem>();
		for (NotificationMediaEnum m : NotificationMediaEnum.values()) {
			result.add(new SelectItem(m, messages.get(m.getName())));
		}
		return result;
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
	
	private void updateNewsReadStatus(boolean newStatus){
		
		//log.info("[updateNewsReadStatus] newsId:#0, userId:#1, instance.id:#2", newsId,
			//	stateBean.getUser().getId(),this.getInstance().getId());
		if(this.getInstance().getId()==null)
			return;
		List<UserNotification> results	=entityManager.createQuery(
							"select un from UserNotification un "
							+"where un.user.id=? and un.news.id=? ")
							.setParameter(1, stateBean.getUser().getId())
							.setParameter(2, this.getInstance().getId())
							.getResultList();
		if(results.size()>0){
			UserNotification userNotification=results.get(0);
			userNotification.setRead(newStatus);		
			entityManager.persist(userNotification);		   
		}
	}

	// getters and setters
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
