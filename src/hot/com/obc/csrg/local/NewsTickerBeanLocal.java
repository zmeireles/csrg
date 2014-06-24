package com.obc.csrg.local;

import javax.ejb.Local;

@Local
public interface NewsTickerBeanLocal {
	public String getNewsTickerTxt();
	public void setNewsTickerTxt(String menuTxt);	
	public String buildNewsTicker();
	
}
