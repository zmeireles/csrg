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
import com.obc.csrg.model.ServiceArea;
import com.obc.csrg.model.Model;
import com.obc.csrg.model.User;
import com.obc.csrg.model.DepartmentArea;
import com.obc.csrg.util.QueryUtil;
import com.obc.csrg.util.ReportFilter;
import com.obc.csrg.events.ModelEventAdapter;
import com.obc.csrg.events.ModelEvent;
import com.obc.csrg.events.ModelEventQueue;

@Name("serviceAreaHome")
public class ServiceAreaHome<E extends Model> extends
		ObcEntityHome<ServiceArea> {

	@RequestParameter
	Long serviceAreaId;

	// users not associated with this serviceArea
	private List<User> usersNotInList = new ArrayList<User>();
	// selected users to add
	private List<User> selectedUsers = new ArrayList<User>();
	//departments relation management
	private List<DepartmentArea> departmentList = new ArrayList<DepartmentArea>();
	private List<Department> departmentsNotInList = new ArrayList<Department>();
	private List<Department> selectedDepartments = new ArrayList<Department>();
	
	private String searchDepartmentsTxt = "";
	private String searchUsersTxt = "";

	// Business functions
	@Override
	public Object getId() {
		if (serviceAreaId == null)
			return super.getId();
		else
			return serviceAreaId;

	}

	@Override
	@Create
	@Begin(join=true)
	public void create(){
		super.create();
		if(this.getId()!=null){
			List<ServiceArea> list = (List<ServiceArea>)entityManager.createQuery("select m from ServiceArea m " +
					"left join fetch m.departments " +
					"left join fetch m.users " +
					"left join fetch m.notifications where m.id=?")
					.setParameter(1, this.getId())
					.getResultList();
			if(list.size()>0)
				instance = list.get(0);
		}
		this.registerModelListener();
	}
	
	@Override
	@Destroy
	public void destroy(){
		log.info("[destroy] going to unregisterListener");
		this.unregisterModelListener();
	}
	
	@Override
	protected void loadAfterCreate() {
		super.loadAfterCreate();
		this.initUsersNotInList();
		this.initDepartmentList();
		this.initDepartmentsNotInList();
		// log.info("[loadAfterCreate]");
	}

	private void initUsersNotInList() {
		usersNotInList.clear();
		QueryUtil<User> queryUtil = new QueryUtil<User>("User","obj.person.name");
		queryUtil.addFilter(new ReportFilter("obj.active=true"));
		if (this.getInstance().getId() != null)
			queryUtil.addFilter(new ReportFilter("(obj.serviceArea is null or obj.serviceArea.id!=?)",
					this.instance.getId()));
		queryUtil.setSearchTxt(searchUsersTxt);
		usersNotInList = queryUtil.query(entityManager);
		/*
		if(this.getInstance().getId()!=null)
			usersNotInList = entityManager.createQuery(
				"select u from User u where u.active=true and (u.serviceArea is null or u.serviceArea.id!=?) order by u.person.name")
				.setParameter(1, this.getInstance().getId())
				.getResultList();
		else
			usersNotInList = entityManager.createQuery(
					"select u from User u where u.active=true order by u.person.name")
					.getResultList();
		*/
	}
	private void initDepartmentList(){
		this.departmentList.clear();
		for(DepartmentArea d:this.instance.getDepartments()){
			this.departmentList.add(d);
		}
	}
	
	private void initDepartmentsNotInList() {
		departmentsNotInList.clear();
		if(this.instance.getId()!=null)
			departmentsNotInList = entityManager.createQuery(
				"select m from Department m where m.id not in " +
				"(select m.department.id from DepartmentArea m where " +
				"m.serviceArea.id=?) order by m.name")
				.setParameter(1, this.getInstance().getId())
				.getResultList();
		else
			departmentsNotInList = entityManager.createQuery(
					"select m from Department m order by m.name")
					.getResultList();
	}

	public void addDndOperation(DragEvent event) {
		//log.info("[addDndOperation] targetId:#0",event.getTargetClientId());
		if (event.getEventType() == DndEvent.DROPPED && 
				this.isSelectionTarget(event.getTargetClientId())) {
			Model model = (Model) ((HtmlPanelGroup) event.getComponent())
					.getDragValue();
			if (model != null) {
				if(model instanceof Department){
					Department department = (Department)model;
					if(!department.isObjectInList(this.selectedDepartments)){
						this.selectedDepartments.add(department);
						this.departmentsNotInList.remove(department);
					}
				}else if(model instanceof User){
					User user = (User)model;
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
		//log.info("[removeDndOperation] eventType:#0, event.Dropped:#1", event.getEventType(),DndEvent.DROPPED);
		if (event.getEventType() == DndEvent.DROPPED && 
				this.isAvailableTarget(event.getTargetClientId())) {
			Model model = (Model) ((HtmlPanelGroup) event.getComponent())
					.getDragValue();
			log.info("[removeDndOperation] model:#0, model class:#1", model,model.getClass());
			if (model != null) {
				if(model instanceof Department){
					Department department = (Department)model;
					if(!department.isObjectInList(this.departmentsNotInList)){
						this.selectedDepartments.remove(department);
						this.departmentsNotInList.add(department);
					}
				}else if(model instanceof User){
					User user = (User)model;
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

	public void savePopupSelectedUsers(){
		this.setShowPopup(!this.isShowPopup());
		if(this.selectedUsers.size()==0)
			return;
		for(User u:this.selectedUsers){
			this.instance.getUsers().add(u);
			u.setServiceArea(this.instance);
			entityManager.merge(u);
		}
		this.selectedUsers.clear();
		this.initUsersNotInList();
	}
	
	public void savePopupSelectedDepartments(){
		this.setShowPopup(!this.isShowPopup());
		if(this.selectedDepartments.size()==0)
			return;
		for(Department m:this.selectedDepartments){
			DepartmentArea da = new DepartmentArea();
			da.setServiceArea(this.getInstance());
			da.setDepartment(m);
			m.getServiceAreas().add(da);
			this.getInstance().getDepartments().add(da);
			entityManager.persist(da);
			this.departmentList.add(da);
		}
		this.selectedDepartments.clear();
		this.initDepartmentsNotInList();
	}

	//Model events handling
	private final class ModelEventHandler extends ModelEventAdapter implements Serializable{
		@Override
		public void newModel(ModelEvent e){
			Model m = (Model) e.getSource();
			if(m instanceof User){
				User u = (User) m;
				if(u.getServiceArea().getId().equals(getInstance().getId()))
					instance.getUsers().add(u);
			}else if(m instanceof DepartmentArea){
				DepartmentArea a = (DepartmentArea)m;
				if(a.getServiceArea().getId().equals(instance.getId())){
					departmentList.add(a);
					departmentsNotInList.remove(a.getDepartment());
					instance.getDepartments().add(a);
				}
			}
		}
		@Override
		public void onBeforeRemove(ModelEvent e){
			Model m = (Model) e.getSource();
			if(m instanceof User){
				User u = (User) m;
				if(u.getServiceArea().getId().equals(getInstance().getId()))
					instance.getUsers().remove(u);
			}else if(m instanceof DepartmentArea){
				DepartmentArea a = (DepartmentArea)m;
				if(a.getServiceArea().getId().equals(instance.getId())){
					departmentList.remove(a);
					departmentsNotInList.add(a.getDepartment());
					instance.getDepartments().remove(a);
				}
			}
		}
		@Override
		public void modelUpdated(ModelEvent e,String property,Object oldValue, Object newValue){
			Model m = (Model) e.getSource();
			if(m instanceof User){
				if(property.equals("serviceArea")){
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
		log.info("[registerModelListener]");
		this.modelEventListener = new ModelEventHandler();
		ModelEventQueue.addMsgListener(this.modelEventListener);
	}
	
	private void unregisterModelListener(){
		if(this.modelEventListener!=null){
			log.info("[unregisterModelListener]");
			ModelEventQueue.removeMsgListener(this.modelEventListener);
		}
	}

	public String searchAvailableUsers(){
		log.info("[searchAvailableUsers] searchUsersTxt:#0", searchUsersTxt);
		this.initUsersNotInList();
		return "";
	}
	// getters and setters

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

	public List<DepartmentArea> getDepartmentList() {
		return departmentList;
	}

	public void setDepartmentList(List<DepartmentArea> departmentList) {
		this.departmentList = departmentList;
	}

	public List<Department> getDepartmentsNotInList() {
		return departmentsNotInList;
	}

	public void setDepartmentsNotInList(List<Department> departmentsNotInList) {
		this.departmentsNotInList = departmentsNotInList;
	}

	public List<Department> getSelectedDepartments() {
		return selectedDepartments;
	}

	public void setSelectedDepartments(List<Department> selectedDepartments) {
		this.selectedDepartments = selectedDepartments;
	}

	public String getSearchDepartmentsTxt() {
		return searchDepartmentsTxt;
	}

	public void setSearchDepartmentsTxt(String searchDepartmentsTxt) {
		this.searchDepartmentsTxt = searchDepartmentsTxt;
	}
	
	public String getSearchUsersTxt() {
		return searchUsersTxt;
	}

	public void setSearchUsersTxt(String searchUsersTxt) {
		this.searchUsersTxt = searchUsersTxt;
	}
	
}
