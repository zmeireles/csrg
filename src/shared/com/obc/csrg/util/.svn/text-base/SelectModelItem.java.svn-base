package com.obc.csrg.util;

import com.obc.csrg.model.Model;

public class SelectModelItem<E extends Model> {

	private boolean selected = false;
	private boolean calculated = false;// if true the item was inserted using a
										// method. if false, it was manually
										// added

	protected E instance;

	private Class<E> entityClass;

	// constructors
	public SelectModelItem(E instance) {
		this.instance = instance;
	}

	public SelectModelItem(E instance,boolean calculated) {
		this.instance = instance;
		this.calculated = calculated;
	}
	public SelectModelItem(E instance,boolean calculated, boolean selected) {
		this.selected = selected;
		this.calculated = calculated;
		this.instance = instance;
	}
	
	// business methods
	@Override
	public String toString() {
		return instance.toString();
	}
	
	public int hashCode() {
		return (this.instance.getId() == null) ? 0 : this.instance.getId().hashCode();
	}
	public boolean equals(Object object) {
		if(object==null)
			return false;
		if (object instanceof SelectModelItem && ((SelectModelItem)object).instance.getClass().equals(instance.getClass())) {
			
			final Model obj = (Model) ((SelectModelItem)object).instance;
			return (this.instance.getId() != null) ? this.instance.getId().equals(obj.getId())
					: (obj.getId() == null);
		}
		return false;
	}

	// getters and setters

	public Class<E> getEntityClass() {
		return entityClass;
	}

	public void setEntityClass(Class<E> entityClass) {
		this.entityClass = entityClass;
	}

	public E getInstance() {
		return instance;
	}

	public void setInstance(E instance) {
		this.instance = instance;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	
	public boolean isCalculated() {
		return calculated;
	}

	public void setCalculated(boolean calculated) {
		this.calculated = calculated;
	}
}
