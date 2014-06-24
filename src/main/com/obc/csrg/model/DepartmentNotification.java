package com.obc.csrg.model;

import static javax.persistence.GenerationType.SEQUENCE;
import static org.jboss.seam.ScopeType.SESSION;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

import com.obc.csrg.events.ModelEventQueue;

@Entity
@Name("departmentNotification")
@Scope(SESSION)
@Table(name = "department_notification")
//@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
public class DepartmentNotification extends Model implements Serializable {

	@SequenceGenerator(name = "generator")
	@Id
	@GeneratedValue(strategy = SEQUENCE, generator = "generator")
	@Column(name = "dep_notification_id", nullable = false)
	private Long id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "notification_id")
	private Notification notification;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "department_id")
	private Department department;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "news_id")
	private News news;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "vi_id")
	private VisualItem visualItem;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "comp_notification_id")
	private ComposedNotification composedNotification;

	// constructors
	public DepartmentNotification() {
	}

	// business functions

	@Override
	public String toString() {
		return "";//this.user.toString() + ":" + this.notification.toString();
	}
	
	@Override
	public boolean remove(EntityManager entityManager) {
		ModelEventQueue.fireOnBeforeModelRemoveEvent(this);
		this.department.getNotifications().remove(this);
		this.department = null;
		if(this.notification!=null){
			this.notification.getDepartments().remove(this);
			this.notification = null;
		}
		if(this.news!=null){
			this.news.getDepartments().remove(this);
			this.news = null;
		}
		if(this.visualItem!=null){
			this.visualItem.getDepartments().remove(this);
			this.visualItem=null;
		}
		if(this.composedNotification!=null){
			this.composedNotification.getDepartments().remove(this);
			this.composedNotification = null;
		}
		
		entityManager.remove(this);
		ModelEventQueue.fireOnAfterModelRemoveEvent(this);
		return true;
	}

	public int hashCode() {
		return (this.id == null) ? 0 : this.id.hashCode();
	}

	public boolean equals(Object object) {
		if (object instanceof DepartmentNotification) {
			final DepartmentNotification obj = (DepartmentNotification) object;
			return (this.id != null) ? this.id.equals(obj.id)
					: (obj.id == null);
		}
		return false;
	}

	// getters and setters

	@Override
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public Notification getNotification() {
		return notification;
	}

	public void setNotification(Notification notification) {
		this.notification = notification;
	}
	
	public News getNews() {
		return news;
	}

	public void setNews(News news) {
		this.news = news;
	}
	
	public VisualItem getVisualItem() {
		return visualItem;
	}

	public void setVisualItem(VisualItem visualItem) {
		this.visualItem = visualItem;
	}
	
	public ComposedNotification getComposedNotification() {
		return composedNotification;
	}

	public void setComposedNotification(ComposedNotification composedNotification) {
		this.composedNotification = composedNotification;
	}

}
