package com.obc.csrg.tree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.EventObject;
import java.util.List;
import java.util.Map;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.Serializable;

import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.persistence.EntityManager;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;

import org.jboss.seam.Component;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Begin;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;

import com.obc.csrg.action.BaseBean;
import com.obc.csrg.events.ModelEvent;
import com.obc.csrg.events.ModelEventAdapter;
import com.obc.csrg.events.ModelEventListener;
import com.obc.csrg.events.ModelEventQueue;
import com.icesoft.faces.component.dragdrop.DropEvent;
import com.icesoft.faces.component.ext.HtmlPanelGroup;
import com.icesoft.faces.component.inputfile.FileInfo;
import com.icesoft.faces.component.inputfile.InputFile;
import com.icesoft.faces.component.tree.IceUserObject;
import com.obc.csrg.local.StateBeanLocal;
import com.obc.csrg.local.report.MediaTreeLocal;
import com.obc.csrg.model.Department;
import com.obc.csrg.model.Model;
import com.obc.csrg.util.FacesUtils;
import com.obc.csrg.util.InputFileData;

@Name("mediaTree")
@Stateful
@Scope(ScopeType.SESSION)
public class MediaTreeReport extends BaseBean implements MediaTreeLocal, Serializable{
	
	@Logger 
	private Log log;
	
	@In
	private EntityManager entityManager;
	@In
	protected StateBeanLocal stateBean;
	
	@In
	protected Map<String,String> messages;

	private DefaultTreeModel model;
	private MediaUserObject selectedUserObject;
	
	protected ModelEventListener modelEventListener;
	
	private String pathSeparator ="/";
	private String UploadDirectory = "";
	private String root = "";
	private String user = "";
	private String aliasRoot ="/";
	//String fileName="";	
	
	String actionMessage = "";
	
	//@RequestParameter
	//@In(required = false)
	private String newFolderName;
	
	private InputFileData currentFile;
    // file upload completed percent (Progress)
    private int fileProgress;

    private boolean autoUpload = true;
    private boolean queryOutdated = true;
    
	@Begin(join=true)
	@Create
	public void create() {
		this.registerModelListener();
		user = stateBean.getUser().getUsername().toString();
		UploadDirectory = stateBean.getConfig().getUploadDirectory();
		root = this.UploadDirectory + this.user;
		init();
	}
	
	@Remove
	public void destroy() {
		this.unregisterModelListener();
	}
	
    public void mediaNodeSelected(ActionEvent event) {
    	this.actionMessage="";
        // get department id
        String mediaPath = FacesUtils.getRequestParameter("mediaPath");
        this.actionMessage=mediaPath;
        //log.info("[mediaNodeSelected] departmentId:#0", mediaPath);
        // find department node by id and make it the selected node
        DefaultMutableTreeNode node = findTreeNode(mediaPath);
        selectedUserObject = (MediaUserObject) node.getUserObject();
        // fire effects.);
        valueChangeEffect.setFired(false);
    }

    public ArrayList getSelectedTreePath() {
    	this.actionMessage="";
        Object[] objectPath = selectedUserObject.getWrapper().getUserObjectPath();
        ArrayList treePath = new ArrayList();
        Object anObjectPath;
        for(int i= 0, max = objectPath.length; i < max; i++){
            anObjectPath = objectPath[i];
            IceUserObject userObject = (IceUserObject) anObjectPath;
            treePath.add(userObject.getText());
        }
        return treePath;
    }

    public boolean isMoveUpDisabled() {
        DefaultMutableTreeNode selectedNode = selectedUserObject.getWrapper();
        return isMoveDisabled(selectedNode, selectedNode.getPreviousNode());
    }

    public boolean isMoveDownDisabled() {
        DefaultMutableTreeNode selectedNode = selectedUserObject.getWrapper();
        return isMoveDisabled(selectedNode, selectedNode.getNextNode());
    }

    public boolean isMoveDisabled(DefaultMutableTreeNode selected, DefaultMutableTreeNode swapper) {
        return selected == null || swapper == null || selected.getAllowsChildren() || swapper.isRoot();
    }

    public void moveUp(ActionEvent event) {
        DefaultMutableTreeNode selectedNode = selectedUserObject.getWrapper();
        exchangeNodes(selectedNode.getPreviousNode(), selectedNode);
    }

    public void moveDown(ActionEvent event) {
        DefaultMutableTreeNode selectedNode = selectedUserObject.getWrapper();
        exchangeNodes(selectedNode, selectedNode.getNextNode());
    }

