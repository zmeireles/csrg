package com.obc.csrg.events;

import java.util.Map;

public class ModelEvent extends BasicEventObject{

	private static final long serialVersionUID = 201001171608L;
	
	//constructors
	public ModelEvent(final Object eventSource){
		super(eventSource);//eventSource will be the Model
	}

}
