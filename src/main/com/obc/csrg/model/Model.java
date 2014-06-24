package com.obc.csrg.model;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;
import java.util.HashSet; //import java.util.Map;
import java.util.ArrayList;
import java.util.Date;

import java.text.SimpleDateFormat;

import javax.persistence.CascadeType;
import javax.persistence.EntityManager;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import org.jboss.seam.Component;
import org.jboss.seam.log.Log;

//import javax.persistence.EntityManager;

//import org.jboss.seam.Component;

import com.obc.csrg.constants.Constants;
import com.obc.csrg.constants.ProfileConstant;

/**
 * Utility super class of the entity beans
 * 
 * @author jmeireles
 * 
 */
public abstract class Model implements Comparable<Object>, Serializable {

	private Long id = 0L;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	// Utilitários
	public boolean isObjectInList(final List list) {
		for (int i = 0; i < list.size(); i++) {
			final Model m = (Model) list.get(i);
			if (m.getId() != null && m.getId().equals(this.getId()))
				return true;
		}
		return false;
	}

	/**
	 * adiciona um elemento à lista caso o mesmo não se encontre na mesma
	 * 
	 * @param list
	 *            - lista de entidades
	 * @param model
	 *            - entidade a ser inserida
	 */
	public static void addToDistinctList(List list, Model model) {
		for (Object m : list) {
			if (((Model) m).getId().equals(model.getId()))
				return;
		}
		list.add(model);
	}

	@Override
	public String toString() {
		String result;
		if (this.getId() == null)
			return this.getClass().getName() + "_NULL";
		result = this.getClass().getName() + "_" + this.getId().toString();
		return result;
	}

	public String getNameFragment(int n) {
		String result;
		System.err
				.println("[Model] [getNameFragment] este metodo nao deve ser chamado pelo model mas sim pelo reescrevido do extended");
		if (this.toString().length() > (n - 5) && n > 5)
			result = this.toString().substring(0, (n - 5)) + "(...)";
		else
			result = this.toString();
		return result;
	}

	public int compareTo(final Object object) {
		final Long otherId = ((Model) object).getId();
		return this.id < otherId ? -1 : (this.id == otherId ? 0 : 1);
	}

	public static String profileName(int profile) {
		ProfileConstant p = ProfileConstant.valueOf(profile);
		return p.getName();
	}

	public static <T extends Model> List<T> listAndOperation(
			final List<T> list1, final List<T> list2) {
		List<T> result = new ArrayList<T>();
		for (T m : list1) {
			if (m.isObjectInList(list2))
				result.add(m);
		}
		return result;
	}

	/* Polimorfism to work with ParentsModel */
	public <T extends ParentsModel> Set<T> getMyAsc() {
		return new HashSet<T>();
	}

	public <T extends ParentsModel> Set<T> getMyDesc() {
		return new HashSet<T>();
	}

	public <T extends ParentsModel> T newInstanceOfParentModel(Model asc) {
		// must be overrided to be used
		System.err
				.println("[Model] [newInstanceOfParentModel] este metodo nao pode ser chamado pelo model mas sim pelo reescrevido do extended");
		return null;
	}

	// trata do indicatico de pais
	public static String parsePhonePrefix(String prefix) {
		if (prefix != null) {
			if (prefix.matches("^\\x2B\\d{1,}$")) {
				prefix = prefix.substring(1);
			} else if (prefix.matches("^00\\d{1,}$")) {
				prefix = prefix.substring(2);
			}
		}
		return prefix;
	}
	//should be overridden
	public boolean remove(EntityManager entityManager){
		return true;
	}
	
	public List<User> relatedUsers(){
		List<User> users = new ArrayList<User>();
		return users;
	}
	
	public List<Department> relatedDepartments(){
		List<Department> departments = new ArrayList<Department>();
		return departments;
	}
	
