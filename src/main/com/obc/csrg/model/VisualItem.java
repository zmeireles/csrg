package com.obc.csrg.model;

import static javax.persistence.GenerationType.SEQUENCE;
import static org.jboss.seam.ScopeType.SESSION;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

import com.obc.csrg.events.ModelEventQueue;
import com.obc.csrg.constants.Constants;

@Entity
@Name("visualItem")
@Scope(SESSION)
@Table(name = "visual_item")
//@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
public class VisualItem extends Model implements Serializable{

	@SequenceGenerator(name = "generator")
	@Id
	@GeneratedValue(strategy = SEQUENCE, generator = "generator")
	@Column(name = "visual_item_id", nullable = false)
	private Long id;
	
	@Column(name = "xhtml_filename")
	private String xhtmlFilename="";
	
	@Column(name = "menu_text")
	private String menuText="";
	
	@OneToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE,
			CascadeType.REFRESH, CascadeType.REMOVE }, fetch = FetchType.EAGER)
	@JoinColumn(name = "webpage_id")
	private Webpage webpage;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "parent_id")
	private VisualItem parent;
	
	@OneToMany(cascade = { CascadeType.MERGE,
			CascadeType.REFRESH }, fetch = FetchType.LAZY, mappedBy = "parent")
	//@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
	private Set<VisualItem> children = new HashSet<VisualItem>(0);
	
	// connection with notification system
	@OneToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE,
			CascadeType.REFRESH, CascadeType.REMOVE }, fetch = FetchType.EAGER)
	@JoinColumn(name = "notification_id")
	private Notification notification;

	@OneToMany(cascade = {CascadeType.MERGE,CascadeType.REFRESH }, fetch = FetchType.LAZY, mappedBy = "visualItem")
	//@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
	private Set<DepartmentNotification> departments = new HashSet<DepartmentNotification>(
			0);
	
	@OneToMany(cascade = {CascadeType.MERGE,CascadeType.REFRESH }, fetch = FetchType.LAZY, mappedBy = "visualItem")
	//@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
	private Set<ProfCategoryNotification> profCategories = new HashSet<ProfCategoryNotification>(
			0);
	
	@OneToMany(cascade = {CascadeType.MERGE,CascadeType.REFRESH }, fetch = FetchType.LAZY, mappedBy = "visualItem")
	//@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
	private Set<ServiceAreaNotification> serviceAreas = new HashSet<ServiceAreaNotification>(
			0);
	
	@OneToMany(cascade = {CascadeType.MERGE,CascadeType.REFRESH }, fetch = FetchType.LAZY, mappedBy = "visualItem")
	//@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
	private Set<ServiceClassificationNotification> serviceClassifications = new HashSet<ServiceClassificationNotification>(
			0);
	
	@OneToMany(cascade = {CascadeType.MERGE,CascadeType.REFRESH }, fetch = FetchType.LAZY, mappedBy = "visualItem")
	//@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
	private Set<UserNotification> users = new HashSet<UserNotification>(
			0);
	
	@Column(name = "item_order")
	private int itemOrder=1;
	
	private boolean publish=false;
	
	@Column(name = "pub_after_notification")
	private boolean publishAfterNotification=false;
	
	private String locale=Constants.DEFAULT_LOCALE;
	
	//constructors
	public VisualItem() {
	}

	//business logic
	
	@Override
	public boolean remove(EntityManager entityManager) {
		ModelEventQueue.fireOnBeforeModelRemoveEvent(this);
		
		if(this.notification!=null){
			this.notification.remove(entityManager);
			this.notification = null;
		}
		if(this.webpage!=null){
			this.webpage.setVisualItem(null);
			entityManager.remove(this.webpage);
			this.webpage = null;
		}
		//detach children
		for(VisualItem v:this.children){
			v.setParent(null);
			entityManager.merge(v);
		}
		this.children.clear();
		
		//update parent
		if(this.parent!=null){
			this.parent.getChildren().remove(this);
			this.setParent(null);
		}
		
		//notifications already removed. All is left to do is garbage dispose
		
		// remove departments relation
		for (DepartmentNotification d : this.departments) {
			d.setVisualItem(null);
			d.getDepartment().getNotifications().remove(d);
			d.setDepartment(null);
			entityManager.remove(d);
		}
		this.departments.clear();
		// remove professional categories relation
		for (ProfCategoryNotification p : this.profCategories) {
			p.setVisualItem(null);
			p.getProfCategory().getNotifications().remove(p);
			p.setProfCategory(null);
			entityManager.remove(p);
		}
		this.profCategories.clear();
		// remove service areas relation
		for (ServiceAreaNotification s : this.serviceAreas) {
			s.setVisualItem(null);
			s.getServiceArea().getNotifications().remove(s);
			s.setServiceArea(null);
			entityManager.remove(s);
		}
		this.serviceAreas.clear();
		// remove service classifications relation
		for (ServiceClassificationNotification s : this.serviceClassifications) {
			s.setVisualItem(null);
			s.getServiceClassification().getNotifications().remove(s);
			s.setServiceClassification(null);
			entityManager.remove(s);
		}
		this.serviceClassifications.clear();
		// remove users relation
		for (UserNotification u : this.users) {
			u.setVisualItem(null);
			u.getUser().getNotifications().remove(u);
			u.setUser(null);
			entityManager.remove(u);
		}
		this.users.clear();
		
		entityManager.remove(this);
		ModelEventQueue.fireOnAfterModelRemoveEvent(this);
		return true;
	}
	
	@Override
	public String toString() {
		return this.menuText;
	}
	
	public int hashCode() {
		return (this.id == null) ? 0 : this.id.hashCode();
	}

	public boolean equals(Object object) {
		if (object instanceof VisualItem) {
			final VisualItem obj = (VisualItem) object;
			return (this.id != null) ? this.id.equals(obj.id)
					: (obj.id == null);
		}
		return false;
	}
	
	public VisualItem imParentOf(VisualItem model) {
		for (VisualItem d : this.children) {
			if (d.equals(model))
				return model;
			else {
				if (d.imParentOf(model) != null)
					return model;
			}
		}
		return null;
	}

	//getters and setters
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public String getXhtmlFilename() {
		return xhtmlFilename;
	}

	public void setXhtmlFilename(String xhtmlFilename) {
		this.xhtmlFilename = xhtmlFilename;
	}

	public String getMenuText() {
		return menuText;
	}

	public void setMenuText(String menuText) {
		this.menuText = menuText;
	}

	public Webpage getWebpage() {
		return webpage;
	}

	public void setWebpage(Webpage webpage) {
		this.webpage = webpage;
	}
	
	public VisualItem getParent() {
		return parent;
	}

	public void setParent(VisualItem parent) {
		this.parent = parent;
	}

	public Set<VisualItem> getChildren() {
		return children;
	}

	public void setChildren(Set<VisualItem> children) {
		this.children = children;
	}
	
	public Notification getNotification() {
		return notification;
	}

	public void setNotification(Notification notification) {
		this.notification = notification;
	}
	
	public Set<DepartmentNotification> getDepartments() {
		return departments;
	}

	public void setDepartments(Set<DepartmentNotification> departments) {
		this.departments = departments;
	}

	public Set<ProfCategoryNotification> getProfCategories() {
		return profCategories;
	}

	public void setProfCategories(Set<ProfCategoryNotification> profCategories) {
		this.profCategories = profCategories;
	}

	public Set<ServiceAreaNotification> getServiceAreas() {
		return serviceAreas;
	}

	public void setServiceAreas(Set<ServiceAreaNotification> serviceAreas) {
		this.serviceAreas = serviceAreas;
	}

	public Set<ServiceClassificationNotification> getServiceClassifications() {
		return serviceClassifications;
	}

	public void setServiceClassifications(
			Set<ServiceClassificationNotification> serviceClassifications) {
		this.serviceClassifications = serviceClassifications;
	}

	public Set<UserNotification> getUsers() {
		return users;
	}

	public void setUsers(Set<UserNotification> users) {
		this.users = users;
	}

	public int getItemOrder() {
		return itemOrder;
	}

	public void setItemOrder(int itemOrder) {
		this.itemOrder = itemOrder;
	}

	public boolean isPublish() {
		return publish;
	}

	public void setPublish(boolean publish) {
		this.publish = publish;
	}

	public boolean isPublishAfterNotification() {
		return publishAfterNotification;
	}

	public void setPublishAfterNotification(boolean publishAfterNotification) {
		this.publishAfterNotification = publishAfterNotification;
	}
	
	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}
}
