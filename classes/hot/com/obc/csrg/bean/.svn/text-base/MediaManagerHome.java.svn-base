package com.obc.csrg.bean;


import java.io.Serializable;

import org.jboss.seam.annotations.Begin;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.Destroy;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.web.RequestParameter;

import com.obc.csrg.events.ModelEvent;
import com.obc.csrg.events.ModelEventAdapter;
import com.obc.csrg.events.ModelEventQueue;
import com.obc.csrg.model.MediaManager;
import com.obc.csrg.model.Model;
import com.obc.csrg.util.MediaController;;

@Name("mediaManagerHome")
public class MediaManagerHome<E extends Model> extends ObcEntityHome<MediaManager> {
	
	public MediaController mediaController;
	
	
	@Override
	@Create
	@Begin(join = true)
	public void create() {
		super.create();
		log.info("[create] mediaManagerHome created");
		this.registerModelListener();		
	}

	@Override
	@Destroy
	public void destroy() {
		super.destroy();
		this.unregisterModelListener();
	}

	@Override
	protected void loadAfterCreate() {		
		super.loadAfterCreate();
		mediaController= new MediaController(stateBean);
	}
	
	
	// Model events handling
	private final class ModelEventHandler extends ModelEventAdapter implements
			Serializable {
		@Override
		public void newModel(ModelEvent e) {
			Model m = (Model) e.getSource();

		}

		@Override
		public void onBeforeRemove(ModelEvent e) {
			Model m = (Model) e.getSource();

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
	public MediaController getMediaController() {
		return mediaController;
	}
	
	public void setMediaController(MediaController mediaController) {
		this.mediaController = mediaController;
	}
	
}

