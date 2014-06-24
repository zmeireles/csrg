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
@Name("notification")
@Scope(SESSION)
@Table(name = "notification")
//@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
public class Notification extends Model implements Serializable {

	@SequenceGenerator(name = "generator")
	@Id
	@GeneratedValue(strategy = SEQUENCE, generator = "generator")
	@Column(name = "notification_id", nullable = false)
	private Long id;

	private String subject = "";

	@Length(max = 2000)
	@Column(name = "message", columnDefinition = "text")
	private String message = "";

	@OneToMany(cascade = { CascadeType.MERGE,
			CascadeType.REFRESH }, fetch = FetchType.LAZY, mappedBy = "notification")
	//@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
	private Set<UserNotification> users = new HashSet<UserNotification>(0);

	@OneToMany(cascade = { CascadeType.MERGE,
			CascadeType.REFRESH  }, fetch = FetchType.LAZY, mappedBy = "notification")
	//@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
	private Set<DepartmentNotification> departments = new HashSet<DepartmentNotification>(
			0);

	@OneToMany(cascade = { CascadeType.MERGE,
			CascadeType.REFRESH  }, fetch = FetchType.LAZY, mappedBy = "notification")
	//@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
	private Set<ProfCategoryNotification> profCategories = new HashSet<ProfCategoryNotification>(
			0);

	@OneToMany(cascade = { CascadeType.MERGE,
			CascadeType.REFRESH  }, fetch = FetchType.LAZY, mappedBy = "notification")
	//@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
	private Set<ServiceClassificationNotification> serviceClassifications = new HashSet<ServiceClassificationNotification>(
			0);

	@OneToMany(cascade = { CascadeType.MERGE,
			CascadeType.REFRESH  }, fetch = FetchType.LAZY, mappedBy = "notification")
	//@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
	private Set<ServiceAreaNotification> serviceAreas = new HashSet<ServiceAreaNotification>(
			0);

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
	
	@Column(name = "keep_alive")
	private boolean keepAlive = false;// if true then dynamically adds/removes
										// new users as they fit in the filters
	@Column(name = "users_notif_media")
	private NotificationMediaEnum usersNotificationMedia = NotificationMediaEnum.email;

	@Column(name = "dep_notif_media")
	private NotificationMediaEnum departmentsNotificationMedia = NotificationMediaEnum.none;

