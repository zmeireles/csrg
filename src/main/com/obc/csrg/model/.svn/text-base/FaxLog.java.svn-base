package com.obc.csrg.model;

import static javax.persistence.GenerationType.SEQUENCE;
import static org.jboss.seam.ScopeType.SESSION;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
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

@Entity
@Name("faxLog")
@Scope(SESSION)
@Table(name = "fax_log")
//@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
public class FaxLog extends Model implements Serializable {
	
	private Long id;
	private Date creationDate = new Date();
	private Person toPerson;
	private String fromName;
	private String fromFaxNumber;
	private String toName;
	private String toFaxNumber;
	private Date sentDate;
	private boolean	sent = false;
	private boolean success = false; 
	private String response="";
	private boolean retry = false;
	private Integer retryAptents = 5; //max number of retries to send the fax
	private Set<FaxAttachment> attachments = new HashSet<FaxAttachment>(0);
	
	
	@Override
	@SequenceGenerator(name = "generator")
	@Id
	@GeneratedValue(strategy = SEQUENCE, generator = "generator")
	@Column(name = "fax_log_id", nullable = false)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_person_id")
	public Person getToPerson() {
		return toPerson;
	}
	public void setToPerson(Person toUtente) {
		this.toPerson= toUtente;
	}
	
	@Length(max=255)
	@Column(name="from_name")
	public String getFromName() {
		return fromName;
	}
	public void setFromName(String fromName) {
		this.fromName = fromName;
	}

	@Length(max=255)
	@Column(name="to_name")
	public String getToName() {
		return toName;
	}
	public void setToName(String toName) {
		this.toName = toName;
	}

	public boolean isRetry() {
		return retry;
	}
	public void setRetry(boolean retry) {
		this.retry = retry;
	}
	
	@Column(name="retry_aptents")
	public Integer getRetryAptents() {
		return retryAptents;
	}
	public void setRetryAptents(Integer retryAptents) {
		this.retryAptents = retryAptents;
	}
	
	@Column(name="creation_date")
	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	
	@Column(name="from_fax_number")
	public String getFromFaxNumber() {
		return fromFaxNumber;
	}
	public void setFromFaxNumber(String fromFaxNumber) {
		this.fromFaxNumber = fromFaxNumber;
	}
	
	@Column(name="to_fax_number")
	public String getToFaxNumber() {
		return toFaxNumber;
	}
	public void setToFaxNumber(String toFaxNumber) {
		this.toFaxNumber = toFaxNumber;
	}
	
	@Column(name="sent_date")
	public Date getSentDate() {
		return sentDate;
	}
	public void setSentDate(Date sentDate) {
		this.sentDate = sentDate;
	}
	
	public boolean isSent() {
		return sent;
	}
	public void setSent(boolean sent) {
		this.sent = sent;
	}
	
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	
	@Length(max=2000)
	@Column(name = "response", columnDefinition="text")//columnDefinition="varchar2(2000 char)")
	public String getResponse() {
		return response;
	}
	public void setResponse(String response) {
		this.response = response;
	}
	
	@OneToMany(cascade={CascadeType.PERSIST,CascadeType.MERGE,CascadeType.REFRESH},fetch = FetchType.LAZY, mappedBy = "faxLog")
	@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
	public Set<FaxAttachment> getAttachments() {
		return attachments;
	}
	public void setAttachments(Set<FaxAttachment> attachments) {
		this.attachments = attachments;
	}
	
}
