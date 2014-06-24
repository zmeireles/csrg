package com.obc.csrg.action;

import javax.activation.MimetypesFileTypeMap;
import javax.ejb.Stateless;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

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

import com.obc.csrg.local.ImageViewerBeanLocal;
import com.obc.csrg.local.StateBeanLocal;




@Stateless
@Name("imageViewer")
public class ImageViewerBean  implements ImageViewerBeanLocal {


	
	//@RequestParameter
	//private String dir;
	@In(required=false)
	protected StateBeanLocal stateBean;
	//private String dirTest = "C:/csrg/files/";//request.getParameter("dir");
	String menuTxt = "This will be the menu code";
	StringBuffer text= new StringBuffer();
	String uploadDirectory = "c:/csrg/files/";
	String userFolder = "admin/";
	
	
	// user entered messages for both dialogs
    private String draggableMessage = "";
    
    // render flags for both dialogs
    private boolean draggableRendered = false;
    
    // if we should use the auto centre attribute on the draggable dialog
    private boolean autoCentre = false;
	
	
	
	
	
	  public String buildImageList(){
		  
		    this.uploadDirectory = this.stateBean.getConfig().getUploadDirectory();
		    this.userFolder = this.stateBean.getUser().getUsername() + "/";
	    	
		    int i = this.text.length();
	    	this.text.delete(0, i);
			//this.text.append("<ul id=\"gallery\" >");
			this.text.append("<div id=\"thumbs\" class=\"navigation\"><ul class=\"thumbs noscript\" >");
	    	String path = this.uploadDirectory + this.userFolder;
			this.createList(path);
			this.text.append("</ul></div>");
	    	return text.toString();
	    }
	    
	
	
	public void createList(String dir)
	{
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
	
			
			// All dirs
			for (String file : files) {
			    if (new File(dir, file).isDirectory()) {
			    	//html = "<li class=\"directory collapsed\"><a href=\"#\" rel=\"" + dir + file + "/\">"
					//	+ file + "</a></li>";
			    	//this.text.append(html);
			    	this.createList(dir + file);
			    }
			}
			// All files
			for (String file : files) {
				
				File f = new File(dir + file);
				String fileType = new MimetypesFileTypeMap().getContentType(f);
				
			    if (!new File(dir, file).isDirectory() && fileType.contains("image")) {
					int dotIndex = file.lastIndexOf('.');
					String ext = dotIndex > 0 ? file.substring(dotIndex + 1) : "";
					
					//String fileForView = this.getPath() + this.getUserFolder() + file;
					String dirView = dir.replace(this.uploadDirectory, "/csrg/mms_files/");
					String fileForView = dirView + file;

					
					html = "<li><img src=\"" +fileForView + "\"  width=\"100\" height=\"100\"/></li>";
					
					
					//html = "<li><img src=\"" +fileForView + "\"/>";
					//html = html + "<div class=\"panel-overlay\">";
					//html = html + "<strong>Description:</strong><br />";
					//html = html + "<strong>Dimention:</strong><br />";
					//html = html + "<strong>Url:</strong>  "+ fileForView +"  [<a href=\"#\" title=\"" + fileForView +"\">Copy link to Clipboard</a>]";
					//html = html + "</div></li>";

					this.text.append(html);
			    	}
			}
		
	    }
	  
	}

	
	    public String getDraggableMessage() {
	    	
	    	return draggableMessage;
	    }

	    public void setDraggableMessage(String draggableMessage) {
	        this.draggableMessage = draggableMessage;
	    }


	    public boolean isDraggableRendered() {
	        return draggableRendered;
	    }

	    public void setDraggableRendered(boolean draggableRendered) {
	        this.draggableRendered = draggableRendered;
	    }

	    public boolean getAutoCentre() {
	        return autoCentre;
	    }

	    public void setAutoCentre(boolean autoCentre) {
	        this.autoCentre = autoCentre;
	    }

	    public void toggleDraggable(ActionEvent event) {
	        draggableRendered = !draggableRendered;
	    }

	
}
