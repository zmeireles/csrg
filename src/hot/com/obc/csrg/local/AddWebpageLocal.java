package com.obc.csrg.local;

import java.util.List;

import javax.ejb.Local;

import com.obc.csrg.model.VisualItem;
import com.obc.csrg.model.Webpage;

@Local
public interface AddWebpageLocal {
	public void create();
	public void destroy();
	public String add();
	public Webpage getNewWebpage();
	public void setNewWebpage(Webpage newWebpage);

}
