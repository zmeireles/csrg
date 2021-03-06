package com.obc.csrg.constants;

public enum ModelEnum {
	ALL_MODELS,USERS,DEPARTMENTS,GEOGRAPHIC_AREAS,NEWS,NOTIFICATIONS,COMPOSED_NOTIFICATIONS,
	PERSONS,PROF_CATEGORIES,SERVICE_AREAS,SERVICE_CLASSIFICATIONS,VISUAL_ITEMS,
	WEB_PAGES,DATA_CHANGE_MODELS;
	
	public String getName(){
		switch(this){
		
		case ALL_MODELS:return Constants.MODEL_ALL;
		case USERS:return Constants.MODEL_USERS;
		case DEPARTMENTS:return Constants.MODEL_DEPARTMENTS;
		case GEOGRAPHIC_AREAS:return Constants.MODEL_GEOGRAPHIC_AREAS;
		case NEWS:return Constants.MODEL_NEWS;
		case NOTIFICATIONS:return Constants.MODEL_NOTIFICATIONS;
		case COMPOSED_NOTIFICATIONS:return Constants.MODEL_COMPOSED_NOTIFICATIONS;
		case PERSONS:return Constants.MODEL_PERSONS;
		case PROF_CATEGORIES:return Constants.MODEL_PROF_CATEGORIES;
		case SERVICE_AREAS:return Constants.MODEL_SERVICE_AREAS;
		case SERVICE_CLASSIFICATIONS:return Constants.MODEL_SERVICE_CLASSIFICATIONS;
		case VISUAL_ITEMS:return Constants.MODEL_VISUAL_ITEMS;
		case WEB_PAGES:return Constants.MODEL_WEB_PAGES;
		case DATA_CHANGE_MODELS:return Constants.MODEL_DATA_CHANGE_MODELS;
		
		default :return Constants.MODEL_USERS;
		}
	}

}
