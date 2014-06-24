package com.obc.csrg.local.report;

import javax.ejb.Local;

import com.obc.csrg.action.VisualItemMenuBean;
import com.obc.csrg.model.VisualItem;

@Local
public interface VisualItemReportBeanLocal extends ReportBeanLocal<VisualItem> {
	
	public void setMenu(VisualItemMenuBean menu);
	public VisualItemMenuBean getMenu();
}
