package com.obc.csrg.local;

import javax.ejb.Local;

@Local
public interface VisualItemMenuBeanLocal {
		
	public String getMenuTxt();
	public void setMenuTxt(String menuTxt);
	public void createMenu();
	public String buildMenu();
	public void pageFactory();
	public String getIFramePageLink();
	public String menuWidth(int index);
	public void setMenuMaxLabel(int menuMaxLabel);
	public int getMenuMaxLabel();
	public String buildSimulatorMenu();
	
}
