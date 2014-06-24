package com.obc.csrg.bean;

import java.util.List;
import java.util.ArrayList;
import java.util.TimeZone;
import java.util.Collections;
import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;


import com.icesoft.faces.component.selectinputtext.SelectInputText;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import org.jboss.seam.annotations.Name;

import com.obc.csrg.model.Config;
import com.obc.csrg.constants.Constants;

@Name("configEditHome")
public class ConfigEditHome extends ConfigHome<Config> {

	private boolean smtpAuth =  false;
	private String contactEmail;
    private String onRegistrationSenderEmail;
    
    private List<SelectItem> timezones = new ArrayList<SelectItem>();
    private List<SelectItem> matchedTimezones = new ArrayList<SelectItem>();
    
    private int timezoneListLength = 20;
    private String selectedTimezone = "";
    
    private int speed = 1000;//news ticker speed for second - milisecond conversion
    private int timeOut = 2000;//news ticker time for second - milisecond conversion
    
	
	@Override
	protected void loadAfterCreate(){
		super.loadAfterCreate();
		//load smtpAuth
		this.smtpAuth = this.getInstance().getSmtpAuth();
		this.selectedTimezone = this.instance.getTimezone();
		//load emails
		this.setContactEmail(this.getInstance().getContactEmail());
    	this.setOnRegistrationSenderEmail(this.getInstance().getOnRegistrationSenderEmail());
    	this.initTimezones();
    	
    	this.speed = convertToSecond(this.getInstance().getSpeed());
    	this.timeOut = convertToSecond(this.getInstance().getTimeOut());

    	
	}
	
	@Override
	protected boolean verifyData(){
		boolean result = false;
		if(super.verifyData()){
			boolean valid = true;
			//smtpAuth save
			this.getInstance().setSmtpAuth(smtpAuth);
			this.getInstance().setTimezone(selectedTimezone);
			//emails save
			valid&=this.saveEmails();
			if(valid) result = true;
			
			this.getInstance().setSpeed(convertToMilisecond(speed));
			this.getInstance().setTimeOut(convertToMilisecond(timeOut));

		}
		return result;
	}

	
	private int convertToMilisecond(int second)
	{
		return second * 1000;
	}

	private int convertToSecond(int miliSecond)
	{
		if(miliSecond <= 0) 
			return 0;
		else
			return  miliSecond/1000;
	}
	
	private boolean saveEmails(){
		boolean result=true;
		if(!this.getContactEmail().matches("^"+Constants.REGEXP_EMAIL+"$")) {
    		facesMessages.addToControl("emailContacto", messages.get("EmailInvalid"));
    		result = false;
    	}else{
    		this.getInstance().setContactEmail(this.getContactEmail());
    	}
    	if(!this.getOnRegistrationSenderEmail().matches("^"+Constants.REGEXP_EMAIL+"$")) {
    		facesMessages.addToControl("emailRegisto", messages.get("EmailInvalid"));
			result = false;
    	}else{
        	this.getInstance().setOnRegistrationSenderEmail(this.getOnRegistrationSenderEmail());
    	}
    	return result;
	}
	
	private void initTimezones(){
		timezones = new ArrayList<SelectItem>();
		String[] ids = TimeZone.getAvailableIDs();
		SortedSet<String> tzs = new TreeSet<String>();
		
		for(String id:ids){
			tzs.add(id);
			//timezones.add(new SelectItem(id,id));
		}
		for(String s:tzs){
			//log.info("[initTimezones] timezone:#0", s);
			timezones.add(new SelectItem(s,s));
		}
	}
	
	public void timezoneInputValueChanged(ValueChangeEvent event) {

        if (event.getComponent() instanceof SelectInputText) {

            // get the number of displayable records from the component
            SelectInputText autoComplete =
                    (SelectInputText) event.getComponent();
            // get the new value typed by component user.
            String newWord = (String) event.getNewValue();

            matchedTimezones = generateTimezonesMatches(newWord, timezoneListLength);

            // if there is a selected item then find the timezone object of the
            // same name
            if (autoComplete.getSelectedItem() != null) {
            	selectedTimezone = (String) autoComplete.getSelectedItem().getValue();
            }
            // if there was no selection we still want to see if a proper
            // timezone was typed and update our selectedCity instance.
            else{
                String tmp = getFindCityMatch(newWord);
                if (tmp != null){
                	selectedTimezone = tmp;
                }
            }

        }
    }
	
