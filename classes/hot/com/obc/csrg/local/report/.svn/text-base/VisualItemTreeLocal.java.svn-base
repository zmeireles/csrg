package com.obc.csrg.local.report;

import java.util.ArrayList;

import javax.ejb.Local;
import javax.faces.event.ActionEvent;
import javax.swing.tree.DefaultTreeModel;

import com.icesoft.faces.context.effects.Effect;
import com.obc.csrg.tree.NodeUserObject;

@Local
public interface VisualItemTreeLocal {

	public void create();
	public void destroy();
	public void visualItemNodeSelected(ActionEvent event);
	public DefaultTreeModel getModel();
    public void setModel(DefaultTreeModel model);
    public NodeUserObject getSelectedUserObject();
    public ArrayList getSelectedTreePath();
    public Effect getValueChangeEffect();
    public void setValueChangeEffect(Effect valueChangeEffect);
}
