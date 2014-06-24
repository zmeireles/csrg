package com.obc.csrg.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import org.jboss.seam.annotations.Begin;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.Destroy;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.web.RequestParameter;

import com.obc.csrg.events.ModelEvent;
import com.obc.csrg.events.ModelEventAdapter;
import com.obc.csrg.events.ModelEventQueue;
import com.obc.csrg.model.Department;
import com.obc.csrg.model.DepartmentNotification;
import com.obc.csrg.model.Notification;
import com.obc.csrg.model.Model;
import com.obc.csrg.model.ProfCategory;
import com.obc.csrg.model.ProfCategoryNotification;
import com.obc.csrg.model.ServiceArea;
import com.obc.csrg.model.ServiceAreaNotification;
import com.obc.csrg.model.ServiceClassification;
import com.obc.csrg.model.ServiceClassificationNotification;
import com.obc.csrg.model.User;
import com.obc.csrg.model.UserNotification;
import com.obc.csrg.model.DBCronTask;
import com.obc.csrg.util.NotificationUtil;
import com.obc.csrg.util.SelectModelItem;
import com.obc.csrg.constants.DBCronTaskEnum;
import com.obc.csrg.constants.NotificationMediaEnum;

@Name("notificationHome")
public class NotificationHome<E extends Model> extends ObcEntityHome<Notification> {

	@RequestParameter 
    Long notificationId;
	
	private List<User> userList	= new ArrayList<User>();
	
	//users
	protected List<SelectModelItem<User>> avUsers = new ArrayList<SelectModelItem<User>>();
	protected List<SelectModelItem<User>> selUsers = new ArrayList<SelectModelItem<User>>();
	protected boolean selectAllAvUsers = true;
	protected boolean selectAllSelUsers = true;
	//departments
	protected List<SelectModelItem<Department>> avDepartments = new ArrayList<SelectModelItem<Department>>();
	protected List<SelectModelItem<Department>> selDepartments = new ArrayList<SelectModelItem<Department>>();
	protected boolean selectAllAvDepartments = true;
	protected boolean selectAllSelDepartments = true;
	//profCategories
	protected List<SelectModelItem<ProfCategory>> avProfCategories = new ArrayList<SelectModelItem<ProfCategory>>();
	protected List<SelectModelItem<ProfCategory>> selProfCategories = new ArrayList<SelectModelItem<ProfCategory>>();
	protected boolean selectAllAvProfCategories = true;
	protected boolean selectAllSelProfCategories = true;
	//service classifications
	protected List<SelectModelItem<ServiceClassification>> avServiceClassifications= new ArrayList<SelectModelItem<ServiceClassification>>();
	protected List<SelectModelItem<ServiceClassification>> selServiceClassifications= new ArrayList<SelectModelItem<ServiceClassification>>();
	protected boolean selectAllAvServiceClassifications= true;
	protected boolean selectAllSelServiceClassifications = true;
	//service areas
	protected List<SelectModelItem<ServiceArea>> avServiceAreas= new ArrayList<SelectModelItem<ServiceArea>>();
	protected List<SelectModelItem<ServiceArea>> selServiceAreas= new ArrayList<SelectModelItem<ServiceArea>>();
	protected boolean selectAllAvServiceAreas= true;
	protected boolean selectAllSelServiceAreas = true;
	
	//includes the calculated and manually inserted users
	protected List<SelectModelItem<User>> calculatedUsers = new ArrayList<SelectModelItem<User>>();
	
	//includes the calculated and manually inserted departments
	private List<SelectModelItem<Department>> calculatedDepartments= new ArrayList<SelectModelItem<Department>>();
	
	protected NotificationUtil notificationUtil;
	
	//control collapse able panels
	private boolean showUsersPanel = false;
	private boolean showDepartmentsPanel = false;
	private boolean showProfCategoriesPanel = false;
	private boolean showServiceAreasPanel = false;
	private boolean showServiceClassificationsPanel = false;
	
	@Override
    public Object getId() {
		if (notificationId == null)
			return super.getId();
		else
			return notificationId;
    }
	
