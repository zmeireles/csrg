package com.obc.csrg.tree;

import java.io.Serializable;

import javax.swing.tree.DefaultMutableTreeNode;

import com.obc.csrg.model.Model;

public class ModelUserObject<E extends Model> extends NodeUserObject implements Serializable{
	
	protected E instance;

	public ModelUserObject(DefaultMutableTreeNode defaultMutableTreeNode) {
        super(defaultMutableTreeNode);
    }
	
	public E getInstance() {
		return instance;
	}

	public void setInstance(E instance) {
		this.instance = instance;
	}
	
}
