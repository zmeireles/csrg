package com.obc.csrg.util;

import com.icesoft.faces.component.inputfile.InputFile;
import com.icesoft.faces.component.inputfile.FileInfo;
import com.obc.csrg.bean.StateBean;
import com.obc.csrg.local.StateBeanLocal;
import com.obc.csrg.model.Config;
import com.obc.csrg.model.User;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.seam.annotations.Begin;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.In;
import org.jboss.seam.security.Credentials;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.persistence.EntityManager;

import java.util.*;
import java.io.Serializable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * <p>The InputFileController is responsible for the file upload
 * logic as well as the file deletion object.  A users file uploads are only
 * visible to them and are deleted when the session is destroyed.</p>
 *
 * @since 1.7
 */
public class MediaController implements Serializable {

    public static final Log log = LogFactory.getLog(MediaController.class);    

    // File sizes used to generate formatted label
    public static final long MEGABYTE_LENGTH_BYTES = 1048000l;
    public static final long KILOBYTE_LENGTH_BYTES = 1024l;

    // files associated with the current user
    private final List fileList =
            Collections.synchronizedList(new ArrayList());
    // latest file uploaded by client
    private InputFileData currentFile;
    // file upload completed percent (Progress)
    private int fileProgress;

    private boolean autoUpload = true;
    
	protected Config config;
    
    @In
	protected EntityManager entityManager;
    
    public FacesMessage curMessage;
    
    private User user;
    
    
  //contructors
	public MediaController(StateBeanLocal stateBean){
		
		this.user =  stateBean.getUser();
		this.config =  stateBean.getConfig();
	}
	
   
    /**
     * <p>Action event method which is triggered when a user clicks on the
     * upload file button.  Uploaded files are added to a list so that user have
     * the option to delete them programatically.  Any errors that occurs
     * during the file uploaded are added the messages output.</p>
     *
     * @param event jsf action event.
     */
    public void uploadFile(ActionEvent event) {
        InputFile inputFile = (InputFile) event.getSource();
        FileInfo fileInfo = inputFile.getFileInfo();
        if (fileInfo.getStatus() == FileInfo.SAVED) {
            // reference our newly updated file for display purposes and
            // added it to our history file list.
        	
            currentFile = new InputFileData(fileInfo);
            log.info("filename:" + currentFile.getFile().getAbsolutePath() + " name:" + currentFile.getFile().getName());
             
            /**
             * following code creates a copy of uploaded file in the file system.
             * This code is good for purpose of changing the file name of the file (removing spaces for example)
             * See MMS requirements that address this issue 
             */
           
            byte[] bytes = new byte[((Long)currentFile.getFileInfo().getSize()).intValue()];
            try{
            	
            
                
                //getting path from config item
                String path =this.config.getUploadDirectory();//"c:/csrg/files/"
            	
               // note: need loop to check and create directory: manually option is set 
                //checking directory exists, if not create it
               // if(!(new File(path).exists()))
            	//{           	
            	//	new File(path).mkdir();
            	//}
            	
                
                String userName = user.getUsername().toString();
            	String userFolder = path + "/"+userName;// add username to path
            	// check whether  folder with username exists, if not create it
            	if(!(new File(userFolder).exists()))
            	{           	
            		new File(userFolder).mkdir();
            	}
            		
            	//getting file name and divide it to name and extension
            	 String fileName = currentFile.getFile().getName();
        		 int indexOFDot=fileName.lastIndexOf(".");
        		 String fileNameOnly = "";
        		 String fileExtensionIncludingDot = "";
        		 
        		 if(indexOFDot>0)
        		 {
        			  fileNameOnly = fileName.substring(0,indexOFDot);
             		  fileExtensionIncludingDot = fileName.substring(indexOFDot);
        		 }
        		 else
        		 {
        			 fileNameOnly = fileName;            			 
        		 }
            	
        		 
        		 //if(!this.isValidExtension(fileExtensionIncludingDot))
        		 //{
        			 //set invalid extension message
        			 
        			 //return;
        		 //}
        		 /*else if(!this.isEnoughDiskSpace(userFolder))
        		 {
        			 set not enough disk space message
        			 
        			 return;
        		 }
        		 elseif(!this.isValidFileSize(currentFile.getSizeFormatted())
        		 {
        			 set not enough disk space message
        			 
        			 return;
        		 }
        		 
        		 */
        		 
        		 
        		 fileNameOnly = this.replaceSpecialCharInFileName(fileNameOnly);
        		//set file name by replaced one 
        		 fileName = fileNameOnly + fileExtensionIncludingDot;
        		 
        		 String fileFullPath = userFolder + "/"+fileName;
        		
        		 int counter=1;
        		 String  fileNameSuffix="";
        		
        		//check whether file name exists. if exists then add appropriate numeric suffix
        		 while(new File(fileFullPath).exists())
        		 {
        			//creating file name suffix;
        			if(counter>9)
        			{
        				fileNameSuffix=""+counter;
        			}
        			else{
        				fileNameSuffix="0"+counter;
        			}

        			if(fileExtensionIncludingDot.length()>0)
        			{
        			fileName=fileNameOnly+"_"+fileNameSuffix+fileExtensionIncludingDot;
        			}
        			else
        			{           				
            			fileName=fileNameOnly+"_"+fileNameSuffix;                			
        			}
        			fileFullPath=userFolder+"/"+fileName;
        			counter++;
        		}
            		
            	
            	//updating file name and file path
            	File file = new File(fileFullPath);
            	currentFile.getFileInfo().setFileName( fileName);
            	FileInputStream fis = new FileInputStream(currentFile.getFile());
            	fis.read(bytes);
            	FileOutputStream fos = new FileOutputStream(file);
            	fos.write(bytes);
            	fos.close();
            }catch(Exception e){
            	e.printStackTrace();
            }
            synchronized (fileList) {
                fileList.add(currentFile);
            }

        }

    }

