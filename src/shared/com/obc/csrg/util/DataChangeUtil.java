package com.obc.csrg.util;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.lang.reflect.Constructor;

import java.util.List;
import java.util.ArrayList;
import java.util.Calendar;
import javax.persistence.EntityManager;

import org.jboss.seam.log.Log;

import com.obc.csrg.model.Model;
import com.obc.csrg.model.DataChangeModel;
import com.obc.csrg.model.DataChangeProperty;
import com.obc.csrg.model.DataChangeValue;
import com.obc.csrg.model.User;

public class DataChangeUtil<E extends Model> implements Serializable{

	protected E instance;
	
	private E replication;
	
	private Log log;
	
	private EntityManager entityManager;
	
	private User user;
	
	private final String[] primitiveTypes = {"char","byte","short","int","long","float","double","boolean"};
	private final String byteArray = "byte[]";
	private final String charArray = "char[]";
	private List<String> modelsList = new ArrayList<String>();
	private List<DataChangeValue> changedValues = new ArrayList<DataChangeValue>();
	private DataChangeModel dataChangeModel;
	
	
	//contructors
	public DataChangeUtil(E instance,E replication){
		this.instance = instance;
		this.replication = replication;
	}
	public DataChangeUtil(E instance){
		this.instance = instance;
		this.createReplicationObject();
	}
	public DataChangeUtil(E instance,E replication,EntityManager entityManager){
		this.instance = instance;
		this.replication = replication;
		this.entityManager = entityManager;
	}
	public DataChangeUtil(E instance,E replication,Log log){
		this.instance = instance;
		this.replication = replication;
		this.log = log;
	}
	public DataChangeUtil(E instance,EntityManager entityManager,Log log){
		this.instance = instance;
		this.entityManager = entityManager;
		this.log = log;
		this.createReplicationObject();
	}
	public DataChangeUtil(E instance,EntityManager entityManager,Log log,User user){
		this.instance = instance;
		this.entityManager = entityManager;
		this.log = log;
		this.createReplicationObject();
		this.user = user;
	}
	public DataChangeUtil(E instance,E replication,EntityManager entityManager,Log log){
		this.instance = instance;
		this.replication = replication;
		this.entityManager = entityManager;
		this.log = log;
	}
	
	//business methods
	@Override
	public String toString() {
		return instance.toString();
	}
	
