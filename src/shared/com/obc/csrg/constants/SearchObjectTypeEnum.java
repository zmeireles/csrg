package com.obc.csrg.constants;

public enum SearchObjectTypeEnum {
	MODEL,XHTML_PAGE,HTML_PAGE;
	
	public String getName(){
		switch(this.ordinal()){
		case 0:return Constants.SOT_MODEL;
		case 1:return Constants.SOT_XHTML_PAGE;
		case 2:return Constants.SOT_HTML_PAGE;
		default :return Constants.SOT_MODEL;
		}
	}

}
