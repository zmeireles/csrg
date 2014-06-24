package com.obc.csrg.model;

import static javax.persistence.GenerationType.SEQUENCE;
import static org.jboss.seam.ScopeType.SESSION;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.Length;
import org.hibernate.validator.Pattern;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

import com.obc.csrg.constants.Constants;
import com.obc.csrg.events.ModelEventQueue;

@Entity
@Name("department")
@Scope(SESSION)
@Table(name = "department")
//@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
public class Department extends Model implements Serializable {

	@SequenceGenerator(name = "generator")
	@Id
	@GeneratedValue(strategy = SEQUENCE, generator = "generator")
	@Column(name = "department_id", nullable = false)
	private Long id;

	@OneToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE,
			CascadeType.REMOVE }, fetch = FetchType.LAZY, mappedBy = "department")
	//@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
	private Set<UserDepartment> users = new HashSet<UserDepartment>(0);

	private String name = "";

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "parent_department_id")
	private Department parent;

	@OneToMany(cascade = { CascadeType.MERGE,CascadeType.REFRESH
			}, fetch = FetchType.LAZY, mappedBy = "parent")
	//@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
	private Set<Department> children = new HashSet<Department>(0);

	@OneToMany(cascade = { CascadeType.MERGE,
			CascadeType.REMOVE }, fetch = FetchType.LAZY, mappedBy = "department")
	//@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
	private Set<DepartmentArea> serviceAreas = new HashSet<DepartmentArea>(0);

	@OneToMany(cascade = { CascadeType.MERGE,
			CascadeType.REMOVE }, fetch = FetchType.LAZY, mappedBy = "department")
	//@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
	private Set<DepartmentNotification> notifications = new HashSet<DepartmentNotification>(
			0);

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "service_class_id")
	private ServiceClassification serviceClassification;

	private String phone = "";

	@Length(max = 10)
	@Pattern(regex = Constants.REGEXP_COUNTRY_PHONE_PREFIX)
	@Column(name = "phone_prefix")
	private String phonePrefix = "";

	@Column(name = "mobile_phone")
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

	private String email = "";

	private String locale=Constants.DEFAULT_LOCALE;
	
	// constructors
	public Department() {
	}

	public Department(String name) {
		this.name = name;
	}

	// business functions

	public int hashCode() {
		return (this.id == null) ? 0 : this.id.hashCode();
	}

	public boolean equals(Object object) {
		if (object instanceof Department) {
			final Department obj = (Department) object;
			return (this.id != null) ? this.id.equals(obj.id)
					: (obj.id == null);
		}
		return false;
	}

	@Override
	public List<User> relatedUsers(){
		List<User> users = new ArrayList<User>();
		for(UserDepartment u:this.getUsers()){
			users.add(u.getUser());
		}
		return users;
	}
	@Override
	public boolean remove(EntityManager entityManager) {
		ModelEventQueue.fireOnBeforeModelRemoveEvent(this);
		// remove users relation
		for (UserDepartment u : this.users) {
			u.getUser().getDepartments().remove(u);
			u.setUser(null);
			u.setDepartment(null);
			entityManager.remove(u);
		}
		this.users.clear();
		// detach from parent
		if (this.parent != null) {
			// this.parent.getChildren().remove(this); this is a problematic
			// instruction (see remove children)
			this.setParent(null);
		}
		// remove children
		for (Department d : this.children) {
			d.remove(entityManager);
		}
		this.children.clear();
		// remove service areas relation
		for (DepartmentArea s : this.serviceAreas) {
			s.getServiceArea().getDepartments().remove(s);
			s.setServiceArea(null);
			s.setDepartment(null);
			entityManager.remove(s);
		}
		this.serviceAreas.clear();
		// remove notifications relation
		for (DepartmentNotification n : this.notifications) {
			n.getNotification().getDepartments().remove(n);
			n.setNotification(null);
			n.setDepartment(null);
			entityManager.remove(n);
		}
		// remove service classification relation
		if (this.serviceClassification != null) {
			this.serviceClassification.getDepartments().remove(this);
			this.setServiceClassification(null);
		}
		this.notifications.clear();
		entityManager.remove(this);
		ModelEventQueue.fireOnAfterModelRemoveEvent(this);
		return true;
	}

	@Override
	public String toString() {
		return this.name;
	}

	public Department imParentOf(Department dep) {
		for (Department d : this.children) {
			if (d.equals(dep))
				return dep;
			else {
				if (d.imParentOf(dep) != null)
					return dep;
			}
		}
		return null;
	}

	// getters and setters
	@Override
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Set<UserDepartment> getUsers() {
		return users;
	}

	public void setUsers(Set<UserDepartment> users) {
		this.users = users;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Department getParent() {
		return parent;
	}

	public void setParent(Department parent) {
		this.parent = parent;
	}

	public Set<Department> getChildren() {
		return children;
	}

	public void setChildren(Set<Department> children) {
		this.children = children;
	}

	public Set<DepartmentArea> getServiceAreas() {
		return serviceAreas;
	}

	public void setServiceAreas(Set<DepartmentArea> serviceAreas) {
		this.serviceAreas = serviceAreas;
	}

	public Set<DepartmentNotification> getNotifications() {
		return notifications;
	}

	public void setNotifications(Set<DepartmentNotification> notifications) {
		this.notifications = notifications;
	}

	public ServiceClassification getServiceClassification() {
		return serviceClassification;
	}

	public void setServiceClassification(
			ServiceClassification serviceClassification) {
		this.serviceClassification = serviceClassification;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}
	
}