	public String formatedDate(Date date){
		SimpleDateFormat df = new SimpleDateFormat(Constants.DT_FORMAT_CSRG);
		if(date== null)
			return "";
		return df.format(date);
	}
	public String returnModelDisplayInfo(EntityManager entityManager,User user){
		String result = "";
		if(this.getClass().getSimpleName().equals("SearchValue")){
			//this is search value, need to find target model
			//System.out.println("[returnModelDisplayInfo] searchValue");
			SearchValue sv = (SearchValue) this;
			String className = sv.getSearchProperty().getSearchObject().getObjectName();
			List objs = entityManager.createQuery("select m from " + className + " m where m.id=?")
							.setParameter(1, sv.getObjectId())
							.getResultList();
			if(objs.size()>0){
				Object object = objs.get(0);
				//System.out.println("[returnModelDisplayInfo] object found! object:" + object.toString()
					//	+ " class:" + object.getClass().getSimpleName());
				Method m=null;
				for(Method method:object.getClass().getMethods()){
					//System.out.println("[returnModelDisplayInfo] method:" + method.getName());
					if(method.getName().equals("returnModelDisplayInfo")){
						m = method;
						break;
					}
				}
				try{
					return (String)m.invoke(object, entityManager,user);
				}catch(Exception e){
					e.printStackTrace();
				}
			}else{
				return "Deleted object";
			}
		}
		List<ModelDisplayInfo> mdiList = entityManager.createQuery("select m from ModelDisplayInfo m where " +
				"m.name=?")
				.setParameter(1, this.getClass().getSimpleName())
				.getResultList();
		//System.out.println("[returnModelDisplayInfo] class:" + this.getClass().getSimpleName() +
			//	", results:"+ mdiList.size());
		if(mdiList.size()>0){
			ModelDisplayInfo mdi = mdiList.get(0);
			List<PropertyDisplayInfo> propList = entityManager.createQuery("select m " +
					"from PropertyDisplayInfo m where m.use=true and " +
					"m.model.id=? order by m.sortOrder")
					.setParameter(1, mdi.getId())
					.getResultList();
			//System.out.println("[returnModelDisplayInfo] properties found:"+ propList.size());
			if(propList.size()>0){
				for(PropertyDisplayInfo p:propList){
					//check if this property refers another ModelDisplayInfo
					//System.out.println("[returnModelDisplayInfo] property:"+ p.getName());
					List<ModelDisplayInfo> mdis = entityManager.createQuery("select m from ModelDisplayInfo m where " +
							"m.name=?")
							.setParameter(1, p.getDataType())
							.getResultList();
					if(mdis.size()>0){
						//this is a reference to object in a on to one or many to one relation
						//need to find object and call this method.
						
						//System.out.println("[returnModelDisplayInfo] property is model:"+ p.getDataType());
						String methodName = this.getMethodName(p.getName());
						try{
							Method m = this.getClass().getMethod(methodName);
							Object object = m.invoke(this);
							System.out.println("[returnModelDisplayInfo] model found is:"+ object.toString() 
									+ " mode class:" + object.getClass().getSimpleName());
							if(object!=null){
								for(Method method:object.getClass().getMethods()){
									if(method.getName().equals("returnModelDisplayInfo")){
										m = method;
										break;
									}
								}
								String objectResult = (String)m.invoke(object, entityManager,user);
								result = result.equals("") ? this.parseLink(mdis.get(0), object,user, p.getAlias()) + ": (" + objectResult + ")" 
										:result + "<br />" + this.parseLink(mdis.get(0), object,user, p.getAlias()) + ": (" + objectResult + ")";
							}else{
								result = result.equals("") ? p.getAlias() + ": Null" 
										:result + "<br />" + p.getAlias() + ": Null";
							}
						}catch(Exception e){
							e.printStackTrace();
						}
					}else{
						//System.out.println("[returnModelDisplayInfo] property is java class:"+ p.getDataType());
						for(Method m:this.getClass().getMethods()){
							if(m.getName().substring(0, 3).equals("get") && 
									this.extractPropertyName(m.getName()).equals(p.getName())){
								try{
									Object returnVal = m.invoke(this);
									result = result.equals("") ? this.parseLink(mdi, this,user, p.getAlias() + ": " + returnVal.toString()) 
											:result + "<br />" + this.parseLink(mdi, this,user, p.getAlias() + ": " + returnVal.toString()) ;
								}catch(Exception e){
									e.printStackTrace();
								}
								break;
							}
						}
					}
				}
			}else{
				//System.out.println("[returnModelDisplayInfo] no properties selected");
				result = result.equals("") ? this.toString() 
						:result + "<br />" + this.toString();
			}
					
		}else{
			//System.out.println("[returnModelDisplayInfo] no ModelDisplayInfo");
			result = result.equals("") ? this.toString() 
					:result + "<br />" + this.toString();
		}
		return result;
	}
	
