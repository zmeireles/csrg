package com.obc.csrg.constants;

public enum GenderEnum {
	male,female;
	
	public String getName(){
		switch(this.ordinal()){
		case 0:return Constants.GENDER_MALE;
		case 1:return Constants.GENDER_FEMALE;
		default :return Constants.GENDER_MALE;
		}
	}

}
