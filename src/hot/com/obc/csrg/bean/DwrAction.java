package com.obc.csrg.bean;

import java.util.Map;

import javax.ejb.Stateless;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.log.Log;

//import org.directwebremoting.ScriptSession;
//import org.directwebremoting.proxy.dwr.Util;
import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;
import org.directwebremoting.proxy.dwr.Util;
import org.directwebremoting.ScriptSession;

import com.obc.csrg.local.ApplicationLocal;
import com.obc.csrg.local.DwrLocal;
import com.obc.csrg.local.StateBeanLocal;

@Name("dwrAction")
@Stateless
public class DwrAction implements DwrLocal{

	@Logger 
    private Log log;
	
	@In(required=false)
    private StateBeanLocal stateBean;
	
	@In(required=false)
	private ApplicationLocal applicationBean;
	
	@In 
	private Map<String,String> messages;
	
	public void setScriptSession(){
		
		log.info("[setScriptSession]");
		WebContext wctx = WebContextFactory.get();
		String currentPage = wctx.getCurrentPage();
		if(applicationBean.notInExceptionPages(currentPage)){
			ScriptSession oldSession = stateBean.getScriptSession();
			//log.info("[setScriptSession] stateBean oldSession:#0", oldSession);
			if(oldSession!=null)
				applicationBean.unloadScriptSession(oldSession);
			stateBean.setScriptSession(wctx.getScriptSession());
			stateBean.setMyPage(new Util(wctx.getScriptSession()));
			stateBean.fireNewSession();
		}
	}
	public void unloadScriptSession(){
		WebContext wctx = WebContextFactory.get();
		applicationBean.unloadScriptSession(wctx.getScriptSession());
	}
}
