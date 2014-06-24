package com.obc.csrg.events;

import java.util.Map;

import javax.swing.event.EventListenerList;

public class ModelEventQueue {

	private static final EventListenerList msgListeners = new EventListenerList();

	public static void addMsgListener(final ModelEventListener listener) {
		msgListeners.add(ModelEventListener.class, listener);
	}

	public static void removeMsgListener(final ModelEventListener listener) {
		msgListeners.remove(ModelEventListener.class, listener);
	}

	public static void fireNewModelEvent(final Object source) {
		if (source != null) {
			if (msgListeners.getListenerCount() > 0) {
				final ModelEvent modelEvent = new ModelEvent(source);

				final Object[] listeners = msgListeners.getListenerList();
				for (int i = 0; i < listeners.length; i += 2) {
					if(listeners[i + 1]!=null)
						((ModelEventListener) listeners[i + 1])
								.newModel(modelEvent);
				}
			}
		}
	}

	public static void fireOnBeforeModelRemoveEvent(final Object source) {
		if (source != null) {
			if (msgListeners.getListenerCount() > 0) {
				final ModelEvent modelEvent = new ModelEvent(source);

				final Object[] listeners = msgListeners.getListenerList();
				for (int i = 0; i < listeners.length; i += 2) {
					if(listeners[i + 1]!=null)
						((ModelEventListener) listeners[i + 1])
							.onBeforeRemove(modelEvent);
				}
			}
		}
	}
	
	public static void fireOnAfterModelRemoveEvent(final Object source) {
		if (source != null) {
			if (msgListeners.getListenerCount() > 0) {
				final ModelEvent modelEvent = new ModelEvent(source);

				final Object[] listeners = msgListeners.getListenerList();
				for (int i = 0; i < listeners.length; i += 2) {
					if(listeners[i + 1]!=null)
						((ModelEventListener) listeners[i + 1])
							.onAfterRemove(modelEvent);
				}
			}
		}
	}

	public static void fireModelUpdatedEvent(final Object source) {
		if (source != null) {
			if (msgListeners.getListenerCount() > 0) {
				final ModelEvent modelEvent = new ModelEvent(source);

				final Object[] listeners = msgListeners.getListenerList();
				for (int i = 0; i < listeners.length; i += 2) {
					if(listeners[i + 1]!=null)
						((ModelEventListener) listeners[i + 1])
							.modelUpdated(modelEvent);
				}
			}
		}
	}
	
	public static void fireModelUpdatedEvent(final Object source,String property, Object oldValue, Object newValue) {
		if (source != null) {
			if (msgListeners.getListenerCount() > 0) {
				final ModelEvent modelEvent = new ModelEvent(source);

				final Object[] listeners = msgListeners.getListenerList();
				for (int i = 0; i < listeners.length; i += 2) {
					if(listeners[i + 1]!=null)
						((ModelEventListener) listeners[i + 1])
							.modelUpdated(modelEvent,property,oldValue,newValue);
				}
			}
		}
	}
	
	public static void firePersonIdDataChanged(final Object source,String property, Object oldValue, Object newValue) {
		if (source != null) {
			if (msgListeners.getListenerCount() > 0) {
				final ModelEvent modelEvent = new ModelEvent(source);

				final Object[] listeners = msgListeners.getListenerList();
				for (int i = 0; i < listeners.length; i += 2) {
					if(listeners[i + 1]!=null)
						((ModelEventListener) listeners[i + 1]).personIdDataChanged(modelEvent,property,oldValue,newValue);
				}
			}
		}
	}
	

}
