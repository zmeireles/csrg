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
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.Length;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

import com.obc.csrg.constants.Constants;
import com.obc.csrg.constants.GeographicAreaTypeEnum;

@Entity
@Name("geographicArea")
@Scope(SESSION)
@Table(name = "geographic_area")
//@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
public class GeographicArea extends Model implements Serializable,
		Comparable<Object> {

	private Long id = 0L;
	private String name = "";
	private String shortName = "";
	private String description = "";
	private byte[] image;
	private String url;
	private String countryPhonePrefix = "";
	private boolean archipelago = false;
	private GeographicArea parent;
	private boolean country = false;
	
	private Set<GeographicArea> children = new HashSet<GeographicArea>(0);
	private Set<GeographicAreaParents> myAsc = new HashSet<GeographicAreaParents>(
			0);
	private Set<GeographicAreaParents> myDesc = new HashSet<GeographicAreaParents>(
			0);
	private GeographicAreaTypeEnum level = GeographicAreaTypeEnum.ISLAND_DISTRICT;

	private String locale=Constants.DEFAULT_LOCALE;
	
	
	/*
	 * business methods
	 */

	@Override
	public GeographicAreaParents newInstanceOfParentModel(Model asc) {
		GeographicAreaParents result = new GeographicAreaParents();
		result.setAsc((GeographicArea) asc);
		result.setDesc(this);
		return result;
	}

	@Override
	public String getNameFragment(int n) {
		String result;
		if (this.getName().length() > (n - 5) && n > 5) {
			result = this.getName().substring(0, (n - 5)) + "(...)";
		} else {
			result = this.getName();
		}
		return result;
	}
	
	@Override
	public String toString() {
		return this.name;
	}

	/**
	 * returns the full name from it's country root
	 */
	public String returnFullName() {
		return returnRelativeNameFrom(GeographicAreaTypeEnum.COUNTRY);
	}

	/**
	 * returns the geographic area name from the district/island type
	 */
	public String returnRelativeNameFromIslandDistrict() {
		return returnRelativeNameFrom(GeographicAreaTypeEnum.ISLAND_DISTRICT);
	}
	
	public String returnRelativeNameFromPreviousType(){
		if(this.level.getPrevious()!=null)
			return returnRelativeNameFrom(this.level.getPrevious());
		return this.name;
	}

	/**
	 * return the geographic area name from the enum type as parameter
	 */
	private String returnRelativeNameFrom(
			GeographicAreaTypeEnum geographicAreaType) {
		StringBuffer result = new StringBuffer("");
		GeographicArea ag = this;
		if (ag.getLevel().ordinal() > geographicAreaType.ordinal()) {
			while (ag != null) {
				result.insert(0, ag.getName());
				result.insert(0, ": ");
				if (ag.getParent() != null
						&& ag.getLevel().ordinal() > geographicAreaType
								.ordinal())
					ag = ag.getParent();
				else
					ag = null;
			}
			return result.substring(2);
		} else {
			return result.append(ag.getName()).toString();
		}
	}
	
	public GeographicArea imParentOf(GeographicArea ga) {
		for (GeographicArea g : this.children) {
			if (g.equals(ga))
				return ga;
			else {
				if (g.imParentOf(ga) != null)
					return ga;
			}
		}
		return null;
	}
	
	//Getters and setters
	@Override
	@SequenceGenerator(name = "generator")
	@Id
	@GeneratedValue(strategy = SEQUENCE, generator = "generator")
	@Column(name = "geo_area_id", nullable = false)
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	@Length(max = 255)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	
	@Column
	// (columnDefinition="blob")
	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		if (image != null)
			this.image = image;
	}

	@Length(max = 255)
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Length(max = 255)
	@Column(name = "country_phone_prefix")
	public String getCountryPhonePrefix() {
		return countryPhonePrefix;
	}

	public void setCountryPhonePrefix(String countryPhonePrefix) {
		this.countryPhonePrefix = countryPhonePrefix;
	}

	/*
	 * @Column(name = "pais_referencia") public boolean isPaisReferencia() {
	 * return paisReferencia; } public void setPaisReferencia(boolean
	 * paisReferencia) { this.paisReferencia = paisReferencia; }
	 */

	@Column(name = "archipelago")
	public boolean isArchipelago() {
		return archipelago;
	}

	public void setArchipelago(boolean archipelago) {
		this.archipelago = archipelago;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "master_id")
	public GeographicArea getParent() {
		return parent;
	}

	public void setParent(GeographicArea parent) {
		this.parent = parent;
	}

	@OneToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE,
			CascadeType.REFRESH }, fetch = FetchType.LAZY, mappedBy = "parent")
	@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
	public Set<GeographicArea> getChildren() {
		return children;
	}

	public void setChildren(Set<GeographicArea> children) {
		this.children = children;
	}

	@Override
	// ATENCAO fazer override do metodo do Model -> public <T extends
	// ParentsModel<U>, U extends Model> T newInstanceOfParentModel(U asc)
	@OneToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE,
			CascadeType.REFRESH }, fetch = FetchType.LAZY, mappedBy = "desc")
	@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
	public Set<GeographicAreaParents> getMyAsc() {
		return myAsc;
	}

	public void setMyAsc(Set<GeographicAreaParents> myAsc) {
		this.myAsc = myAsc;
	}

	@Override
	@OneToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE,
			CascadeType.REFRESH }, fetch = FetchType.LAZY, mappedBy = "asc")
	@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
	public Set<GeographicAreaParents> getMyDesc() {
		return myDesc;
	}

	public void setMyDesc(Set<GeographicAreaParents> myDesc) {
		this.myDesc = myDesc;
	}

	@Column(name = "level")
	// ,columnDefinition="number(1)")
	public GeographicAreaTypeEnum getLevel() {
		return level;
	}

	public void setLevel(GeographicAreaTypeEnum level) {
		this.level = level;
	}

	@Length(max = 255)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Length(max = 255)
	@Column(name = "short_name")
	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public boolean isCountry() {
		return country;
	}

	public void setCountry(boolean country) {
		this.country = country;
	}
	
	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

}
