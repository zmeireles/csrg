package com.obc.csrg.model;

import static javax.persistence.GenerationType.SEQUENCE;
import static org.jboss.seam.ScopeType.SESSION;

import java.io.Serializable;
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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.Length;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

import com.obc.csrg.events.ModelEventQueue;

@Entity
@Name("sessionLog")
@Scope(SESSION)
@Table(name = "session_log")
//@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
public class SessionLog extends Model implements Serializable {

	@SequenceGenerator(name = "generator")
	@Id
	@GeneratedValue(strategy = SEQUENCE, generator = "generator")
	@Column(name = "session_log_id", nullable = false)
	private Long id;
	
	@Column(name = "begin_date")
	private Date beginDate = new Date();
	
	@Column(name = "end_date")
	private Date endDate = null;
	
	@Length(max = 255)
	@Column(name = "my_ip")
	private String myIP = "";
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id")
	private User user;
	
	@Length(max = 255)
	@Column(name = "user_agent")
	private String userAgent = "";
	
	@Length(max = 255)
	@Column(name = "accept_language")
	private String acceptLanguage = "";
	
	@Column(name = "page_count")
	private Long pageCount = new Long(0);
	
	@Length(max = 255)
	@Column(name = "state_bean_signature")
	private String stateBeanSignature = "";

	/*
	 * BUSINESS LOGIC
	 */

	public void pageHit() {
		this.pageCount++;
	}
	
	@Override
	public String toString() {
		return "";
	}
	
	@Override
	public boolean remove(EntityManager entityManager){
		ModelEventQueue.fireOnBeforeModelRemoveEvent(this);
		
		if(this.user!=null){
			user.getSessionLog().remove(this);
		}
		
		entityManager.remove(this);
		ModelEventQueue.fireOnAfterModelRemoveEvent(this);
		return true;
	}
	// getters and setters
	@Override
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getMyIP() {
		return myIP;
	}

	public void setMyIP(String myIP) {
		this.myIP = myIP;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getUserAgent() {
		return userAgent;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	public String getAcceptLanguage() {
		return acceptLanguage;
	}

	public void setAcceptLanguage(String acceptLanguage) {
		this.acceptLanguage = acceptLanguage;
	}

	public Long getPageCount() {
		return pageCount;
	}

	public void setPageCount(Long pageCount) {
		this.pageCount = pageCount;
	}

	public String getStateBeanSignature() {
		return stateBeanSignature;
	}

	public void setStateBeanSignature(String stateBeanSignature) {
		this.stateBeanSignature = stateBeanSignature;
	}

}
