package com.obc.csrg.model;

import static javax.persistence.GenerationType.SEQUENCE;
import static org.jboss.seam.ScopeType.SESSION;

import java.io.Serializable;
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
import org.hibernate.validator.Min;
import org.hibernate.validator.Max;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

@Entity
@Name("config")
@Scope(SESSION)
@Table(name = "config")
//@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
public class Config extends Model implements Serializable {

	private Long id = 0L;
	private int newRegistrationExpirationTime = 24; // hours

	private boolean generateUsername = false;
	// SMS config
	private String smsSource="";
	private String urlSms = "";
	private String usernameSms = "";
	private String passwordSms = "";
	private boolean allowCSms = true;
	private boolean sendSmsOnRegistration = false;
	// SMTP settings
	private String smtpHost = "localhost";
	private int smtpPort = 25;
	private boolean smtpAuth = false;
	private String smtpAuthUser = "";
	private String smtpAuthPwd = "";
	// emails
	private String contactEmail = "csrg@csrg.pt";
	private String contactName = "CSRG";
	private String onRegistrationSenderEmail = "csrg@csrg.pt";
	private String onRegistrationSenderName = "CSRG";
	
	private String notificationSourceEmail = "";
	private String notificationSourceName = "CSRG";

	// fax
	private String faxAccount = "";
	private String faxPwd = "";
	private String faxEmail = "";
	private boolean sendEmailOnRegistration = false;
	private String timezone="";

	//news ticker
	private String animationType = "fade"; //"slide"
	private int speed = 2000; //milliseconds 
	private int timeOut = 8000; //milliseconds 

	//backoffice top menu bar
	private int menuBarItemWidth = 200; //pixel
	private int menuItemWidth = 200; //pixel

	//visual item menu
	private int viMenuMaxWidth = 200; //pixel

	//on demand font size
	private int onDemandFontSizeMax = 16; //px
	private int onDemandFontSizeMin = 10; //px
	private int onDemandFontSizeDefault = 12; //px

	//mms
	private String uploadDirectory = "c:/csrg/user_files/"; //px
	private String allowedFileExtensions = ".jpg .bmp .doc .pdf"; //px
	private int maxFileSize = 10; //MB
	private int maxDiskSpace = 100; //MB
	
	private GeographicArea country;
	private String dataChangeNotificationEmail="";
	

