package com.obc.csrg.model;

import static javax.persistence.GenerationType.SEQUENCE;
import static org.jboss.seam.ScopeType.SESSION;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.Length;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

@Entity
@Name("accessLogExclusion")
@Scope(SESSION)
@Table(name = "access_log_exclusion")
//@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
public class AccessLogExclusion extends Model implements Serializable {

	private Long id;
	private int ipClassAInitial = 0;
	private int ipClassBInitial = 0;
	private int ipClassCInitial = 0;
	private int ipClassDInitial = 0;
	private int ipClassAFinal = 0;
	private int ipClassBFinal = 0;
	private int ipClassCFinal = 0;
	private int ipClassDFinal = 0;
	private String description = "";
	private String domain = "";
	
	
	@Override
	@SequenceGenerator(name = "generator")
	@Id
	@GeneratedValue(strategy = SEQUENCE, generator = "generator")
	@Column(name = "access_log_exclusion_id", nullable = false)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name = "ip_class_a_initial")
	public int getIpClassAInitial() {
		return ipClassAInitial;
	}
	public void setIpClassAInitial(int ipClassAInitial) {
		this.ipClassAInitial = ipClassAInitial;
	}
	
	@Column(name = "ip_class_b_initial")
	public int getIpClassBInitial() {
		return ipClassBInitial;
	}
	public void setIpClassBInitial(int ipClassBInitial) {
		this.ipClassBInitial = ipClassBInitial;
	}
	
	@Column(name = "ip_class_c_initial")
	public int getIpClassCInitial() {
		return ipClassCInitial;
	}
	public void setIpClassCInitial(int ipClassCInitial) {
		this.ipClassCInitial = ipClassCInitial;
	}
	
	@Column(name = "ip_class_d_initial")
	public int getIpClassDInitial() {
		return ipClassDInitial;
	}
	public void setIpClassDInitial(int ipClassDInitial) {
		this.ipClassDInitial = ipClassDInitial;
	}
	
	@Column(name = "ip_class_a_final")
	public int getIpClassAFinal() {
		return ipClassAFinal;
	}
	public void setIpClassAFinal(int ipClassAFinal) {
		this.ipClassAFinal = ipClassAFinal;
	}
	
	@Column(name = "ip_class_b_final")
	public int getIpClassBFinal() {
		return ipClassBFinal;
	}
	public void setIpClassBFinal(int ipClassBFinal) {
		this.ipClassBFinal = ipClassBFinal;
	}
	
	@Column(name = "ip_class_c_final")
	public int getIpClassCFinal() {
		return ipClassCFinal;
	}
	public void setIpClassCFinal(int ipClassCFinal) {
		this.ipClassCFinal = ipClassCFinal;
	}
	
	@Column(name = "ip_class_d_final")
	public int getIpClassDFinal() {
		return ipClassDFinal;
	}
	public void setIpClassDFinal(int ipClassDFinal) {
		this.ipClassDFinal = ipClassDFinal;
	}
	
	@Length(max=255)
	@Column(name = "description")
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	@Length(max=255)
	@Column(name = "domain")
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}
	
	
	
	/*
	 * business functions
	 */
	
	public String obterIPInicial(){
		if(!(this.ipClassAInitial==0 && this.ipClassBInitial==0 && this.ipClassCInitial==0 && this.ipClassDInitial==0)){
			return this.ipClassAInitial+"."+this.ipClassBInitial+"."+this.ipClassCInitial+"."+this.ipClassDInitial;
		}else{
			return "";
		}
	}
	
	public String obterIPFinal(){
		if(!(this.ipClassAFinal==0 && this.ipClassBFinal==0 && this.ipClassCFinal==0 && this.ipClassDFinal==0)){
			return this.ipClassAFinal+"."+this.ipClassBFinal+"."+this.ipClassCFinal+"."+this.ipClassDFinal;
		}else{
			return "";
		}
	}
	
}
