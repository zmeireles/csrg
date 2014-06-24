package com.obc.csrg.search;

import java.lang.reflect.Method;
import java.util.List;
import java.util.HashSet;
import java.util.Set;
import java.util.ArrayList;
import java.util.Locale;
import javax.persistence.EntityManager;

import org.jboss.seam.log.Log;

import com.obc.csrg.model.Model;
import com.obc.csrg.model.SearchObject;
import com.obc.csrg.model.SearchProperty;
import com.obc.csrg.model.SearchValue;
import com.obc.csrg.model.KeywordsLang;
import com.obc.csrg.constants.SearchObjectTypeEnum;
import com.obc.csrg.constants.Constants;

/**
 * @author jmeireles
 *
 * Analyzes a Model object, discovering  its properties by reflection (extracting getter methods)
 * fill the keywords for properties and values 
 */
public class KeywordsFactory<E extends Model> {

	protected E instance;
	
	private EntityManager entityManager;
	
	private final String DEFAULT_LANG = "en";
	
	private Log log;
	
	private String language = "pt";
	
	private Locale locale;

	//constructors
	public KeywordsFactory(E instance){
		this.instance = instance;
	}
	
	public KeywordsFactory(E instance,Locale locale){
		this.instance = instance;
		this.locale = locale;
		this.language = this.locale.getLanguage();
	}
	
	public KeywordsFactory(E instance,Locale locale, EntityManager entityManager){
		this.instance = instance;
		this.locale = locale;
		this.entityManager = entityManager;
		this.language = this.locale.getLanguage();
	}
	public KeywordsFactory(E instance,Locale locale, EntityManager entityManager,Log log){
		this.instance = instance;
		this.locale = locale;
		this.entityManager = entityManager;
		this.log = log;
		this.language = this.locale.getLanguage();
	}
	
	//business logic

	public void createKeywords(){
		if(this.instance== null)
			return;
		//create/reuse SearchObject
		SearchObject obj = this.createModelKeywords();
		//create/reuse object properties
		this.createProperties(obj);
		//delete unused/deleted properties
		this.deleteUnusedProperties(obj);
		//create values for each property
		for(SearchProperty prop:obj.getProperties()){
			this.createValueKeywords(prop);
		}
	}
	
