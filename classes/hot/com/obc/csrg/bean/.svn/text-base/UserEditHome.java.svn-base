package com.obc.csrg.bean;


import javax.faces.event.ValueChangeEvent;

import org.jboss.seam.annotations.Name;

import com.obc.csrg.model.User;
import com.obc.csrg.model.DBFile;
import com.obc.csrg.util.GeographicAreaUtil;

@Name("userEditHome")
public class UserEditHome extends UserHome<User> {

	private GeographicAreaUtil geographicAreaUtil = null;
	
	//business logic
	@Override
	protected void loadAfterCreate(){
		super.loadAfterCreate();
		//log.info("[loadAfterCreate] photo:#0", this.getInstance().getPerson().getPhoto());
		this.setPriFile(new DBFile());
		if(this.getInstance().getPerson().getPhoto()!=null){
			this.getPriFile().setData(this.getInstance().getPerson().getPhoto());
		}
		if(this.instance.getPerson().getGeographicArea()!=null)
			geographicAreaUtil = new GeographicAreaUtil(this.instance.getPerson().getGeographicArea());
		else
			geographicAreaUtil = new GeographicAreaUtil(stateBean.getConfig().getCountry());
		//geographicAreaUtil.setCountry(stateBean.getConfig().getCountry());
		
		//log.info("[loadAfterCreate] country:#0", stateBean.getConfig().getCountry());
		geographicAreaUtil.setLog(log);
	}
	
	@Override
	protected boolean verifyData(){
		boolean result = false;
		if(super.verifyData()){
			boolean valid = true;
			log.info("[verifyData] newUsername:#0", this.getNewUsername());
			valid = this.usernameValid;
			if(valid){
				if(!this.getNewUsername().equals(instance.getUsername()))
					instance.setUsername(this.getNewUsername());
				if(this.instance.getLocale()==null || this.instance.getLocale().equals(""))
					this.instance.setLocale(locale.getLanguage());
				//log.info("[verifyData] priFile:#0", this.getPriFile().getData());
				if(this.fileIsImage(this.getPriFile()) || true){
					//log.info("[verifyData] priFile is image!!!");
					this.getInstance().getPerson().setPhoto(this.getPriFile().getData());
				}
				log.info("[verifyData] level:#0, geo:#1", this.geographicAreaUtil.getSelectedLevel(),
						this.geographicAreaUtil.getGeographicArea(this.geographicAreaUtil.getSelectedLevel()));
				this.instance.getPerson().setGeographicArea(this.geographicAreaUtil.getGeographicArea(this.geographicAreaUtil.getSelectedLevel()));
				this.instance.getPerson().setZipCode(mainZipCode+subZipCode);
			}
			if(valid) result = true;
		}
		return result;
	}

	public void valueChangeListener(ValueChangeEvent event){
		log.info("[valueChangeListener] region:#0", this.geographicAreaUtil.getRegion());
	}
	//getters and setters
	public GeographicAreaUtil getGeographicAreaUtil() {
		return geographicAreaUtil;
	}

	public void setGeographicAreaUtil(GeographicAreaUtil geographicAreaUtil) {
		this.geographicAreaUtil = geographicAreaUtil;
	}
}
