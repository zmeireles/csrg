package com.obc.csrg.action;

import javax.ejb.Stateless;
import javax.faces.context.FacesContext;

import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;


import java.util.Arrays;
import java.util.List;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.web.RequestParameter;

import com.obc.csrg.local.FileTreeConnectorBeanLocal;
import com.obc.csrg.local.StateBeanLocal;




@Stateless
@Name("fileTreeConnector")
public class FileTreeConnectorBean  implements FileTreeConnectorBeanLocal {


	
	@RequestParameter
	private String dir;
	
	@In(required=false)
	protected StateBeanLocal stateBean;
	String uploadDirectory = "c:/csrg/files/";
	//private String dirTest = "C:/csrg/files/";//request.getParameter("dir");
	String menuTxt = "This will be the menu code";
	StringBuffer text= new StringBuffer();
	
	  public String buildTree(){
	    	this.menuTxt = "some text";
		    this.uploadDirectory = this.stateBean.getConfig().getUploadDirectory();

	    	//log.info("[buildMenu] menuTxt:#0", menuTxt);
	    	//this.createMenu();
	    	
	    	//return this.menuTxt;
	    	int i = this.text.length();
	    	this.text.delete(0, i);
	    	this.initMenu();
	    	//log.info("[buildMenu] menuTxt:#0", menuTxt);
	    	//log.info("[buildMenu] text:#0", text.toString());
	    	return text.toString();
	    }
	    
	
	
	public void initMenu()
	{
		
		//String dir = "C:/csrg/files/";//request.getParameter("dir");
	    String html = "";
		
	    
		if (dir == null) {
	    	return;
	    }
		
		if (dir.charAt(dir.length()-1) == '\\') {
	    	dir = dir.substring(0, dir.length()-1) + "/";
		} else if (dir.charAt(dir.length()-1) != '/') {
		    dir += "/";
		}
		
		try {
			dir = java.net.URLDecoder.decode(dir, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
	    if (new File(dir).exists()) {
			String[] files = new File(dir).list(new FilenameFilter() {
			    public boolean accept(File dir, String name) {
					return name.charAt(0) != '.';
			    }
			});
			Arrays.sort(files, String.CASE_INSENSITIVE_ORDER);
			html = "<ul class=\"jqueryFileTree\" style=\"display: none;\">";
			this.text.append(html);
			
			// All dirs
			for (String file : files) {
			    if (new File(dir, file).isDirectory()) {
			    	
			    	
					//String fileForView = this.getPath() + this.getUserFolder() + file;
					String dirView = dir.replace(this.uploadDirectory, "/csrg/mms_files/");
					String fileForView = dirView + file;
			    	
			    	html = "<li class=\"directory collapsed\"><a href=\"#\" rel=\"" + fileForView + "/\">"
						+ file + "</a></li>";
			    	this.text.append(html);
			    }
			}
			// All files
			for (String file : files) {
			    if (!new File(dir, file).isDirectory()) {
					int dotIndex = file.lastIndexOf('.');
					String ext = dotIndex > 0 ? file.substring(dotIndex + 1) : "";
					html = "<li class=\"file ext_" + ext + "\"><a href=\"#\" rel=\"" + dir + file + "\">"
						+ file + "</a></li>";
					this.text.append(html);
			    	}
			}
			html = "</ul>";
			this.text.append(html);
	    }
	  
	}
	
}