	public SearchObject updateKeywords(){
		//happens when an existing object is updated/changed
		if(this.instance== null)
			return null;
		//create/reuse SearchObject
		SearchObject obj = this.createModelKeywords();
		//create/reuse object properties
		this.createProperties(obj);
		//log.info("[updateKeywords] after createProperties");
		//delete unused/deleted properties
		this.deleteUnusedProperties(obj);
		//update values for each property
		//log.info("[updateKeywords] going to updateValueKeywords");
		this.updateValueKeywords(obj);
		//log.info("[updateKeywords] after updateValueKeywords");
		return obj;
	}
	/**
	 * checks for new properties in the model and adds
	 * also remove non existing properties, including search values
	 */
	public synchronized void updateProperties(){
		//create/reuse SearchObject
		SearchObject obj = this.createModelKeywords();
		//create/reuse object properties
		this.createProperties(obj);
		//log.info("[updateProperties] after createProperties");
		//delete unused/deleted properties
		this.deleteUnusedProperties(obj);
		//log.info("[updateProperties] after delete unused properties");
	}
	/**
	 * this method will remove SearchValue entries and associated KeywordsLang objects
	 * 
	 */
	public void removeKeywords(){
		if(this.instance==null)
			return;
		List<SearchValue> values = entityManager.createQuery("select m from SearchValue m " +
				"where m.objectId=?")
				.setParameter(1, instance.getId())
				.getResultList();
		if(values.size()>0){
			for(SearchValue value:values){
				entityManager.remove(value);
			}
			//entityManager.flush();
		}
	}
	private SearchObject createModelKeywords(){
		if(this.instance == null || this.entityManager == null)
			return null;
		//log.info("[createModelKeywords] class:#0", instance.getClass().getSimpleName());
		String modelName = instance.getClass().getSimpleName();
		modelName = modelName.split("[$]")[0];
		//log.info("[createModelKeywords] treated modelName:#0", modelName);
		List<SearchObject> objs = entityManager.createQuery("select m from SearchObject m where m.objectName=? " +
				"and m.objectType=?")
				.setParameter(1, modelName)
				.setParameter(2, SearchObjectTypeEnum.MODEL)
				.getResultList();
		if(objs.size()==0){
			SearchObject obj = new SearchObject();
			obj.setObjectName(modelName);
			obj.setObjectType(SearchObjectTypeEnum.MODEL);
			entityManager.persist(obj);
			//create default keywords in English language
			KeywordsLang key = new KeywordsLang();
			key.setKeys(instance.getClass().getSimpleName());
			key.setLang(DEFAULT_LANG); // all classes are in English words
			key.setSearchObject(obj);
			entityManager.persist(key);
			obj.getKeywords().add(key);
			entityManager.merge(obj);
			return obj;
		}else
			return objs.get(0);
	}
	private void createProperties(SearchObject obj){
		//first extract locale language if exists
		//log.info("[createProperties] for object:#0", instance.getClass().getSimpleName());
		for(Method m:instance.getClass().getMethods()){
			if(this.extractPropertyName(m.getName()).equals("locale")){
				String language="";
				try{
					language = (String) m.invoke(this.instance);
				}catch(Exception e){
					e.printStackTrace();
				}
				if(language!=null && !language.equals(""))
					this.language = language;
				break;
			}
		}
		for(Method m:instance.getClass().getMethods()){
			//log.info("[createProperties] method:#0", m.getName().substring(0, 3));
			if(this.isGetter(m.getName())){
				this.createPropertyKeywords(this.extractPropertyName(m.getName()), obj,m);
				//if(log!=null) log.info("[createProperties] method:#0,property:#1", m.getName(),property);
			}
		}
	}
	private void deleteUnusedProperties(SearchObject obj){
		List<SearchProperty> deletedProperties = new ArrayList<SearchProperty>();
		for(SearchProperty p:obj.getProperties()){
			boolean exist = false;
			for(Method m:instance.getClass().getMethods()){
				if(this.isGetter(m.getName()) && p.getName().equals(this.extractPropertyName(m.getName()))){
					exist=true;
					break;
				}
			}
			if(!exist){//put property in the delete list
				deletedProperties.add(p);
			}
		}
		for(SearchProperty p:deletedProperties){
			log.info("[deletedUnusedProperties] property:#0", p.getName());
			p.setSearchObject(null);
			obj.getProperties().remove(p);
			entityManager.remove(p);
		}
	}
	private SearchProperty createPropertyKeywords(String propertyName,SearchObject obj,Method m){
		//log.info("[createPropertyKeywords] before validation");
		if(!this.isValidMethod(m))
			return null;
		//first check if property already exists
		//log.info("[createPropertyKeywords] property:#0", propertyName);
		for(SearchProperty prop: obj.getProperties()){
			//log.info("[createPropertyKeywords] existing property:#0", prop.getName());
			if(prop.getName().equals(propertyName))
				return null;
		}
		//log.info("[createPropertyKeywords] create property:#0", propertyName);
		SearchProperty property = new SearchProperty();
		property.setSearchObject(obj);
		property.setName(propertyName);
		property.setDataType(m.getReturnType().getSimpleName());
		entityManager.persist(property);
		//create default keywords in English version
		KeywordsLang key = new KeywordsLang();
		key.setKeys(propertyName);
		key.setLang(DEFAULT_LANG);
		key.setSearchProperty(property);
		entityManager.persist(key);
		property.getKeywords().add(key);
		entityManager.merge(property);
		obj.getProperties().add(property);
		return property;
	}
	private void createValueKeywords(SearchProperty property){
		
		//log.info("[createValueKeywords] for property:#0", property.getName());
		
		//exception for geographic areas
		if(property.getName().equals("myAsc") || property.getName().equals("myDesc")){
			//log.info("[createValueKeywords] ignore property!");
			return;
		}
		SearchValue value = new SearchValue();
		String methodName = this.getGetterMethod(property); // this.getMethodName(property.getName());
		//log.info("[createValueKeywords] property:#0, getter:#1", property.getName(),methodName);
		try{
			Method m = instance.getClass().getMethod(methodName);
			//Object x = null;
			Object returnVal = m.invoke(this.instance);
			if(returnVal!=null){
				//persist search value
				value.setObjectId(instance.getId());
				value.setSearchProperty(property);
				entityManager.persist(value);
				property.getValues().add(value);
				//log.info("[createValuesKeywords] returnVal:#0", returnVal);
				Object[] lines = null;
				if(property.getDataType().equals("Set")){
					Set<Object> values = (Set<Object>) returnVal;
					//log.info("[createValuesKeywords] returnVal:#0, values.size:#1", returnVal.toString(),values.size());
					String stringValue = "";
					for(Object obj:values){
						if(!obj.toString().trim().equals("")){
							//log.info("[createValuesKeywords] obj:#0, stringValue:#1", obj.toString(),stringValue);
							stringValue = stringValue.equals("") ? obj.toString().trim() 
									: stringValue + Constants.SENTENCE_DIVIDER + obj.toString().trim();
							//log.info("[createValuesKeywords] stringValue:#0", stringValue);
						}
					}
					if(!stringValue.equals(""))
						lines = this.getSplitedKeywords(stringValue,Constants.SENTENCE_DIVIDER);
				}else{
					lines = this.getSplitedKeywords(returnVal.toString(), " ");
				}
				if(lines!=null){
					for(int i=0;i<lines.length;i++){
						if(!((String)lines[i]).trim().equals("")){
							KeywordsLang keys = new KeywordsLang();
							keys.setKeys(((String)lines[i]).trim());
							keys.setLang(language);
							keys.setSearchValue(value);
							entityManager.persist(keys);
							value.getKeywords().add(keys);
						}
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void updateValueKeywords(SearchObject obj){
		
		List<SearchValue> values = entityManager.createQuery("select m from SearchValue m " +
				"where m.objectId=?")
				.setParameter(1, instance.getId())
				.getResultList();
		if(values.size()>0){
			for(SearchValue value:values){
				value.getSearchProperty().getValues().remove(value);
				entityManager.remove(value);
			}
		}
		//log.info("[updateValueKeywords] old values deleted");
		for(SearchProperty property:obj.getProperties())
			this.createValueKeywords(property);
	}
	
	/**
	 * just creates the values for each property inoring already existing values.
	 * this is ideal for empty tables
	 * @param obj
	 */
	public void createValueKeywords(SearchObject obj){
		for(SearchProperty property:obj.getProperties())
			this.createValueKeywords(property);
	}
	
	//auxiliary methods
	private boolean isGetter(String methodName){
		if(methodName.length()>3 && 
				(methodName.substring(0, 3).equals("get") &&
				methodName.substring(3,4).equals(methodName.substring(3,4).toUpperCase())) || 
				methodName.substring(0, 2).equals("is") &&
				methodName.substring(2,3).equals(methodName.substring(2,3).toUpperCase()))
			return true;
		return false;
	}
	private String extractPropertyName(String name){
		String letter="";
		String result ="";
		//method can start by "get" or "is"
		if(name.substring(0, 3).equals("get")){
			letter = name.substring(3,4).toLowerCase();
			result = letter + name.substring(4);
		}else{
			letter = name.substring(2,3).toLowerCase();
			result = letter + name.substring(3);
		}
		//log.info("[extractProperty] letter:#0,result:#1", letter,result);
		return result;
	}
	/*
	private String getMethodName(String property){
		String letter = property.substring(0, 1).toUpperCase();
		//log.info("[getMethodName] property:#0, result:#1", property,"get" + letter + property.substring(1));
		return "get" + letter + property.substring(1);
	}
	*/
	private String getGetterMethod(SearchProperty property){
		String letter = property.getName().substring(0, 1).toUpperCase();
		if(property.getDataType().equals("boolean"))
			return "is" + letter + property.getName().substring(1);
		else
			return "get" + letter + property.getName().substring(1);
			
	}
	private Object[] getSplitedKeywords(String keywords,String divider){
		List<String> returnVal = new ArrayList<String>();
		keywords = keywords.trim();
		String[] words = keywords.split(divider);
		String keywordLine = "";
		for(int i=0;i<words.length;i++){
			words[i] = words[i].trim();
			if(keywordLine.length() + divider.length() + words[i].length()>255){
				returnVal.add(keywordLine);
				keywordLine = words[i];
			}else{
				keywordLine = keywordLine.equals("") ? words[i] : keywordLine + divider + words[i];
			}
			if(keywordLine.length()>255)
				keywordLine = keywordLine.substring(0,255);
		}
		//put last string
		if(!keywordLine.equals("")){
			returnVal.add(keywordLine);
		}
		return returnVal.toArray();
	}
	private boolean isValidMethod(Method m){
		
		//if(log!=null) log.info("[isValidMethod] method:#0", m.getName());
		String returnType = m.getReturnType().getSimpleName();
		if(/*returnType.equals("Set") ||*/ returnType.equals("Class"))
			return false;
		if(m.getName().equals("getLocale"))//we don't need locale property as keyword
			return false;
		if(m.isVarArgs()|| m.getParameterTypes().length>0)
			return false;
		return true;
	}
	//getters and setters
	public E getInstance() {
		return instance;
	}

	public void setInstance(E instance) {
		this.instance = instance;
	}
	
	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}
	
	public EntityManager getEntityManager() {
		return entityManager;
	}

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public Log getLog() {
		return log;
	}

	public void setLog(Log log) {
		this.log = log;
	}
}
