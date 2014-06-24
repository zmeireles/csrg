package com.obc.csrg.action;

import java.io.Serializable;

import java.util.List;

import javax.ejb.Stateless;
import javax.faces.event.ActionEvent;
import javax.persistence.EntityManager;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.log.Log;

import java.util.ArrayList;
import java.util.Enumeration;

import com.icesoft.faces.component.dragdrop.DropEvent;
import com.icesoft.faces.component.ext.HtmlPanelGroup;
import com.icesoft.faces.component.tree.IceUserObject;

import com.obc.csrg.local.StateBeanLocal;
import com.obc.csrg.local.TreeControllerLocal;
import com.obc.csrg.tree.DepartmentUserObject;
import com.obc.csrg.tree.NodeUserObject;
import com.obc.csrg.util.FacesUtils;
import com.obc.csrg.model.Department;

@Stateless
@Name("treeController")
public class TreeController extends BaseBean implements Serializable, TreeControllerLocal {

	@In
	private EntityManager entityManager;
	
	private DefaultTreeModel model;
    private DepartmentUserObject selectedUserObject;
    
    /**
     * Construct the default tree structure by combining tree nodes.
     */
    public TreeController() {
    	logger.info("[Constructor]");
        init();
    }

    public DefaultTreeModel getModel() {
        return model;
    }

    public void setModel(DefaultTreeModel model) {
        this.model = model;
    }

    public NodeUserObject getSelectedUserObject() {
        return selectedUserObject;
    }

    public void departmentNodeSelected(ActionEvent event) {
        // get department id
        String departmentId = FacesUtils.getRequestParameter("departmentId");

        // find department node by id and make it the selected node
        DefaultMutableTreeNode node = findTreeNode(departmentId);
        selectedUserObject = (DepartmentUserObject) node.getUserObject();
        // fire effects.);
        valueChangeEffect.setFired(false);
    }

    public ArrayList getSelectedTreePath() {
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
                                           String title,
                                           Department department) {
        DefaultMutableTreeNode node = new DefaultMutableTreeNode();
        DepartmentUserObject userObject = new DepartmentUserObject(node);
        node.setUserObject(userObject);
        userObject.setDepartment(department);

        // non-department node or branch
        if (title != null) {
            userObject.setText(title);
            userObject.setLeaf(false);
            userObject.setExpanded(true);
            node.setAllowsChildren(true);
        }
        // department node
        else {
            userObject.setText(department.getName());
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
    	Department parentDepartment = ((DepartmentUserObject)parent.getUserObject()).getDepartment();
    	for(Department d:parentDepartment.getChildren()){
    		DefaultMutableTreeNode node = this.addNode(parent, d.getName(), d);
    		this.getChildrenNodes(node);
    	}
    }
    protected void init() {
    	
    	Department rootModel = new Department();
    	rootModel.setName("CSRG");
    	
    	DefaultMutableTreeNode rootNode = addNode(null, "CSRG",
                rootModel);
        model = new DefaultTreeModel(rootNode);
        selectedUserObject = (DepartmentUserObject) rootNode.getUserObject();
        selectedUserObject.setExpanded(true);
        
    	List<Department> rootModels = entityManager.createQuery(
    			"select m from Department m where m.parent.id is null")
    			.getResultList();
    	for(Department d: rootModels){
    		DefaultMutableTreeNode node = this.addNode(rootNode, rootModel.getName(), rootModel);
    		
    		getChildrenNodes(node);
    		
    	}
    }

    private DefaultMutableTreeNode findTreeNode(String nodeId) {
        DefaultMutableTreeNode rootNode =
                (DefaultMutableTreeNode) model.getRoot();
        DefaultMutableTreeNode node;
        DepartmentUserObject tmp;
        Enumeration nodes = rootNode.depthFirstEnumeration();
        while (nodes.hasMoreElements()) {
            node = (DefaultMutableTreeNode) nodes.nextElement();
            tmp = (DepartmentUserObject) node.getUserObject();
            if (nodeId.equals(String.valueOf(tmp.getDepartment().getId()))) {
                return node;
            }
        }
        return null;
    }
    
}