    public void exchangeNodes(DefaultMutableTreeNode node1, DefaultMutableTreeNode node2) {
        DefaultMutableTreeNode node1Parent = (DefaultMutableTreeNode) node1.getParent();
        DefaultMutableTreeNode node2Parent = (DefaultMutableTreeNode) node2.getParent();
        DefaultMutableTreeNode node1PrevNode = node1.getPreviousNode();
        DefaultMutableTreeNode node1PrevNodeParent = (DefaultMutableTreeNode) node1PrevNode.getParent();
        int childCount = 0;
        
        if (node1.isNodeDescendant(node2)) {
            while (node2.getChildCount() > 0) {
                node1.insert((MutableTreeNode) node2.getFirstChild(), childCount++);
            }
            if (node1PrevNode == node1Parent ||
                    (node1PrevNode.isNodeSibling(node1) && !node1PrevNode.getAllowsChildren())) {
                node1Parent.insert(node2, node1Parent.getIndex(node1));
            } else if (node1PrevNode.getAllowsChildren()) {
                node1PrevNode.add(node2);
            } else {
                node1PrevNodeParent.add(node2);
            }

            return;
        }

        if (node2.getAllowsChildren()) {
            node2.insert(node1, 0);
        } else {
            node1.removeFromParent();
            node2Parent.insert(node1, node2Parent.getIndex(node2) + 1);
        }
    }

    public void dropListener(DropEvent event) {
        HtmlPanelGroup panelGroup = (HtmlPanelGroup) event.getComponent();

        DefaultMutableTreeNode dragNode = (DefaultMutableTreeNode) event.getTargetDragValue();
        DefaultMutableTreeNode dropNode = (DefaultMutableTreeNode) panelGroup.getDropValue();
        DefaultMutableTreeNode dropNodeParent = (DefaultMutableTreeNode) dropNode.getParent();

        if (dragNode.isNodeDescendant(dropNode)) return;

        if (dropNode.getAllowsChildren()) {
            dropNode.insert(dragNode, 0);
        } else {
            dragNode.removeFromParent();
            dropNodeParent.insert(dragNode, dropNodeParent.getIndex(dropNode) + 1);
        }
    }

    private DefaultMutableTreeNode addNode(DefaultMutableTreeNode parent,
                                           String mediaName,
                                           String path) {
        DefaultMutableTreeNode node = new DefaultMutableTreeNode();
        MediaUserObject userObject = new MediaUserObject(node);
        node.setUserObject(userObject);
        userObject.setPath(path);
        
        // non-department node or branch
        if (new File(path).isDirectory()) {
            userObject.setText(mediaName);
            userObject.setLeaf(false);
            userObject.setExpanded(true);
            node.setAllowsChildren(true);
        }
        // department node
        else {
            userObject.setText(mediaName);
            userObject.setLeaf(true);
            node.setAllowsChildren(false);
        }
        // finally add the node to the parent. 
        if (parent != null) {
            parent.add(node);
        }

        return node;
    }

    private void getChildrenNodes(DefaultMutableTreeNode parent){
    	String parentPath = ((MediaUserObject)parent.getUserObject()).getPath();
    	//log.info("[getChildrenNodes] parent:#0, children:#1", parentDepartment.getName(),parentDepartment.getChildren());

    	if (new File(parentPath).exists()) {
			String[] files = new File(parentPath).list(new FilenameFilter() {
			    public boolean accept(File dir, String name) {
					return name.charAt(0) != '.';
			    }
			});		
			
			// All dirs
			for (String file : files) {
			    if (new File(parentPath, file).isDirectory()) {
			    	DefaultMutableTreeNode node;
			    	node = this.addNode(parent, file, parentPath+pathSeparator+file);
			    	getChildrenNodes( node);
			    }			   
			    if (!new File(parentPath, file).isDirectory()) {	
			    	DefaultMutableTreeNode node;
			    	node = this.addNode(parent, file, parentPath+pathSeparator+file);
			    }
			}
    	}	
    }
    