    /**
     * <p>This method is bound to the inputFile component and is executed
     * multiple times during the file upload process.  Every call allows
     * the user to finds out what percentage of the file has been uploaded.
     * This progress information can then be used with a progressBar component
     * for user feedback on the file upload progress. </p>
     *
     * @param event holds a InputFile object in its source which can be probed
     *              for the file upload percentage complete.
     */
    public void fileUploadProgress(EventObject event) {
        InputFile ifile = (InputFile) event.getSource();
        fileProgress = ifile.getFileInfo().getPercent();
    }

    /**
     * <p>Allows a user to remove a file from a list of uploaded files.  This
     * methods assumes that a request param "fileName" has been set to a valid
     * file name that the user wishes to remove or delete</p>
     *
     * @param event jsf action event
     */
    public void removeUploadedFile(ActionEvent event) {
        // Get the inventory item ID from the context.
        FacesContext context = FacesContext.getCurrentInstance();
        Map map = context.getExternalContext().getRequestParameterMap();
        String fileName = (String) map.get("fileName");

        synchronized (fileList) {
            InputFileData inputFileData;
            for (int i = 0; i < fileList.size(); i++) {
                inputFileData = (InputFileData)fileList.get(i);
                // remove our file
                if (inputFileData.getFileInfo().getFileName().equals(fileName)) {
                    fileList.remove(i);
                    break;
                }
            }
        }
    }

