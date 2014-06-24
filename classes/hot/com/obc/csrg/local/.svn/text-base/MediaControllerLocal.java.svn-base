package com.obc.csrg.local;

import java.util.EventObject;
import java.util.List;

import javax.faces.event.ActionEvent;

import com.obc.csrg.util.InputFileData;

public interface MediaControllerLocal {

	public void uploadFile(ActionEvent event);
	public void fileUploadProgress(EventObject event);
	public void removeUploadedFile(ActionEvent event);
	public InputFileData getCurrentFile();
    public int getFileProgress();
    public List getFileList();
    public boolean isAutoUpload();
    public void setAutoUpload(boolean autoUpload);
}