	public int hashCode() {
		return (this.instance.getId() == null) ? 0 : this.instance.getId().hashCode();
	}
	public boolean equals(Object object) {
		if(object==null)
			return false;
		if (object instanceof DataChangeUtil && ((DataChangeUtil)object).instance.getClass().equals(instance.getClass())) {
			
			final Model obj = (Model) ((DataChangeUtil)object).instance;
			return (this.instance.getId() != null) ? this.instance.getId().equals(obj.getId())
					: (obj.getId() == null);
		}
		return false;
	}
	public void replicateValues(){
		
		for(Method m:instance.getClass().getMethods()){
			if(this.isGetter(m.getName())){
				if(this.hasSetter(replication, this.extractPropertyName(m.getName()))){
					String property = this.extractPropertyName(m.getName());
					String setterName = this.getSetterMethod(property);
					//log.info("[replicateValues] property:#0, getter:#1, setter:#2", property,m.getName(),setterName);
					//get the setter method
					for(Method setter:replication.getClass().getMethods()){
						if(setter.getName().equals(setterName)){
							try{
								//replication operation
								//log.info("[replicateValues] getter value:#0", m.invoke(this.instance));
								setter.invoke(this.replication, m.invoke(this.instance));
								//log.info("[replicateValues] replicated value after setter:#0", m.invoke(this.replication));
							}catch(Exception e){
								e.printStackTrace();
							}
							break;
						}
					}
				}
			}
		}
	}
	/**
	 * iterates on the instance properties included for log changes. When it finds a difference between old an new values
	 * it creates an instance of DataChangeValue class.
	 * Also prepares a report for user notification.
	 */
	public void compareChanges(){
		changedValues.clear();
		if(this.entityManager==null)
			return;
		//get the models list
		modelsList = entityManager.createQuery("select m.name from DataChangeModel m")
								.getResultList();
		//get the appropriate DataChangeModel
		List<DataChangeModel> models = entityManager.createQuery(
				"select m from DataChangeModel m where m.name=?")
				.setParameter(1, instance.getClass().getSimpleName())
				.getResultList();
		if(models.size()>0){
			DataChangeModel model = models.get(0);
			this.setDataChangeModel(model);
			for(Method m:instance.getClass().getMethods()){
				if(this.isGetter(m.getName()) && 
						this.hasSetter(replication, this.extractPropertyName(m.getName()))){
					try{
						Method rep = replication.getClass().getMethod(m.getName());
						//log.info("[compareChanges] method:#0, rep:#1", m.getName(),rep);
						if(rep!=null){
							DataChangeProperty p = this.getProperty(model, this.extractPropertyName(m.getName()));
							if(p!=null && (p.isInclude() || model.isIncludeAllProperties())){
								//compare values according to type
								//primitive types are wrapped into corresponding java objects
								Object newValue = m.invoke(instance);
								Object oldValue =  rep.invoke(replication);
								if((newValue==null && oldValue!=null) || (newValue!=null && !newValue.equals(oldValue))){
									DataChangeValue changedValue;
									log.info("[compareChanges] property:#0, dataType:#1, old value:#2, new value:#3", 
											p.getName(),p.getDataType(),rep.invoke(replication),m.invoke(instance));
									changedValue = this.createDataChangeValue(p);
									if(this.isByteArray(p.getDataType()))
										changedValue.setByteArrayValue(oldValue==null? null:(byte[])oldValue);
									else if(this.isCharArray(p.getDataType()))
										changedValue.setCharArrayValue(oldValue==null? null:(char[])oldValue);
									else if(this.isModel(p.getDataType())){
										if(oldValue!=null)
											changedValue.setModelIdValue(((E)oldValue).getId());
										else
											changedValue.setModelId(null);
									}else
										changedValue.setRegularValue(oldValue==null? "" : oldValue.toString());
									entityManager.persist(changedValue);
									p.getValues().add(changedValue);
									changedValues.add(changedValue);
								}
							}
						}
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			}
		}
	}
	/**
	 * checks if is there any included properties in the model to keep log of changes
	 * @return
	 */
	public boolean logForChanges(){
		List<DataChangeModel> models = entityManager.createQuery(
				"select m from DataChangeModel m where m.name=?")
				.setParameter(1, instance.getClass().getSimpleName())
				.getResultList();
		//log.info("[logForChanges] models:#0", models);
		if(models.size()>0){
			DataChangeModel model = models.get(0);
			if(model.isIncludeAllProperties())
				return true;//don't even need to check for individual values
			for(DataChangeProperty p:model.getProperties()){
				if(p.isInclude())
					return true;
			}
		}
		return false;
	}
	public String getOldValue(DataChangeProperty property){
		String methodName = this.getGetterMethod(property);
		try{
			Method m = this.instance.getClass().getMethod(methodName);
			return m.invoke(this.replication)==null? "" :m.invoke(this.replication).toString();
		}catch(Exception e){
			e.printStackTrace();
		}
		return "";
	}
	public String getNewValue(DataChangeProperty property){
		String methodName = this.getGetterMethod(property);
		try{
			Method m = this.instance.getClass().getMethod(methodName);
			return m.invoke(this.instance).toString();
		}catch(Exception e){
			e.printStackTrace();
		}
		return "";
	}
	/**
	 * merge changed values adding the changeUtil changed values list to this changed values 
	 * in order to produce a single report
	 * @param changeUtil
	 */
	public void mergeChangedValues(DataChangeUtil changeUtil){
		for(Object cv:changeUtil.getChangedValues()){
			this.changedValues.add((DataChangeValue)cv);
		}
	}
	//auxiliary methods
	
	/**
	 * checks if there is a setter for a particular property.
	 */
	private boolean hasSetter(E object,String property){
		boolean result = false;
		String setterName = this.getSetterMethod(property);
		for(Method m:object.getClass().getMethods()){
			if(m.getName().equals(setterName)){
				result = true;
				break;
			}
		}
		//log.info("[hasSetter] property:#0, setter:#1, result:#2", property,setterName,result);
		return result;
	}
	private DataChangeValue createDataChangeValue(DataChangeProperty p){
		DataChangeValue result = new DataChangeValue();
		result.setUser(user);
		result.setDate(Calendar.getInstance().getTime());
		result.setModelId(instance.getId());
		result.setDataChangeProperty(p);
		return result;
	}
	private boolean isModel(String type){
		for(String m:modelsList)
			if(type.equals(m))
				return true;
		return false;
	}
	private boolean isByteArray(String type){
		if(type.toLowerCase().equals(byteArray))
			return true;
		return false;
	}
	private boolean isCharArray(String type){
		if(type.toLowerCase().equals(charArray))
			return true;
		return false;
	}
	private boolean isArray(String type){
		if(type.toLowerCase().equals(byteArray) || type.toLowerCase().equals(charArray))
			return true;
		return false;
	}
	private DataChangeProperty getProperty(DataChangeModel model,String property){
		
		//log.info("[getProperty] property:#0", property);
		for(DataChangeProperty p:model.getProperties()){
			if(p.getName().equals(property))
				return p;
		}
		return null;
	}
	private boolean isPrimitive(String type){
		for(String pt:this.primitiveTypes){
			if(type.equals(pt))
				return true;
		}
		return false;
	}
	private void createReplicationObject(){
		try{
			Constructor c = instance.getClass().getConstructor(null);
			replication = (E)c.newInstance(null);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	private boolean isGetter(String methodName){
		/*
		log.info("[isGetter] method:#0", methodName);
		
		if(methodName.substring(0, 2).equals("is"))
			log.info("[isGetter] capital:#0", methodName.substring(2,3));
		if(methodName.substring(0, 3).equals("get"))
			log.info("[isGetter] capital:#0", methodName.substring(3,4));
		*/
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
	private String getGetterMethod(String property){
		String letter = property.substring(0, 1).toUpperCase();
		//log.info("[getGetterMethod] property:#0, result:#1", property,"get" + letter + property.substring(1));
		return "get" + letter + property.substring(1);
	}
	*/
	private String getGetterMethod(DataChangeProperty property){
		String letter = property.getName().substring(0, 1).toUpperCase();
		if(property.getDataType().equals("boolean"))
			return "is" + letter + property.getName().substring(1);
		else
			return "get" + letter + property.getName().substring(1);
			
	}
	private String getSetterMethod(String property){
		String letter = property.substring(0, 1).toUpperCase();
		//log.info("[getSetterMethod] property:#0, result:#1", property,"set" + letter + property.substring(1));
		return "set" + letter + property.substring(1);
	}
	
	//getters and setters
	
	public E getInstance() {
		return instance;
	}

	public void setInstance(E instance) {
		this.instance = instance;
	}
	
	public E getReplication() {
		return replication;
	}

	public void setReplication(E replication) {
		this.replication = replication;
	}
	
	public Log getLog() {
		return log;
	}
	public void setLog(Log log) {
		this.log = log;
	}
	public EntityManager getEntityManager() {
		return entityManager;
	}
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public List<DataChangeValue> getChangedValues() {
		return changedValues;
	}
	public void setChangedValues(List<DataChangeValue> changedValues) {
		this.changedValues = changedValues;
	}
	public DataChangeModel getDataChangeModel() {
		return dataChangeModel;
	}
	public void setDataChangeModel(DataChangeModel dataChangeModel) {
		this.dataChangeModel = dataChangeModel;
	}
}
