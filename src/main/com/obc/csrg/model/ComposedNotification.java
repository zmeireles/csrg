package com.obc.csrg.model;

import static javax.persistence.GenerationType.SEQUENCE;
import static org.jboss.seam.ScopeType.SESSION;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Date;

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
import org.hibernate.validator.Length;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

import com.obc.csrg.constants.Constants;
import com.obc.csrg.constants.DBCronTaskEnum;
import com.obc.csrg.constants.NotificationMediaEnum;
import com.obc.csrg.events.ModelEventQueue;

@Entity
@Name("composedNotification")
@Scope(SESSION)
@Table(name = "comp_notification")
//@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
public class ComposedNotification extends Model implements Serializable {

	@SequenceGenerator(name = "generator")
	@Id
	@GeneratedValue(strategy = SEQUENCE, generator = "generator")
	@Column(name = "comp_notification_id", nullable = false)
	private Long id;

	private String subject = "";
	
	@OneToMany(cascade = { CascadeType.MERGE,
			CascadeType.REFRESH }, fetch = FetchType.LAZY, mappedBy = "composedNotification")
	//@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
	private Set<Notification> notifications = new HashSet<Notification>(0);

	@Column(name = "send_at")
	private Date sendAt = new Date(); // day and hour to send notification
	
	@Column(name = "end_at")
	private Date endAt; //after this day and hour notification won't be sent anymore

	@Column(name = "ready_to_send")
	private boolean readyToSend = false;// to avoid creating a notification and
										// updating without finishing all data

	@Column(name = "creation_date")
	private Date creationDate = new Date();

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "sender_id")
	private User sender;
	
	@OneToMany(cascade = { CascadeType.MERGE,
			CascadeType.REFRESH  }, fetch = FetchType.LAZY, mappedBy = "composedNotification")
	//@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
	private Set<DepartmentNotification> departments = new HashSet<DepartmentNotification>(
			0);
	
	@OneToMany(cascade = { CascadeType.MERGE,
			CascadeType.REFRESH  }, fetch = FetchType.LAZY, mappedBy = "composedNotification")
	//@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
	private Set<ProfCategoryNotification> profCategories = new HashSet<ProfCategoryNotification>(
			0);
	
	@OneToMany(cascade = { CascadeType.MERGE,
			CascadeType.REFRESH  }, fetch = FetchType.LAZY, mappedBy = "composedNotification")
	//@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
	private Set<ServiceAreaNotification> serviceAreas = new HashSet<ServiceAreaNotification>(
			0);
	
	@OneToMany(cascade = { CascadeType.MERGE,
			CascadeType.REFRESH  }, fetch = FetchType.LAZY, mappedBy = "composedNotification")
	//@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
	private Set<ServiceClassificationNotification> serviceClassifications = new HashSet<ServiceClassificationNotification>(
			0);
	
	@OneToMany(cascade = { CascadeType.MERGE,
			CascadeType.REFRESH }, fetch = FetchType.LAZY, mappedBy = "composedNotification")
	//@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
	private Set<UserNotification> users = new HashSet<UserNotification>(0);
	
	@Column(name = "keep_alive")
	private boolean keepAlive = false;// if true then dynamically adds/removes

	// new users as they fit in the filters
	private String locale=Constants.DEFAULT_LOCALE;
	
	// constructors
	public ComposedNotification() {
	}

	// business functions

	@Override
	public boolean remove(EntityManager entityManager) {
		ModelEventQueue.fireOnBeforeModelRemoveEvent(this);
		//Notification -> News
		//order here is important
		
		//since news was already removed I can't pay with it anymore. All relations with news are broken.
		
		// remove notifications
		for (Notification n : this.getNotifications()) {
			n.setComposedNotification(null);
			if(n.getVisualItem()!=null){
				entityManager.merge(n);
			}else{
				n.remove(entityManager);
			}
		}
		this.notifications.clear();
		
		//remove users relation
		for(UserNotification u:this.users){
			u.setComposedNotification(null);
			entityManager.remove(u);
		}
		this.users.clear();
		
		// remove departments relation
		for (DepartmentNotification d : this.departments) {
			d.setComposedNotification(null);
			entityManager.remove(d);
		}
		this.departments.clear();
		
		// remove profCategories relation
		for (ProfCategoryNotification p : this.profCategories) {
			p.setComposedNotification(null);
			entityManager.remove(p);
		}
		this.profCategories.clear();
		
		// remove service classifications relation
		for (ServiceClassificationNotification s : this.serviceClassifications) {
			s.setComposedNotification(null);
			entityManager.remove(s);
		}
		this.serviceClassifications.clear();
		
		// remove service areas relation
		for (ServiceAreaNotification s : this.serviceAreas) {
			s.setComposedNotification(null);
			entityManager.remove(s);
		}
		this.serviceAreas.clear();
		
		// remove sender relation
		if (this.sender != null) {
			this.sender.getComposedNotificationsSent().remove(this);
			this.sender = null;
		}
				
		entityManager.remove(this);

		ModelEventQueue.fireOnAfterModelRemoveEvent(this);
		return true;
	}

	public int hashCode() {
		return (this.id == null) ? 0 : this.id.hashCode();
	}

	public boolean equals(Object object) {
		if (object instanceof ComposedNotification) {
			final ComposedNotification obj = (ComposedNotification) object;
			return (this.id != null) ? this.id.equals(obj.id)
					: (obj.id == null);
		}
		return false;
	}

	@Override
	public String toString() {
		return this.subject;
	}
	// getters and setters

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public Date getSendAt() {
		return sendAt;
	}

	public void setSendAt(Date sendAt) {
		this.sendAt = sendAt;
	}

	public boolean isReadyToSend() {
		return readyToSend;
	}

	public void setReadyToSend(boolean readyToSend) {
		this.readyToSend = readyToSend;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public User getSender() {
		return sender;
	}

	public void setSender(User sender) {
		this.sender = sender;
	}

	public Date getEndAt() {
		return endAt;
	}

	public void setEndAt(Date endAt) {
		this.endAt = endAt;
	}
	
	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}
	
	public Set<Notification> getNotifications() {
		return notifications;
	}

	public void setNotifications(Set<Notification> notifications) {
		this.notifications = notifications;
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
	
	public boolean isKeepAlive() {
		return keepAlive;
	}

	public void setKeepAlive(boolean keepAlive) {
		this.keepAlive = keepAlive;
	}
}
