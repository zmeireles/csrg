package com.obc.csrg.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.faces.event.ActionEvent;
import javax.persistence.EntityManager;

import org.jboss.seam.log.Log;

import com.obc.csrg.model.Department;
import com.obc.csrg.model.Model;
import com.obc.csrg.model.ProfCategory;
import com.obc.csrg.model.ServiceArea;
import com.obc.csrg.model.ServiceClassification;
import com.obc.csrg.model.User;

public class TargetsManager {

	//users
	private List<SelectModelItem<User>> avUsers = new ArrayList<SelectModelItem<User>>();
	private List<SelectModelItem<User>> selUsers = new ArrayList<SelectModelItem<User>>();
	private boolean selectAllAvUsers = true;
	private boolean selectAllSelUsers = true;
	//departments
	private List<SelectModelItem<Department>> avDepartments = new ArrayList<SelectModelItem<Department>>();
	private List<SelectModelItem<Department>> selDepartments = new ArrayList<SelectModelItem<Department>>();
	private boolean selectAllAvDepartments = true;
	private boolean selectAllSelDepartments = true;
	//profCategories
	private List<SelectModelItem<ProfCategory>> avProfCategories = new ArrayList<SelectModelItem<ProfCategory>>();
	private List<SelectModelItem<ProfCategory>> selProfCategories = new ArrayList<SelectModelItem<ProfCategory>>();
	private boolean selectAllAvProfCategories = true;
	private boolean selectAllSelProfCategories = true;
	//service classifications
	private List<SelectModelItem<ServiceClassification>> avServiceClassifications= new ArrayList<SelectModelItem<ServiceClassification>>();
	private List<SelectModelItem<ServiceClassification>> selServiceClassifications= new ArrayList<SelectModelItem<ServiceClassification>>();
	private boolean selectAllAvServiceClassifications= true;
	private boolean selectAllSelServiceClassifications = true;
	//service areas
	private List<SelectModelItem<ServiceArea>> avServiceAreas= new ArrayList<SelectModelItem<ServiceArea>>();
	private List<SelectModelItem<ServiceArea>> selServiceAreas= new ArrayList<SelectModelItem<ServiceArea>>();
	private boolean selectAllAvServiceAreas= true;
	private boolean selectAllSelServiceAreas = true;
	
	//includes the calculated and manually inserted users
	private List<SelectModelItem<User>> calculatedUsers = new ArrayList<SelectModelItem<User>>();
	
	//includes the calculated and manually inserted departments
	private List<SelectModelItem<Department>> calculatedDepartments= new ArrayList<SelectModelItem<Department>>();
	
	//control collapse able panels
	private boolean showUsersPanel = false;
	private boolean showDepartmentsPanel = false;
	private boolean showProfCategoriesPanel = false;
	private boolean showServiceAreasPanel = false;
	private boolean showServiceClassificationsPanel = false;
	
	private EntityManager entityManager;
	private Log log;
	private Map<String,String> messages;
	
	//contructors
	public TargetsManager(EntityManager entityManager,Log log, Map<String,String> messages){
		this.entityManager = entityManager;
		this.log = log;
		this.messages = messages;
	}
	//business logic
	public void initAvUsers(){
		List<User>users = entityManager.createQuery("select m from User m where m.active=true order by m.person.name")
							.getResultList();
		for(User u:users){
			u.getNotifications().size();//force fetch
			SelectModelItem<User> su = new SelectModelItem<User>(u);
			avUsers.add(su);
		}
	}
	public void initAvDepartments(){
		List<Department> departments = entityManager.createQuery("select m from Department m order by m.name")
							.getResultList();
		for(Department m:departments){
			m.getNotifications().size();
			m.getUsers().size();
			SelectModelItem<Department> sm = new SelectModelItem<Department>(m);
			avDepartments.add(sm);
		}
	}
	public void initAvProfCategories(){
		List<ProfCategory> profCategories = entityManager.createQuery("select m from ProfCategory m order by m.name")
							.getResultList();
		for(ProfCategory m:profCategories){
			m.getNotifications().size();
			m.getUsers().size();
			SelectModelItem<ProfCategory> sm = new SelectModelItem<ProfCategory>(m);
			avProfCategories.add(sm);
		}
	}
	public void initAvServiceClassifications(){
		List<ServiceClassification> serviceClassifications = entityManager.createQuery("select m from ServiceClassification m order by m.name")
							.getResultList();
		for(ServiceClassification m:serviceClassifications){
			m.getDepartments().size();
			m.getNotifications().size();
			SelectModelItem<ServiceClassification> sm = new SelectModelItem<ServiceClassification>(m);
			avServiceClassifications.add(sm);
		}
	}
	public void initAvServiceAreas(){
		List<ServiceArea> serviceAreas = entityManager.createQuery("select m from ServiceArea m order by m.name")
							.getResultList();
		for(ServiceArea m:serviceAreas){
			m.getDepartments().size();
			m.getNotifications().size();
			m.getUsers().size();
			SelectModelItem<ServiceArea> sm = new SelectModelItem<ServiceArea>(m);
			avServiceAreas.add(sm);
		}
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
				//log.info("[addItems] addItem:#0", o);
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
	public void initCollapsiblePanels(){
		this.showDepartmentsPanel = this.selDepartments.size()>0?true:false;
		this.showUsersPanel = this.selUsers.size()>0?true:false;
		this.showProfCategoriesPanel = this.selProfCategories.size()>0?true:false;
		this.showServiceAreasPanel = this.selServiceAreas.size()>0?true:false;
		this.showServiceClassificationsPanel = this.selServiceClassifications.size()>0?true:false;
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
	
	//getters and setters
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
