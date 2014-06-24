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
@Name("departmentArea")
@Scope(SESSION)
@Table(name = "department_area")
//@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
public class DepartmentArea extends Model implements Serializable {

	@SequenceGenerator(name = "generator")
	@Id
	@GeneratedValue(strategy = SEQUENCE, generator = "generator")
	@Column(name = "department_area_id", nullable = false)
	private Long id;
	
	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "service_area_id")
	private ServiceArea serviceArea;

	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "department_id")
	private Department department;

	//constructors
	public DepartmentArea() {
	}

	//business functions
	
	@Override
	public String toString() {
		return "";
	}
	
	@Override
	public boolean remove(EntityManager entityManager){
		ModelEventQueue.fireOnBeforeModelRemoveEvent(this);
		this.department.getServiceAreas().remove(this);
		this.department =null;
		this.serviceArea.getDepartments().remove(this);
		this.serviceArea=null;
		
		entityManager.remove(this);
		ModelEventQueue.fireOnAfterModelRemoveEvent(this);
		return true;
	}
	
	public int hashCode() {
		return (this.id == null) ? 0 : this.id.hashCode();
	}

	public boolean equals(Object object) {
		if (object instanceof DepartmentArea) {
			final DepartmentArea obj = (DepartmentArea) object;
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

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public ServiceArea getServiceArea() {
		return serviceArea;
	}

	public void setServiceArea(ServiceArea serviceArea) {
		this.serviceArea = serviceArea;
	}
	
}
