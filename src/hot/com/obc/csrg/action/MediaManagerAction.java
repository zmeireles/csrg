package com.obc.csrg.action;

import javax.ejb.Stateless;
import javax.faces.event.ActionEvent;
import javax.swing.tree.DefaultMutableTreeNode;


import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.log.Log;


import java.io.File;
import java.io.Serializable;
import java.util.Map;

import com.obc.csrg.local.MediaManagerLocal;
import com.obc.csrg.local.StateBeanLocal;

@Stateless
@Name("mediaManagerAction")
public class MediaManagerAction implements  MediaManagerLocal {

		
	@In
	protected StateBeanLocal stateBean;
	
	//@RequestParameter
	//@In(required = false)
	//String fileName="C:/csrg/mms_files/admin/";
	String fileName="";
	
	String newFileOrFolderName="";
	String message="";
	
	//@RequestParameter
	//@In(required = false)
	private String newFolderName;
	
	public String createNewFolder() {
		//fileName=mediaTree.selectedUserObject.path;
		message="createNewFolder called##"+fileName;
		//this.fileName="C:/csrg/mms_files/admin/";
		String parentPath="";
		if (!new File(fileName).isDirectory()) {	
			parentPath=""+fileName.substring(0,fileName.lastIndexOf("/"));
		}	
		else{
			parentPath=fileName;
		}
		String path=parentPath+"/"+newFolderName;
		if(!(new File(path).exists()))
        	{           	
        		boolean isFoldercreated=new File(path).mkdir();
        		if(isFoldercreated)
        			message+="new folder created successfully. ";
        		else
        			message+="can't create foldeer. ";
        	}
		else
			message+="folder existts with the same name. ";
		return "";
	}
	
	
	public String renameFileOrFolder() {		
		try
		{			
		  File oldfile = new File(fileName);  
			  if(oldfile.exists())	{	  	    
				  String parentPath=""+fileName.substring(0,fileName.lastIndexOf("/"));
				   File newfile = new File(parentPath+"/"+newFileOrFolderName);
				   boolean isRenamed = oldfile.renameTo(newfile);
				   if(isRenamed){
					   message+="renamed successfully. ";
				   }
				   else
					   message+="error occured during rename. ";
			   }	
			   else
			   {
				   message+="old file does not exists. ";
			   }	
		   
		}
		catch(Exception exp)
		{
			 message+=""+exp.getMessage().toString()+". ";
		}	      
	      return "";
	}
	
	public String deleteFileOrFolder() {		
		try
		{
			 File selectedFile = new File(fileName);			  
			  if(selectedFile.exists())	{				   
				   boolean isDeleted = selectedFile.delete();
				   if(isDeleted){
					   message+="deleted successfully. ";
				   }
				   else
					   message+="error occured during delete. ";
			   }	
			   else
			   {
				   message+="old file does not exists. ";
			   }	
		   
		}
		catch(Exception exp)
		{
			 message+=""+exp.getMessage().toString()+". ";
		}	      
	      return "";
	}
	
	
	
	
	
	
	// getters and setters
	
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	public String getNewFolderName() {
		return newFolderName;
	}

	public void setNewFolderName(String newFolderName) {
		this.newFolderName = newFolderName;
	}
	
	public String getNewFileOrFolderName() {
		return newFileOrFolderName;
	}

	public void setNewFileOrFolderName(String newFileOrFolderName) {
		this.newFileOrFolderName = newFileOrFolderName;
	}
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	
}
