package com.obc.csrg.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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

import com.obc.csrg.events.ModelEvent;
import com.obc.csrg.events.ModelEventAdapter;
import com.obc.csrg.events.ModelEventQueue;
import com.obc.csrg.model.User;
import com.obc.csrg.model.Model;
import com.obc.csrg.model.ProfCategory;
import com.obc.csrg.model.ServiceArea;
import com.obc.csrg.model.UserDepartment;
import com.obc.csrg.model.Department;
import com.obc.csrg.util.DataChangeUtil;
import com.obc.csrg.util.QueryUtil;
import com.obc.csrg.util.ReportFilter;
import com.obc.csrg.model.Person;

@Name("userHome")
public class UserHome<E extends Model> extends ObcEntityHome<User> {

	@RequestParameter 
    Long userId;
	
	private List<ProfCategory> profCategoryList = new ArrayList<ProfCategory>();

	private List<ServiceArea> serviceAreaList = new ArrayList<ServiceArea>();
	//departments relation management
	private List<UserDepartment> departmentList = new ArrayList<UserDepartment>();
	private List<Department> departmentsNotInList = new ArrayList<Department>();
	private List<Department> selectedDepartments = new ArrayList<Department>();
	
	protected String mainZipCode="";
	protected String subZipCode="";
	protected boolean usernameValid = true;
	
	protected DataChangeUtil<Person> dataChangePerson;
	private String newUsername = "";
	private String newPassword = "";
	private String newPasswordConfirmation = "";
	
	private boolean changePassword=false;
	private boolean newPasswordOk = true;
	
	private String searchTxt = "";

	@Override
    public Object getId() {
		//try{
			if(userId!=null){
				//log.info("[getId] userId:#0, this.id:#1", userId);
				User result = (User)(entityManager.createQuery("select u from User u where u.id=?")
					.setParameter(1, userId)
					.getSingleResult());
				return result.getId();
			}else{
				return this.getInstance().getId();
			}
					
		//}catch(Exception ex){
		//	stateBean.log4Debug(this.toString(), "[getId] #0", ex.toString());
		//	return super.getId();
		//}
    }
	
	@Override
	@Create
	@Begin(join=true)
	public void create(){
		super.create();
		log.info("[create] userHome created");
		this.registerModelListener();
	}
	
	@Override
	@Destroy
	public void destroy(){
		this.unregisterModelListener();
	}
	@Override
	protected void loadAfterCreate() {
		super.loadAfterCreate();
		this.newUsername = instance.getUsername();
		dataChangePerson = new DataChangeUtil<Person>(this.instance.getPerson(),entityManager,log,stateBean.getUser());
		dataChangePerson.replicateValues();
		dataChangeList.add(dataChangePerson);
		
		this.initProfCategoryList();
		this.initServiceAreaList();
		this.initDepartmentList();
		this.initDepartmentsNotInList();
		if(this.instance.getId()!=null){
			if(this.instance.getPerson().getZipCode()!=null && 
					this.instance.getPerson().getZipCode().length()>=4)
				this.mainZipCode = this.instance.getPerson().getZipCode().substring(0, 4);
			if(this.instance.getPerson().getZipCode()!=null && 
					this.instance.getPerson().getZipCode().length()>4)
				this.subZipCode = this.instance.getPerson().getZipCode().substring(4);
		}
		// log.info("[loadAfterCreate]");
	}
	
	private void initProfCategoryList() {
		profCategoryList = entityManager.createQuery("select m from ProfCategory m order by m.name")
									.getResultList();
	}

	private void initServiceAreaList(){
		serviceAreaList = entityManager.createQuery("select m from ServiceArea m order by m.name")
								.getResultList();
		//log.info("[initServiceAreaList] list size:#0", serviceAreaList.size());
	}
	
	private void initDepartmentList(){
		departmentList.clear();
		for(UserDepartment d:this.getInstance().getDepartments()){
			departmentList.add(d);
		}
	}
	
