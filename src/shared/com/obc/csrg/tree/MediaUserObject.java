package com.obc.csrg.tree;

import java.io.Serializable;

import javax.swing.tree.DefaultMutableTreeNode;

import com.obc.csrg.model.Department;

public class MediaUserObject extends NodeUserObject implements Serializable{
	
	private String path;

	public MediaUserObject(DefaultMutableTreeNode defaultMutableTreeNode) {
        super(defaultMutableTreeNode);
    }
	
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		
		this.path = path;
	}

}