	@Override
	@SequenceGenerator(name = "generator")
	@Id
	@GeneratedValue(strategy = SEQUENCE, generator = "generator")
	@Column(name = "config_id", nullable = false)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "gen_username")
	public boolean isGenerateUsername() {
		return generateUsername;
	}

	public void setGenerateUsername(boolean generateUsername) {
		this.generateUsername = generateUsername;
	}

	@Length(max = 255)
	@Column(name = "url_sms")
	public String getUrlSms() {
		return urlSms;
	}

	public void setUrlSms(String urlSms) {
		this.urlSms = urlSms;
	}

	@Length(max = 255)
	@Column(name = "username_sms")
	public String getUsernameSms() {
		return usernameSms;
	}

	public void setUsernameSms(String usernameSms) {
		this.usernameSms = usernameSms;
	}

	@Length(max = 255)
	@Column(name = "password_sms")
	public String getPasswordSms() {
		return passwordSms;
	}

	public void setPasswordSms(String passwordSms) {
		this.passwordSms = passwordSms;
	}

	@Column(name = "allow_csms")
	public boolean isAllowCSms() {
		return allowCSms;
	}

	public void setAllowCSms(boolean allowCSms) {
		this.allowCSms = allowCSms;
	}

	@Length(max = 255)
	@Column(name = "smtp_host")
	public String getSmtpHost() {
		return smtpHost;
	}

	public void setSmtpHost(String smtpHost) {
		this.smtpHost = smtpHost;
	}

	@Column(name = "smtp_port")
	public int getSmtpPort() {
		return smtpPort;
	}

	public void setSmtpPort(int smtpPort) {
		this.smtpPort = smtpPort;
	}

	@Column(name = "smtp_auth")
	public boolean getSmtpAuth() {
		return smtpAuth;
	}

	public void setSmtpAuth(boolean smtpAuth) {
		this.smtpAuth = smtpAuth;
	}

	@Length(max = 255)
	@Column(name = "smtp_auth_user")
	public String getSmtpAuthUser() {
		return smtpAuthUser;
	}

	public void setSmtpAuthUser(String smtpAuthUser) {
		this.smtpAuthUser = smtpAuthUser;
	}

	@Length(max = 255)
	@Column(name = "smtp_auth_pwd")
	public String getSmtpAuthPwd() {
		return smtpAuthPwd;
	}

	public void setSmtpAuthPwd(String smtpAuthPwd) {
		this.smtpAuthPwd = smtpAuthPwd;
	}

	@Column(name = "fax_account")
	public String getFaxAccount() {
		return faxAccount;
	}

	public void setFaxAccount(String faxAccount) {
		this.faxAccount = faxAccount;
	}

	@Column(name = "fax_pwd")
	public String getFaxPwd() {
		return faxPwd;
	}

	public void setFaxPwd(String faxPwd) {
		this.faxPwd = faxPwd;
	}

	@Column(name = "fax_email")
	public String getFaxEmail() {
		return faxEmail;
	}

	public void setFaxEmail(String faxEmail) {
		this.faxEmail = faxEmail;
	}

	@Column(name = "new_reg_exp_time")
	public int getNewRegistrationExpirationTime() {
		return newRegistrationExpirationTime;
	}

	public void setNewRegistrationExpirationTime(
			int newRegistrationExpirationTime) {
		this.newRegistrationExpirationTime = newRegistrationExpirationTime;
	}

	@Column(name = "send_email_on_reg")
	public boolean isSendSmsOnRegistration() {
		return sendSmsOnRegistration;
	}

	public void setSendSmsOnRegistration(boolean sendSmsOnRegistration) {
		this.sendSmsOnRegistration = sendSmsOnRegistration;
	}

	@Column(name = "contact_email")
	public String getContactEmail() {
		return contactEmail;
	}

	public void setContactEmail(String contactEmail) {
		this.contactEmail = contactEmail;
	}

	@Column(name = "contact_name")
	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	@Column(name = "on_reg_sender_email")
	public String getOnRegistrationSenderEmail() {
		return onRegistrationSenderEmail;
	}

	public void setOnRegistrationSenderEmail(String onRegistrationSenderEmail) {
		this.onRegistrationSenderEmail = onRegistrationSenderEmail;
	}

	@Column(name = "on_reg_sender_name")
	public String getOnRegistrationSenderName() {
		return onRegistrationSenderName;
	}

	public void setOnRegistrationSenderName(String onRegistrationSenderName) {
		this.onRegistrationSenderName = onRegistrationSenderName;
	}

	@Column(name = "on_reg_send_email")
	public boolean isSendEmailOnRegistration() {
		return sendEmailOnRegistration;
	}

	public void setSendEmailOnRegistration(boolean sendEmailOnRegistration) {
		this.sendEmailOnRegistration = sendEmailOnRegistration;
	}
	
	@Column(name = "sms_source")
	public String getSmsSource() {
		return smsSource;
	}

	public void setSmsSource(String smsSource) {
		this.smsSource = smsSource;
	}
	
	@Column(name = "notif_source_email")
	public String getNotificationSourceEmail() {
		return notificationSourceEmail;
	}

	public void setNotificationSourceEmail(String notificationSourceEmail) {
		this.notificationSourceEmail = notificationSourceEmail;
	}

	@Column(name = "notif_source_name")
	public String getNotificationSourceName() {
		return notificationSourceName;
	}

	public void setNotificationSourceName(String notificationSourceName) {
		this.notificationSourceName = notificationSourceName;
	}
	
	public String getTimezone() {
		return timezone;
	}

	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}

	public void setAnimationType(String animationType) {
		this.animationType = animationType;
	}

	@Length(max = 255)
	@Column(name = "animation_type")
	public String getAnimationType() {
		return animationType;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	@Column(name = "speed")
	public int getSpeed() {
		
		return speed;
	}

	
	public void setTimeOut(int timeOut) {
		this.timeOut = timeOut;
	}

	@Column(name = "time_out")
	public int getTimeOut() {
		return timeOut;
	}

	@Column(name = "menu_bar_item_width")
	public int getMenuBarItemWidth() {
		return menuBarItemWidth;
	}
	
	public void setMenuBarItemWidth(int menuBarItemWidth) {
		this.menuBarItemWidth = menuBarItemWidth;
	}

	@Column(name = "menu_item_width")
	public int getMenuItemWidth() {
		return menuItemWidth;
	}
	
	public void setMenuItemWidth(int menuItemWidth) {
		this.menuItemWidth = menuItemWidth;
	}

	public void setViMenuMaxWidth(int viMenuMaxWidth) {
		this.viMenuMaxWidth = viMenuMaxWidth;
	}
	
	@Column(name = "vi_menu_max_width")
	public int getViMenuMaxWidth() {
		return viMenuMaxWidth;
	}

	public void setOnDemandFontSizeMax(int onDemandFontSizeMax) {
		this.onDemandFontSizeMax = onDemandFontSizeMax;
	}

	@Column(name = "on_demand_font_size_max")
	public int getOnDemandFontSizeMax() {
		return onDemandFontSizeMax;
	}

	public void setOnDemandFontSizeMin(int onDemandFontSizeMin) {
		this.onDemandFontSizeMin = onDemandFontSizeMin;
	}

	@Column(name = "on_demand_font_size_min")
	public int getOnDemandFontSizeMin() {
		return onDemandFontSizeMin;
	}

	public void setOnDemandFontSizeDefault(int onDemandFontSizeDefault) {
		this.onDemandFontSizeDefault = onDemandFontSizeDefault;
	}

	@Column(name = "on_demand_font_size_default")
	public int getOnDemandFontSizeDefault() {
		return onDemandFontSizeDefault;
	}


	public void setUploadDirectory(String uploadDirectory) {
		this.uploadDirectory = uploadDirectory;
	}

	@Column(name = "upload_directory")
	public String getUploadDirectory() {
		return uploadDirectory;
	}

	public void setAllowedFileExtensions(String allowedFileExtensions) {
		this.allowedFileExtensions = allowedFileExtensions;
	}

	@Column(name = "allowed_file_extensions")
	public String getAllowedFileExtensions() {
		return allowedFileExtensions;
	}

	public void setMaxFileSize(int maxFileSize) {
		this.maxFileSize = maxFileSize;
	}

	@Column(name = "max_file_size")
	public int getMaxFileSize() {
		return maxFileSize;
	}

	public void setMaxDiskSpace(int maxDiskSpace) {
		this.maxDiskSpace = maxDiskSpace;
	}

	@Column(name = "max_disk_space")
	public int getMaxDiskSpace() {
		return maxDiskSpace;
	}

	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "country_id")
	public GeographicArea getCountry() {
		return country;
	}

	public void setCountry(GeographicArea country) {
		this.country = country;
	}
	
	@Column(name = "data_change_notif_email")
	public String getDataChangeNotificationEmail() {
		return dataChangeNotificationEmail;
	}

	public void setDataChangeNotificationEmail(String dataChangeNotificationEmail) {
		this.dataChangeNotificationEmail = dataChangeNotificationEmail;
	}
}
