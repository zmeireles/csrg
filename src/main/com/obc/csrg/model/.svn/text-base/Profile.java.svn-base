package com.obc.csrg.model;

import static javax.persistence.GenerationType.SEQUENCE;
import static org.jboss.seam.ScopeType.SESSION;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.NotNull;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

import com.obc.csrg.constants.ProfileConstant;

@Entity
@Name("profile")
@Scope(SESSION)
@Table(name = "profile")
//@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
public class Profile extends Model implements Serializable {
	
	@SequenceGenerator(name = "generator")
	@Id
	@GeneratedValue(strategy = SEQUENCE, generator = "generator")
	@Column(name = "profile_id", nullable = false)
	private Long id;
	
	@NotNull
	@Column(name = "profile_type", nullable = false)
	private int type=0;
	
	@OneToMany(cascade={CascadeType.MERGE,CascadeType.REMOVE},fetch = FetchType.LAZY, mappedBy = "profile")
	//@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
	private Set<UserProfile> users = new HashSet<UserProfile>(0);
	
	@OneToMany(cascade={CascadeType.MERGE,CascadeType.REMOVE},fetch = FetchType.LAZY, mappedBy = "profile")
	//@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
	private Set<UrlProfileDisplayInfo> urls = new HashSet<UrlProfileDisplayInfo>(0);
	
	//business methods
	@Override
	public String toString() {
		return ProfileConstant.valueOf(type).getName();
	}
	
	public String returnName(Map<String, String> messages){
		return messages.get(ProfileConstant.valueOf(type).getName());
	}
	
	//getters and setters
	
	@Override
	public Long getId() {
	     return id;
	}

	@Override
	public void setId(Long id) {
	     this.id = id;
	}
	
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public Set<UserProfile> getUsers() {
		return users;
	}

	public void setUsers(Set<UserProfile> users) {
		this.users = users;
	}
	
	public Set<UrlProfileDisplayInfo> getUrls() {
		return urls;
	}

	public void setUrls(Set<UrlProfileDisplayInfo> urls) {
		this.urls = urls;
	}
	
}