	/**
     * Utility method for finding detailed city information from the list of
     * possibile city names.
     *
     * @param cityName city name to do city search on.
     * @return found city object if any, null otherwise.
     */
    private String getFindCityMatch(String timezoneName) {
        if (matchedTimezones != null) {
            SelectItem timezone;
            for(int i = 0, max = matchedTimezones.size(); i < max; i++){
                timezone = (SelectItem)matchedTimezones.get(i);
                if (timezone.getLabel().compareToIgnoreCase(timezoneName) == 0) {
                    return (String) timezone.getValue();
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
    
	public ArrayList<SelectItem> generateTimezonesMatches(String searchWord, int maxMatches) {

        ArrayList<SelectItem> matchList = new ArrayList<SelectItem>(maxMatches);

        // ensure the autocomplete search word is present
        if ((searchWord == null) || (searchWord.trim().length() == 0)) {
            return matchList;
        }

        try {
            SelectItem searchItem = new SelectItem("", searchWord);
            int insert = Collections.binarySearch(
                    timezones,
                    searchItem,
                    LABEL_COMPARATOR);

            // less then zero if we have a partial match
            if (insert < 0) {
                insert = Math.abs(insert) - 1;
            }
            else {
                // If there are duplicates in a list, ensure we start from the first one
                if(insert != timezones.size() && LABEL_COMPARATOR.compare(searchItem, timezones.get(insert)) == 0) {
                    while(insert > 0 && LABEL_COMPARATOR.compare(searchItem, timezones.get(insert-1)) == 0) {
                        insert = insert - 1;
                    }
                }
            }
            
            for (int i = 0; i < maxMatches; i++) {
                // quit the match list creation if the index is larger than
                // max entries in the cityDictionary if we have added maxMatches.
                if ((insert + i) >= timezones.size() ||
                        i >= maxMatches) {
                    break;
                }
                matchList.add(timezones.get(insert + i));
            }
        } catch (Throwable e) {
            log.info("[generateTimezoneMatches] error finding matches :#0", e);
        }
        // assign new matchList
        return matchList;
    }
	//Getters and setters
	public String getContactEmail() {
		return contactEmail;
	}

	public void setContactEmail(String emailContacto) {
		this.contactEmail = emailContacto;
	}

	public String getOnRegistrationSenderEmail() {
		return onRegistrationSenderEmail;
	}

	public void setOnRegistrationSenderEmail(String onRegistrationSenderEmail) {
		this.onRegistrationSenderEmail = onRegistrationSenderEmail;
	}

	public boolean isSmtpAuth() {
		return smtpAuth;
	}

	public void setSmtpAuth(boolean smtpAuth) {
		this.smtpAuth = smtpAuth;
	}
	
	public List<SelectItem> getTimezones() {
		return timezones;
	}

	public void setTimezones(List<SelectItem> timezones) {
		this.timezones = timezones;
	}

	public List<SelectItem> getMatchedTimezones() {
		return matchedTimezones;
	}

	public void setMatchedTimezones(List<SelectItem> matchedTimezones) {
		this.matchedTimezones = matchedTimezones;
	}
	
	public int getTimezoneListLength() {
		return timezoneListLength;
	}

	public void setTimezoneListLength(int timezoneListLength) {
		this.timezoneListLength = timezoneListLength;
	}
	
	public String getSelectedTimezone() {
		return selectedTimezone;
	}

	public void setSelectedTimezone(String selectedTimezone) {
		this.selectedTimezone = selectedTimezone;
	}
	
	// getters and setters
	public void setSpeed(int speed) {
		this.speed = speed;
	}


	public int getSpeed() {
		return speed;
	}

	public void setTimeOut(int timeOut) {
		this.timeOut = timeOut;
	}


	public int getTimeOut() {
		return timeOut;
	}

}
