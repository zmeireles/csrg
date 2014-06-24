package com.obc.csrg.events;

import java.util.EventListener;

public interface ModelEventListener extends EventListener{
	public void newModel(ModelEvent e);
	public void modelUpdated(ModelEvent e);
	public void modelUpdated(ModelEvent e,String property, Object oldValue,Object newValue);
	public void onBeforeRemove(ModelEvent e);
	public void onAfterRemove(ModelEvent e);
	public void personIdDataChanged(ModelEvent e,String property, Object oldValue, Object newValue);
}
