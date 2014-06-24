package com.obc.csrg.model;

import static javax.persistence.GenerationType.SEQUENCE;
import static org.jboss.seam.ScopeType.SESSION;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import java.io.Serializable;

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
import java.text.SimpleDateFormat;

import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

import com.obc.csrg.constants.Constants;
import com.obc.csrg.events.ModelEventQueue;

@Entity
@Name("news")
@Scope(SESSION)
@Table(name = "news")
//@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
public class News extends Model implements Serializable {

	@SequenceGenerator(name = "generator")
	@Id
	@GeneratedValue(strategy = SEQUENCE, generator = "generator")
	@Column(name = "news_id", nullable = false)
	private Long id;

	@Column(name = "creation_date")
	private Date creationDate = new Date();

	private boolean active = true;

	private String title = "";

	@Column(name = "html", columnDefinition = "text")
	private String html = "";

	private String synopsis = "";// text to be showed in the news list

	private boolean event;

	@Column(name = "event_start")
	private Date eventStart;

	@Column(name = "event_end")
	private Date eventEnd;

	@Column(name = "begin_date")
	private Date beginDate = new Date(); // start publishing

	@Column(name = "end_date")
	private Date endDate = new Date(); // end publishing

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "created_by_id")
	private User createdBy;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "approved_by_id")
	private User approvedBy;

	private boolean approved = false;

	private byte[] thumbnail;

	private byte[] image;

	private boolean highlight = false;
	
	@OneToMany(cascade = {CascadeType.MERGE,CascadeType.REFRESH }, fetch = FetchType.LAZY, mappedBy = "news")
	//@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
	private Set<DepartmentNotification> departments = new HashSet<DepartmentNotification>(
			0);
	
	@OneToMany(cascade = {CascadeType.MERGE,CascadeType.REFRESH }, fetch = FetchType.LAZY, mappedBy = "news")
	//@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
	private Set<ProfCategoryNotification> profCategories = new HashSet<ProfCategoryNotification>(
			0);
	
	@OneToMany(cascade = {CascadeType.MERGE,CascadeType.REFRESH }, fetch = FetchType.LAZY, mappedBy = "news")
	//@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
	private Set<ServiceAreaNotification> serviceAreas = new HashSet<ServiceAreaNotification>(
			0);
	
	@OneToMany(cascade = {CascadeType.MERGE,CascadeType.REFRESH }, fetch = FetchType.LAZY, mappedBy = "news")
	//@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
	private Set<ServiceClassificationNotification> serviceClassifications = new HashSet<ServiceClassificationNotification>(
			0);
	
	@OneToMany(cascade = {CascadeType.MERGE,CascadeType.REFRESH }, fetch = FetchType.LAZY, mappedBy = "news")
	//@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
	private Set<UserNotification> users = new HashSet<UserNotification>(
			0);

	@OneToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE,
			CascadeType.REFRESH, CascadeType.REMOVE }, fetch = FetchType.EAGER)
	@JoinColumn(name = "notification_id")
	private Notification notification;

	private String locale=Constants.DEFAULT_LOCALE;
	
	// constructors
	public News() {
	}

	// business logic
	public String returnNewsDate(){
		Date date;
		if(this.isEvent())
			date = this.eventStart;
		else
			date = this.beginDate;
		SimpleDateFormat df = new SimpleDateFormat(Constants.DT_FORMAT_GLOBAL);
		if(date==null)
			date= new Date();
		return df.format(date);
	}
	
	@Override
	public boolean remove(EntityManager entityManager) {
		ModelEventQueue.fireOnBeforeModelRemoveEvent(this);
		//remove relation to user
		if(this.createdBy!=null){
			this.createdBy.getCreatedNews().remove(this);
			this.createdBy =null;
		}
		// remove departments relation
		for (DepartmentNotification d : this.departments) {
			d.setNews(null);
			if(d.getNotification()!=null){
				entityManager.merge(d);
			}else{
				d.getDepartment().getNotifications().remove(d);
				d.setDepartment(null);
				entityManager.remove(d);
			}
		}
		this.departments.clear();
		//remove professional categories relation
		for(ProfCategoryNotification p:this.profCategories){
			p.setNews(null);
			if(p.getNotification()!=null){
				entityManager.merge(p);
			}else{
				p.getProfCategory().getNotifications().remove(p);
				p.setProfCategory(null);
				entityManager.remove(p);
			}
		}
		this.profCategories.clear();
		//remove service areas relation
		for(ServiceAreaNotification s:this.serviceAreas){
			s.setNews(null);
			if(s.getNotification()!=null){
				entityManager.merge(s);
			}else{
				s.getServiceArea().getNotifications().remove(s);
				s.setServiceArea(null);
				entityManager.remove(s);
			}
		}
		this.serviceAreas.clear();
		//remove service classification relation
		for(ServiceClassificationNotification s:this.serviceClassifications){
			s.setNews(null);
			if(s.getNotification()!=null){
				entityManager.merge(s);
			}else{
				s.getServiceClassification().getNotifications().remove(s);
				s.setServiceClassification(null);
				entityManager.remove(s);
			}
		}
		this.serviceClassifications.clear();
		//remove users relation
		for(UserNotification u:this.users){
			u.setNews(null);
			if(u.getNotification()!=null){
				entityManager.merge(u);
			}else{
				u.getUser().getNotifications().remove(u);
				u.setUser(null);
				entityManager.remove(u);
			}
		}
		this.users.clear();
		//check for notification relation
		if(this.notification!=null){
			this.notification.setNews(null);
			entityManager.merge(this.notification);
			this.setNotification(null);
		}
		
		entityManager.remove(this);
		ModelEventQueue.fireOnAfterModelRemoveEvent(this);
		return true;
	}

	public int hashCode() {
		return (this.id == null) ? 0 : this.id.hashCode();
	}

	public boolean equals(Object object) {
		if (object instanceof News) {
			final News obj = (News) object;
			return (this.id != null) ? this.id.equals(obj.id)
					: (obj.id == null);
		}
		return false;
	}
	@Override
	public String toString() {
		return this.title;
	}

	// getters and setters
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getHtml() {
		return html;
	}

	public void setHtml(String html) {
		this.html = html;
	}

	public String getSynopsis() {
		return synopsis;
	}

	public void setSynopsis(String synopsis) {
		this.synopsis = synopsis;
	}

	public boolean isEvent() {
		return event;
	}

	public void setEvent(boolean event) {
		this.event = event;
	}

	public Date getEventStart() {
		return eventStart;
	}

	public void setEventStart(Date eventStart) {
		this.eventStart = eventStart;
	}

	public Date getEventEnd() {
		return eventEnd;
	}

	public void setEventEnd(Date eventEnd) {
		this.eventEnd = eventEnd;
	}

	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public User getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(User createdBy) {
		this.createdBy = createdBy;
	}

	public User getApprovedBy() {
		return approvedBy;
	}

	public void setApprovedBy(User approvedBy) {
		this.approvedBy = approvedBy;
	}

	public boolean isApproved() {
		return approved;
	}

	public void setApproved(boolean approved) {
		this.approved = approved;
	}

	public byte[] getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(byte[] thumbnail) {
		this.thumbnail = thumbnail;
	}

	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}

	public boolean isHighlight() {
		return highlight;
	}

	public void setHighlight(boolean highlight) {
		this.highlight = highlight;
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
	
	public Notification getNotification() {
		return notification;
	}

	public void setNotification(Notification notification) {
		this.notification = notification;
	}
	
	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}
}
