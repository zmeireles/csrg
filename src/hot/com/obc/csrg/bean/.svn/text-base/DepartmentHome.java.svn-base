package com.obc.csrg.bean;

import java.util.List;
import java.util.ArrayList;
import java.io.Serializable;

import org.jboss.seam.annotations.Begin;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.Destroy;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.web.RequestParameter;

import com.icesoft.faces.component.dragdrop.DndEvent;
import com.icesoft.faces.component.dragdrop.DragEvent;
import com.icesoft.faces.component.ext.HtmlPanelGroup;

import com.obc.csrg.model.Department;
import com.obc.csrg.model.Model;
import com.obc.csrg.model.UserDepartment;
import com.obc.csrg.model.User;
import com.obc.csrg.events.ModelEventAdapter;
import com.obc.csrg.events.ModelEvent;
import com.obc.csrg.events.ModelEventQueue;
import com.obc.csrg.model.DepartmentArea;
import com.obc.csrg.model.ServiceArea;
import com.obc.csrg.model.ServiceClassification;
import com.obc.csrg.util.QueryUtil;
import com.obc.csrg.util.ReportFilter;

@Name("departmentHome")
public class DepartmentHome<E extends Model> extends ObcEntityHome<Department> {

	@RequestParameter
	Long departmentId;

	private List<UserDepartment> userList = new ArrayList<UserDepartment>();
	// users not associated with this category
	private List<User> usersNotInList = new ArrayList<User>();
	// selected users to add
	private List<User> selectedUsers = new ArrayList<User>();
	// Service areas relation management
	private List<DepartmentArea> serviceAreaList = new ArrayList<DepartmentArea>();
	private List<ServiceArea> serviceAreasNotInList = new ArrayList<ServiceArea>();
	private List<ServiceArea> selectedServiceAreas = new ArrayList<ServiceArea>();

	// list of possible parents
	private List<Department> parentList = new ArrayList<Department>();
	private Department newParent = null;

	private List<ServiceClassification> serviceClassificationList = new ArrayList<ServiceClassification>();
	
	private String searchUsersTxt = "";
	private String searchServiceAreasTxt = "";
	

	// Business functions
	@Override
	public Object getId() {
		if (departmentId == null)
			return super.getId();
		else
			return departmentId;
	}

	@Override
	@Create
	@Begin(join = true)
	public void create() {
		super.create();
		this.registerModelListener();
	}

	@Override
	@Destroy
	public void destroy() {
		this.unregisterModelListener();
	}

	@Override
	protected void loadAfterCreate() {
		super.loadAfterCreate();
		this.initUserList();
		this.initUsersNotInList();
		this.initServiceAreaList();
		this.initServiceAreasNotInList();
		newParent = this.getInstance().getParent();
		this.initParentList();
		this.initServiceClassificationList();
		// log.info("[loadAfterCreate]");
	}
	
	private void initUserList() {
		userList.clear();
		for (UserDepartment u : this.getInstance().getUsers()) {
			userList.add(u);
		}
	}

	private void initUsersNotInList() {
		usersNotInList.clear();
		QueryUtil<User> queryUtil = new QueryUtil<User>("User","obj.person.name");
		queryUtil.addFilter(new ReportFilter("obj.active=true"));
		if (this.getInstance().getId() != null)
			queryUtil.addFilter(new ReportFilter("obj.id not in "
					+ "(select ud.user.id from UserDepartment ud where "
					+ "ud.department.id=?)",
					this.instance.getId()));
		queryUtil.setSearchTxt(searchUsersTxt);
		usersNotInList = queryUtil.query(entityManager);
	}

	private void initServiceAreaList() {
		userList.clear();
		for (DepartmentArea a : this.getInstance().getServiceAreas()) {
			serviceAreaList.add(a);
		}
	}

	private void initServiceAreasNotInList() {
		serviceAreasNotInList.clear();
		QueryUtil<ServiceArea> queryUtil = new QueryUtil<ServiceArea>("ServiceArea","obj.name");
		if (this.getInstance().getId() != null)
			queryUtil.addFilter(new ReportFilter("obj.id not in "
					+ "(select m.serviceArea.id from DepartmentArea m where "
					+ "m.department.id=?)",
					this.instance.getId()));
		queryUtil.setSearchTxt(searchServiceAreasTxt);
		serviceAreasNotInList = queryUtil.query(entityManager);
	}

