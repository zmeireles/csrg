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
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.Length;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

@Entity
@Name("chatProfile")
@Scope(SESSION)
@Table(name = "chat_profile")
//@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
public class ChatProfile extends Model implements Serializable {
	
	@SequenceGenerator(name = "generator")
	@Id
	@GeneratedValue(strategy = SEQUENCE, generator = "generator")
	@Column(name = "chat_profile_id", nullable = false)
	private Long id;
	@OneToOne(cascade={CascadeType.PERSIST,CascadeType.MERGE,CascadeType.REFRESH}, fetch=FetchType.LAZY)
    @JoinColumn(name = "user_csrg_id")
	private User user;
	@Column(name = "public_receiver")
	private boolean publicReceiver=false;//can receive messages from anyone
	@Column(name = "show_online")
	private boolean showOnline=false;//appear online in the public users list
	@Column(name = "online_helper")
	private boolean onlineHelper=false;//it means he can access and talk with anyone
	
	
	@Override
	public Long getId() {
	     return id;
	}

	@Override
	public void setId(Long id) {
	     this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public boolean isPublicReceiver() {
		return publicReceiver;
	}

	public void setPublicReceiver(boolean publicReceiver) {
		this.publicReceiver = publicReceiver;
	}

	public boolean isShowOnline() {
		return showOnline;
	}

	public void setShowOnline(boolean showOnline) {
		this.showOnline = showOnline;
	}

	public boolean isOnlineHelper() {
		return onlineHelper;
	}

	public void setOnlineHelper(boolean onlineHelper) {
		this.onlineHelper = onlineHelper;
	}

}
