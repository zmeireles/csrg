package com.obc.csrg.local.report;

import java.util.List;

import javax.ejb.Local;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import com.obc.csrg.model.User;

@Local
public interface UserReportBeanLocal extends ReportBeanLocal<User> {

	public int getActiveStatus();
	public void setActiveStatus(int activeStatus);
	public List<SelectItem> getActiveStatusItems();
	public void setActiveStatusItems(List<SelectItem> activeStatusItems);
	public void activeStatusListener(ValueChangeEvent event);
}