	private String extractPropertyName(String name){
		
		String letter = name.substring(3,4).toLowerCase();
		String result = letter + name.substring(4);
		//log.info("[extractProperty] letter:#0,result:#1", letter,result);
		return result;
	}
	private String getMethodName(String property){
		String letter = property.substring(0, 1).toUpperCase();
		//log.info("[getMethodName] property:#0, result:#1", property,"get" + letter + property.substring(1));
		return "get" + letter + property.substring(1);
	}
	/**
	 * if there is an actionLink associated with mdi this link will be placed in result
	 * also if there are any references to dynamic values like parameters, those methods
	 * will be executed and results added to the link
	 * @param mdi - where the link is defined
	 * @param obj - the object that will execute methods
	 * @param text - text to show
	 * @return
	 */
	private String parseLink(ModelDisplayInfo mdi, Object obj,User user, String text){
		String result = text;
		String actionLink=this.getUrlForUser(user, mdi);
		if(actionLink!=null && !actionLink.equals("")){
			String url = actionLink;
			String[] vars = url.split("%");
			if(vars.length>0){
				String resultUrl = "";
				for(int i=0;i<vars.length;i++){
					if(i %2 ==0)// number is even
						resultUrl += vars[i];
					else{//number is odd
						String var = vars[i];
						///System.out.println("[parseLink] var:" + var);
						Object objResult = this.executeMethod(obj, var);
						if(objResult!=null)
							resultUrl += objResult.toString();
					}
				}
				result = resultUrl;
			}else
				result = url;//direct link
			//now, enclose in a HTML anchor tag
			result = "<a href='" + result + "'>"+text + "</a>";
		}
		//System.out.println("[parseLink] result:" + result);
		return result;
	}
	
	private Object executeMethod(Object obj,String methodName){
		String[] methods = methodName.split("[.]");
		//System.out.println("[executeMethod] methodName:" + methodName +
			//	" methods #:" + methods.length);
		Object result=null;
		if(methods.length>1){
			//System.out.println("[executeMethod] method[0]:" + methods[0]);
			Object object = this.executeSingleMethod(obj, methods[0]);
			//System.out.println("[executeMethod] returned object:" + object);
			if(object!=null)
				result = this.executeMethod(object, methodName.substring(methodName.indexOf(".")+1));
		}else{
			result =this.executeSingleMethod(obj, methodName);
		}
		return result;
	}
	
	private Object executeSingleMethod(Object obj, String methodName){
		if(methodName.indexOf("(")>=0){
			methodName = methodName.substring(0, methodName.indexOf("("));
		}else{
			methodName = this.getMethodName(methodName);
		}
		//System.out.println("[executeSingleMethod] methodName:" + methodName);
		try{
			Method m=null;
			for(Method method:obj.getClass().getMethods()){
				if(method.getName().equals(methodName)){
					m = method;
					break;
				}
			}
			if(m!=null){
				Object objResult = m.invoke(obj);
				//System.out.println("[executeSingleMethod] objResult:" + objResult);
				return objResult;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * examines user profiles and decides about the appropriated URL according with mdi 
	 * @param user
	 * @param mdi
	 * @return
	 */
	private String getUrlForUser(User user,ModelDisplayInfo mdi){
		String result = "";
		if(user==null){//get URL for anonymous user
			UrlProfileDisplayInfo url = mdi.returnUrlProfile(null);
			if(url!=null)
				result = url.getUrl();
		}else{
			//int userProfile=-1;
			for(UserProfile up: user.getProfiles()){
				UrlProfileDisplayInfo url = mdi.returnUrlProfile(up.getProfile());
				if(url!=null)
					result = url.getUrl();
				break;
			}
		}
		return result;
	}
}
