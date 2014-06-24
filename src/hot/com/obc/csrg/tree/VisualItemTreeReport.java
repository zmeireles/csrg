package com.obc.csrg.tree;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import java.io.Serializable;

import javax.ejb.Remove;
import javax.ejb.Stateful;
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
import com.icesoft.faces.component.tree.IceUserObject;
import com.obc.csrg.local.report.VisualItemTreeLocal;
import com.obc.csrg.model.VisualItem;
import com.obc.csrg.model.Model;
import com.obc.csrg.util.FacesUtils;

@Name("visualItemTree")
@Stateful
@Scope(ScopeType.SESSION)
public class VisualItemTreeReport extends BaseBean implements VisualItemTreeLocal, Serializable{
	
	@Logger 
	private Log log;
	
	@In
	private EntityManager entityManager;

	private DefaultTreeModel model;
	private ModelUserObject<VisualItem> selectedUserObject;
	
	protected ModelEventListener modelEventListener;
	
	private boolean queryOutdated = false;
	

	@Begin(join=true)
	@Create
	public void create() {
		this.registerModelListener();
		init();
	}
	
	@Remove
	public void destroy() {
		this.unregisterModelListener();
	}
	
    public void visualItemNodeSelected(ActionEvent event) {
        // get visualItem id
        String visualItemId = FacesUtils.getRequestParameter("visualItemId");

        log.info("[visualItemNodeSelected] visualItemId:#0", visualItemId);
        // find visualItem node by id and make it the selected node
        DefaultMutableTreeNode node = findTreeNode(visualItemId);
        selectedUserObject = (ModelUserObject<VisualItem>) node.getUserObject();
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
                                           VisualItem visualItem) {
        DefaultMutableTreeNode node = new DefaultMutableTreeNode();
        ModelUserObject<VisualItem> userObject = new ModelUserObject<VisualItem>(node);
        node.setUserObject(userObject);
        userObject.instance = visualItem;

        // non-visualItem node or branch
        if (title != null) {
            userObject.setText(title);
            userObject.setLeaf(false);
            userObject.setExpanded(true);
            node.setAllowsChildren(true);
        }
        // visualItem node
        else {
            userObject.setText(visualItem.getMenuText());
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
    	VisualItem parentVisualItem = ((ModelUserObject<VisualItem>)parent.getUserObject()).instance;
    	//log.info("[getChildrenNodes] parent:#0, children:#1", parentVisualItem.getName(),parentVisualItem.getChildren());
    	
    	List<VisualItem> children =entityManager.createQuery("select m from VisualItem m " +
							"where m.parent.id is not null and m.parent.id=? order by m.itemOrder")
							.setParameter(1, parentVisualItem.getId())
							.getResultList();
    	for(VisualItem d:children){
    		DefaultMutableTreeNode node;
    		if(d.getChildren().size()==0)
    			node = this.addNode(parent, null, d);
    		else{
    			node = this.addNode(parent, d.getMenuText(), d);
    			getChildrenNodes(node);
    		}
    		//log.info("[getChildrenNodes] nodeAdded:#0", d.getName());
    	}
    }
    protected void init() {
    	
    	log.info("[init]");
    	VisualItem rootModel = new VisualItem();
    	rootModel.setMenuText("CSRG");
    	
    	DefaultMutableTreeNode rootNode = addNode(null, "CSRG",
                rootModel);
        model = new DefaultTreeModel(rootNode);
        selectedUserObject = (ModelUserObject<VisualItem>) rootNode.getUserObject();
        selectedUserObject.setExpanded(true);
        
        if(entityManager==null)
        	entityManager = (EntityManager) Component.getInstance("entityManager",true);
    	List<VisualItem> rootModels = entityManager.createQuery(
    			"select m from VisualItem m where m.parent.id is null order by m.itemOrder")
    			.getResultList();
    	for(VisualItem d: rootModels){
    		DefaultMutableTreeNode node;
    		if(d.getChildren().size()==0)
    			node = this.addNode(rootNode, null, d);
    		else{
    			node = this.addNode(rootNode, d.getMenuText(), d);
    			getChildrenNodes(node);
    		}
    		//log.info("[init] nodeAdded:#0", d.getName());
    		
    	}
    }

    private DefaultMutableTreeNode findTreeNode(String nodeId) {
        DefaultMutableTreeNode rootNode =
                (DefaultMutableTreeNode) model.getRoot();
        DefaultMutableTreeNode node;
        ModelUserObject<VisualItem> tmp;
        Enumeration nodes = rootNode.depthFirstEnumeration();
        while (nodes.hasMoreElements()) {
            node = (DefaultMutableTreeNode) nodes.nextElement();
            tmp = (ModelUserObject<VisualItem>) node.getUserObject();
            if (nodeId.equals(String.valueOf(tmp.instance.getId()))) {
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
			log.info("[newModel] source:#0, class:#1", e.getSource(),e.getSource().getClass().toString());
			Model m = (Model) e.getSource();
			if(m instanceof VisualItem){
				//init();
				queryOutdated = true;
			}
		}
		@Override
		public void modelUpdated(ModelEvent e){
			log.info("[modelUpdated] source:#0, class:#1", e.getSource(),e.getSource().getClass().toString());
			Model m = (Model) e.getSource();
			if(m instanceof VisualItem){
				//init();
				queryOutdated = true;
			}
		}
		@Override
		public void onAfterRemove(ModelEvent e){
			//log.info("[onAfterRemove] source:#0, class:#1", e.getSource(),e.getSource().getClass().toString());
			Model m = (Model) e.getSource();
			if(m instanceof VisualItem){
				//init();
				queryOutdated = true;
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
    //getters and setters
    
    public DefaultTreeModel getModel() {
    	if(this.queryOutdated)
    		init();
    	this.queryOutdated = false;
        return model;
    }

    public void setModel(DefaultTreeModel model) {
        this.model = model;
    }

    public NodeUserObject getSelectedUserObject() {
        return selectedUserObject;
    }

    public boolean isQueryOutdated() {
		return queryOutdated;
	}

	public void setQueryOutdated(boolean queryOutdated) {
		this.queryOutdated = queryOutdated;
	}
}
