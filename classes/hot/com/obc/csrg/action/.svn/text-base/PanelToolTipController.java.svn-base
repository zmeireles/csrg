package com.obc.csrg.action;

import java.io.Serializable;

import com.icesoft.faces.component.DisplayEvent;
import javax.ejb.Stateless;
import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.component.ValueHolder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.jboss.seam.annotations.Name;

import com.obc.csrg.local.PanelToolTipControllerLocal;
import com.obc.csrg.icefaces.PanelToolTipModel;

@Stateless
@Name("tooltipController")
public class PanelToolTipController implements PanelToolTipControllerLocal,Serializable{

	private PanelToolTipModel panelToolTipModel = new PanelToolTipModel();

	//business logic
	
	public void displayListener(DisplayEvent event) {
        // listener whe tooltip appears
        if (event.isVisible()) {
            String objectName = event.getContextValue().toString();
            
        }
    }
	//getters and setters
	
	public PanelToolTipModel getPanelToolTipModel() {
		if(panelToolTipModel==null)
			panelToolTipModel = new PanelToolTipModel();
		return panelToolTipModel;
	}

	public void setPanelToolTipModel(PanelToolTipModel panelToolTipModel) {
		this.panelToolTipModel = panelToolTipModel;
	}
}
