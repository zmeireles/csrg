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
@Name("userNotification")
@Scope(SESSION)
@Table(name = "user_notification")
//@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
public class UserNotification extends Model implements Serializable {

	@SequenceGenerator(name = "generator")
	@Id
	@GeneratedValue(strategy = SEQUENCE, generator = "generator")
	@Column(name = "user_notification_id", nullable = false)
	private Long id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "notification_id")
	private Notification notification;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "news_id")
	private News news;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "vi_id")
	private VisualItem visualItem;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "comp_notification_id")
	private ComposedNotification composedNotification;

	private boolean sent = false;
	private boolean showned = false;
	private boolean read = false;
	private boolean calculated = true;//if true means that it follow filter rules. if false it's a direct input

	// constructors
	public UserNotification() {
	}

	// business functions

	@Override
	public boolean remove(EntityManager entityManager) {
		ModelEventQueue.fireOnBeforeModelRemoveEvent(this);
		
		this.user.getNotifications().remove(this);
		this.user = null;
		if(this.notification!=null){
			this.notification.getUsers().remove(this);
			this.notification = null;
		}
		if(this.news!=null){
			this.news.getUsers().remove(this);
			this.news = null;
		}
		if(this.visualItem!=null){
			this.visualItem.getUsers().remove(this);
			this.visualItem=null;
		}
		if(this.composedNotification!=null){
			this.composedNotification.getUsers().remove(this);
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
		if (object instanceof UserNotification) {
			final UserNotification obj = (UserNotification) object;
			return (this.id != null) ? this.id.equals(obj.id)
					: (obj.id == null);
		}
		return false;
	}

	@Override
	public String toString() {
		return user.toString();//this.user.toString() + ":" + this.notification.toString();
	}
	// getters and setters

	@Override
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Notification getNotification() {
		return notification;
	}

	public void setNotification(Notification notification) {
		this.notification = notification;
	}

	public boolean isSent() {
		return sent;
	}

	public void setSent(boolean sent) {
		this.sent = sent;
	}

	public boolean isShowned() {
		return showned;
	}

	public void setShowned(boolean showned) {
		this.showned = showned;
	}

	public boolean isRead() {
		return read;
	}

	public void setRead(boolean read) {
		this.read = read;
	}

	public boolean isCalculated() {
		return calculated;
	}

	public void setCalculated(boolean calculated) {
		this.calculated = calculated;
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
