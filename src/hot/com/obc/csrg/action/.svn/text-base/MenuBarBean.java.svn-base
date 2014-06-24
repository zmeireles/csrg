package com.obc.csrg.action;

import javax.ejb.Stateless;


import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.log.Log;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import com.obc.csrg.local.MenuBarBeanLocal;
import com.obc.csrg.local.StateBeanLocal;
import com.obc.csrg.util.FacesUtils;

@Stateless
@Name("menuBar")
public class MenuBarBean extends BaseBean implements MenuBarBeanLocal {

	@Logger 
    private Log log;
	
	@In
	protected StateBeanLocal stateBean;
	// records which menu item fired the event
    private String actionFired;

    // records the param value for the menu item which fired the event
    private String param;

    // orientation of the menubar ("Horizontal" or "Vertical")
    private String orientation = "Horizontal";

    /**
     * Get the param value for the menu item which fired the event.
     *
     * @return the param value
     */
    public String getParam() {
        return param;
    }

    /**
     * Set the param value.
     */
    public void setParam(String param) {
        this.param = param;
    }

    /**
     * Get the modified ID of the fired action.
     *
     * @return the modified ID
     */
    public String getActionFired() {
        return actionFired;
    }

    /**
     * Identify the ID of the element that fired the event and return it in a
     * form suitable for display.
     *
     * @param e the event that fired the listener
     */
    public void primaryListener(ActionEvent e) {

        actionFired = ((UIComponent) e.getSource())
                .getClientId(FacesContext.getCurrentInstance());
        param = FacesUtils.getRequestParameter("myParam");

        // highlight server side backing bean values. 
        //valueChangeEffect.setFired(false);

        //log.info("[primaryListener] action:#0, param:#1", actionFired,param);
        if(param.equals("users")){
        	
        }
        
    }
    public String showUserList() {
    	log.info("[showUserList] profilesArray:#0",stateBean.getDir(stateBean.getUser().profilesArray()));
        return stateBean.getDir(stateBean.getUser().profilesArray()) + "/userList.xhtml";
    }

    /**
     * Get the orientation of the menu ("horizontal" or "vertical")
     *
     * @return the orientation of the menu
     */
    public String getOrientation() {
        return orientation;
    }

    /**
     * Set the orientation of the menu ("horizontal" or "vertical").
     *
     * @param orientation the new orientation of the menu
     */
    public void setOrientation(String orientation) {
        this.orientation = orientation;
    }
}
