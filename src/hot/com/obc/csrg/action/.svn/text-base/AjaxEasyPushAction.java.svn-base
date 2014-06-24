package com.obc.csrg.action;

import javax.ejb.Stateless;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.log.Log;

import com.icesoft.faces.async.render.SessionRenderer;
import com.obc.csrg.local.AjaxEasyPushLocal;
import com.obc.csrg.local.ApplicationLocal;
import com.obc.csrg.local.StateBeanLocal;

@Stateless
@Name("ajaxEasyPushAction")
public class AjaxEasyPushAction implements AjaxEasyPushLocal{

	@Logger
	private Log log;
	
	@In(required=false)
    private StateBeanLocal stateBean;
	
	@In(required = false)
	private ApplicationLocal applicationBean;
	
	public AjaxEasyPushAction(){
		if(log!=null) log.info("[constructor]");
		SessionRenderer.addCurrentSession("all");
	}
	
	public Long getUsersCount(){
		if(applicationBean==null)
			return 0L;
		return applicationBean.getUsersCount();
	}
}
