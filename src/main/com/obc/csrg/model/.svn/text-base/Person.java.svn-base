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
import org.hibernate.validator.Pattern;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

import com.obc.csrg.constants.CivilStateEnum;
import com.obc.csrg.constants.GenderEnum;
import com.obc.csrg.constants.Constants;

@Entity
@Name("person")
@Scope(SESSION)
@Table(name = "person")
//@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
public class Person extends Model implements Serializable {

	@SequenceGenerator(name = "generator")
	@Id
	@GeneratedValue(strategy = SEQUENCE, generator = "generator")
	@Column(name = "person_id", nullable = false)
	private Long id;
	@OneToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE,
			CascadeType.REFRESH }, fetch = FetchType.LAZY)
	@JoinColumn(name = "user_csrg_id")
	private User user;
	private String name = "";
	private String email = "";
	private byte[] photo;
	private String city = "";
	
	@Column(name = "zip_code")
	private String zipCode = "";
	
	private String phone = "";
	
	@Length(max = 10)
	@Pattern(regex = Constants.REGEXP_COUNTRY_PHONE_PREFIX)
	@Column(name = "phone_prefix")
	private String phonePrefix = "";
	
	@Column(name = "mobile_phone")
	@Pattern(regex = Constants.REGEXP_PHONE_NUMBER)
	private String mobilePhone = "";
	
	@Length(max = 10)
	@Pattern(regex = Constants.REGEXP_COUNTRY_PHONE_PREFIX)
	@Column(name = "mob_phone_prefix")
	private String mobilePhonePrefix = "";
	
	private String fax = "";
	
	@Length(max = 10)
	@Pattern(regex = Constants.REGEXP_COUNTRY_PHONE_PREFIX)
	@Column(name = "fax_prefix")
	private String faxPrefix = "";
	
	@Column(name = "tax_number")
	private String taxNumber = "";
	
	@Column(name = "social_sec_number")
	private String socialSecurityNumber = "";
	
	private Date birthday = null;
	
	@Column(name = "civil_state")
	private CivilStateEnum civilState = CivilStateEnum.single;
	
	private GenderEnum gender = GenderEnum.male;
	
	@OneToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE,
			CascadeType.REFRESH }, fetch = FetchType.LAZY, mappedBy = "toPerson")
	//@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
	private Set<FaxLog> faxesReceived = new HashSet<FaxLog>(0);
	
	@OneToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE,
			CascadeType.REFRESH }, fetch = FetchType.LAZY, mappedBy = "toPerson")
	//@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
	private Set<EmailLog> emailsReceived = new HashSet<EmailLog>(0);
	
	@OneToOne(cascade={CascadeType.PERSIST,CascadeType.MERGE,CascadeType.REFRESH,CascadeType.REMOVE}, fetch=FetchType.LAZY)
    @JoinColumn(name="video_id")
	private DBFile video;
	
	private String address="";
	private String nib = "";
	
	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "geographic_area_id")
	private GeographicArea geographicArea;

	private String locale=Constants.DEFAULT_LOCALE;
	
	// constructors

	public Person() {
		super();
	}

	public Person(User user) {
		super();
		this.user = user;
	}

	// business functions

	public String returnFaxFullNumber(){
		return this.faxPrefix + this.fax;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	// getters and setters
	@Override
	public Long getId() {
		return id;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public byte[] getPhoto() {
		return photo;
	}

	public void setPhoto(byte[] photo) {
		this.photo = photo;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPhonePrefix() {
		return phonePrefix;
	}

	public void setPhonePrefix(String phonePrefix) {
		this.phonePrefix = phonePrefix;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public String getMobilePhonePrefix() {
		return mobilePhonePrefix;
	}

	public void setMobilePhonePrefix(String mobilePhonePrefix) {
		this.mobilePhonePrefix = mobilePhonePrefix;
	}

	public String getTaxNumber() {
		return taxNumber;
	}

	public void setTaxNumber(String taxNumber) {
		this.taxNumber = taxNumber;
	}

	public String getSocialSecurityNumber() {
		return socialSecurityNumber;
	}

	public void setSocialSecurityNumber(String socialSecurityNumber) {
		this.socialSecurityNumber = socialSecurityNumber;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public CivilStateEnum getCivilState() {
		return civilState;
	}

	public void setCivilState(CivilStateEnum civilState) {
		this.civilState = civilState;
	}

	public GenderEnum getGender() {
		return gender;
	}

	public void setGender(GenderEnum gender) {
		this.gender = gender;
	}

	public Set<FaxLog> getFaxesReceived() {
		return faxesReceived;
	}

	public void setFaxesReceived(Set<FaxLog> faxesReceived) {
		this.faxesReceived = faxesReceived;
	}
	
	public Set<EmailLog> getEmailsReceived() {
		return emailsReceived;
	}

	public void setEmailsReceived(Set<EmailLog> emailsReceived) {
		this.emailsReceived = emailsReceived;
	}

	public DBFile getVideo() {
		return video;
	}

	public void setVideo(DBFile video) {
		this.video = video;
	}
	
	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getFaxPrefix() {
		return faxPrefix;
	}

	public void setFaxPrefix(String faxPrefix) {
		this.faxPrefix = faxPrefix;
	}
	
	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}
	
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getNib() {
		return nib;
	}

	public void setNib(String nib) {
		this.nib = nib;
	}
	
	public GeographicArea getGeographicArea() {
		return geographicArea;
	}

	public void setGeographicArea(GeographicArea geographicArea) {
		this.geographicArea = geographicArea;
	}
}
