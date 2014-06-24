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

import com.obc.csrg.constants.Constants;
import com.obc.csrg.events.ModelEventQueue;

@Entity
@Name("urlProfileDisplayInfo")
@Scope(SESSION)
@Table(name = "url_profile_disp_info")
//@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
public class UrlProfileDisplayInfo extends Model implements Serializable{

	@SequenceGenerator(name = "generator")
	@Id
	@GeneratedValue(strategy = SEQUENCE, generator = "generator")
	@Column(name = "url_profile_disp_info_id", nullable = false)
	private Long id;

	private String locale=Constants.DEFAULT_LOCALE;
	
	@Column(name = "url")
	private String url="";
	
	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "model_disp_info_id")
	private ModelDisplayInfo model;
	
	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "profile_id")
	private Profile profile;
	
	//constructors
	public UrlProfileDisplayInfo() {
	}

	//business logic

	
	@Override
	public boolean remove(EntityManager entityManager) {
		ModelEventQueue.fireOnBeforeModelRemoveEvent(this);
		
		entityManager.remove(this);
		ModelEventQueue.fireOnAfterModelRemoveEvent(this);
		return true;
	}
	public int hashCode() {
		return (this.id == null) ? 0 : this.id.hashCode();
	}

	public boolean equals(Object object) {
		if (object instanceof UrlProfileDisplayInfo) {
			final UrlProfileDisplayInfo obj = (UrlProfileDisplayInfo) object;
			return (this.id != null) ? this.id.equals(obj.id)
					: (obj.id == null);
		}
		return false;
	}
	
	//getters and setters
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}
	
	public ModelDisplayInfo getModel() {
		return model;
	}

	public void setModel(ModelDisplayInfo model) {
		this.model = model;
	}
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Profile getProfile() {
		return profile;
	}

	public void setProfile(Profile profile) {
		this.profile = profile;
	}
}