    protected void init() {
    	//this.actionMessage="";
    	log.info("[init]");
    	String  rootModel = "";
    	//rootModel="C:/csrg/mms_files/admin";
    	//String userName="admin";
    	String userName = this.user;//stateBean.getUser().getUsername().toString();
    	rootModel = "" + this.root;//stateBean.getConfig().getUploadDirectory()+userName;
    	
    	//DefaultMutableTreeNode rootNode = addNode(null, "admin",rootModel);

        if (! (new File(rootModel).exists())) 
        {
        	new File(rootModel).mkdir();
        }
        
        if (new File(rootModel).exists()) 
        {
			String[] files = new File(rootModel).list(new FilenameFilter() {
			    public boolean accept(File dir, String name) {
					return name.charAt(0) != '.';
			    }
			});					
			
			DefaultMutableTreeNode rootNode = addNode(null, userName,
	                rootModel);
		    MediaUserObject userObject = new MediaUserObject(rootNode);
		    rootNode.setUserObject(userObject);
		    userObject.setPath(rootModel);

		    userObject.setText(userName);
		    userObject.setLeaf(false);
		    
		    model = new DefaultTreeModel(rootNode);
	        selectedUserObject = (MediaUserObject) rootNode.getUserObject();
	        selectedUserObject.setExpanded(true);   
	        
		    userObject.setExpanded(true);
		    rootNode.setAllowsChildren(true);
		    
			// All dirs
			for (String file : files) {			    	
							         
			       
				if (new File(rootModel+pathSeparator+file).isDirectory()) {
					DefaultMutableTreeNode node1;
					node1 = this.addNode(rootNode, file, rootModel+pathSeparator+file);
					getChildrenNodes( node1);
				}		
				else
				{
					DefaultMutableTreeNode node1;
					node1 = this.addNode(rootNode, file, rootModel+pathSeparator+file);
				}
						
			}//end for
		 }// end if			
			
    }

    private DefaultMutableTreeNode findTreeNode(String path) {
        DefaultMutableTreeNode rootNode =
                (DefaultMutableTreeNode) model.getRoot();
        DefaultMutableTreeNode node;
        MediaUserObject tmp;
        Enumeration nodes = rootNode.depthFirstEnumeration();
        while (nodes.hasMoreElements()) {
            node = (DefaultMutableTreeNode) nodes.nextElement();
            tmp = (MediaUserObject) node.getUserObject();
            if (path.equals(String.valueOf(tmp.getPath()))) {
                return node;
            }
        }
        return null;
    }
    
    // Model events handling
	private final class ModelEventHandler extends ModelEventAdapter implements
			Serializable {
		@Override
		public void newModel(ModelEvent e){
			//log.info("[newModel] source:#0, class:#1", e.getSource(),e.getSource().getClass().toString());
			Model m = (Model) e.getSource();
			if(m instanceof Department){
				//init();
				queryOutdated=true;
			}
		}
		@Override
		public void modelUpdated(ModelEvent e){
			//log.info("[modelUpdated] source:#0, class:#1", e.getSource(),e.getSource().getClass().toString());
			Model m = (Model) e.getSource();
			if(m instanceof Department){
				//init();
				queryOutdated=true;
			}
		}
		@Override
		public void onAfterRemove(ModelEvent e){
			//log.info("[onAfterRemove] source:#0, class:#1", e.getSource(),e.getSource().getClass().toString());
			Model m = (Model) e.getSource();
			if(m instanceof Department){
				//init();
				queryOutdated=true;
			}
		}
	}
    
	private void registerModelListener() {
		log.info("[registerModelListener]");
		this.modelEventListener = new ModelEventHandler();
		ModelEventQueue.addMsgListener(this.modelEventListener);
	}

	private void unregisterModelListener() {
		if (this.modelEventListener != null) {
			log.info("[unregisterModelListener]");
			ModelEventQueue.removeMsgListener(this.modelEventListener);
		}
	}
	
