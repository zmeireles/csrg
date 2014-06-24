package com.obc.csrg.util;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;

import javax.faces.event.ValueChangeEvent;

import org.jboss.seam.log.Log;

import com.obc.csrg.model.GeographicArea;
import com.obc.csrg.constants.GeographicAreaTypeEnum;

public class GeographicAreaUtil implements Serializable {

	private Log log;

	//data
	private HashMap<GeographicAreaTypeEnum,GeographicArea> areaGeograficaHash;
	private boolean dataChangedFlag;
	private GeographicArea selectedGeographicArea;
	private GeographicAreaTypeEnum selectedLevel;
	
	
	//constructor
	public GeographicAreaUtil(GeographicArea areaGeografica) {
		super();
		//System.out.println("[GeographicAreaUtil] [GeographicAreaUtil] areaGeografica: "+((areaGeografica!=null)? areaGeografica.obterNameFull() : "null"));
		areaGeograficaHash = new HashMap<GeographicAreaTypeEnum,GeographicArea>();
		this.setGeographicArea(areaGeografica);
		dataChangedFlag = true;
	}
	
	public void cleanGeographicArea(){
		//System.out.println("[GeographicAreaUtil] [cleanGeographicArea]");
		this.setWorld(null);
		this.cleanGeographicAreaTo(GeographicAreaTypeEnum.WORLD);
	}
	private void cleanGeographicAreaTo(GeographicAreaTypeEnum level){
		//System.out.println("[GeographicAreaUtil] [cleanGeographicAreaTo] nivel: "+nivel.getName());
		switch(level){
		case WORLD:
			this.setCountry(null);
		case COUNTRY:
			this.setRegion(null);
		case REGION:
			this.setIslandDistrict(null);
		case ISLAND_DISTRICT:
			this.setCounty(null);
		case COUNTY:
			this.setFreguesia(null);
		case FREGUESIA:
		default :
		}
	}
	
	public GeographicAreaTypeEnum getSelectedLevel(){
		
		//log.info("[getSelectedLevel] dataChangedFlag:#0", dataChangedFlag);
		if(dataChangedFlag){
			this.getSelectedGeographicArea();
			//System.out.println("[GeographicAreaUtil] [getSelectedNivel] selectedNivel: "+((selectedNivel==null)? "null" : selectedNivel.getName())+" ordinal: "+((selectedNivel==null)? "null" : selectedNivel.ordinal()));
		}
		return selectedLevel;
	}
	private void setSelectedLevel(GeographicArea selectedGeographicArea){
		if(selectedGeographicArea!=null){
			selectedLevel = selectedGeographicArea.getLevel();
		}else{
			selectedLevel = null;
		}
	}
	public GeographicArea getSelectedGeographicArea(){
		if(dataChangedFlag){
			selectedGeographicArea = this.getFreguesia();
			//log.info("[getSelectedGeographicArea] country:#0",this.getCountry());
			if(selectedGeographicArea==null) selectedGeographicArea = this.getCounty();
			if(selectedGeographicArea==null) selectedGeographicArea = this.getIslandDistrict();
			if(selectedGeographicArea==null) selectedGeographicArea = this.getRegion();
			if(selectedGeographicArea==null) selectedGeographicArea = this.getCountry();
			if(selectedGeographicArea==null) selectedGeographicArea = this.getWorld();
			//log.info("[getSelectedGeographicArea] selectedArea:#0",this.selectedGeographicArea);
			selectedGeographicArea = verifyResultSelected(selectedGeographicArea);
			//System.out.println("[GeographicAreaUtil] [getSelectedGeographicArea] selectedGeographicArea: "+((selectedGeographicArea==null)? "null" : selectedGeographicArea.getName()));
			setSelectedLevel(selectedGeographicArea);
			//log.info("[getSelectedGeographicArea] selectedArea:#0, level:#1",this.selectedGeographicArea, this.selectedLevel);
			dataChangedFlag=false;
		}
		return selectedGeographicArea;
	}
	private GeographicArea verifyResultSelected(GeographicArea result){
		//System.out.println("[GeographicAreaUtil] [verifyResultSelected] arg: "+((result==null)? "null" : result.getName()));
		if(result!=null){
			GeographicArea aux = result.getParent();
			//log.info("[verifyResultSelected] aux:#0, result:#1", aux,result);
			while(aux!=null){
				//log.info("[verifyResultSelected] getGEO:#0, id:#1, aux.id:#2", this.getGeographicArea(aux.getLevel()),
					//	this.getGeographicArea(aux.getLevel())==null? "null" : 
						//	this.getGeographicArea(aux.getLevel()).getId(),aux.getId());
				if(this.getGeographicArea(aux.getLevel())==null || !this.getGeographicArea(aux.getLevel()).getId().equals(aux.getId())){
					result = this.getGeographicArea(aux.getLevel());
					//log.info("[verifyResultSelected] result changed: aux:#0, result:#1", aux,result);
				}
				aux=aux.getParent();
				//log.info("[verifyResultSelected] inside while: aux:#0, result:#1", aux,result);
			}
		}
		//System.out.println("[GeographicAreaUtil] [verifyResultSelected] result: "+((result==null)? "null" : result.getName()));
		return result;
	}
	
