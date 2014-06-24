package com.obc.csrg.local.report;

import java.util.ArrayList;
import java.util.EventObject;

import javax.ejb.Local;
import javax.faces.event.ActionEvent;
import javax.swing.tree.DefaultTreeModel;

import com.icesoft.faces.context.effects.Effect;
import com.obc.csrg.tree.NodeUserObject;
import com.obc.csrg.util.InputFileData;

@Local
public interface MediaTreeLocal {


	public void create();
	public void destroy();
	public void mediaNodeSelected(ActionEvent event);
	public DefaultTreeModel getModel();
	public void setModel(DefaultTreeModel model);
	public NodeUserObject getSelectedUserObject();
	public ArrayList getSelectedTreePath();
	public Effect getValueChangeEffect();
	public void setValueChangeEffect(Effect valueChangeEffect);
	
	public String createNewFolder();
	public String renameFileOrFolder();
	public String deleteFileOrFolder();
	public void uploadFile(ActionEvent event);
	public void fileUploadProgress(EventObject event);
	public String getNewFolderName();
	public void setNewFolderName(String newFolderName);
	public String getActionMessage();
	public void setActionMessage(String actionMessage);
	public InputFileData getCurrentFile();
	public int getFileProgress();
	public boolean isAutoUpload();
	public void setAutoUpload(boolean autoUpload);
	public void setAliasRoot(String aliasRoot);
	public String getAliasRoot();
	
	

}
