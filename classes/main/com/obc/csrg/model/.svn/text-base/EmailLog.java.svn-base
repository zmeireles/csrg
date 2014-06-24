package com.obc.csrg.model;

import static javax.persistence.GenerationType.SEQUENCE;
import static org.jboss.seam.ScopeType.SESSION;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.Length;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

@Entity
@Name("emailLog")
@Scope(SESSION)
@Table(name = "email_log")
//@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
public class EmailLog extends Model implements Serializable {

	private Long id;
	private Date creationDate = new Date();
	private Person toPerson;
	private String fromName;
	private String fromAddress;
	private String toName;
	private String toAddress;
	private String subject;
	private String text;
	private Date sentDate;
	private boolean sent;
	private String response;
	private String errorClass = "";
	private boolean retry = false;
	private Integer retryAptents = 5; // número máximo de tentativas

	@Override
	@SequenceGenerator(name = "generator")
	@Id
	@GeneratedValue(strategy = SEQUENCE, generator = "generator")
	@Column(name = "email_log_id", nullable = false)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getErrorClass() {
		return errorClass;
	}

	public void setErrorClass(String errorClass) {
		this.errorClass = errorClass;
	}

	@Column(name = "creation_date")
	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "to_person_id")
	public Person getToPerson() {
		return toPerson;
	}

	public void setToPerson(Person toPerson) {
		this.toPerson = toPerson;
	}

	@Length(max = 255)
	@Column(name = "from_name")
	public String getFromName() {
		return fromName;
	}

	public void setFromName(String fromName) {
		this.fromName = fromName;
	}

	@Length(max = 255)
	@Column(name = "from_address")
	public String getFromAddress() {
		return fromAddress;
	}

	public void setFromAddress(String fromAddress) {
		this.fromAddress = fromAddress;
	}

	@Length(max = 255)
	@Column(name = "to_name")
	public String getToName() {
		return toName;
	}

	public void setToName(String toName) {
		this.toName = toName;
	}

	@Length(max = 255)
	@Column(name = "to_address")
	public String getToAddress() {
		return toAddress;
	}

	public void setToAddress(String toAddress) {
		this.toAddress = toAddress;
	}

	@Length(max = 255)
	@Column(name = "subject")
	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	@Length(max = 2000)
	@Column(name = "text", columnDefinition = "text")
	// columnDefinition="varchar2(2000 char)")
	public String getText() {
		return text;
	}

	public void setText(String texto) {
		this.text = texto;
	}

	@Column(name = "sent_date")
	public Date getSentDate() {
		return sentDate;
	}

	public void setSentDate(Date sentDate) {
		this.sentDate = sentDate;
	}

	@Column(name = "sent")
	public boolean isSent() {
		return sent;
	}

	public void setSent(boolean sent) {
		this.sent = sent;
	}

	@Length(max = 2000)
	@Column(name = "response", columnDefinition = "text")
	// columnDefinition="varchar2(2000 char)")
	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public boolean isRetry() {
		return retry;
	}

	public void setRetry(boolean retry) {
		this.retry = retry;
	}

	@Column(name = "retry_aptents")
	public Integer getRetryAptents() {
		return retryAptents;
	}

	public void setRetryAptents(Integer retryAptents) {
		this.retryAptents = retryAptents;
	}

}
