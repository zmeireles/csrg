package com.obc.csrg.bean;

import java.io.Serializable;
import java.io.File;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Begin;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.End;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.log.Log;

import com.obc.csrg.local.DBCronAppLocal;
import com.obc.csrg.local.AddWebpageLocal;
import com.obc.csrg.local.StateBeanLocal;
import com.obc.csrg.model.Config;
import com.obc.csrg.model.User;
import com.obc.csrg.model.VisualItem;
import com.obc.csrg.model.Webpage;


@Stateful
@Scope(ScopeType.CONVERSATION)
@Name("addWebpage")
public class AddWebpageBean implements AddWebpageLocal, Serializable {

	@Logger
	private Log log;

	@In
	private EntityManager entityManager;

	@In
	private FacesMessages facesMessages;

	@In
	private Map<String, String> messages;

	@In
	protected StateBeanLocal stateBean;

	private Config config;

	private Webpage newWebpage = new Webpage();



	@Begin(join = true)
	@Create
	public void create() {
		config = stateBean.getConfig();
		log.info("#0 [create]", this.toString().substring(
				this.toString().lastIndexOf(".")));
		
	}

	@Remove
	public void destroy() {
	}

	@End
	public String add() {
		String result ="";
		//newWebpage.
		log.info("[add] html:#0", newWebpage.getHtml());
		newWebpage.setHtml(newWebpage.getHtml().trim());
		entityManager.persist(newWebpage);
		entityManager.flush();
		return  result;
	}
	
	public Webpage getNewWebpage() {
		if (newWebpage == null) {
			newWebpage = new Webpage();

		}
		return newWebpage;
	}
	public void setNewWebpage(Webpage newWebpage) {
		this.newWebpage = newWebpage;
	}
}
