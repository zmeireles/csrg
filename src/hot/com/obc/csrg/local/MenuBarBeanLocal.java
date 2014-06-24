package com.obc.csrg.local;

import javax.ejb.Local;
import javax.faces.event.ActionEvent;

@Local
public interface MenuBarBeanLocal {
	
	public void primaryListener(ActionEvent e);
	public String showUserList();
}
