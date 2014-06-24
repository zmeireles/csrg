package com.obc.csrg.bean;

import org.jboss.seam.annotations.Name;

import com.obc.csrg.model.GeographicArea;

@Name("geographicAreaEditHome")
public class GeographicAreaEditHome extends GeographicAreaHome<GeographicArea> {

	
	@Override
	protected void loadAfterUpdate(){
		super.loadAfterUpdate();
	}
	@Override
	protected void loadAfterPersist(){
		super.loadAfterPersist();
	}
	
	@Override
	protected boolean verifyData(){
		boolean result = false;
		if(super.verifyData()){
			boolean valid = true;
			if(this.instance.getLocale()==null || this.instance.getLocale().equals(""))
				this.instance.setLocale(locale.getLanguage());
			
			//take care of parent management
			if(this.instance.getId()==null){
				this.instance.setParent(this.getSelectedParent());
				if(this.getSelectedParent()!=null)
					this.getSelectedParent().getChildren().add(this.instance);
			}else if(!(instance.getParent() == null && this.getSelectedParent() == null)
					|| this.getSelectedParent()==null || !instance.getId().equals(this.getSelectedParent().getId())){
				//parent changed
				if (this.getSelectedParent() != null) {
					// if new parent is one of the descendant
					GeographicArea directChild = instance.imParentOf(this
							.getSelectedParent());
					if (directChild != null) {// new parent is a descendant
						if (this.instance.getParent() != null) {
							this.instance.getParent().getChildren().add(
									directChild);
							this.instance.getParent().getChildren().remove(
									this.instance);
						}
						directChild.setParent(this.instance.getParent());
						this.instance.setParent(this.getSelectedParent());
						this.getSelectedParent().getChildren().add(this.instance);
					} else {// new parent it's not a descendant
						if (this.instance.getParent() != null) {
							this.instance.getParent().getChildren().remove(
									this.instance);
						}
						this.instance.setParent(this.getSelectedParent());
						this.getSelectedParent().getChildren().add(this.instance);
					}
				} else {// new parent is null
					if (this.instance.getParent() != null) {
						this.instance.getParent().getChildren().remove(
								this.instance);
					}
					this.instance.setParent(this.getSelectedParent());
				}
			}
			if(valid) result = true;
		}
		return result;
	}

}