    /* Replace special characters in file name*/
    public String replaceSpecialCharInFileName(String fileName) {
  
    	//below two lines are used to test
    	//fileName=fileName.replace('A', 'X');
    	//fileName=fileName.replace('a', 'X');
    	
    	
    	fileName=fileName.replace(' ', '_');
    	fileName=fileName.replace('ƒ', 'F');
    	fileName=fileName.replace('Š', 'S');
    	fileName=fileName.replace('Œ', 'O');
    	fileName=fileName.replace('Ž', 'Z');
    	fileName=fileName.replace('š', 's');
    	fileName=fileName.replace('œ', 'o');
    	fileName=fileName.replace('ž', 'z');

    	fileName=fileName.replace('Ä', 'A');
    	fileName=fileName.replace('Å', 'A');
    	fileName=fileName.replace('Æ', 'A');
    	fileName=fileName.replace('À', 'A');
    	fileName=fileName.replace('Á', 'A');
    	fileName=fileName.replace('Â', 'A');
    	fileName=fileName.replace('Ã', 'A');
			
    	fileName=fileName.replace('Ç', 'C');
			
    	fileName=fileName.replace('É', 'E');
    	fileName=fileName.replace('È', 'E');
    	fileName=fileName.replace('Ê', 'E');
    	fileName=fileName.replace('Ë', 'E');
			
    	fileName=fileName.replace('Ì', 'I');
    	fileName=fileName.replace('Í', 'I');
    	fileName=fileName.replace('Î', 'I');
    	fileName=fileName.replace('Ï', 'I');
			
    	fileName=fileName.replace('Ñ', 'N');
			
    	fileName=fileName.replace('Ö', 'O');
    	fileName=fileName.replace('Ø', 'O');
    	fileName=fileName.replace('Ò', 'O');
    	fileName=fileName.replace('Ó', 'O');
    	fileName=fileName.replace('Ô', 'O');
    	fileName=fileName.replace('Õ', 'O');
			
    	fileName=fileName.replace('Ü', 'U');
    	fileName=fileName.replace('Ù', 'U');
    	fileName=fileName.replace('Ú', 'U');
    	fileName=fileName.replace('Û', 'U');
			
    	fileName=fileName.replace('Ý', 'Y');
    	fileName=fileName.replace('Ÿ', 'Y');

    	fileName=fileName.replace('à', 'a');
    	fileName=fileName.replace('ä', 'a');
    	fileName=fileName.replace('å', 'a');
    	fileName=fileName.replace('æ', 'a');
    	fileName=fileName.replace('á', 'a');
    	fileName=fileName.replace('â', 'a');
    	fileName=fileName.replace('ã', 'a');
			
    	fileName=fileName.replace('ç', 'c');
			
    	fileName=fileName.replace('ê', 'e');
    	fileName=fileName.replace('ë', 'e');
    	fileName=fileName.replace('è', 'e');
    	fileName=fileName.replace('é', 'e');
			
    	fileName=fileName.replace('ì', 'i');
    	fileName=fileName.replace('ï', 'i');
    	fileName=fileName.replace('î', 'i');
    	fileName=fileName.replace('ò', 'o');
			
    	fileName=fileName.replace('ö', 'o');
    	fileName=fileName.replace('ø', 'o');
    	fileName=fileName.replace('ô', 'o');
    	fileName=fileName.replace('ó', 'o');
    	fileName=fileName.replace('õ', 'o');
			
    	fileName=fileName.replace('ñ', 'n');
			
    	fileName=fileName.replace('ù', 'u');
    	fileName=fileName.replace('ü', 'u');
    	fileName=fileName.replace('ú', 'u');
    	fileName=fileName.replace('û', 'u');

    	return fileName;
    }
    
    private boolean isValidExtension(String fileExtensionIncludingDot)
    {
    	boolean isValidExtension=false;
    	//String allowedExtensions = ".jpg .png .bmp .pdf .txt";
    	String allowedExtensions =  this.config.getAllowedFileExtensions();
    	if(allowedExtensions.indexOf(fileExtensionIncludingDot)>0)
    	{
    		isValidExtension=true;
    	}
    	return isValidExtension;
    }
    
    private boolean isEnoughDiskSpace(String userFolder)
    {
    	int maxDiskSpace = this.config.getMaxDiskSpace();
    	
    	return true;
    }
    
    
    public  long getFolderSize(File folder) {
    	
     long foldersize = 10;
    /*File[] filelist = folder.listFiles();
     for (int i = 0; i < filelist.length; i++) {
       if (filelist[i].isDirectory()) {
         foldersize += getFolderSize(filelist[i]);
       } else {         
         foldersize += filelist[i].length();
       }
     }*/
         return foldersize;
    }
    
    private boolean isValidFileSize(int fileSize)
    {
    	int maxFileSize =  this.config.getMaxFileSize();
    	
    	return true;
    }
    
    
    
    public InputFileData getCurrentFile() {
        return currentFile;
    }

    public int getFileProgress() {
        return fileProgress;
    }

    public List getFileList() {
        return fileList;
    }

    public boolean isAutoUpload() {
        return autoUpload;
    }

    public void setAutoUpload(boolean autoUpload) {
        this.autoUpload = autoUpload;
    }
    
    public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}