	private void initDepartmentsNotInList() {
		
		log.info("[initDepartmentsNotInList]");
		departmentsNotInList.clear();
		QueryUtil<Department> queryUtil = new QueryUtil<Department>("Department","obj.name");
		
		if(this.instance.getId()!=null){
			queryUtil.addFilter(new ReportFilter(
				"obj.id not in " +
				"(select ud.department.id from UserDepartment ud where " +
				"ud.user.id=?)",
				this.instance.getId()));
		}
		queryUtil.setSearchTxt(searchTxt);
		departmentsNotInList = queryUtil.query(entityManager);
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
				}
			}
		}// fire effect when a drag is started.
		else if (event.getEventType() == DndEvent.HOVER_START) {
			this.getSelectedPopupItemsEffect().setFired(false);
			this.getAvailablePopupItemsEffect().setFired(false);
		}
	}
	
	public void savePopupSelectedDepartments(ActionEvent event){
		this.setShowPopup(!this.isShowPopup());
		if(this.selectedDepartments.size()==0)
			return;
		for(Department m:this.selectedDepartments){
			UserDepartment ud = new UserDepartment();
			ud.setUser(this.getInstance());
			ud.setDepartment(m);
			m.getUsers().add(ud);
			this.getInstance().getDepartments().add(ud);
			entityManager.persist(ud);
			this.departmentList.add(ud);
		}
		this.selectedDepartments.clear();
		this.initDepartmentsNotInList();
	}
	
	//Model events handling
	private final class ModelEventHandler extends ModelEventAdapter implements Serializable{
		@Override
		public void newModel(ModelEvent e){
			Model m = (Model) e.getSource();
			if(m instanceof UserDepartment){
				UserDepartment d=(UserDepartment)m;
				if(d.getUser().getId().equals(instance.getId())){
					departmentList.add(d);
					departmentsNotInList.remove(d.getDepartment());
					instance.getDepartments().add(d);
				}
			}
		}
		@Override
		public void onBeforeRemove(ModelEvent e){
			Model m = (Model) e.getSource();
			if(m instanceof UserDepartment){
				UserDepartment d=(UserDepartment)m;
				if(d.getUser().getId().equals(instance.getId())){
					departmentList.remove(d);
					departmentsNotInList.add(d.getDepartment());
					//initDepartmentsNotInList();
					instance.getDepartments().remove(d);
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
	
	public void validateUsername(ValueChangeEvent event){
		String username = (String)event.getNewValue();
		this.newUsername = username.trim();
		log.info("[validateUsername] username:#0", newUsername);
		
		boolean validUsername = this.verifyUsername();
		if(!validUsername)
			facesMessages.addToControl("username", messages.get("RegisterUsernameNotAvailable"));
		//else
			//facesMessages.addToControl("username", messages.get("RegisterUsernameAvailable"));
	}
	
	// verifies if there is no repeated username
	public boolean verifyUsername() {
		if(newUsername==null || newUsername.equals("")){
			this.usernameValid = false;
			return false;
		}
		List existing = entityManager.createQuery(
				"select u.username from User u where lower(u.username)=? and" +
				" u.id!=?")
				.setParameter(1, newUsername.toLowerCase())
				.setParameter(2, instance.getId())
				.getResultList();
		if (existing.size() != 0)
			this.usernameValid = false;
		else
			this.usernameValid = true;
		log.info("[verifyUsername] usernameValid:#0", usernameValid);
		return this.usernameValid;
	}
	
	public void toggleShowChangePasswordPanel(ActionEvent event) {
		this.changePassword = !this.changePassword;
	}
	
	public void saveNewPassword(ActionEvent event) {
		
		if(this.newPassword!=null && !this.newPassword.equals("") &&
				this.newPasswordConfirmation!=null && ! this.newPasswordConfirmation.equals("")
				&& this.newPassword.trim().equals(this.newPasswordConfirmation.trim())){
			this.newPasswordOk = true;
			this.changePassword = !this.changePassword;
			log.info("[saveNewPassword] new pwd:[#0]", this.getNewPassword());
			this.instance.setPassword(User.encrypt(this.getNewPassword()));
			entityManager.merge(instance);
		}else{
			this.newPasswordOk = false;
		}
		
		log.info("[saveNewPassword] newPassowrdOk:#0", this.newPasswordOk);
	}
	
	public String searchAvailableDepartments(){
		this.initDepartmentsNotInList();
		return "";
	}
	//getters and setters
	public List<ProfCategory> getProfCategoryList() {
		return profCategoryList;
	}

	public void setProfCategoryList(List<ProfCategory> profCategoryList) {
		this.profCategoryList = profCategoryList;
	}

	public List<ServiceArea> getServiceAreaList() {
		return serviceAreaList;
	}

	public void setServiceAreaList(List<ServiceArea> serviceAreaList) {
		this.serviceAreaList = serviceAreaList;
	}
	
	public List<UserDepartment> getDepartmentList() {
		return departmentList;
	}

	public void setDepartmentList(List<UserDepartment> departmentList) {
		this.departmentList = departmentList;
	}
	
	public List<Department> getDepartmentsNotInList() {
		//log.info("[getDepartmentsNotInList]");
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
	public String getMainZipCode() {
		return mainZipCode;
	}

	public void setMainZipCode(String mainZipCode) {
		this.mainZipCode = mainZipCode;
	}

	public String getSubZipCode() {
		return subZipCode;
	}

	public void setSubZipCode(String subZipCode) {
		this.subZipCode = subZipCode;
	}
	
	public String getNewUsername() {
		return newUsername;
	}

	public void setNewUsername(String newUsername) {
		this.newUsername = newUsername;
	}
	
	public boolean isChangePassword() {
		return changePassword;
	}

	public void setChangePassword(boolean changePassword) {
		this.changePassword = changePassword;
	}
	
	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getNewPasswordConfirmation() {
		return newPasswordConfirmation;
	}

	public void setNewPasswordConfirmation(String newPasswordConfirmation) {
		this.newPasswordConfirmation = newPasswordConfirmation;
	}
	
	public boolean isNewPasswordOk() {
		return newPasswordOk;
	}

	public void setNewPasswordOk(boolean newPasswordOk) {
		this.newPasswordOk = newPasswordOk;
	}
	
	public String getSearchTxt() {
		return searchTxt;
	}

	public void setSearchTxt(String searchTxt) {
		this.searchTxt = searchTxt;
	}
}
