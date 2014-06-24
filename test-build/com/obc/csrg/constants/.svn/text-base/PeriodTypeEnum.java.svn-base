package com.obc.csrg.constants;

import java.util.Calendar;
import java.util.Date;

public enum PeriodTypeEnum {
	segundo,minuto,hora,dia,semana,mes,ano;
	
	public String getName(){
		switch(this.ordinal()){
		case 0:return Constants.PERIOD_TYPE_SECOND;
		case 1:return Constants.PERIOD_TYPE_MINUTE;
		case 2:return Constants.PERIOD_TYPE_HOUR;
		case 3:return Constants.PERIOD_TYPE_DAY;
		case 4:return Constants.PERIOD_TYPE_WEEK;
		case 5:return Constants.PERIOD_TYPE_MONTH;
		case 6:return Constants.PERIOD_TYPE_YEAR;
		default :return Constants.PERIOD_TYPE_YEAR;
		}
	}
	
	public int getCalendarField(){
		switch(this.ordinal()){
		case 0:return Calendar.SECOND;
		case 1:return Calendar.MINUTE;
		case 2:return Calendar.HOUR;
		case 3:return Calendar.DAY_OF_MONTH;
		case 4:return Calendar.WEEK_OF_MONTH;
		case 5:return Calendar.MONTH;
		case 6:return Calendar.YEAR;
		default :return Calendar.YEAR;
		}
	}
	
	public static Date dateAdd(final Date date, final PeriodTypeEnum tipoPeriodo, final int periodo){
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(tipoPeriodo.getCalendarField(), periodo);
		return c.getTime();
	}

}
