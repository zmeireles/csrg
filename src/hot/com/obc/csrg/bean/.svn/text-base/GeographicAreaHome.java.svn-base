package com.obc.csrg.bean;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;
import java.io.Serializable;

import javax.ejb.Remove;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import org.jboss.seam.annotations.Begin;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.Destroy;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.web.RequestParameter;


import com.icesoft.faces.component.selectinputtext.SelectInputText;
import com.obc.csrg.model.GeographicArea;
import com.obc.csrg.model.Model;
import com.obc.csrg.events.ModelEventAdapter;
import com.obc.csrg.events.ModelEvent;
import com.obc.csrg.events.ModelEventQueue;
import com.obc.csrg.constants.GeographicAreaTypeEnum;

@Name("geographicAreaHome")
public class GeographicAreaHome<E extends Model> extends
		ObcEntityHome<GeographicArea> {

	@RequestParameter
	Long geographicAreaId;

	private int parentListLength = 20;
	private List<SelectItem> parents = new ArrayList<SelectItem>();
	private List<SelectItem> matchedParents = new ArrayList<SelectItem>();
	
	private List<SelectItem> levelItems = new ArrayList<SelectItem>();
	
	private GeographicArea selectedParent=null;
	private String selectedParentName="";

	// Business functions
	@Override
	public Object getId() {
		if (geographicAreaId == null)
			return super.getId();
		else
			return geographicAreaId;

	}

	@Override
	@Create
	@Begin(join=true)
	public void create(){
		super.create();
		this.registerModelListener();
	}
	
	@Override
	@Remove
	@Destroy
	public void destroy(){
		super.destroy();
		this.unregisterModelListener();
	}
	
	@Override
	protected void loadAfterCreate() {
		super.loadAfterCreate();
		//this.initParents();
		this.initLevelItems();
		log.info("[loadAfterCreate]");
		if(this.instance.getId()!=null){
			this.selectedParent = this.instance.getParent();
			if(this.selectedParent!=null)
				this.selectedParentName = this.selectedParent.returnFullName(); 
		}
	}
	
	private void initLevelItems(){
		levelItems.clear();
		for(GeographicAreaTypeEnum gaEnum:GeographicAreaTypeEnum.values()){
			levelItems.add(new SelectItem(gaEnum,messages.get(gaEnum.getName())));
		}
		log.info("[initLevelItems] items:#0", levelItems.size());
	}
	private void initParents(){
		parents = new ArrayList<SelectItem>();
		List<GeographicArea> parentList=null;
		if(this.getInstance().getId()==null)
			parentList = entityManager.createQuery("select m from GeographicArea m order by m.name")
							.getResultList();
		else
			parentList = entityManager.createQuery("select m from GeographicArea m where " +
							"m.id!=? order by m.name")
							.setParameter(1, this.instance.getId())
							.getResultList();
		for(GeographicArea ga:parentList){
			parents.add(new SelectItem(ga,ga.getName()));
		}
	}

	//Model events handling
	private final class ModelEventHandler extends ModelEventAdapter implements Serializable{
		@Override
		public void newModel(ModelEvent e){
			Model m = (Model) e.getSource();
			
		}
		@Override
		public void onBeforeRemove(ModelEvent e){
			Model m = (Model) e.getSource();
			
		}
		@Override
		public void modelUpdated(ModelEvent e,String property,Object oldValue, Object newValue){
			Model m = (Model) e.getSource();
		}
	}
	
	private void registerModelListener(){
		log.info("[registerModelListener] this.modelEventListener:#0",this.modelEventListener);
		if(this.modelEventListener!=null)
			this.unregisterModelListener();
		this.modelEventListener = new ModelEventHandler();
		ModelEventQueue.addMsgListener(this.modelEventListener);
	}
	
	private void unregisterModelListener(){
		if(this.modelEventListener!=null){
			log.info("[unregisterModelListener]");
			ModelEventQueue.removeMsgListener(this.modelEventListener);
		}
	}
	
	public void valueChanged(ValueChangeEvent event) {

        if (event.getComponent().getId().equals("parentsAutoComplete")) {

            // get the number of displayable records from the component
            SelectInputText autoComplete =
                    (SelectInputText) event.getComponent();
            // get the new value typed by component user.
            String newValue = (String) event.getNewValue();

            matchedParents = generateParentMatches(newValue, parentListLength);

            this.selectedParent = null;
            
            // if there is a selected item then find the geographic area object of the
            // same name
            if (autoComplete.getSelectedItem() != null) {
            	selectedParent = (GeographicArea) autoComplete.getSelectedItem().getValue();
            	this.selectedParentName = selectedParent.returnFullName();
            }
            // if there was no selection we still want to see if a proper
            // name was typed and update our selected instance.
            else{
                GeographicArea tmp = getFindParentMatch(newValue);
                if (tmp != null){
                	selectedParent = tmp;
                	selectedParentName = selectedParent.returnFullName();
                }
            }
        }else if(event.getComponent().getId().equals("level")){
        	this.instance.setLevel((GeographicAreaTypeEnum)event.getNewValue());
        	this.selectedParent=null;
        	this.selectedParentName = "";
        }
    }
	public boolean isCountry(){
		return this.instance.getLevel()==GeographicAreaTypeEnum.COUNTRY;
	}
	
	private GeographicArea getFindParentMatch(String parentName) {
        if (matchedParents != null) {
            SelectItem parent;
            for(int i = 0, max = matchedParents.size(); i < max; i++){
                parent = (SelectItem)matchedParents.get(i);
                if (parent.getLabel().compareToIgnoreCase(parentName) == 0) {
                    return (GeographicArea) parent.getValue();
                }
            }
        }
        return null;
    }
	
	/**
     * Comparator utility for sorting names.
     */
    private static final Comparator LABEL_COMPARATOR = new Comparator() {

        // compare method for city entries.
        public int compare(Object o1, Object o2) {
            SelectItem selectItem1 = (SelectItem) o1;
            SelectItem selectItem2 = (SelectItem) o2;
            // compare ignoring case, give the user a more automated feel when typing
            return selectItem1.getLabel().compareToIgnoreCase(selectItem2.getLabel());
        }
    };
    
	public ArrayList<SelectItem> generateParentMatches(String searchValue, int maxMatches) {

        ArrayList<SelectItem> matchList = new ArrayList<SelectItem>(maxMatches);

        // ensure the autocomplete search word is present
        if ((searchValue == null) || (searchValue.trim().length() == 0)) {
            return matchList;
        }

        try {
        	
        	searchValue = ObcEntityHome.toSearchRegExp(searchValue.toLowerCase());
        	List<GeographicArea> matches = entityManager.createQuery(
        			"select m from GeographicArea m where level=? and lower(m.name) like ?")
        			.setParameter(1, this.instance.getLevel().getPrevious())
        			.setParameter(2, searchValue)
        			.getResultList();
        	
        	int i=1;
        	for(GeographicArea ga:matches){
        		if(i>maxMatches)
        			break;
        		matchList.add(new SelectItem(ga,ga.getParent()==null ? 
        				ga.getName() : ga.getName() +" -> " + ga.getParent().getName()));
        	}
            
        } catch (Throwable e) {
            log.info("[generateParentMatches] error finding matches :#0", e);
        }
        // assign new matchList
        return matchList;
    }
	
	// getters and setters

	public int getParentListLength() {
		return parentListLength;
	}

	public void setParentListLength(int parentListLength) {
		this.parentListLength = parentListLength;
	}
	
	public List<SelectItem> getMatchedParents() {
		return matchedParents;
	}

	public void setMatchedParents(List<SelectItem> matchedParents) {
		this.matchedParents = matchedParents;
	}
	
	public List<SelectItem> getParents() {
		return parents;
	}

	public void setParents(List<SelectItem> parents) {
		this.parents = parents;
	}

	public GeographicArea getSelectedParent() {
		return selectedParent;
	}

	public void setSelectedParent(GeographicArea selectedParent) {
		this.selectedParent = selectedParent;
	}

	public String getSelectedParentName() {
		return selectedParentName;
	}

	public void setSelectedParentName(String selectedParentName) {
		this.selectedParentName = selectedParentName;
	}
	
	public List<SelectItem> getLevelItems() {
		if(this.levelItems.size()==0)
			this.initLevelItems();
		return levelItems;
	}

	public void setLevelItems(List<SelectItem> levelItems) {
		this.levelItems = levelItems;
	}
}
