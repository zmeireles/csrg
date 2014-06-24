package com.obc.csrg.model;

import static javax.persistence.GenerationType.SEQUENCE;
import static org.jboss.seam.ScopeType.SESSION;

import java.util.Date;
import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
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

@Entity
@Name("smsLog")
@Scope(SESSION)
@Table(name = "sms_log")
//@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
public class SmsLog extends Model implements Serializable {

	private Long id;
	private Date creationDate= new Date();
	private User sourceUser;
	private String smsSource;
	private String destinationCellphone = "";
	private User destinationUser;
	private String text = "";
	private boolean	sent = false;
	private Date sentDate = new Date();
	private String response;
	private boolean registration = false;
	
	//business methods
	
	@Override
	public String toString() {
		return "";
	}
	
	//getters and setters
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_user_id")
	public User getSourceUser() {
		return sourceUser;
	}
	public void setSourceUser(User sourceUser) {
		this.sourceUser = sourceUser;
	}
	
	@Override
	@SequenceGenerator(name = "generator")
	@Id
	@GeneratedValue(strategy = SEQUENCE, generator = "generator")
	@Column(name = "sms_log_id", nullable = false)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
    @Column(name = "sms_source")
	public String getSmsSource() {
		return smsSource;
	}
	public void setSmsSource(String smsSource) {
		this.smsSource = smsSource;
	}
	
	@Column(name="sent_date")
	public Date getSentDate() {
		return sentDate;
	}
	public void setSentDate(Date sentDate) {
		this.sentDate = sentDate;
	}
	
	@Length(max=160)
	public String getText() {
		return text;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public boolean isSent() {
		return sent;
	}
	
	public void setSent(boolean sent) {
		this.sent = sent;
	}
	
	@Column(name="creation_date")
	public Date getCreationDate() {
		return creationDate;
	}
	
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	
	@Length(max=255)
	@Column(name="dest_cellphone")
	public String getDestinationCellphone() {
		return destinationCellphone;
	}
	
	public void setDestinationCellphone(String destinationCellphone) {
		this.destinationCellphone = destinationCellphone;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dest_user_id")
	public User getDestinationUser() {
		return destinationUser;
	}
	
	public void setDestinationUser(User destinationUser) {
		this.destinationUser = destinationUser;
	}
	
	public boolean isRegistration() {
		return registration;
	}
	public void setRegistration(boolean registration) {
		this.registration = registration;
	}
	
	@Length(max=255)
	@Column(name="response")
	public String getResponse() {
		return response;
	}
	public void setResponse(String response) {
		this.response = response;
	}
	
}
