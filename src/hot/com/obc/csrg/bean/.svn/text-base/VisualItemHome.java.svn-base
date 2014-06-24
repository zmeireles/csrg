package com.obc.csrg.bean;

import java.util.List;
import java.util.ArrayList;
import java.io.Serializable;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import org.jboss.seam.annotations.Begin;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.Destroy;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.web.RequestParameter;

import com.icesoft.faces.component.dragdrop.DndEvent;
import com.icesoft.faces.component.dragdrop.DragEvent;
import com.icesoft.faces.component.ext.HtmlPanelGroup;
import com.icesoft.faces.component.ext.HtmlSelectBooleanCheckbox;
import com.icesoft.faces.context.effects.JavascriptContext;

import com.obc.csrg.action.BaseBean;
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
import com.obc.csrg.model.Webpage;
import com.obc.csrg.model.Model;
import com.obc.csrg.model.Notification;
import com.obc.csrg.util.NotificationUtil;
import com.obc.csrg.util.SelectModelItem;
import com.obc.csrg.util.TargetsManager;
import com.obc.csrg.constants.NotificationMediaEnum;
import com.obc.csrg.events.ModelEventAdapter;
import com.obc.csrg.events.ModelEvent;
import com.obc.csrg.events.ModelEventQueue;


@Name("visualItemHome")
public class VisualItemHome<E extends Model> extends ObcEntityHome<VisualItem> {

	@RequestParameter
	Long visualItemId;

	// list of possible parents
	private List<VisualItem> parentList = new ArrayList<VisualItem>();
	private VisualItem newParent = null;

	private boolean xhtmlpage = false;
	
	private TargetsManager targetsManager;
	
	protected NotificationUtil notificationUtil;
	
	//integration with notification system variables
	protected boolean integrateWithNS=false;
	protected Notification notification;
	private BaseBean baseBean = new BaseBean();

	
	// Business functions
	@Override
	public Object getId() {
		if (visualItemId == null)
			return super.getId();
		else
			return visualItemId;
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
		super.destroy();
		this.unregisterModelListener();
	}

	@Override
	protected void loadAfterCreate() {
		super.loadAfterCreate();
		
		//avoid lazy initialization error
		if(this.getInstance().getId()!=null){
			this.instance.getUsers().size();
			this.instance.getDepartments().size();
			this.instance.getProfCategories().size();
			this.instance.getServiceAreas().size();
			this.instance.getServiceClassifications().size();
		}
		//NS integration
		notification = this.instance.getNotification();
		if(notification==null){
			notification = new Notification();
			notification.setSender(stateBean.getUser());
			this.integrateWithNS=false;
		}else{
			this.integrateWithNS=true;
		}
		
		//end of NS integration
		this.xhtmlpage = this.getInstance().getXhtmlFilename() == null
				|| this.instance.getXhtmlFilename().equals("") ? false : true;
		if(this.instance.getWebpage()==null){
			this.instance.setWebpage(new Webpage());
			this.instance.getWebpage().setVisualItem(instance);
		}
		newParent = this.getInstance().getParent();
		this.initParentList();
		
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
		
		notificationUtil = new NotificationUtil(entityManager,log,messages);
	}

	public void integrateWithNSValueChangeListener(ValueChangeEvent event){
		//log.info("[integrateWithNSValueChangeListener] componentClass:#0", event.getComponent().getClass().toString());
		if(event.getComponent() instanceof HtmlSelectBooleanCheckbox){
			boolean value = (Boolean) event.getComponent().getAttributes().get("value");
			//log.info("[integrateWithNSValueChangeListener] integrate:#0", this.integrateWithNS);
			if(value){
				baseBean.getValueChangeEffect().setFired(false);
			}else{
			}
		}
	}
	protected void initParentList() {
		this.parentList.clear();
		if (this.instance.getId() != null) {
			parentList = entityManager.createQuery(
					"select m from VisualItem m "
							+ "where m.id!=? order by m.menuText")
					.setParameter(1, instance.getId()).getResultList();
		} else {
			parentList = entityManager.createQuery(
					"select m from VisualItem m order by m.menuText")
					.getResultList();
		}
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
	
	
	public void popupImageWindow(ActionEvent event){

		 String link ="mms/imageViewer.seam";
		JavascriptContext.addJavascriptCall(FacesContext.getCurrentInstance(), "window.open('" + link + "',  'toolbar=0,scrollbars=0,location=0,statusbar=0,menubar=0,resizable=1,width=630,height=525,left = 350,top = 210');");
	}
	
	
	//getters and setters
	public List<VisualItem> getParentList() {
		return parentList;
	}

	public void setParentList(List<VisualItem> parentList) {
		this.parentList = parentList;
	}

	public VisualItem getNewParent() {
		return newParent;
	}

	public void setNewParent(VisualItem newParent) {
		this.newParent = newParent;
	}

	public boolean isXhtmlpage() {
		this.xhtmlpage = this.getInstance().getXhtmlFilename() == null
			|| this.instance.getXhtmlFilename().equals("") ? false : true;
		return xhtmlpage;
	}

	public void setXhtmlpage(boolean xhtmlpage) {
		this.xhtmlpage = xhtmlpage;
	}

	public TargetsManager getTargetsManager() {
		return targetsManager;
	}

	public void setTargetsManager(TargetsManager targetsManager) {
		this.targetsManager = targetsManager;
	}
	
	public boolean isIntegrateWithNS() {
		return integrateWithNS;
	}

	public void setIntegrateWithNS(boolean integrateWithNS) {
		this.integrateWithNS = integrateWithNS;
	}

	public Notification getNotification() {
		return notification;
	}

	public void setNotification(Notification notification) {
		this.notification = notification;
	}
	
	public BaseBean getBaseBean() {
		return baseBean;
	}

	public void setBaseBean(BaseBean baseBean) {
		this.baseBean = baseBean;
	}
}