	public void addDndOperation(DragEvent event) {
		// log.info("[addDndOperation] targetId:#0",event.getTargetClientId());
		if (event.getEventType() == DndEvent.DROPPED
				&& this.isSelectionTarget(event.getTargetClientId())) {
			Model model = (Model) ((HtmlPanelGroup) event.getComponent())
					.getDragValue();
			if (model != null) {
				if (model instanceof ServiceArea) {
					ServiceArea serviceArea = (ServiceArea) model;
					if (!serviceArea.isObjectInList(this.selectedServiceAreas)) {
						this.selectedServiceAreas.add(serviceArea);
						this.serviceAreasNotInList.remove(serviceArea);
					}
				} else if (model instanceof User) {
					User user = (User) model;
					if (!user.isObjectInList(this.selectedUsers)) {
						selectedUsers.add(user);
						usersNotInList.remove(user);
					}
				}
			}
		}// fire effect when a drag is started.
		else if (event.getEventType() == DndEvent.HOVER_START) {
			this.getSelectedPopupItemsEffect().setFired(false);
			this.getAvailablePopupItemsEffect().setFired(false);
		}
	}

	public void removeDndOperation(DragEvent event) {
		// log.info("[removeDndOperation] eventType:#0, event.Dropped:#1",
		// event.getEventType(),DndEvent.DROPPED);
		if (event.getEventType() == DndEvent.DROPPED
				&& this.isAvailableTarget(event.getTargetClientId())) {
			Model model = (Model) ((HtmlPanelGroup) event.getComponent())
					.getDragValue();
			log.info("[removeDndOperation] model:#0, model class:#1", model,
					model.getClass());
			if (model != null) {
				if (model instanceof ServiceArea) {
					ServiceArea serviceArea = (ServiceArea) model;
					if (!serviceArea.isObjectInList(this.serviceAreasNotInList)) {
						this.selectedServiceAreas.remove(serviceArea);
						this.serviceAreasNotInList.add(serviceArea);
					}
				} else if (model instanceof User) {
					User user = (User) model;
					if (!user.isObjectInList(this.usersNotInList)) {
						selectedUsers.remove(user);
						usersNotInList.add(user);
					}
				}
			}
		}// fire effect when a drag is started.
		else if (event.getEventType() == DndEvent.HOVER_START) {
			this.getSelectedPopupItemsEffect().setFired(false);
			this.getAvailablePopupItemsEffect().setFired(false);
		}
	}

	
	public void removeDndUser(DragEvent event) {

		log.info("[removeDndUser]");
		if (event.getEventType() == DndEvent.DROPPED
				&& this.isAvailableTarget(event.getTargetClientId())) {
			User user = (User) ((HtmlPanelGroup) event.getComponent())
					.getDragValue();
			log.info("[addDndUser] user:#0", user);
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
	
	public void savePopupSelectedUsers() {
		this.setShowPopup(!this.isShowPopup());
		if (this.selectedUsers.size() == 0)
			return;
		for (User u : this.selectedUsers) {
			UserDepartment ud = new UserDepartment();
			ud.setUser(u);
			ud.setDepartment(this.getInstance());
			u.getDepartments().add(ud);
			this.getInstance().getUsers().add(ud);
			entityManager.persist(ud);
			this.userList.add(ud);
		}
		this.selectedUsers.clear();
		this.initUsersNotInList();
	}

	public void savePopupSelectedServiceAreas() {
		this.setShowPopup(!this.isShowPopup());
		if (this.selectedServiceAreas.size() == 0)
			return;
		for (ServiceArea a : this.selectedServiceAreas) {
			DepartmentArea da = new DepartmentArea();
			da.setServiceArea(a);
			da.setDepartment(this.getInstance());
			a.getDepartments().add(da);
			this.getInstance().getServiceAreas().add(da);
			entityManager.persist(da);
			this.serviceAreaList.add(da);
		}
		this.selectedServiceAreas.clear();
		this.initServiceAreasNotInList();
	}

	// Model events handling
	private final class ModelEventHandler extends ModelEventAdapter implements
			Serializable {
		@Override
		public void newModel(ModelEvent e) {
			Model m = (Model) e.getSource();
			if (m instanceof UserDepartment) {
				UserDepartment u = (UserDepartment) m;
				if (u.getDepartment().getId().equals(getInstance().getId())) {
					userList.add(u);
					usersNotInList.remove(u.getUser());
					instance.getUsers().add(u);
				}
			} else if (m instanceof DepartmentArea) {
				DepartmentArea a = (DepartmentArea) m;
				if (a.getDepartment().getId().equals(instance.getId())) {
					serviceAreaList.add(a);
					serviceAreasNotInList.remove(a.getServiceArea());
					instance.getServiceAreas().add(a);
				}
			}
		}

		@Override
		public void onBeforeRemove(ModelEvent e) {
			Model m = (Model) e.getSource();
			// log.info("[onBeforeRemove] modelClass:#0", m.getClass());
			if (m instanceof UserDepartment) {
				UserDepartment u = (UserDepartment) m;
				if (u.getDepartment().getId().equals(getInstance().getId())) {
					userList.remove(u);
					usersNotInList.add(u.getUser());
					instance.getUsers().remove(u);
				}
			} else if (m instanceof DepartmentArea) {
				DepartmentArea a = (DepartmentArea) m;
				if (a.getDepartment().getId().equals(instance.getId())) {
					serviceAreaList.remove(a);
					serviceAreasNotInList.add(a.getServiceArea());
					instance.getServiceAreas().remove(a);
				}
			}
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

	protected void initParentList() {
		this.parentList.clear();
		if (this.instance.getId() != null) {
			parentList = entityManager.createQuery(
					"select m from Department m "
							+ "where m.id!=? order by m.name").setParameter(1,
					instance.getId()).getResultList();
		} else {
			parentList = entityManager.createQuery(
					"select m from Department m order by m.name")
					.getResultList();
		}
	}

	private void initServiceClassificationList() {
		this.serviceClassificationList = entityManager.createQuery(
				"select m from ServiceClassification m order by m.name")
				.getResultList();
	}

	public String searchAvailableUsers(){
		log.info("[searchAvailableUsers] searchUsersTxt:#0", searchUsersTxt);
		this.initUsersNotInList();
		return "";
	}
	
	public String searchAvailableServiceAreas(){
		this.initServiceAreasNotInList();
		return "";
	}
	
	// getters and setters
	public List<UserDepartment> getUserList() {
		return userList;
	}

	public void setUserList(List<UserDepartment> userList) {
		this.userList = userList;
	}

	public List<User> getUsersNotInList() {
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

	public List<DepartmentArea> getServiceAreaList() {
		return serviceAreaList;
	}

	public void setServiceAreaList(List<DepartmentArea> serviceAreaList) {
		this.serviceAreaList = serviceAreaList;
	}

	public List<ServiceArea> getServiceAreasNotInList() {
		return serviceAreasNotInList;
	}

	public void setServiceAreasNotInList(List<ServiceArea> serviceAreasNotInList) {
		this.serviceAreasNotInList = serviceAreasNotInList;
	}

	public List<ServiceArea> getSelectedServiceAreas() {
		return selectedServiceAreas;
	}

	public void setSelectedServiceAreas(List<ServiceArea> selectedServiceAreas) {
		this.selectedServiceAreas = selectedServiceAreas;
	}

	public List<Department> getParentList() {
		return parentList;
	}

	public void setParentList(List<Department> parentList) {
		this.parentList = parentList;
	}

	public Department getNewParent() {
		return newParent;
	}

	public void setNewParent(Department newParent) {
		this.newParent = newParent;
	}

	public List<ServiceClassification> getServiceClassificationList() {
		return serviceClassificationList;
	}

	public void setServiceClassificationList(
			List<ServiceClassification> serviceClassificationList) {
		this.serviceClassificationList = serviceClassificationList;
	}
	
	public String getSearchUsersTxt() {
		return searchUsersTxt;
	}

	public void setSearchUsersTxt(String searchUsersTxt) {
		this.searchUsersTxt = searchUsersTxt;
	}

	public String getSearchServiceAreasTxt() {
		return searchServiceAreasTxt;
	}

	public void setSearchServiceAreasTxt(String searchServiceAreasTxt) {
		this.searchServiceAreasTxt = searchServiceAreasTxt;
	}
}