	public void valueChangeListener(ValueChangeEvent event){
		//log.info("[valueChangeListener] region:#0", this.getRegion());
	}
	
	/*
	 * GETTERS and SETTERS
	 */
	public GeographicArea getGeographicArea(GeographicAreaTypeEnum level){
		return areaGeograficaHash.get(level);
	}
	public void setGeographicArea(GeographicArea geographicArea){
		//System.out.println("[GeographicAreaUtil] [setGeographicArea] areaGeografica: "+((areaGeografica!=null)? areaGeografica.obterNameFull() : "null"));
		//System.out.println("[GeographicAreaUtil] [setGeographicArea] 0");
		if(geographicArea!=null){
			GeographicArea agMem = this.getGeographicArea(geographicArea.getLevel());
			//System.out.println("[GeographicAreaUtil] [setGeographicArea] 1");
			if(agMem==null || !geographicArea.getId().equals(agMem.getId())){
				//System.out.println("[GeographicAreaUtil] [setGeographicArea] 2");
				this.cleanGeographicAreaTo(geographicArea.getLevel());
				GeographicArea aux = geographicArea;
				while(aux!=null){
					this.setGeographicArea(aux.getLevel(), aux);
					//System.out.println("[GeographicAreaUtil] [setGeographicArea] nivel: "+aux.getLevel().getName()+" areaGeografica: "+aux.getName());
					aux=aux.getParent();
				}
			}
		}else{
			//System.out.println("[GeographicAreaUtil] [setGeographicArea] 3");
			this.cleanGeographicArea();
		}
		//System.out.println("[GeographicAreaUtil] [setGeographicArea] 4");
	}
	private void setGeographicArea(GeographicAreaTypeEnum level,GeographicArea geographicArea){
		switch(geographicArea.getLevel()){
		case WORLD:
			this.setWorld(geographicArea);
			break;
		case COUNTRY:
			this.setCountry(geographicArea);
			break;
		case REGION:
			this.setRegion(geographicArea);
			break;
		case ISLAND_DISTRICT:
			this.setIslandDistrict(geographicArea);
			break;
		case COUNTY:
			this.setCounty(geographicArea);
			break;
		case FREGUESIA:
			this.setFreguesia(geographicArea);
			break;
		default :
			break;
		}
	}
	
