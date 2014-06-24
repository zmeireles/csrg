package com.obc.csrg.local;

import javax.ejb.Local;


@Local
public interface FileBrowserBeanLocal {
		
	public String  buildFileList();


}
