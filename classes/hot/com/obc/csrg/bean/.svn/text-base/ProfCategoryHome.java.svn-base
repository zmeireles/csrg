package com.obc.csrg.bean;

import java.util.List;
import java.util.ArrayList;
import java.io.Serializable;

import javax.ejb.Remove;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;

import org.jboss.seam.annotations.Begin;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.Destroy;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.web.RequestParameter;

import com.icesoft.faces.component.dragdrop.DndEvent;
import com.icesoft.faces.component.dragdrop.DragEvent;
import com.icesoft.faces.component.ext.HtmlPanelGroup;
import com.icesoft.faces.context.effects.Effect;
import com.icesoft.faces.context.effects.Highlight;

import com.obc.csrg.model.ProfCategory;
import com.obc.csrg.model.Model;
import com.obc.csrg.model.User;
import com.obc.csrg.util.ReportFilter;
import com.obc.csrg.events.ModelEventAdapter;
import com.obc.csrg.events.ModelEvent;
import com.obc.csrg.events.ModelEventQueue;
import com.obc.csrg.constants.Constants;
import com.obc.csrg.util.QueryUtil;
import com.obc.csrg.util.ReportFilter;

@Name("profCategoryHome")
public class ProfCategoryHome<E extends Model> extends
		ObcEntityHome<ProfCategory> {

	@RequestParameter
	Long profCategoryId;

	// users not associated with this category
	private List<User> usersNotInList = new ArrayList<User>();
	// selected users to add
	private List<User> selectedUsers = new ArrayList<User>();
	
	private boolean filterUsersWithoutProfCategory = true;
	private String searchTxt = "";

	// Business functions
	@Override
	public Object getId() {
		if (profCategoryId == null)
			return super.getId();
		else
			return profCategoryId;
	}

	@Override
	@Create
	@Begin(join=true)
	public void create(){
		super.create();
		this.registerModelListener();
	}
	
	@Override
	@Remove
	@Destroy
	public void destroy(){
		super.destroy();
		this.unregisterModelListener();
	}
	
	@Override
	protected void loadAfterCreate() {
		super.loadAfterCreate();
		this.initUsersNotInList();
		log.info("[loadAfterCreate]");
	}

	private void initUsersNotInList() {
		
		log.info("[initUsersNotInList]");
		usersNotInList.clear();
		QueryUtil<User> queryUtil = new QueryUtil<User>("User","obj.person.name");
		//queryUtil.setLog(log);
		//String userFilter = "";
		if(this.filterUsersWithoutProfCategory){
			queryUtil.addFilter(new ReportFilter("obj.profCategory.id is not null"));
			//userFilter = "u.profCategory.id is not null";
		}
		queryUtil.addFilter(new ReportFilter("obj.active=true"));
		if(this.getInstance().getId()!=null)
			queryUtil.addFilter(new ReportFilter(
					"(obj.profCategory is null or obj.profCategory.id!=?)",
					this.instance.getId()));
		queryUtil.setSearchTxt(searchTxt);
		//this.createTextFilter4Users(queryUtil);
		usersNotInList = queryUtil.query(entityManager);
		/*
		if(this.getInstance().getId()!=null)
			usersNotInList = entityManager.createQuery(
					"select u from User u where " + (!userFilter.equals("") ? userFilter + " and " : "") +
					"u.active=true and (u.profCategory is null or u.profCategory.id!=?) order by u.person.name")
					.setParameter(1, this.getInstance().getId())
					.getResultList();
		else
			usersNotInList = entityManager.createQuery(
					"select u from User u where " + (!userFilter.equals("") ? userFilter + " and " : "") + 
					"u.active=true order by u.person.name")
					.getResultList();
		*/
		//log.info("[initUsersNotInList] users:#0", this.usersNotInList);
	}

	public void addDndUser(DragEvent event) {
		//log.info("[addDndUser] targetId:#0",event.getTargetClientId());
		if (event.getEventType() == DndEvent.DROPPED && 
				this.isSelectionTarget(event.getTargetClientId())) {
			User user = (User) ((HtmlPanelGroup) event.getComponent())
					.getDragValue();
			//log.info("[addDndUser] user:#0", user);
			if (user != null) {
				if (!user.isObjectInList(this.selectedUsers)) {
					selectedUsers.add(user);
					usersNotInList.remove(user);
				}
			}
		}// fire effect when a drag is started.
		else if (event.getEventType() == DndEvent.HOVER_START) {
			this.getSelectedPopupItemsEffect().setFired(false);
			this.getAvailablePopupItemsEffect().setFired(false);
		}
	}

	public void removeDndUser(DragEvent event) {

		//log.info("[removeDndUser] targetId:#0",event.getTargetClientId());
		if (event.getEventType() == DndEvent.DROPPED && 
				this.isAvailableTarget(event.getTargetClientId())) {
			User user = (User) ((HtmlPanelGroup) event.getComponent())
					.getDragValue();
			//log.info("[addDndUser] user:#0", user);
			if (user != null) {
				if (!user.isObjectInList(this.usersNotInList)) {
					selectedUsers.remove(user);
					usersNotInList.add(user);
				}
			}
		}// fire effect when a drag is started.
		else if (event.getEventType() == DndEvent.HOVER_START) {
			this.getSelectedPopupItemsEffect().setFired(false);
			this.getAvailablePopupItemsEffect().setFired(false);
		}
	}

	public void savePopupSelectedUsers(){
		this.setShowPopup(!this.isShowPopup());
		if(this.selectedUsers.size()==0)
			return;
		for(User u:this.selectedUsers){
			this.instance.getUsers().add(u);
			u.setProfCategory(this.instance);
			entityManager.merge(u);
		}
		this.selectedUsers.clear();
		this.initUsersNotInList();
	}

	//Model events handling
	private final class ModelEventHandler extends ModelEventAdapter implements Serializable{
		@Override
		public void newModel(ModelEvent e){
			Model m = (Model) e.getSource();
			if(m instanceof User){
				User u = (User) m;
				if(u.getProfCategory().getId().equals(getInstance().getId()))
					getInstance().getUsers().add(u);
			}
		}
		@Override
		public void onBeforeRemove(ModelEvent e){
			Model m = (Model) e.getSource();
			if(m instanceof User){
				User u = (User) m;
				if(u.getProfCategory().getId().equals(getInstance().getId()))
					getInstance().getUsers().remove(u);
			}
		}
		@Override
		public void modelUpdated(ModelEvent e,String property,Object oldValue, Object newValue){
			Model m = (Model) e.getSource();
			if(m instanceof User){
				if(property.equals("profCategory")){
					if(oldValue!=null && oldValue instanceof Long &&
							((Long)oldValue).equals(instance.getId())){
						User u = (User) m;
						log.info("[modelUpdated] user:#0", u);
						instance.getUsers().remove(u);
						usersNotInList.add(u);
					}else if(newValue!=null && newValue instanceof Long &&
							((Long)newValue).equals(instance.getId())){
						User u = (User) m;
						instance.getUsers().add(u);
						usersNotInList.remove(u);
					}
				}
			}
		}
	}
	
	private void registerModelListener(){
		log.info("[registerModelListener] this.modelEventListener:#0",this.modelEventListener);
		if(this.modelEventListener!=null)
			this.unregisterModelListener();
		this.modelEventListener = new ModelEventHandler();
		ModelEventQueue.addMsgListener(this.modelEventListener);
	}
	
	private void unregisterModelListener(){
		if(this.modelEventListener!=null){
			log.info("[unregisterModelListener]");
			ModelEventQueue.removeMsgListener(this.modelEventListener);
		}
	}

	public void valueChanged(ValueChangeEvent event) {
		if(event.getComponent().getId().equals("filterUsers")){
			this.filterUsersWithoutProfCategory = (Boolean) event.getNewValue();
			this.initUsersNotInList();
			log.info("[valueChanged] searchTxt:#0", searchTxt);
		}
	}
	
	public String searchAvailableUsers(){
		log.info("[searchAvailableUsers] searchTxt:#0", searchTxt);
		this.initUsersNotInList();
		return "";
	}
	
	// getters and setters
	public List<User> getUsersNotInList() {
		//log.info("[getUsersNotInList]");
		return usersNotInList;
	}

	public void setUsersNotInList(List<User> usersNotInList) {
		this.usersNotInList = usersNotInList;
	}

	public List<User> getSelectedUsers() {
		return selectedUsers;
	}

	public void setSelectedUsers(List<User> selectedUsers) {
		this.selectedUsers = selectedUsers;
	}

	public boolean isFilterUsersWithoutProfCategory() {
		return filterUsersWithoutProfCategory;
	}

	public void setFilterUsersWithoutProfCategory(
			boolean filterUsersWithoutProfCategory) {
		this.filterUsersWithoutProfCategory = filterUsersWithoutProfCategory;
	}
	
	public String getSearchTxt() {
		return searchTxt;
	}

	public void setSearchTxt(String searchTxt) {
		this.searchTxt = searchTxt;
	}
}