	@Override
	@Create
	@Begin(join=true)
	public void create(){
		super.create();
		log.info("[create] notificationHome created");
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
		this.initDefaultMessages();
		this.initUserList();
		this.initAvUsers();
		this.initAvDepartments();
		this.initAvProfCategories();
		this.initAvServiceClassifications();
		this.initAvServiceAreas();
		this.loadNotificationUsers();
		this.loadNotificationDepartments();
		this.loadNotificationProfCategories();
		this.loadNotificationServiceAreas();
		this.loadNotificationServiceClassifications();
		this.initCollapsiblePanels();
		notificationUtil = new NotificationUtil(entityManager,log,messages);
		
		if(this.instance.getId()==null)
			//I'm creating the notification
			instance.setSender(stateBean.getUser());
		// log.info("[loadAfterCreate]");
	}
	
	private void initUserList(){
		userList = entityManager.createQuery("select m from User m order by m.person.name")
							.getResultList();
	}
	
	//Model events handling
	private final class ModelEventHandler extends ModelEventAdapter implements Serializable{
		@Override
		public void newModel(ModelEvent e){
			Model m = (Model) e.getSource();
			
		}
		@Override
		public void onBeforeRemove(ModelEvent e){
			Model m = (Model) e.getSource();
			
		}
		@Override
		public void onAfterRemove(ModelEvent e){
			/*Model m = (Model) e.getSource();
			if(m instanceof Notification){
				//remove DBCronTask
				DBCronTask task = getExistingTask();
				if(task!=null)
					entityManager.remove(task);
			}*/
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
	
	protected DBCronTask getExistingTask(){
		List<DBCronTask> tasks = entityManager.createQuery("select m from DBCronTask m where " +
				"m.task=? and m.entityId is not null and m.entityId=?")
				.setParameter(1, DBCronTaskEnum.ADD_NOTIFICATION)
				.setParameter(2, this.instance.getId())
				.getResultList();
		//log.info("[getExistingTask] taskList size:#0, instanceId:#1",tasks.size(),this.instance.getId());
		if(tasks.size()>0){
			DBCronTask task = tasks.get(0);
			task.setBeginDate(instance.getSendAt());
			//log.info("[getExistingTask] taskId:#0",task.getId());
			return task;
		}
		return null;
	}
	
	private void initAvUsers(){
		List<User>users = entityManager.createQuery("select m from User m order by m.person.name")
							.getResultList();
		for(User u:users){
			SelectModelItem<User> su = new SelectModelItem<User>(u);
			avUsers.add(su);
		}
	}
	private void initAvDepartments(){
		List<Department> departments = entityManager.createQuery("select m from Department m order by m.name")
							.getResultList();
		for(Department m:departments){
			SelectModelItem<Department> sm = new SelectModelItem<Department>(m);
			avDepartments.add(sm);
		}
	}
	private void initAvProfCategories(){
		List<ProfCategory> profCategories = entityManager.createQuery("select m from ProfCategory m order by m.name")
							.getResultList();
		for(ProfCategory m:profCategories){
			SelectModelItem<ProfCategory> sm = new SelectModelItem<ProfCategory>(m);
			avProfCategories.add(sm);
		}
	}
	private void initAvServiceClassifications(){
		List<ServiceClassification> serviceClassifications = entityManager.createQuery("select m from ServiceClassification m order by m.name")
							.getResultList();
		for(ServiceClassification m:serviceClassifications){
			SelectModelItem<ServiceClassification> sm = new SelectModelItem<ServiceClassification>(m);
			avServiceClassifications.add(sm);
		}
	}
	private void initAvServiceAreas(){
		List<ServiceArea> serviceAreas = entityManager.createQuery("select m from ServiceArea m order by m.name")
							.getResultList();
		for(ServiceArea m:serviceAreas){
			SelectModelItem<ServiceArea> sm = new SelectModelItem<ServiceArea>(m);
			avServiceAreas.add(sm);
		}
	}
	
	protected void loadNotificationUsers(){
		this.selUsers.clear();
		for(UserNotification u:instance.getUsers()){
			//log.info("[loadNotificationUsers] users.size:#0",this.instance.getUsers().size());
			if(!u.isCalculated()){
				//log.info("[loadNotificationUsers] native user:#0", u.getUser());
				this.selUsers.add(new SelectModelItem<User>(u.getUser(),false));
			}
			this.calculatedUsers.add(new SelectModelItem<User>(u.getUser(),u.isCalculated()));
		}
		this.avUsers.removeAll(this.selUsers);
	}
	protected void loadNotificationDepartments(){
		this.selDepartments.clear();
		for(DepartmentNotification mn:instance.getDepartments()){
			this.selDepartments.add(new SelectModelItem<Department>(mn.getDepartment(),true));
		}
		this.avDepartments.removeAll(this.selDepartments);
	}
	protected void loadNotificationProfCategories(){
		this.selProfCategories.clear();
		for(ProfCategoryNotification mn:instance.getProfCategories()){
			this.selProfCategories.add(new SelectModelItem<ProfCategory>(mn.getProfCategory(),true));
		}
		this.avProfCategories.removeAll(this.selProfCategories);
	}
	protected void loadNotificationServiceAreas(){
		this.selServiceAreas.clear();
		for(ServiceAreaNotification mn:instance.getServiceAreas()){
			this.selServiceAreas.add(new SelectModelItem<ServiceArea>(mn.getServiceArea(),true));
		}
		this.avServiceAreas.removeAll(this.selServiceAreas);
	}
	protected void loadNotificationServiceClassifications(){
		this.selServiceClassifications.clear();
		for(ServiceClassificationNotification mn:instance.getServiceClassifications()){
			this.selServiceClassifications.add(new SelectModelItem<ServiceClassification>(mn.getServiceClassification(),true));
		}
		this.avServiceClassifications.removeAll(this.selServiceClassifications);
	}
	public List<SelectItem> getNotificationsMedia(){
		List<SelectItem> result = new ArrayList<SelectItem>();
		for(NotificationMediaEnum m:NotificationMediaEnum.values()){
			result.add(new SelectItem(m,messages.get(m.getName())));
		}
		return result;
	}
	
	public void addItems(ActionEvent event) {
		String id = event.getComponent().getId();
		//log.info("[addItems] id:#0",id);
		List avList = new ArrayList();
		List selList = new ArrayList();
		if(id.equals("addUsers")){
			avList = this.avUsers;
			selList = this.selUsers;
		}else if(id.equals("removeUsers")){
			avList = this.selUsers;
			selList = this.avUsers;
		}else if(id.equals("addDepartments")){
			avList = this.avDepartments;
			selList = this.selDepartments;
		}else if(id.equals("removeDepartments")){
			avList = this.selDepartments;
			selList = this.avDepartments;
		}else if(id.equals("addProfCategories")){
			avList = this.avProfCategories;
			selList = this.selProfCategories;
		}else if(id.equals("removeProfCategories")){
			avList = this.selProfCategories;
			selList = this.avProfCategories;
		}else if(id.equals("addServiceClassifications")){
			avList = this.avServiceClassifications;
			selList = this.selServiceClassifications;
		}else if(id.equals("removeServiceClassifications")){
			avList = this.selServiceClassifications;
			selList = this.avServiceClassifications;
		}else if(id.equals("addServiceAreas")){
			avList = this.avServiceAreas;
			selList = this.selServiceAreas;
		}else if(id.equals("removeServiceAreas")){
			avList = this.selServiceAreas;
			selList = this.avServiceAreas;
		}else{
			return;
		}
		for(Object o:avList){
			SelectModelItem si = (SelectModelItem)o;
			if(si.isSelected()){
				log.info("[addItems] addItem:#0", o);
				si.setSelected(false);
				selList.add(si);
			}
		}
		avList.removeAll(selList);
		//log.info("[addItems] av size:#0, selSize:#1", avList.size(),selList.size());
		
	}
	
	public void selectAllButton(ActionEvent event) {
		String id = event.getComponent().getId();
		boolean value = true;
		List list = new ArrayList();
		if(id.equals("checkAvUsers")){
			value = this.selectAllAvUsers;
			list = this.avUsers;
			this.selectAllAvUsers=!value;
		}else if(id.equals("checkSelUsers")){
			value = this.selectAllSelUsers;
			list = this.selUsers;
			this.selectAllSelUsers=!value;
		}else if(id.equals("checkAvDepartments")){
			value = this.selectAllAvDepartments;
			list = this.avDepartments;
			this.selectAllAvDepartments=!value;
		}else if(id.equals("checkSelDepartments")){
			value = this.selectAllSelDepartments;
			list = this.selDepartments;
			this.selectAllSelDepartments=!value;
		}else if(id.equals("checkAvProfCategories")){
			value = this.selectAllAvProfCategories;
			list = this.avProfCategories;
			this.selectAllAvProfCategories=!value;
		}else if(id.equals("checkSelProfCategories")){
			value = this.selectAllSelProfCategories;
			list = this.selProfCategories;
			this.selectAllSelProfCategories=!value;
		}else if(id.equals("checkAvServiceClassifications")){
			value = this.selectAllAvServiceClassifications;
			list = this.avServiceClassifications;
			this.selectAllAvServiceClassifications=!value;
		}else if(id.equals("checkSelServiceClassifications")){
			value = this.selectAllSelServiceClassifications;
			list = this.selServiceClassifications;
			this.selectAllSelServiceClassifications=!value;
		}else if(id.equals("checkAvServiceAreas")){
			value = this.selectAllAvServiceAreas;
			list = this.avServiceAreas;
			this.selectAllAvServiceAreas=!value;
		}else if(id.equals("checkSelServiceAreas")){
			value = this.selectAllSelServiceAreas;
			list = this.selServiceAreas;
			this.selectAllSelServiceAreas=!value;
		}else
			return;
		for(Object o:list){
			SelectModelItem<Model> si = (SelectModelItem<Model>) o;
			si.setSelected(value);
		}
	}
	
	//select all buttons for users
	public String getAvUsersButtonValue(){
		return this.selectAllAvUsers==true?messages.get("SelectAllCheck"):messages.get("RemoveSelection");
	}
	public String getSelUsersButtonValue(){
		return this.selectAllSelUsers==true?messages.get("SelectAllCheck"):messages.get("RemoveSelection");
	}
	//select all buttons for departments
	public String getAvDepartmentsButtonValue(){
		return this.selectAllAvDepartments==true?messages.get("SelectAllCheck"):messages.get("RemoveSelection");
	}
	public String getSelDepartmentsButtonValue(){
		return this.selectAllSelDepartments==true?messages.get("SelectAllCheck"):messages.get("RemoveSelection");
	}
	//select all buttons for professional categories
	public String getAvProfCategoriesButtonValue(){
		return this.selectAllAvProfCategories==true?messages.get("SelectAllCheck"):messages.get("RemoveSelection");
	}
	public String getSelProfCategoriesButtonValue(){
		return this.selectAllSelProfCategories==true?messages.get("SelectAllCheck"):messages.get("RemoveSelection");
	}
	//select all buttons for service classifications
	public String getAvServiceClassificationsButtonValue(){
		return this.selectAllAvServiceClassifications==true?messages.get("SelectAllCheck"):messages.get("RemoveSelection");
	}
	public String getSelServiceClassificationsButtonValue(){
		return this.selectAllSelServiceClassifications==true?messages.get("SelectAllCheck"):messages.get("RemoveSelection");
	}
	//select all buttons for service areas
	public String getAvServiceAreasButtonValue(){
		return this.selectAllAvServiceAreas==true?messages.get("SelectAllCheck"):messages.get("RemoveSelection");
	}
	public String getSelServiceAreasButtonValue(){
		return this.selectAllSelServiceAreas==true?messages.get("SelectAllCheck"):messages.get("RemoveSelection");
	}
	
	public void calculateUsers(){
		this.notificationUtil = new NotificationUtil(entityManager,log,messages);
		this.notificationUtil.addUsers(this.selUsers);
		this.notificationUtil.addDepartmentUsers(this.selDepartments);
		this.notificationUtil.addProfCategoryUsers(this.selProfCategories);
		this.notificationUtil.addServiceAreaUsers(selServiceAreas);
		this.notificationUtil.addServiceClassificationUsers(selServiceClassifications);
		this.notificationUtil.calculateUsers();
		this.calculatedUsers.clear();
		for(SelectModelItem<User> item:this.notificationUtil.getSelectedUsers()){
			log.info("[calculateUsers] user:#0", item.getInstance());
			this.calculatedUsers.add(item);
		}
	}
	
	protected void initCollapsiblePanels(){
		this.showDepartmentsPanel = this.selDepartments.size()>0?true:false;
		this.showUsersPanel = this.selUsers.size()>0?true:false;
		this.showProfCategoriesPanel = this.selProfCategories.size()>0?true:false;
		this.showServiceAreasPanel = this.selServiceAreas.size()>0?true:false;
		this.showServiceClassificationsPanel = this.selServiceClassifications.size()>0?true:false;
	}
	
	//getters and setters
	public List<User> getUserList() {
		return userList;
	}

	public void setUserList(List<User> userList) {
		this.userList = userList;
	}
	
	public List<SelectModelItem<User>> getAvUsers() {
		return avUsers;
	}

	public void setAvUsers(List<SelectModelItem<User>> avUsers) {
		this.avUsers = avUsers;
	}
	
	public List<SelectModelItem<User>> getSelUsers() {
		return selUsers;
	}

	public void setSelUsers(List<SelectModelItem<User>> selUsers) {
		this.selUsers = selUsers;
	}

	public boolean isSelectAllAvUsers() {
		return selectAllAvUsers;
	}

	public void setSelectAllAvUsers(boolean selectAllAvUsers) {
		this.selectAllAvUsers = selectAllAvUsers;
	}

	public boolean isSelectAllSelUsers() {
		return selectAllSelUsers;
	}

	public void setSelectAllSelUsers(boolean selectAllSelUsers) {
		this.selectAllSelUsers = selectAllSelUsers;
	}
	
	public List<SelectModelItem<Department>> getAvDepartments() {
		return avDepartments;
	}

	public void setAvDepartments(List<SelectModelItem<Department>> avDepartments) {
		this.avDepartments = avDepartments;
	}

	public List<SelectModelItem<Department>> getSelDepartments() {
		return selDepartments;
	}

	public void setSelDepartments(List<SelectModelItem<Department>> selDepartments) {
		this.selDepartments = selDepartments;
	}

	public boolean isSelectAllAvDepartments() {
		return selectAllAvDepartments;
	}

	public void setSelectAllAvDepartments(boolean selectAllAvDepartments) {
		this.selectAllAvDepartments = selectAllAvDepartments;
	}

	public boolean isSelectAllSelDepartments() {
		return selectAllSelDepartments;
	}

	public void setSelectAllSelDepartments(boolean selectAllSelDepartments) {
		this.selectAllSelDepartments = selectAllSelDepartments;
	}
	
	public List<SelectModelItem<ProfCategory>> getAvProfCategories() {
		return avProfCategories;
	}

	public void setAvProfCategories(
			List<SelectModelItem<ProfCategory>> avProfCategories) {
		this.avProfCategories = avProfCategories;
	}

	public List<SelectModelItem<ProfCategory>> getSelProfCategories() {
		return selProfCategories;
	}

	public void setSelProfCategories(
			List<SelectModelItem<ProfCategory>> selProfCategories) {
		this.selProfCategories = selProfCategories;
	}

	public boolean isSelectAllAvProfCategories() {
		return selectAllAvProfCategories;
	}

	public void setSelectAllAvProfCategories(boolean selectAllAvProfCategories) {
		this.selectAllAvProfCategories = selectAllAvProfCategories;
	}

	public boolean isSelectAllSelProfCategories() {
		return selectAllSelProfCategories;
	}

	public void setSelectAllSelProfCategories(boolean selectAllSelProfCategories) {
		this.selectAllSelProfCategories = selectAllSelProfCategories;
	}
	
	public List<SelectModelItem<ServiceClassification>> getAvServiceClassifications() {
		return avServiceClassifications;
	}

	public void setAvServiceClassifications(
			List<SelectModelItem<ServiceClassification>> avServiceClassifications) {
		this.avServiceClassifications = avServiceClassifications;
	}

	public List<SelectModelItem<ServiceClassification>> getSelServiceClassifications() {
		return selServiceClassifications;
	}

	public void setSelServiceClassifications(
			List<SelectModelItem<ServiceClassification>> selServiceClassifications) {
		this.selServiceClassifications = selServiceClassifications;
	}

	public boolean isSelectAllAvServiceClassifications() {
		return selectAllAvServiceClassifications;
	}

	public void setSelectAllAvServiceClassifications(
			boolean selectAllAvServiceClassifications) {
		this.selectAllAvServiceClassifications = selectAllAvServiceClassifications;
	}

	public boolean isSelectAllSelServiceClassifications() {
		return selectAllSelServiceClassifications;
	}

	public void setSelectAllSelServiceClassifications(
			boolean selectAllSelServiceClassifications) {
		this.selectAllSelServiceClassifications = selectAllSelServiceClassifications;
	}

	public List<SelectModelItem<ServiceArea>> getAvServiceAreas() {
		return avServiceAreas;
	}

	public void setAvServiceAreas(List<SelectModelItem<ServiceArea>> avServiceAreas) {
		this.avServiceAreas = avServiceAreas;
	}

	public List<SelectModelItem<ServiceArea>> getSelServiceAreas() {
		return selServiceAreas;
	}

	public void setSelServiceAreas(
			List<SelectModelItem<ServiceArea>> selServiceAreas) {
		this.selServiceAreas = selServiceAreas;
	}

	public boolean isSelectAllAvServiceAreas() {
		return selectAllAvServiceAreas;
	}

	public void setSelectAllAvServiceAreas(boolean selectAllAvServiceAreas) {
		this.selectAllAvServiceAreas = selectAllAvServiceAreas;
	}

	public boolean isSelectAllSelServiceAreas() {
		return selectAllSelServiceAreas;
	}

	public void setSelectAllSelServiceAreas(boolean selectAllSelServiceAreas) {
		this.selectAllSelServiceAreas = selectAllSelServiceAreas;
	}
	
	public List<SelectModelItem<User>> getCalculatedUsers() {
		return calculatedUsers;
	}

	public void setCalculatedUsers(List<SelectModelItem<User>> calculatedUsers) {
		this.calculatedUsers = calculatedUsers;
	}
	
	public List<SelectModelItem<Department>> getCalculatedDepartments() {
		return calculatedDepartments;
	}

	public void setCalculatedDepartments(
			List<SelectModelItem<Department>> calculatedDepartments) {
		this.calculatedDepartments = calculatedDepartments;
	}
	
	public boolean isShowUsersPanel() {
		return showUsersPanel;
	}

	public void setShowUsersPanel(boolean showUsersPanel) {
		this.showUsersPanel = showUsersPanel;
	}

	public boolean isShowDepartmentsPanel() {
		return showDepartmentsPanel;
	}

	public void setShowDepartmentsPanel(boolean showDepartmentsPanel) {
		this.showDepartmentsPanel = showDepartmentsPanel;
	}

	public boolean isShowProfCategoriesPanel() {
		return showProfCategoriesPanel;
	}

	public void setShowProfCategoriesPanel(boolean showProfCategoriesPanel) {
		this.showProfCategoriesPanel = showProfCategoriesPanel;
	}

	public boolean isShowServiceAreasPanel() {
		return showServiceAreasPanel;
	}

	public void setShowServiceAreasPanel(boolean showServiceAreasPanel) {
		this.showServiceAreasPanel = showServiceAreasPanel;
	}

	public boolean isShowServiceClassificationsPanel() {
		return showServiceClassificationsPanel;
	}

	public void setShowServiceClassificationsPanel(
			boolean showServiceClassificationsPanel) {
		this.showServiceClassificationsPanel = showServiceClassificationsPanel;
	}
}