	/***************************************************************** upload related functions ************************************************************************/
	public void uploadFile(ActionEvent event) {
		this.actionMessage="";
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
            try
            {	
            	String userFolder = "";
                
                String currentPath = selectedUserObject.getPath();//current selected file or folder
                
                if(new File(currentPath).isDirectory())
                {
                	userFolder = currentPath;
                }
                else
                {
                	userFolder = "" + currentPath.substring(0,currentPath.lastIndexOf(pathSeparator));
                }

               // note: need loop to check and create directory: manually option is set 
                //checking directory exists, if not create it
               // if(!(new File(path).exists()))
            	//{           	
            	//	new File(path).mkdir();
            	//}

                //String userName = this.user;//stateBean.getUser().getUsername().toString();
            	//String userFolder = path + "/"+userName;// add username to path
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
        		 log.info("[uploadFile] filenameOnly:#0, fileName:#1", fileNameOnly,fileName);
        		 String fileFullPath = userFolder + "/" + fileName;
        		
        		 int counter = 1;
        		 String  fileNameSuffix="";
        		 
        		 fileNameOnly = this.checkSuffix(fileNameOnly);
        		//check whether file name exists. if exists then add appropriate numeric suffix
        		 while(new File(fileFullPath).exists())
        		 {
        			 //creating file name suffix;
        			if(counter>9)
        			{
        				fileNameSuffix = "" + counter;
        			}
        			else
        			{
        				fileNameSuffix = "0" + counter;
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
        		log.info("[uploadFile] fileFullPath:#0", fileFullPath);
            	File file = new File(fileFullPath);
            	currentFile.getFileInfo().setFileName( fileName);
            	FileInputStream fis = new FileInputStream(currentFile.getFile());
            	fis.read(bytes);
            	FileOutputStream fos = new FileOutputStream(file);
            	fos.write(bytes);
            	fos.close();
            	
            	this.actionMessage+=messages.get("FileUploadSuccessMessage");
            	init();
            }catch(Exception e){
            	this.actionMessage+=messages.get("FileUploadFailedMessage");
            	e.printStackTrace();
            }
            /*synchronized (fileList) {
                fileList.add(currentFile);
            }*/

        }

    }
	
	private String checkSuffix(String filename){
		
		int cIndex = filename.length() - 3;
		char ch = '_';		
		if(filename.charAt(cIndex) == ch)
		{
			cIndex = filename.length() - 2;
			ch = filename.charAt(cIndex);
			int chInt = (int) ch;
			
			if(chInt >=0 && chInt<= 9)
			{
				cIndex = filename.length() - 1;
				ch = filename.charAt(cIndex);
				chInt = (int) ch;
				
				if(chInt >=0 && chInt<= 9)
				{
					cIndex = filename.length() - 4;
					return filename.substring(0, cIndex);
					
				}
				else return filename;
				
			}
			else return filename;
				
		}
		else return filename;
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
    
    /* Replace special characters in file name*/
    private String replaceSpecialCharInFileName(String fileName) {
  
    	String filen = fileName;
    	log.info("[replaceSpecialCharInFileName] filename size:#0", filen.length());
    	char[] chars = filen.toCharArray();
    	for(char c:chars){
    		log.info("[replaceSpecialCharInFileName] char:#0, hex:#1", c,Integer.toHexString((int)c));
    	}
    	log.info("[replaceSpecialCharInFileName] filen:#0", filen);
    	log.info("[replaceSpecialCharInFileName] fileName:#0", fileName);
    	//below two lines are used to test
    	//fileName=fileName.replace('A', 'X');
    	//fileName=fileName.replace('a', 'X');
    	fileName=fileName.replace(' ', '_');
    	log.info("[replaceSpecialCharInFileName] fileName:#0", fileName);
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
    	//fileName=fileName.replace('é', 'e');
    	fileName=fileName.replace("é", "e");
    	log.info("[replaceSpecialCharInFileName] fileName:#0", fileName);
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

    	log.info("[replaceSpecialCharInFileName] fileName:#0", fileName);
    	return fileName;
    }
    
    private boolean isValidExtension(String fileExtensionIncludingDot)
    {
    	boolean isValidExtension=false;
    	//String allowedExtensions = ".jpg .png .bmp .pdf .txt";
    	String allowedExtensions =  stateBean.getConfig().getAllowedFileExtensions();
    	if(allowedExtensions.indexOf(fileExtensionIncludingDot)>0)
    	{
    		isValidExtension=true;
    	}
    	return isValidExtension;
    }
    
    private boolean isEnoughDiskSpace(String userFolder)
    {
    	int maxDiskSpace = stateBean.getConfig().getMaxDiskSpace();
    	
    	return true;
    }

    private   long getFolderSize(File folder) {
    	
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
    	int maxFileSize =  stateBean.getConfig().getMaxFileSize();
    	
    	return true;
    }
    
	/***************************************************************** upload related functions ends ************************************************************************/


	/***************************************************************** mms functions ************************************************************************/
   
	public String createNewFolder() {
		this.actionMessage="";
		String fileName=this.selectedUserObject.getPath();
		
		//this.fileName="C:/csrg/mms_files/admin/";
		String parentPath="";
		if (!new File(fileName).isDirectory()) {	
			parentPath=""+fileName.substring(0,fileName.lastIndexOf(pathSeparator));
		}	
		else{
			parentPath=fileName;
		}
		String path=parentPath+pathSeparator+newFolderName;
		if(!(new File(path).exists()))
        	{           	
        		boolean isFoldercreated=new File(path).mkdir();
        		if(isFoldercreated)
        		{
        			this.actionMessage+=messages.get("CreateNewFolderSuccessMessage");
        			init();
        		}
        		else
        		{
        			this.actionMessage+=messages.get("CreateNewFolderFailedMessage");
        		}
        	}
		else
		{
			this.actionMessage+=messages.get("FolderExistMessage");
		}
		return "";
	}
	
	
	public String renameFileOrFolder() {	
		this.actionMessage="";
		try
		{			
			String fileName=this.selectedUserObject.getPath();
			
		    File oldfile = new File(fileName);  
			  if(oldfile.exists())	{	 
				  
				  String parentPath=""+fileName.substring(0,fileName.lastIndexOf(pathSeparator));
				  this.actionMessage+=""+parentPath+pathSeparator+newFolderName;
				   if(!(new File(parentPath+pathSeparator+newFolderName).exists()))
				   {
					   File newfile = new File(parentPath+pathSeparator+newFolderName);
					   boolean isRenamed = oldfile.renameTo(newfile);
					   if(isRenamed){
						   this.actionMessage=messages.get("FolderRenameSuccessMessage");
						  
						   init();
					   }
					   else{
						   this.actionMessage=messages.get("FolderRenameFailedMessage");
						   
					   }
				   }
				   else
				   {
					   this.actionMessage+=messages.get("FolderExistMessage");
					  
				   }
			   }	
			   else
			   {
				  
				   this.actionMessage+=messages.get("FolderOrFileNotFoundMessage");
			   }	
		   
		}
		catch(Exception exp)
		{						
			log.info("[renameFileOrFolder] Exception Message:#0", exp.getMessage().toString());
			
		}	      
	      return "";
	}
	
	private  boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i=0; i<children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
    
        // The directory is now empty so delete it
        return dir.delete();
    } 
	
	public String deleteFileOrFolder() {	
		this.actionMessage="";
		try
		{
			String fileName=this.selectedUserObject.getPath();
			
			if(fileName.equals(this.root))
			{
				 this.actionMessage+=messages.get("DeleteFailedMessage");
				  init();
				  return "";
			}	
			
			File selectedFile = new File(fileName);			  
			  if(selectedFile.exists())	{			
				 // if(selectedFile.isDirectory())
				   //boolean isDeleted = selectedFile.delete();
				  boolean isDeleted =deleteDir(selectedFile);
				   if(isDeleted){
					   this.actionMessage+=messages.get("DeleteSuccessMessage");
					   init();
				   }
				   else
				   {
					   this.actionMessage+=messages.get("DeleteFailedMessage");
				   }
			   }	
			   else
			   {
				   this.actionMessage+=messages.get("FolderOrFileNotFoundMessage");
			   }	
		   
		}
		catch(Exception exp)
		{					
			log.info("[deleteFileOrFolder] Exception Message:#0", exp.getMessage().toString());
			
		}	      
	      return "";
	}
	
	/***************************************************************** mms functions ends************************************************************************/


   
    //getters and setters
    

    public DefaultTreeModel getModel() {
    	//log.info("[getModel] queryAutdated:#0", this.queryOutdated);
    	if(this.queryOutdated)
    		init();
    	this.queryOutdated = false;
        return model;
    }
    public void setModel(DefaultTreeModel model) {
        this.model = model;
    }

    public boolean isQueryOutdated() {
		return queryOutdated;
	}

	public void setQueryOutdated(boolean queryOutdated) {
		this.queryOutdated = queryOutdated;
	}
    
    public NodeUserObject getSelectedUserObject() {
        return selectedUserObject;
    }
    
    
	
	public String getNewFolderName() {
		return newFolderName;
	}

	public void setNewFolderName(String newFolderName) {
		this.newFolderName = newFolderName;
	}
	
	
	
	public String getActionMessage() {
		return actionMessage;
	}

	public void setActionMessage(String actionMessage) {
		this.actionMessage = actionMessage;
	}
	
	public InputFileData getCurrentFile() {
        return currentFile;
    }

    public int getFileProgress() {
        return fileProgress;
    }
   

    public boolean isAutoUpload() {
        return autoUpload;
    }

    public void setAutoUpload(boolean autoUpload) {
        this.autoUpload = autoUpload;
    }

	public void setRoot(String root) {
		this.root = root;
	}

	public String getRoot() {
		return root;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getUser() {
		return user;
	}

	public void setAliasRoot(String aliasRoot) {
		this.aliasRoot = aliasRoot;
	}

	public String getAliasRoot() {
		this.aliasRoot = this.selectedUserObject.getPath().replace(this.UploadDirectory, "/");
		return aliasRoot;
	}

	public void setUploadDirectory(String uploadDirectory) {
		UploadDirectory = uploadDirectory;
	}

	public String getUploadDirectory() {
		return UploadDirectory;
	}

}