	@OneToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE,
			CascadeType.REFRESH, CascadeType.REMOVE }, fetch = FetchType.EAGER)
	@JoinColumn(name = "news_id")
	private News news;
	
	@OneToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE,
			CascadeType.REFRESH, CascadeType.REMOVE }, fetch = FetchType.EAGER)
	@JoinColumn(name = "vi_id")
	private VisualItem visualItem;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "comp_notification_id")
	private ComposedNotification composedNotification;
	
	private String locale=Constants.DEFAULT_LOCALE;
	
	// constructors
	public Notification() {
	}

	// business functions

	public DBCronTask returnExistingTask(EntityManager entityManager){
		List<DBCronTask> tasks = entityManager.createQuery("select m from DBCronTask m where " +
				"m.task=? and m.entityId is not null and m.entityId=?")
				.setParameter(1, DBCronTaskEnum.ADD_NOTIFICATION)
				.setParameter(2, this.id)
				.getResultList();
		if(tasks.size()>0){
			DBCronTask task = tasks.get(0);
			return task;
		}
		return null;
	}
	
	@Override
	public boolean remove(EntityManager entityManager) {
		ModelEventQueue.fireOnBeforeModelRemoveEvent(this);
		//Notification -> News
		//order here is important
		
		if(this.news!=null){
			this.news.remove(entityManager);
			this.setNews(null);
		}
		
		//since news was already removed I can't pay with it anymore. All relations with news are broken.
		
		// remove users relation
		for (UserNotification u : this.getUsers()) {
			u.setNotification(null);
			if(u.getVisualItem()!=null){
				entityManager.merge(u);
			}else{
				u.getUser().getNotifications().remove(u);
				u.setUser(null);
				entityManager.remove(u);
			}
		}
		this.users.clear();
		// remove departments relation
		for (DepartmentNotification d : this.departments) {
			d.setNotification(null);
			if(d.getVisualItem()!=null){
				entityManager.merge(d);
			}else{
				d.getDepartment().getNotifications().remove(d);
				d.setDepartment(null);
				entityManager.remove(d);
			}
		}
		this.departments.clear();
		// remove profCategories relation
		for (ProfCategoryNotification p : this.profCategories) {
			p.setNotification(null);
			if(p.getVisualItem()!=null){
				entityManager.merge(p);
			}else{
				p.getProfCategory().getNotifications().remove(p);
				p.setProfCategory(null);
				entityManager.remove(p);
			}
		}
		this.profCategories.clear();
		// remove service classifications relation
		for (ServiceClassificationNotification s : this.serviceClassifications) {
			s.setNotification(null);
			if(s.getVisualItem()!=null){
				entityManager.merge(s);
			}else{
				s.getServiceClassification().getNotifications().remove(s);
				s.setServiceClassification(null);
				entityManager.remove(s);
			}
		}
		this.serviceClassifications.clear();
		// remove service areas relation
		for (ServiceAreaNotification s : this.serviceAreas) {
			s.setNotification(null);
			if(s.getVisualItem()!=null){
				entityManager.merge(s);
			}else{
				s.getServiceArea().getNotifications().remove(s);
				s.setServiceArea(null);
				entityManager.remove(s);
			}
		}
		this.serviceAreas.clear();
		// remove sender relation
		if (this.sender != null) {
			this.sender.getNotificationsSent().remove(this);
			this.sender = null;
		}
				
		if(this.visualItem!=null){
			this.visualItem.setNotification(null);
			this.visualItem = null;
		}
		
		entityManager.remove(this);

		ModelEventQueue.fireOnAfterModelRemoveEvent(this);
		return true;
	}

	public int hashCode() {
		return (this.id == null) ? 0 : this.id.hashCode();
	}

	public boolean equals(Object object) {
		if (object instanceof Notification) {
			final Notification obj = (Notification) object;
			return (this.id != null) ? this.id.equals(obj.id)
					: (obj.id == null);
		}
		return false;
	}

	@Override
	public String toString() {
		return subject;
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

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Set<UserNotification> getUsers() {
		return users;
	}

	public void setUsers(Set<UserNotification> users) {
		this.users = users;
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

	public Set<ServiceClassificationNotification> getServiceClassifications() {
		return serviceClassifications;
	}

	public void setServiceClassifications(
			Set<ServiceClassificationNotification> serviceClassifications) {
		this.serviceClassifications = serviceClassifications;
	}

	public Set<ServiceAreaNotification> getServiceAreas() {
		return serviceAreas;
	}

	public void setServiceAreas(Set<ServiceAreaNotification> serviceAreas) {
		this.serviceAreas = serviceAreas;
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

	public boolean isKeepAlive() {
		return keepAlive;
	}

	public void setKeepAlive(boolean keepAlive) {
		this.keepAlive = keepAlive;
	}

	public NotificationMediaEnum getUsersNotificationMedia() {
		return usersNotificationMedia;
	}

	public void setUsersNotificationMedia(
			NotificationMediaEnum usersNotificationMedia) {
		this.usersNotificationMedia = usersNotificationMedia;
	}

	public NotificationMediaEnum getDepartmentsNotificationMedia() {
		return departmentsNotificationMedia;
	}

	public void setDepartmentsNotificationMedia(
			NotificationMediaEnum departmentsNotificationMedia) {
		this.departmentsNotificationMedia = departmentsNotificationMedia;
	}
	
	public News getNews() {
		return news;
	}

	public void setNews(News news) {
		this.news = news;
	}
	
	public Date getEndAt() {
		return endAt;
	}

	public void setEndAt(Date endAt) {
		this.endAt = endAt;
	}
	
	public VisualItem getVisualItem() {
		return visualItem;
	}

	public void setVisualItem(VisualItem visualItem) {
		this.visualItem = visualItem;
	}
	
	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}
	
	public ComposedNotification getComposedNotification() {
		return composedNotification;
	}

	public void setComposedNotification(ComposedNotification composedNotification) {
		this.composedNotification = composedNotification;
	}
}
