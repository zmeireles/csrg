package com.obc.csrg.local;

import java.io.File;

import javax.ejb.Local;
import javax.faces.event.ActionEvent;

@Local
public interface MediaManagerLocal {
	public String createNewFolder();
	public String renameFileOrFolder();
	public String deleteFileOrFolder();
	
	// getters and setters
	
	public String getFileName();

	public void setFileName(String fileName) ;
	
	public String getNewFolderName() ;
	

	public void setNewFolderName(String newFolderName) ;
	
	public String getNewFileOrFolderName() ;
	public void setNewFileOrFolderName(String newFileOrFolderName);
	
	public String getMessage();
	public void setMessage(String message);
	
}
