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
@Name("serviceClassificationNotification")
@Scope(SESSION)
@Table(name = "service_class_notification")
//@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
public class ServiceClassificationNotification extends Model implements
		Serializable {

	@SequenceGenerator(name = "generator")
	@Id
	@GeneratedValue(strategy = SEQUENCE, generator = "generator")
	@Column(name = "serv_class_notif_id", nullable = false)
	private Long id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "notification_id")
	private Notification notification;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "service_class_id")
	private ServiceClassification serviceClassification;
	
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
	public ServiceClassificationNotification() {
	}

	// business functions

	@Override
	public String toString() {
		return "";//this.user.toString() + ":" + this.notification.toString();
	}
	
	@Override
	public boolean remove(EntityManager entityManager) {
		ModelEventQueue.fireOnBeforeModelRemoveEvent(this);
		this.serviceClassification.getNotifications().remove(this);
		this.serviceClassification = null;
		if(this.notification!=null){
			this.notification.getServiceClassifications().remove(this);
			this.notification = null;
		}
		if(this.news!=null){
			this.news.getServiceClassifications().remove(this);
			this.news = null;
		}
		if(this.visualItem!=null){
			this.visualItem.getServiceClassifications().remove(this);
			this.visualItem=null;
		}
		if(this.composedNotification!=null){
			this.composedNotification.getServiceClassifications().remove(this);
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
		if (object instanceof ServiceClassificationNotification) {
			final ServiceClassificationNotification obj = (ServiceClassificationNotification) object;
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

	public Notification getNotification() {
		return notification;
	}

	public void setNotification(Notification notification) {
		this.notification = notification;
	}

	public ServiceClassification getServiceClassification() {
		return serviceClassification;
	}

	public void setServiceClassification(
			ServiceClassification serviceClassification) {
		this.serviceClassification = serviceClassification;
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
