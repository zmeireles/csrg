package com.obc.csrg.model;

import static javax.persistence.GenerationType.SEQUENCE;
import static org.jboss.seam.ScopeType.SESSION;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.Length;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

import com.obc.csrg.constants.Constants;
import com.obc.csrg.events.ModelEventQueue;

@Entity
@Name("mediaManager")
@Scope(SESSION)
@Table(name = "media_manager")
public class MediaManager extends Model implements Serializable{
	
	private Long id = 0L;
	private String fileName="";
	private String fileType="";
	private String userName = "";
	private Date uploadDate;
	private String description="";
	
	@Override
	@SequenceGenerator(name = "generator")
	@Id
	@GeneratedValue(strategy = SEQUENCE, generator = "generator")
	@Column(name = "media_manager_id", nullable = false)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	@Length(max = 255)
	@Column(name = "file_name")
	public String getFileName() {
		return fileName;
	}
	
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	
	@Length(max = 255)
	@Column(name = "file_type")
	public String getFileType() {
		return fileType;
	}
	
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	@Length(max = 255)
	@Column(name = "user_name")
	public String getUserName() {
		return userName;
	}
	
	public void setUploadDate(Date uploadDate) {
		this.uploadDate = uploadDate;
	}
	
	@Length(max = 255)
	@Column(name = "upload_date")
	public Date getUploadDate() {
		return uploadDate;
	}

	
	public void setDescription(String description) {
		this.description = description;
	}
	
	@Length(max = 255)
	@Column(name = "description")
	public String getDescription() {
		return description;
	}

}
