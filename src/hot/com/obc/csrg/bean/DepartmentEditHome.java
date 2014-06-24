package com.obc.csrg.bean;

import org.jboss.seam.annotations.Name;

import com.obc.csrg.model.Department;

@Name("departmentEditHome")
public class DepartmentEditHome extends DepartmentHome<Department> {

	@Override
	protected void loadAfterCreate() {
		super.loadAfterCreate();
		this.setNewParent(instance.getParent());
		this.initParentList();
	}

	@Override
	protected void loadAfterUpdate() {
		super.loadAfterUpdate();
		this.setNewParent(instance.getParent());
		this.initParentList();
	}

	@Override
	protected boolean verifyData() {
		boolean result = false;
		if (super.verifyData()) {
			boolean valid = true;
			if(this.instance.getLocale()==null || this.instance.getLocale().equals(""))
					this.instance.setLocale(locale.getLanguage());
			//TODO: perform more intensive tests
			if (instance.getId() == null) {// new model
				instance.setParent(getNewParent());
				if (this.getNewParent() != null) {
					this.getNewParent().getChildren().add(this.instance);
				}
			} else if (!(instance.getParent() == null && this.getNewParent() == null)
					|| this.getNewParent()==null || !instance.getId().equals(this.getNewParent().getId())) {
				// parent changed
				if (this.getNewParent() != null) {
					// if new parent is one of the descendant
					Department directChild = instance.imParentOf(this
							.getNewParent());
					if (directChild != null) {// new parent is a descendant
						if (this.instance.getParent() != null) {
							this.instance.getParent().getChildren().add(
									directChild);
							this.instance.getParent().getChildren().remove(
									this.instance);
						}
						directChild.setParent(this.instance.getParent());
						this.instance.setParent(this.getNewParent());
						this.getNewParent().getChildren().add(this.instance);
					} else {// new parent it's not a descendant
						if (this.instance.getParent() != null) {
							this.instance.getParent().getChildren().remove(
									this.instance);
						}
						this.instance.setParent(this.getNewParent());
						this.getNewParent().getChildren().add(this.instance);
					}
				} else {// new parent is null
					if (this.instance.getParent() != null) {
						this.instance.getParent().getChildren().remove(
								this.instance);
					}
					this.instance.setParent(this.getNewParent());
				}
			}
			if (valid)
				result = true;
		}
		return result;
	}

}
