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
import com.obc.csrg.model.ServiceClassification;
import com.obc.csrg.model.Model;
import com.obc.csrg.events.ModelEventAdapter;
import com.obc.csrg.events.ModelEvent;
import com.obc.csrg.events.ModelEventQueue;

@Name("serviceClassificationHome")
public class ServiceClassificationHome<E extends Model> extends
		ObcEntityHome<ServiceClassification> {

	@RequestParameter
	Long serviceClassificationId;

	// departments relation management
	private List<Department> departmentsNotInList = new ArrayList<Department>();
	private List<Department> selectedDepartments = new ArrayList<Department>();

	// Business functions
	@Override
	public Object getId() {
		if (serviceClassificationId == null)
			return super.getId();
		else
			return serviceClassificationId;

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
		log.info("[destroy] going to unregisterListener");
		this.unregisterModelListener();
	}

	@Override
	protected void loadAfterCreate() {
		super.loadAfterCreate();
		this.initDepartmentsNotInList();
		// log.info("[loadAfterCreate]");
	}

	private void initDepartmentsNotInList() {
		departmentsNotInList.clear();
		if (this.instance.getId() != null)
			departmentsNotInList = entityManager
					.createQuery("select m from Department m where " +
							"m.serviceClassification is null or m.serviceClassification.id!=? order by m.name")
							.setParameter(1, this.getInstance().getId())
							.getResultList();
		else
			departmentsNotInList = entityManager.createQuery(
					"select m from Department m order by m.name")
					.getResultList();
	}

	public void addDndOperation(DragEvent event) {
		// log.info("[addDndOperation] targetId:#0",event.getTargetClientId());
		if (event.getEventType() == DndEvent.DROPPED
				&& this.isSelectionTarget(event.getTargetClientId())) {
			Model model = (Model) ((HtmlPanelGroup) event.getComponent())
					.getDragValue();
			if (model != null) {
				if (model instanceof Department) {
					Department department = (Department) model;
					if (!department.isObjectInList(this.selectedDepartments)) {
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
		// log.info("[removeDndOperation] eventType:#0, event.Dropped:#1",
		// event.getEventType(),DndEvent.DROPPED);
		if (event.getEventType() == DndEvent.DROPPED
				&& this.isAvailableTarget(event.getTargetClientId())) {
			Model model = (Model) ((HtmlPanelGroup) event.getComponent())
					.getDragValue();
			log.info("[removeDndOperation] model:#0, model class:#1", model,
					model.getClass());
			if (model != null) {
				if (model instanceof Department) {
					Department department = (Department) model;
					if (!department.isObjectInList(this.departmentsNotInList)) {
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

	public void savePopupSelectedDepartments() {
		this.setShowPopup(!this.isShowPopup());
		if (this.selectedDepartments.size() == 0)
			return;
		for (Department m : this.selectedDepartments) {
			this.instance.getDepartments().add(m);
			m.setServiceClassification(instance);
			entityManager.merge(m);
		}
		this.selectedDepartments.clear();
	}

	// Model events handling
	private final class ModelEventHandler extends ModelEventAdapter implements
			Serializable {
		@Override
		public void newModel(ModelEvent e) {
			Model m = (Model) e.getSource();
			if (m instanceof Department) {
				Department d = (Department) m;
				if (d.getServiceClassification().getId().equals(
						getInstance().getId()))
					instance.getDepartments().add(d);
			}
		}

		@Override
		public void onBeforeRemove(ModelEvent e) {
			Model m = (Model) e.getSource();
			if (m instanceof Department) {
				Department d = (Department) m;
				if (d.getServiceClassification().getId().equals(
						getInstance().getId()))
					instance.getDepartments().remove(d);
			}
		}

		@Override
		public void modelUpdated(ModelEvent e, String property,
				Object oldValue, Object newValue) {
			Model m = (Model) e.getSource();
			//log.info("[modelUpdated] model:#0, property:#1, oldValue:#2", m,property,oldValue);
			if (m instanceof Department) {
				if (property.equals("serviceClassification")) {
					if (oldValue != null && oldValue instanceof Long
							&& ((Long) oldValue).equals(instance.getId())) {
						Department d = (Department) m;
						instance.getDepartments().remove(d);
						departmentsNotInList.add(d);
					} else if (newValue != null && newValue instanceof Long
							&& ((Long) newValue).equals(instance.getId())) {
						Department d = (Department) m;
						instance.getDepartments().add(d);
						departmentsNotInList.remove(d);
					}
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

	// getters and setters

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

}
