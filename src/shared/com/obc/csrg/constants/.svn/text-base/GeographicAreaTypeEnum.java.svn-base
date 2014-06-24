package com.obc.csrg.constants;


public enum GeographicAreaTypeEnum {
	WORLD,COUNTRY,REGION,ISLAND_DISTRICT,COUNTY,FREGUESIA,PLACE;
	
	public String getName(){
		switch(this){
		case WORLD:return Constants.GA_WORLD;
		case COUNTRY:return Constants.GA_COUNTRY;
		case REGION:return Constants.GA_REGION;
		case ISLAND_DISTRICT:return Constants.GA_ISLAND_DISTRICT;
		case COUNTY:return Constants.GA_COUNTY;
		case FREGUESIA:return Constants.GA_FREGUESIA;
		case PLACE:return Constants.GA_PLACE;
		default :return Constants.GA_ISLAND_DISTRICT;
		}
	}
	
	public boolean isPais(){
		return this==GeographicAreaTypeEnum.COUNTRY;
	}
	
	public static GeographicAreaTypeEnum getTipoAreaGeograficaEnum(int ordinal){
		GeographicAreaTypeEnum result = null;
		for(GeographicAreaTypeEnum nivel :GeographicAreaTypeEnum.values()){
			if(nivel.ordinal()==ordinal) result = nivel;
		}
		return result;
	}
	public GeographicAreaTypeEnum getPrevious(){
		switch(this){
		case WORLD:return null;
		case COUNTRY:return WORLD;
		case REGION:return COUNTRY;
		case ISLAND_DISTRICT:return REGION;
		case COUNTY:return ISLAND_DISTRICT;
		case FREGUESIA:return COUNTY;
		case PLACE:return FREGUESIA;
		}
		return null;
	}
}
