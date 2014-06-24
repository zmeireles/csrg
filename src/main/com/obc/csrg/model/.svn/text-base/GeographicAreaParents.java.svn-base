package com.obc.csrg.model;

import static javax.persistence.GenerationType.SEQUENCE;
import static org.jboss.seam.ScopeType.SESSION;

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
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

@Entity
@Name("geographicAreaParents")
@Scope(SESSION)
@Table(name = "geographic_area_parents")
//@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
public class GeographicAreaParents extends ParentsModel<GeographicArea> {

	private Long id;
	private GeographicArea desc;
	private GeographicArea asc;
	
	
	@Override
	@SequenceGenerator(name = "generator")
	@Id
	@GeneratedValue(strategy = SEQUENCE, generator = "generator")
	@Column(name = "geo_area_parents_id", nullable = false)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	@Override
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "desc_id")
	public GeographicArea getDesc() {
		return desc;
	}
	public void setDesc(GeographicArea desc) {
		this.desc = desc;
	}
	
	@Override
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "asc_id")
	public GeographicArea getAsc() {
		return asc;
	}
	public void setAsc(GeographicArea asc) {
		this.asc = asc;
	}
	
}