	public GeographicArea getWorld() {
		return areaGeograficaHash.get(GeographicAreaTypeEnum.WORLD);
	}
	public void setWorld(GeographicArea world) {
		areaGeograficaHash.put(GeographicAreaTypeEnum.WORLD, world);
		if(world!=null && this.getCountry()!=null && !this.getCountry().getParent().getId().equals(world.getId())){
			this.cleanGeographicAreaTo(GeographicAreaTypeEnum.WORLD);
		}
		dataChangedFlag=true;
	}
	public GeographicArea getCountry(){
		return areaGeograficaHash.get(GeographicAreaTypeEnum.COUNTRY);
	}
	public void setCountry(GeographicArea country){
		areaGeograficaHash.put(GeographicAreaTypeEnum.COUNTRY, country);
		if(country!=null && this.getRegion()!=null && !this.getRegion().getParent().getId().equals(country.getId())){
			this.cleanGeographicAreaTo(GeographicAreaTypeEnum.COUNTRY);
		}
		dataChangedFlag=true;
	}
	public GeographicArea getRegion(){
		return areaGeograficaHash.get(GeographicAreaTypeEnum.REGION);
	}
	public void setRegion(GeographicArea region){
		areaGeograficaHash.put(GeographicAreaTypeEnum.REGION, region);
		if(region!=null && this.getIslandDistrict()!=null && !this.getIslandDistrict().getParent().getId().equals(region.getId())){
			this.cleanGeographicAreaTo(GeographicAreaTypeEnum.REGION);
		}
		dataChangedFlag=true;
	}
	public GeographicArea getIslandDistrict(){
		return areaGeograficaHash.get(GeographicAreaTypeEnum.ISLAND_DISTRICT);
	}
	public void setIslandDistrict(GeographicArea islandDistrict){
		areaGeograficaHash.put(GeographicAreaTypeEnum.ISLAND_DISTRICT, islandDistrict);
		if(islandDistrict!=null && this.getCounty()!=null && !this.getCounty().getParent().getId().equals(islandDistrict.getId())){
			this.cleanGeographicAreaTo(GeographicAreaTypeEnum.ISLAND_DISTRICT);
		}
		dataChangedFlag=true;
	}
	public GeographicArea getCounty(){
		return areaGeograficaHash.get(GeographicAreaTypeEnum.COUNTY);
	}
	public void setCounty(GeographicArea county){
		areaGeograficaHash.put(GeographicAreaTypeEnum.COUNTY, county);
		if(county!=null && this.getFreguesia()!=null && !this.getFreguesia().getParent().getId().equals(county.getId())){
			this.cleanGeographicAreaTo(GeographicAreaTypeEnum.COUNTY);
		}
		dataChangedFlag=true;
	}
	public GeographicArea getFreguesia(){
		return areaGeograficaHash.get(GeographicAreaTypeEnum.FREGUESIA);
	}
	public void setFreguesia(GeographicArea freguesia){
		areaGeograficaHash.put(GeographicAreaTypeEnum.FREGUESIA, freguesia);
		dataChangedFlag=true;
	}

	public boolean isShowWorldSelection() {
		return true;
	}
	public boolean isShowCountrySelection() {
		return (this.getSelectedLevel()!=null && !this.getPossibleCountryList().isEmpty());
	}
	public boolean isShowRegionSelection() {
		//log.info("[isShowRegionSelection] level:#0, regionList:#1", this.selectedLevel,this.getPossibleRegionList().size());
		return (this.getSelectedLevel()!=null && this.getSelectedLevel().ordinal()>0 && !this.getPossibleRegionList().isEmpty());
	}
	public boolean isShowIslandDistrictSelection() {
		return (this.getSelectedLevel()!=null && this.getSelectedLevel().ordinal()>1 && !this.getPossibleIslandDistrictList().isEmpty());
	}
	public boolean isShowCountySelection() {
		return (this.getSelectedLevel()!=null && this.getSelectedLevel().ordinal()>2 && !this.getPossibleCountyList().isEmpty());
	}
	public boolean isShowFreguesiaSelection() {
		return (this.getSelectedLevel()!=null && this.getSelectedLevel().ordinal()>3 && !this.getPossibleFreguesiaList().isEmpty());
	}
	
	public Set<GeographicArea> getPossibleCountryList(){
		return this.getWorld().getChildren();
	}
	public Set<GeographicArea> getPossibleRegionList(){
		return this.getCountry().getChildren();
	}
	public Set<GeographicArea> getPossibleIslandDistrictList(){
		return this.getRegion().getChildren();
	}
	public Set<GeographicArea> getPossibleCountyList(){
		return this.getIslandDistrict().getChildren();
	}
	public Set<GeographicArea> getPossibleFreguesiaList(){
		return this.getCounty().getChildren();
	}
	
	public String getIslandDistrictLabel(){
		String result;
		GeographicArea region = this.getRegion();
		if(region==null){
			result = "GeographicAreaIslandDistrictLabel";
		}else if(region.isArchipelago()){
			result = "GeographicAreaIslandLabel";
		}else{
			result = "GeographicAreaDistrictLabel";
		}
		return result;
	}
	
	public Log getLog() {
		return log;
	}

	public void setLog(Log log) {
		this.log = log;
	}
}
